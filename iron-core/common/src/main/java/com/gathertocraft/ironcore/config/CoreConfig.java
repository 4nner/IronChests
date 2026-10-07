package com.gathertocraft.ironcore.config;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/** Shared ironcore.json settings. Restart after editing, like the chest config. */
public final class CoreConfig {
  private CoreConfig() {}

  public static final String FILE_NAME = "ironcore.json";
  public static final String ENABLE_LOCKS = "enableLocks";

  private static final String HEADER =
      """
      IronCore shared settings.
      Unknown keys are ignored; missing or invalid values reset to defaults.\
      """;

  private static volatile boolean locksEnabled = true;

  public static List<JsonConfig.BoolOption> flags() {
    return List.of(
        new JsonConfig.BoolOption(ENABLE_LOCKS, true, "Enable Container Key locks (true/false)"));
  }

  public static JsonConfig.LoadResult load(Path configDir) throws IOException {
    JsonConfig.LoadResult result =
        JsonConfig.loadOrCreate(configDir.resolve(FILE_NAME), HEADER, List.of(), flags());
    locksEnabled = result.flags().getOrDefault(ENABLE_LOCKS, true);
    return result;
  }

  public static boolean locksEnabled() {
    return locksEnabled;
  }
}
