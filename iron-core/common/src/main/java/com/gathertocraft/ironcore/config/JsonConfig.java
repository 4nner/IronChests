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
 * Reads and writes flat JSONC files of the form {@code { "key": 1, "flag": true }} with {@code //}
 * comments.
 *
 * <p>Strict JSON has no comments, so reading is lenient (Gson): {@code //} comments are accepted.
 * Integer values must be within range and boolean values real booleans; anything else falls back to
 * the default with a warning. Unknown keys are ignored with a warning, so the file stays canonical.
 */
public final class JsonConfig {
  private JsonConfig() {}

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

  /** One boolean entry: its key, default, and the {@code //} comment above it. */
  public record BoolOption(String key, boolean defaultValue, String comment) {
    public BoolOption {
      Objects.requireNonNull(key, "key");
      Objects.requireNonNull(comment, "comment");
      if (key.isBlank()) {
        throw new IllegalArgumentException("Option key must not be blank");
      }
    }
  }

  /**
   * Parsed values plus anything a caller should log. Order of {@code values} and {@code flags}
   * matches the option lists. {@code corrected} is true when a key was missing, invalid, or
   * unrecognized, i.e. the file should be rewritten with effective values.
   */
  public record LoadResult(
      Map<String, Integer> values,
      Map<String, Boolean> flags,
      List<String> warnings,
      boolean corrected) {
    public LoadResult {
      values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
      flags = Collections.unmodifiableMap(new LinkedHashMap<>(flags));
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
    return loadOrCreate(file, headerComment, options, List.of());
  }

  /**
   * Loads {@code file}, creating it from defaults when absent. Missing, invalid, or unrecognized
   * keys are merged back into the file (comments refreshed, user values kept); a clean file is left
   * byte-identical so hand edits to comments are preserved.
   */
  public static LoadResult loadOrCreate(
      Path file, String headerComment, List<Option> options, List<BoolOption> flags)
      throws IOException {
    Objects.requireNonNull(file, "file");
    Objects.requireNonNull(options, "options");
    Objects.requireNonNull(flags, "flags");
    if (Files.notExists(file)) {
      LoadResult defaults = withDefaults(options, flags, "Created default config at " + file);
      write(file, render(defaults, headerComment, options, flags));
      return defaults;
    }
    LoadResult parsed = parse(Files.readString(file, StandardCharsets.UTF_8), options, flags);
    if (parsed.corrected()) {
      write(file, render(parsed, headerComment, options, flags));
    }
    return parsed;
  }

  /** Parses text without touching the filesystem. Never throws on malformed input. */
  public static LoadResult parse(String text, List<Option> options) {
    return parse(text, options, List.of());
  }

  /** Parses text without touching the filesystem. Never throws on malformed input. */
  public static LoadResult parse(String text, List<Option> options, List<BoolOption> flags) {
    Objects.requireNonNull(options, "options");
    Objects.requireNonNull(flags, "flags");
    List<String> warnings = new ArrayList<>();
    Map<String, Integer> values = new LinkedHashMap<>();
    Map<String, Boolean> flagValues = new LinkedHashMap<>();
    Map<String, Option> byKey = new LinkedHashMap<>();
    for (Option option : options) {
      byKey.put(option.key(), option);
    }
    Map<String, BoolOption> boolByKey = new LinkedHashMap<>();
    for (BoolOption flag : flags) {
      boolByKey.put(flag.key(), flag);
    }

    JsonObject root = readObject(text, warnings);
    boolean corrected = root == null;
    if (root != null) {
      // Gson silently keeps the last of any duplicate keys, matching standard JSON behavior.
      for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
        String key = entry.getKey();
        Option option = byKey.get(key);
        if (option != null) {
          ResolvedValue resolved = resolveInt(key, entry.getValue(), option, warnings);
          values.put(key, resolved.value());
          corrected |= resolved.corrected();
          continue;
        }
        BoolOption flag = boolByKey.get(key);
        if (flag != null) {
          ResolvedFlag resolved = resolveBool(key, entry.getValue(), flag, warnings);
          flagValues.put(key, resolved.value());
          corrected |= resolved.corrected();
          continue;
        }
        warnings.add("Unrecognized key '" + key + "' ignored.");
        corrected = true;
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
    Map<String, Boolean> orderedFlags = new LinkedHashMap<>();
    for (BoolOption flag : flags) {
      if (flagValues.containsKey(flag.key())) {
        orderedFlags.put(flag.key(), flagValues.get(flag.key()));
      } else {
        orderedFlags.put(flag.key(), flag.defaultValue());
        if (root != null) {
          warnings.add(
              "Missing key '" + flag.key() + "'; using default " + flag.defaultValue() + ".");
          corrected = true;
        }
      }
    }
    return new LoadResult(ordered, orderedFlags, warnings, corrected);
  }

  /**
   * Renders effective values with the header block and one {@code //} comment per option. No
   * trailing comma, so strict parsers also cope.
   */
  public static String render(LoadResult result, String headerComment, List<Option> options) {
    return render(result, headerComment, options, List.of());
  }

  /**
   * Renders effective values with the header block and one {@code //} comment per option: integers
   * first, then booleans. No trailing comma, so strict parsers also cope.
   */
  public static String render(
      LoadResult result, String headerComment, List<Option> options, List<BoolOption> flags) {
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
    for (BoolOption flag : flags) {
      Boolean value = result.flags().get(flag.key());
      lines.add("  // " + flag.comment() + "\n  \"" + flag.key() + "\": " + value);
    }
    out.append(String.join(",\n", lines));
    out.append('\n').append('}').append('\n');
    return out.toString();
  }

  private static LoadResult withDefaults(
      List<Option> options, List<BoolOption> flags, String note) {
    Map<String, Integer> values = new LinkedHashMap<>();
    for (Option option : options) {
      values.put(option.key(), option.defaultValue());
    }
    Map<String, Boolean> flagValues = new LinkedHashMap<>();
    for (BoolOption flag : flags) {
      flagValues.put(flag.key(), flag.defaultValue());
    }
    return new LoadResult(values, flagValues, List.of(note), false);
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

  private record ResolvedFlag(boolean value, boolean corrected) {}

  private static ResolvedFlag resolveBool(
      String key, JsonElement element, BoolOption flag, List<String> warnings) {
    if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
      return new ResolvedFlag(element.getAsBoolean(), false);
    }
    warnings.add(
        "Key '"
            + key
            + "' is not a boolean ("
            + element
            + "); using default "
            + flag.defaultValue()
            + ".");
    return new ResolvedFlag(flag.defaultValue(), true);
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
