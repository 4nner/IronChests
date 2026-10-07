package com.gathertocraft.ironcore.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CoreConfigTest {
  @Test
  void flagDefaultsToEnabled() {
    List<JsonConfig.BoolOption> flags = CoreConfig.flags();
    assertEquals(1, flags.size());
    assertEquals(CoreConfig.ENABLE_LOCKS, flags.get(0).key());
    assertTrue(flags.get(0).defaultValue());
  }

  @Test
  void parseRespectsExplicitValues() {
    Map<String, Boolean> off =
        JsonConfig.parse("{\"enableLocks\": false}", List.of(), CoreConfig.flags()).flags();
    assertEquals(Map.of("enableLocks", false), off);

    Map<String, Boolean> on =
        JsonConfig.parse("{\"enableLocks\": true}", List.of(), CoreConfig.flags()).flags();
    assertEquals(Map.of("enableLocks", true), on);
  }

  @Test
  void parseFallsBackToDefault() {
    JsonConfig.LoadResult missing = JsonConfig.parse("{}", List.of(), CoreConfig.flags());
    assertEquals(Map.of("enableLocks", true), missing.flags());
    assertTrue(missing.corrected());

    JsonConfig.LoadResult invalid =
        JsonConfig.parse("{\"enableLocks\": \"yes\"}", List.of(), CoreConfig.flags());
    assertEquals(Map.of("enableLocks", true), invalid.flags());
    assertTrue(invalid.corrected());
    assertFalse(invalid.warnings().isEmpty());
  }
}
