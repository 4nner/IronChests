package com.gathertocraft.ironcore;

import net.minecraft.world.entity.player.Inventory;

/**
 * Detects player inventories enlarged by mods such as Inventory Extended (Fabric), which grows the
 * non-equipment section from 36 slots (3 rows + hotbar) to 63 slots (6 rows + hotbar).
 *
 * <p>Detection is dynamic so vanilla players keep the exact vanilla layout.
 */
public final class ExtendedInventory {
  public static final int VANILLA_PLAYER_ROWS = 3;

  private ExtendedInventory() {}

  public static boolean isExtended(Inventory inventory) {
    return inventory.getNonEquipmentItems().size() > Inventory.INVENTORY_SIZE;
  }

  public static int playerRows(Inventory inventory) {
    return playerRowsForSize(inventory.getNonEquipmentItems().size());
  }

  static int playerRowsForSize(int nonEquipmentSize) {
    if (nonEquipmentSize <= Inventory.INVENTORY_SIZE) {
      return VANILLA_PLAYER_ROWS;
    }
    return Math.max(
        VANILLA_PLAYER_ROWS,
        (nonEquipmentSize - Inventory.SELECTION_SIZE) / Inventory.SELECTION_SIZE);
  }
}
