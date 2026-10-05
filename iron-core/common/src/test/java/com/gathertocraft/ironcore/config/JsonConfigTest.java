package com.gathertocraft.ironcore.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class JsonConfigTest {
  private static final List<JsonConfig.Option> OPTIONS =
      List.of(
          new JsonConfig.Option("copper", 5, 1, 12, "copper: rows 1-12, default 5"),
          new JsonConfig.Option("iron", 6, 1, 12, "iron: rows 1-12, default 6"));

  private static final String HEADER = "IronChests rows.\nAllowed rows 1-12.";

  private static final List<JsonConfig.BoolOption> FLAGS =
      List.of(
          new JsonConfig.BoolOption(
              "openUnderSolidBlocks",
              false,
              "openUnderSolidBlocks: allow opening under solid blocks"));

  @Test
  void renderContainsHeaderAndPerKeyComments() {
    JsonConfig.LoadResult result = JsonConfig.parse("{}", OPTIONS);

    String rendered = JsonConfig.render(result, HEADER, OPTIONS);

    assertTrue(rendered.contains("// IronChests rows."));
    assertTrue(rendered.contains("// copper: rows 1-12, default 5"));
    assertTrue(rendered.contains("\"copper\": 5"));
    assertTrue(rendered.contains("\"iron\": 6"));
    assertTrue(rendered.endsWith("}\n"));
  }

  @Test
  void roundTripKeepsUserValues() {
    JsonConfig.LoadResult result = JsonConfig.parse("{\"copper\": 3, \"iron\": 9}", OPTIONS);

    assertEquals(Map.of("copper", 3, "iron", 9), result.values());
    assertTrue(result.warnings().isEmpty());
    assertFalse(result.corrected());

    JsonConfig.LoadResult reparsed =
        JsonConfig.parse(JsonConfig.render(result, HEADER, OPTIONS), OPTIONS);
    assertEquals(Map.of("copper", 3, "iron", 9), reparsed.values());
    assertTrue(reparsed.warnings().isEmpty());
  }

  @Test
  void acceptsComments() {
    String text =
        """
        // header comment
        {
          // copper rows
          "copper": 4, // trailing comment
          "iron": 7
        }
        """;

    JsonConfig.LoadResult result = JsonConfig.parse(text, OPTIONS);

    assertEquals(Map.of("copper", 4, "iron", 7), result.values());
    assertTrue(result.warnings().isEmpty());
  }

  @Test
  void outOfRangeFallsBackToDefaultWithWarning() {
    JsonConfig.LoadResult result = JsonConfig.parse("{\"copper\": 99, \"iron\": 0}", OPTIONS);

    assertEquals(Map.of("copper", 5, "iron", 6), result.values());
    assertEquals(2, result.warnings().size());
    assertTrue(result.warnings().get(0).contains("out of range"));
    assertTrue(result.corrected());
  }

  @Test
  void nonIntegerFallsBackToDefaultWithWarning() {
    JsonConfig.LoadResult result =
        JsonConfig.parse("{\"copper\": 4.5, \"iron\": \"many\"}", OPTIONS);

    assertEquals(Map.of("copper", 5, "iron", 6), result.values());
    assertEquals(2, result.warnings().size());
  }

  @Test
  void missingKeysUseDefaultsWithWarning() {
    JsonConfig.LoadResult result = JsonConfig.parse("{\"copper\": 2}", OPTIONS);

    assertEquals(Map.of("copper", 2, "iron", 6), result.values());
    assertEquals(1, result.warnings().size());
    assertTrue(result.warnings().get(0).contains("Missing key 'iron'"));
    assertTrue(result.corrected());
  }

  @Test
  void unknownKeysAreIgnoredWithWarning() {
    JsonConfig.LoadResult result =
        JsonConfig.parse("{\"copper\": 2, \"iron\": 6, \"mithril\": {\"rows\": 3}}", OPTIONS);

    assertEquals(Map.of("copper", 2, "iron", 6), result.values());
    assertEquals(1, result.warnings().size());
    assertTrue(result.warnings().get(0).contains("Unrecognized key 'mithril'"));
    assertTrue(result.corrected());

    String rendered = JsonConfig.render(result, HEADER, OPTIONS);
    assertFalse(rendered.contains("mithril"));
  }

  @Test
  void duplicateKeyLastWins() {
    JsonConfig.LoadResult result =
        JsonConfig.parse("{\"copper\": 2, \"iron\": 6, \"copper\": 4}", OPTIONS);

    assertEquals(4, result.values().get("copper"));
    assertTrue(result.warnings().isEmpty());
  }

  @Test
  void commentMarkersInsideStringsDoNotBreakParsing() {
    JsonConfig.LoadResult result =
        JsonConfig.parse("{\"copper\": 2, \"iron\": 6, \"note\": \"a // b\"}", OPTIONS);

    assertEquals(Map.of("copper", 2, "iron", 6), result.values());
    assertEquals(1, result.warnings().size());
    assertTrue(result.warnings().get(0).contains("Unrecognized key 'note'"));
  }

  @Test
  void malformedFileUsesDefaultsWithWarning() {
    JsonConfig.LoadResult result = JsonConfig.parse("not json at all", OPTIONS);

    assertEquals(Map.of("copper", 5, "iron", 6), result.values());
    assertFalse(result.warnings().isEmpty());
  }

  @Test
  void emptyFileUsesDefaultsWithWarning() {
    JsonConfig.LoadResult result = JsonConfig.parse("", OPTIONS);

    assertEquals(Map.of("copper", 5, "iron", 6), result.values());
    assertFalse(result.warnings().isEmpty());
  }

  @Test
  void optionRejectsDefaultOutsideRange() {
    assertThrows(
        IllegalArgumentException.class, () -> new JsonConfig.Option("bad", 99, 1, 12, "bad"));
  }

  @Test
  void loadOrCreateWritesDefaultsWhenMissing(@TempDir Path dir) throws Exception {
    Path file = dir.resolve("nested").resolve("ironchest.json");

    JsonConfig.LoadResult result = JsonConfig.loadOrCreate(file, HEADER, OPTIONS);

    assertEquals(Map.of("copper", 5, "iron", 6), result.values());
    assertTrue(Files.isRegularFile(file));
    String written = Files.readString(file, StandardCharsets.UTF_8);
    assertTrue(written.contains("// IronChests rows."));
    assertTrue(written.contains("\"copper\": 5"));
  }

  @Test
  void loadOrCreateLeavesCleanFileUntouched(@TempDir Path dir) throws Exception {
    Path file = dir.resolve("ironchest.json");
    JsonConfig.loadOrCreate(file, HEADER, OPTIONS);
    String before = Files.readString(file, StandardCharsets.UTF_8) + "// user note kept\n";

    Files.writeString(file, before, StandardCharsets.UTF_8);
    JsonConfig.LoadResult reread = JsonConfig.loadOrCreate(file, HEADER, OPTIONS);

    assertEquals(before, Files.readString(file, StandardCharsets.UTF_8));
    assertFalse(reread.corrected());
    assertEquals(Map.of("copper", 5, "iron", 6), reread.values());
  }

  @Test
  void loadOrCreateHealsMissingAndInvalidKeys(@TempDir Path dir) throws Exception {
    Path file = dir.resolve("ironchest.json");
    Files.writeString(
        file, "// user header\n{\"copper\": 99, \"extra\": true}\n", StandardCharsets.UTF_8);

    JsonConfig.LoadResult result = JsonConfig.loadOrCreate(file, HEADER, OPTIONS);

    assertEquals(Map.of("copper", 5, "iron", 6), result.values());
    String healed = Files.readString(file, StandardCharsets.UTF_8);
    assertTrue(healed.contains("\"copper\": 5"));
    assertTrue(healed.contains("\"iron\": 6"));
    assertFalse(healed.contains("\"extra\""));
  }

  @Test
  void boolTrueAndFalseRead() {
    JsonConfig.LoadResult on =
        JsonConfig.parse(
            "{\"copper\": 5, \"iron\": 6, \"openUnderSolidBlocks\": true}", OPTIONS, FLAGS);
    JsonConfig.LoadResult off =
        JsonConfig.parse(
            "{\"copper\": 5, \"iron\": 6, \"openUnderSolidBlocks\": false}", OPTIONS, FLAGS);

    assertEquals(Map.of("openUnderSolidBlocks", true), on.flags());
    assertEquals(Map.of("openUnderSolidBlocks", false), off.flags());
    assertTrue(on.warnings().isEmpty());
    assertFalse(on.corrected());
  }

  @Test
  void nonBooleanFallsBackToDefaultWithWarning() {
    JsonConfig.LoadResult number =
        JsonConfig.parse(
            "{\"copper\": 5, \"iron\": 6, \"openUnderSolidBlocks\": 1}", OPTIONS, FLAGS);
    JsonConfig.LoadResult text =
        JsonConfig.parse(
            "{\"copper\": 5, \"iron\": 6, \"openUnderSolidBlocks\": \"true\"}", OPTIONS, FLAGS);

    assertEquals(Map.of("openUnderSolidBlocks", false), number.flags());
    assertEquals(Map.of("openUnderSolidBlocks", false), text.flags());
    assertTrue(number.warnings().get(0).contains("is not a boolean"));
    assertTrue(text.warnings().get(0).contains("is not a boolean"));
    assertTrue(number.corrected());
  }

  @Test
  void missingBoolUsesDefaultWithWarning() {
    JsonConfig.LoadResult result = JsonConfig.parse("{\"copper\": 5, \"iron\": 6}", OPTIONS, FLAGS);

    assertEquals(Map.of("openUnderSolidBlocks", false), result.flags());
    assertEquals(1, result.warnings().size());
    assertTrue(result.warnings().get(0).contains("Missing key 'openUnderSolidBlocks'"));
    assertTrue(result.corrected());
  }

  @Test
  void mixedRoundTripKeepsIntsAndBools() {
    JsonConfig.LoadResult result =
        JsonConfig.parse(
            "{\"copper\": 3, \"iron\": 9, \"openUnderSolidBlocks\": true}", OPTIONS, FLAGS);

    String rendered = JsonConfig.render(result, HEADER, OPTIONS, FLAGS);
    assertTrue(rendered.contains("\"openUnderSolidBlocks\": true"));
    // Integers keep their section first, so pre-bool files render byte-identical.
    assertTrue(rendered.indexOf("\"iron\": 9") < rendered.indexOf("\"openUnderSolidBlocks\""));

    JsonConfig.LoadResult reparsed = JsonConfig.parse(rendered, OPTIONS, FLAGS);
    assertEquals(Map.of("copper", 3, "iron", 9), reparsed.values());
    assertEquals(Map.of("openUnderSolidBlocks", true), reparsed.flags());
    assertTrue(reparsed.warnings().isEmpty());
  }

  @Test
  void boolOptionRejectsBlankKey() {
    assertThrows(IllegalArgumentException.class, () -> new JsonConfig.BoolOption("  ", true, "x"));
  }

  @Test
  void loadOrCreateAddsMissingBoolKey(@TempDir Path dir) throws Exception {
    Path file = dir.resolve("ironchest.json");
    Files.writeString(file, "{\"copper\": 3, \"iron\": 9}\n", StandardCharsets.UTF_8);

    JsonConfig.LoadResult result = JsonConfig.loadOrCreate(file, HEADER, OPTIONS, FLAGS);

    assertEquals(Map.of("openUnderSolidBlocks", false), result.flags());
    String healed = Files.readString(file, StandardCharsets.UTF_8);
    assertTrue(healed.contains("\"copper\": 3"));
    assertTrue(healed.contains("\"openUnderSolidBlocks\": false"));
  }
}
