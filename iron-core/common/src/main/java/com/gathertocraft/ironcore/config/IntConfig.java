package com.gathertocraft.ironcore.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Reads and writes flat JSONC files of the form {@code { "key": 1 }} with {@code //} comments.
 *
 * <p>Strict JSON has no comments, so reading is lenient (Gson): {@code //} comments are accepted.
 * Values of known options must be integers within range; anything else falls back to the default
 * with a warning. Unknown keys are ignored with a warning, so the file stays canonical.
 */
public final class IntConfig {
  private IntConfig() {}

  /** One integer entry: its key, default, allowed range, and the {@code //} comment above it. */
  public record Option(String key, int defaultValue, int minValue, int maxValue, String comment) {
    public Option {
      Objects.requireNonNull(key, "key");
      Objects.requireNonNull(comment, "comment");
      if (key.isBlank()) {
        throw new IllegalArgumentException("Option key must not be blank");
      }
      if (minValue > defaultValue || defaultValue > maxValue) {
        throw new IllegalArgumentException(
            "Default for '" + key + "' must be within [" + minValue + ", " + maxValue + "]");
      }
    }
  }

  /**
   * Parsed values plus anything a caller should log. Order of {@code values} matches options.
   * {@code corrected} is true when a key was missing, invalid, or unrecognized, i.e. the file
   * should be rewritten with effective values.
   */
  public record LoadResult(Map<String, Integer> values, List<String> warnings, boolean corrected) {
    public LoadResult {
      values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
      warnings = List.copyOf(warnings);
    }
  }

  /**
   * Loads {@code file}, creating it from defaults when absent. Missing, invalid, or unrecognized
   * keys are merged back into the file (comments refreshed, user values kept); a clean file is left
   * byte-identical so hand edits to comments are preserved.
   */
  public static LoadResult loadOrCreate(Path file, String headerComment, List<Option> options)
      throws IOException {
    Objects.requireNonNull(file, "file");
    Objects.requireNonNull(options, "options");
    if (Files.notExists(file)) {
      LoadResult defaults = withDefaults(options, "Created default config at " + file);
      write(file, render(defaults, headerComment, options));
      return defaults;
    }
    LoadResult parsed = parse(Files.readString(file, StandardCharsets.UTF_8), options);
    if (parsed.corrected()) {
      write(file, render(parsed, headerComment, options));
    }
    return parsed;
  }

  /** Parses text without touching the filesystem. Never throws on malformed input. */
  public static LoadResult parse(String text, List<Option> options) {
    Objects.requireNonNull(options, "options");
    List<String> warnings = new ArrayList<>();
    Map<String, Integer> values = new LinkedHashMap<>();
    Map<String, Option> byKey = new LinkedHashMap<>();
    for (Option option : options) {
      byKey.put(option.key(), option);
    }

    JsonObject root = readObject(text, warnings);
    boolean corrected = root == null;
    if (root != null) {
      // Gson silently keeps the last of any duplicate keys, matching standard JSON behavior.
      for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
        String key = entry.getKey();
        Option option = byKey.get(key);
        if (option == null) {
          warnings.add("Unrecognized key '" + key + "' ignored.");
          corrected = true;
        } else {
          ResolvedValue resolved = resolveInt(key, entry.getValue(), option, warnings);
          values.put(key, resolved.value());
          corrected |= resolved.corrected();
        }
      }
    }
    Map<String, Integer> ordered = new LinkedHashMap<>();
    for (Option option : options) {
      if (values.containsKey(option.key())) {
        ordered.put(option.key(), values.get(option.key()));
      } else {
        ordered.put(option.key(), option.defaultValue());
        if (root != null) {
          warnings.add(
              "Missing key '" + option.key() + "'; using default " + option.defaultValue() + ".");
          corrected = true;
        }
      }
    }
    return new LoadResult(ordered, warnings, corrected);
  }

  /**
   * Renders effective values with the header block and one {@code //} comment per option. No
   * trailing comma, so strict parsers also cope.
   */
  public static String render(LoadResult result, String headerComment, List<Option> options) {
    StringBuilder out = new StringBuilder();
    if (headerComment != null && !headerComment.isBlank()) {
      for (String line : headerComment.split("\n", -1)) {
        if (line.isBlank()) {
          out.append('\n');
        } else if (line.stripLeading().startsWith("//")) {
          out.append(line.stripTrailing()).append('\n');
        } else {
          out.append("// ").append(line.stripTrailing()).append('\n');
        }
      }
    }
    out.append("{\n");
    List<String> lines = new ArrayList<>();
    for (Option option : options) {
      Integer value = result.values().get(option.key());
      lines.add("  // " + option.comment() + "\n  \"" + option.key() + "\": " + value);
    }
    out.append(String.join(",\n", lines));
    out.append('\n').append('}').append('\n');
    return out.toString();
  }

  private static LoadResult withDefaults(List<Option> options, String note) {
    Map<String, Integer> values = new LinkedHashMap<>();
    for (Option option : options) {
      values.put(option.key(), option.defaultValue());
    }
    return new LoadResult(values, List.of(note), false);
  }

  private record ResolvedValue(int value, boolean corrected) {}

  private static void write(Path file, String text) throws IOException {
    if (file.getParent() != null) {
      Files.createDirectories(file.getParent());
    }
    Files.writeString(
        file,
        text,
        StandardCharsets.UTF_8,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);
  }

  /** Leniently reads the top-level object, recording a warning and returning null on failure. */
  private static JsonObject readObject(String text, List<String> warnings) {
    if (text == null || text.isBlank()) {
      warnings.add("Config is empty; using defaults.");
      return null;
    }
    try {
      JsonReader reader = new JsonReader(new StringReader(text));
      reader.setLenient(true);
      JsonElement parsed = JsonParser.parseReader(reader);
      if (!parsed.isJsonObject()) {
        warnings.add("Config is not a JSON object; using defaults.");
        return null;
      }
      return parsed.getAsJsonObject();
    } catch (JsonSyntaxException | IllegalStateException malformed) {
      warnings.add("Config is not a JSON object; using defaults.");
      return null;
    }
  }

  private static ResolvedValue resolveInt(
      String key, JsonElement element, Option option, List<String> warnings) {
    Integer parsed = parseInteger(element);
    if (parsed == null) {
      warnings.add(
          "Key '"
              + key
              + "' is not an integer ("
              + element
              + "); using default "
              + option.defaultValue()
              + ".");
      return new ResolvedValue(option.defaultValue(), true);
    }
    if (parsed < option.minValue() || parsed > option.maxValue()) {
      warnings.add(
          "Key '"
              + key
              + "' is out of range ["
              + option.minValue()
              + ", "
              + option.maxValue()
              + "] ("
              + parsed
              + "); using default "
              + option.defaultValue()
              + ".");
      return new ResolvedValue(option.defaultValue(), true);
    }
    return new ResolvedValue(parsed, false);
  }

  private static Integer parseInteger(JsonElement element) {
    if (element == null || !element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber()) {
      return null;
    }
    String raw = element.getAsJsonPrimitive().getAsString().strip();
    if (!raw.matches("[+-]?\\d+")) {
      return null;
    }
    try {
      long value = Long.parseLong(raw);
      return (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) ? (int) value : null;
    } catch (NumberFormatException outOfLongRange) {
      return null;
    }
  }
}
