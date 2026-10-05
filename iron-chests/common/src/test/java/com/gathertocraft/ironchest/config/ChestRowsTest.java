package com.gathertocraft.ironchest.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gathertocraft.ironchest.blocks.ChestTypes;
import com.gathertocraft.ironcore.config.IntConfig;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ChestRowsTest {
  private static final List<String> EXPECTED_KEYS =
      List.of(
          "copper",
          "iron",
          "gold",
          "diamond",
          "emerald",
          "crystal",
          "obsidian",
          "netherite",
          "christmas");

  private static final Map<String, Integer> EXPECTED_ROWS =
      Map.of(
          "copper", 5,
          "iron", 6,
          "gold", 9,
          "diamond", 9,
          "emerald", 9,
          "crystal", 9,
          "obsidian", 9,
          "netherite", 9,
          "christmas", 3);

  @Test
  void optionsCoverPlayableTiersInOrder() {
    List<IntConfig.Option> options = ChestRows.options();

    assertEquals(EXPECTED_KEYS, options.stream().map(IntConfig.Option::key).toList());
    for (IntConfig.Option option : options) {
      assertEquals(ChestRows.MIN_ROWS, option.minValue());
      assertEquals(ChestRows.MAX_ROWS, option.maxValue());
    }
  }

  @Test
  void defaultsMatchCurrentRows() {
    for (IntConfig.Option option : ChestRows.options()) {
      assertEquals(EXPECTED_ROWS.get(option.key()), option.defaultValue());
    }
  }

  @Test
  void configuredRowsChangeSize() {
    try {
      ChestTypes.setConfiguredRows(Map.of("copper", 2));

      assertEquals(2, ChestTypes.COPPER.rowCount());
      assertEquals(18, ChestTypes.COPPER.size());
      assertEquals(5, ChestTypes.COPPER.defaultRowCount());
    } finally {
      ChestTypes.setConfiguredRows(Map.of());
    }
  }

  @Test
  void maxCapacityCoversPreviouslySavedInventories() {
    // The shrink fix sizes the load list to MAX_ROWS x rowLength, so every inventory
    // saved before the config existed (i.e. at default rows) must fit inside it.
    for (ChestTypes tier : ChestTypes.PLAYABLE) {
      assertTrue(
          ChestRows.MAX_ROWS >= tier.defaultRowCount(),
          tier.configKey() + " default exceeds MAX_ROWS");
    }
  }

  @Test
  void loadReadsRowsFromDisk(@TempDir Path dir) throws Exception {
    Files.writeString(
        dir.resolve(ChestRows.FILE_NAME),
        """
        {
          "copper": 2, "iron": 6, "gold": 9, "diamond": 9, "emerald": 9,
          "crystal": 9, "obsidian": 9, "netherite": 9, "christmas": 3
        }
        """,
        StandardCharsets.UTF_8);

    IntConfig.LoadResult result = ChestRows.load(dir);

    assertEquals(2, result.values().get("copper"));
    assertTrue(result.warnings().isEmpty());
  }
}
