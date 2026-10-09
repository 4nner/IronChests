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
    assertEquals(2, flags.size());
    assertEquals(CoreConfig.ENABLE_LOCKS, flags.get(0).key());
    assertTrue(flags.get(0).defaultValue());
    assertEquals(CoreConfig.HAND_TRUCK_SPAWNERS, flags.get(1).key());
    assertTrue(flags.get(1).defaultValue());
  }

  @Test
  void parseRespectsExplicitValues() {
    Map<String, Boolean> off =
        JsonConfig.parse("{\"enableLocks\": false}", List.of(), CoreConfig.flags()).flags();
    assertFalse(off.get("enableLocks"));

    Map<String, Boolean> on =
        JsonConfig.parse("{\"enableLocks\": true}", List.of(), CoreConfig.flags()).flags();
    assertTrue(on.get("enableLocks"));

    Map<String, Boolean> spawnersOn =
        JsonConfig.parse("{\"handTruckCanMoveSpawners\": true}", List.of(), CoreConfig.flags())
            .flags();
    assertTrue(spawnersOn.get("handTruckCanMoveSpawners"));

    Map<String, Boolean> spawnersOff =
        JsonConfig.parse("{\"handTruckCanMoveSpawners\": false}", List.of(), CoreConfig.flags())
            .flags();
    assertFalse(spawnersOff.get("handTruckCanMoveSpawners"));
  }

  @Test
  void parseFallsBackToDefault() {
    JsonConfig.LoadResult missing = JsonConfig.parse("{}", List.of(), CoreConfig.flags());
    assertTrue(missing.flags().get("enableLocks"));
    assertTrue(missing.flags().get("handTruckCanMoveSpawners"));
    assertTrue(missing.corrected());

    JsonConfig.LoadResult invalid =
        JsonConfig.parse(
            "{\"enableLocks\": \"yes\", \"handTruckCanMoveSpawners\": 1}",
            List.of(),
            CoreConfig.flags());
    assertTrue(invalid.flags().get("enableLocks"));
    assertTrue(invalid.flags().get("handTruckCanMoveSpawners"));
    assertTrue(invalid.corrected());
    assertFalse(invalid.warnings().isEmpty());
  }
}
