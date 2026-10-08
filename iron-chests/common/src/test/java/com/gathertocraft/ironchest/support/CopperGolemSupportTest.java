package com.gathertocraft.ironchest.support;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class CopperGolemSupportTest {
  @AfterEach
  void resetToDefaults() {
    CopperGolemSupport.setEnabled(true);
  }

  @Test
  void enabledByDefault() {
    assertTrue(CopperGolemSupport.isEnabled());
  }

  @Test
  void togglePersists() {
    CopperGolemSupport.setEnabled(false);

    assertFalse(CopperGolemSupport.isEnabled());
  }
}
