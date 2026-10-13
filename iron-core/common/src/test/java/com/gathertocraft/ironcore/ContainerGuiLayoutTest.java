package com.gathertocraft.ironcore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ContainerGuiLayoutTest {
  @Test
  void vanillaScreenHeightUnchanged() {
    assertEquals(114 + 6 * 18, ContainerGuiLayout.screenHeight(6));
    assertEquals(114 + 9 * 18, ContainerGuiLayout.screenHeight(9));
  }

  @Test
  void extendedScreenHeightGrowsOneRowPerExtraPlayerRow() {
    assertEquals(114 + 6 * 18 + 3 * 18, ContainerGuiLayout.screenHeight(6, 6));
    assertEquals(ContainerGuiLayout.screenHeight(6), ContainerGuiLayout.screenHeight(6, 3));
  }

  @Test
  void hotbarOffsetMatchesVanillaAndExtended() {
    assertEquals(58, ContainerGuiLayout.hotbarOffset(3));
    assertEquals(112, ContainerGuiLayout.hotbarOffset(6));
    assertEquals(ContainerGuiLayout.EXTENDED_HOTBAR_OFFSET, ContainerGuiLayout.hotbarOffset(6));
  }

  @Test
  void playerPanelHeightGrowsWithRows() {
    assertEquals(96, ContainerGuiLayout.playerPanelHeight(3));
    assertEquals(150, ContainerGuiLayout.playerPanelHeight(6));
  }

  @Test
  void playerRowsDerivedFromInventorySize() {
    assertEquals(3, ExtendedInventory.playerRowsForSize(36));
    assertEquals(6, ExtendedInventory.playerRowsForSize(63));
  }
}
