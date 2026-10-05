package com.gathertocraft.ironchest.config;

import com.gathertocraft.ironchest.blocks.ChestTypes;
import com.gathertocraft.ironcore.config.JsonConfig;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Row-count options for ironchest.json. Columns are fixed per tier; only rows are configurable. */
public final class ChestRows {
  private ChestRows() {}

  public static final String FILE_NAME = "ironchest.json";
  public static final int MIN_ROWS = 1;
  public static final int MAX_ROWS = 12;

  private static final String HEADER =
      """
      IronChests chest sizes.
      Rows per chest tier, 1-12. Columns are fixed per tier and cannot change.
      Unknown keys are ignored; missing or invalid values reset to defaults.\
      """;

  /** One option per playable tier, in tier order. Defaults match the built-in row counts. */
  public static List<JsonConfig.Option> options() {
    List<JsonConfig.Option> options = new ArrayList<>();
    for (ChestTypes type : ChestTypes.PLAYABLE) {
      String name = type.name().charAt(0) + type.name().substring(1).toLowerCase(Locale.ROOT);
      options.add(
          new JsonConfig.Option(
              type.configKey(),
              type.defaultRowCount(),
              MIN_ROWS,
              MAX_ROWS,
              name + " chest rows (1-12)"));
    }
    return List.copyOf(options);
  }

  public static JsonConfig.LoadResult load(Path configDir) throws IOException {
    return JsonConfig.loadOrCreate(configDir.resolve(FILE_NAME), HEADER, options());
  }
}
