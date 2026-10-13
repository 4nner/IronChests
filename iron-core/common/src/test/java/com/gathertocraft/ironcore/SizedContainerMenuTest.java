package com.gathertocraft.ironcore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.junit.jupiter.api.Test;

class SizedContainerMenuTest {
  private static final TierSpec CHEST = tier(27, 9);

  @Test
  void vanillaInventoryAddsThirtySixSlots() {
    SizedContainerMenu menu = menu(36);

    assertFalse(menu.isExtendedInventory());
    assertEquals(3, menu.getPlayerRows());
    assertEquals(27 + 36, menu.slots.size());
    // Main inventory indices 9-35 then hotbar 0-8.
    assertEquals(9, menu.getSlot(27).getContainerSlot());
    assertEquals(35, menu.getSlot(27 + 26).getContainerSlot());
    assertEquals(0, menu.getSlot(27 + 27).getContainerSlot());
    assertEquals(
        ContainerGuiLayout.playerInventoryY(3) + ContainerGuiLayout.HOTBAR_OFFSET,
        menu.getSlot(27 + 27).y);
  }

  @Test
  void extendedInventoryAddsSixtyThreeSlots() {
    SizedContainerMenu menu = menu(63);

    assertTrue(menu.isExtendedInventory());
    assertEquals(6, menu.getPlayerRows());
    assertEquals(27 + 63, menu.slots.size());
    // Formerly cut-off indices are now reachable.
    assertEquals(62, menu.getSlot(27 + 53).getContainerSlot());
    // Hotbar sits below the extra rows.
    Slot hotbar = menu.getSlot(27 + 54);
    assertEquals(0, hotbar.getContainerSlot());
    assertEquals(
        ContainerGuiLayout.playerInventoryY(3) + ContainerGuiLayout.EXTENDED_HOTBAR_OFFSET,
        hotbar.y);
    // Sixth row sits five rows below the player section start.
    assertEquals(
        ContainerGuiLayout.playerInventoryY(3) + 5 * ContainerGuiLayout.SLOT_SIZE,
        menu.getSlot(27 + 53).y);
  }

  private static SizedContainerMenu menu(int nonEquipmentSize) {
    // MenuType is only stored, never touched on this path, so null keeps the
    // test free of registry bootstrap (vanilla MenuType statics need it).
    return new SizedContainerMenu(
        null, CHEST, 0, fakeInventory(nonEquipmentSize), new SimpleContainer(27));
  }

  private static TierSpec tier(int size, int rowLength) {
    return new TierSpec() {
      @Override
      public int size() {
        return size;
      }

      @Override
      public int rowLength() {
        return rowLength;
      }
    };
  }

  /** Flat-list Inventory stand-in sized like a mod-extended player inventory. */
  private static Inventory fakeInventory(int nonEquipmentSize) {
    NonNullList<ItemStack> items = NonNullList.withSize(nonEquipmentSize, ItemStack.EMPTY);
    return new Inventory(null, null) {
      @Override
      public NonNullList<ItemStack> getNonEquipmentItems() {
        return items;
      }

      @Override
      public int getContainerSize() {
        return items.size();
      }

      @Override
      public ItemStack getItem(int slot) {
        return items.get(slot);
      }

      @Override
      public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
      }

      @Override
      public void setChanged() {}

      @Override
      public boolean stillValid(Player player) {
        return true;
      }
    };
  }
}
