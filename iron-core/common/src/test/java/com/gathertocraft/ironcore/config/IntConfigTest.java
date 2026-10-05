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

class IntConfigTest {
  private static final List<IntConfig.Option> OPTIONS =
      List.of(
          new IntConfig.Option("copper", 5, 1, 12, "copper: rows 1-12, default 5"),
          new IntConfig.Option("iron", 6, 1, 12, "iron: rows 1-12, default 6"));

  private static final String HEADER = "IronChests rows.\nAllowed rows 1-12.";

  @Test
  void renderContainsHeaderAndPerKeyComments() {
    IntConfig.LoadResult result = IntConfig.parse("{}", OPTIONS);

    String rendered = IntConfig.render(result, HEADER, OPTIONS);

    assertTrue(rendered.contains("// IronChests rows."));
    assertTrue(rendered.contains("// copper: rows 1-12, default 5"));
    assertTrue(rendered.contains("\"copper\": 5"));
    assertTrue(rendered.contains("\"iron\": 6"));
    assertTrue(rendered.endsWith("}\n"));
  }

  @Test
  void roundTripKeepsUserValues() {
    IntConfig.LoadResult result = IntConfig.parse("{\"copper\": 3, \"iron\": 9}", OPTIONS);

    assertEquals(Map.of("copper", 3, "iron", 9), result.values());
    assertTrue(result.warnings().isEmpty());
    assertFalse(result.corrected());

    IntConfig.LoadResult reparsed =
        IntConfig.parse(IntConfig.render(result, HEADER, OPTIONS), OPTIONS);
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

    IntConfig.LoadResult result = IntConfig.parse(text, OPTIONS);

    assertEquals(Map.of("copper", 4, "iron", 7), result.values());
    assertTrue(result.warnings().isEmpty());
  }

  @Test
  void outOfRangeFallsBackToDefaultWithWarning() {
    IntConfig.LoadResult result = IntConfig.parse("{\"copper\": 99, \"iron\": 0}", OPTIONS);

    assertEquals(Map.of("copper", 5, "iron", 6), result.values());
    assertEquals(2, result.warnings().size());
    assertTrue(result.warnings().get(0).contains("out of range"));
    assertTrue(result.corrected());
  }

  @Test
  void nonIntegerFallsBackToDefaultWithWarning() {
    IntConfig.LoadResult result = IntConfig.parse("{\"copper\": 4.5, \"iron\": \"many\"}", OPTIONS);

    assertEquals(Map.of("copper", 5, "iron", 6), result.values());
    assertEquals(2, result.warnings().size());
  }

  @Test
  void missingKeysUseDefaultsWithWarning() {
    IntConfig.LoadResult result = IntConfig.parse("{\"copper\": 2}", OPTIONS);

    assertEquals(Map.of("copper", 2, "iron", 6), result.values());
    assertEquals(1, result.warnings().size());
    assertTrue(result.warnings().get(0).contains("Missing key 'iron'"));
    assertTrue(result.corrected());
  }

  @Test
  void unknownKeysAreIgnoredWithWarning() {
    IntConfig.LoadResult result =
        IntConfig.parse("{\"copper\": 2, \"iron\": 6, \"mithril\": {\"rows\": 3}}", OPTIONS);

    assertEquals(Map.of("copper", 2, "iron", 6), result.values());
    assertEquals(1, result.warnings().size());
    assertTrue(result.warnings().get(0).contains("Unrecognized key 'mithril'"));
    assertTrue(result.corrected());

    String rendered = IntConfig.render(result, HEADER, OPTIONS);
    assertFalse(rendered.contains("mithril"));
  }

  @Test
  void duplicateKeyLastWins() {
    IntConfig.LoadResult result =
        IntConfig.parse("{\"copper\": 2, \"iron\": 6, \"copper\": 4}", OPTIONS);

    assertEquals(4, result.values().get("copper"));
    assertTrue(result.warnings().isEmpty());
  }

  @Test
  void commentMarkersInsideStringsDoNotBreakParsing() {
    IntConfig.LoadResult result =
        IntConfig.parse("{\"copper\": 2, \"iron\": 6, \"note\": \"a // b\"}", OPTIONS);

    assertEquals(Map.of("copper", 2, "iron", 6), result.values());
    assertEquals(1, result.warnings().size());
    assertTrue(result.warnings().get(0).contains("Unrecognized key 'note'"));
  }

  @Test
  void malformedFileUsesDefaultsWithWarning() {
    IntConfig.LoadResult result = IntConfig.parse("not json at all", OPTIONS);

    assertEquals(Map.of("copper", 5, "iron", 6), result.values());
    assertFalse(result.warnings().isEmpty());
  }

  @Test
  void emptyFileUsesDefaultsWithWarning() {
    IntConfig.LoadResult result = IntConfig.parse("", OPTIONS);

    assertEquals(Map.of("copper", 5, "iron", 6), result.values());
    assertFalse(result.warnings().isEmpty());
  }

  @Test
  void optionRejectsDefaultOutsideRange() {
    assertThrows(
        IllegalArgumentException.class, () -> new IntConfig.Option("bad", 99, 1, 12, "bad"));
  }

  @Test
  void loadOrCreateWritesDefaultsWhenMissing(@TempDir Path dir) throws Exception {
    Path file = dir.resolve("nested").resolve("ironchest.json");

    IntConfig.LoadResult result = IntConfig.loadOrCreate(file, HEADER, OPTIONS);

    assertEquals(Map.of("copper", 5, "iron", 6), result.values());
    assertTrue(Files.isRegularFile(file));
    String written = Files.readString(file, StandardCharsets.UTF_8);
    assertTrue(written.contains("// IronChests rows."));
    assertTrue(written.contains("\"copper\": 5"));
  }

  @Test
  void loadOrCreateLeavesCleanFileUntouched(@TempDir Path dir) throws Exception {
    Path file = dir.resolve("ironchest.json");
    IntConfig.loadOrCreate(file, HEADER, OPTIONS);
    String before = Files.readString(file, StandardCharsets.UTF_8) + "// user note kept\n";

    Files.writeString(file, before, StandardCharsets.UTF_8);
    IntConfig.LoadResult reread = IntConfig.loadOrCreate(file, HEADER, OPTIONS);

    assertEquals(before, Files.readString(file, StandardCharsets.UTF_8));
    assertFalse(reread.corrected());
    assertEquals(Map.of("copper", 5, "iron", 6), reread.values());
  }

  @Test
  void loadOrCreateHealsMissingAndInvalidKeys(@TempDir Path dir) throws Exception {
    Path file = dir.resolve("ironchest.json");
    Files.writeString(
        file, "// user header\n{\"copper\": 99, \"extra\": true}\n", StandardCharsets.UTF_8);

    IntConfig.LoadResult result = IntConfig.loadOrCreate(file, HEADER, OPTIONS);

    assertEquals(Map.of("copper", 5, "iron", 6), result.values());
    String healed = Files.readString(file, StandardCharsets.UTF_8);
    assertTrue(healed.contains("\"copper\": 5"));
    assertTrue(healed.contains("\"iron\": 6"));
    assertFalse(healed.contains("\"extra\""));
  }
}
