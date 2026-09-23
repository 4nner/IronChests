package anner.ironchest.screenhandlers;

import anner.ironchest.blocks.TierSpec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.jspecify.annotations.Nullable;

public final class ChestScreenHandler extends AbstractContainerMenu {
  private final Container container;
  private final TierSpec tier;
  private final @Nullable BlockPos chestBlockPos;

  public ChestScreenHandler(
      MenuType<?> menuType,
      TierSpec tier,
      int syncId,
      Inventory playerInventory,
      Container container) {
    super(menuType, syncId);
    checkContainerSize(container, tier.size());
    this.tier = tier;
    this.container = container;
    this.chestBlockPos =
        container instanceof ChestBlockEntity blockEntity ? blockEntity.getBlockPos() : null;
    container.startOpen(playerInventory.player);

    int columns = tier.rowLength();
    int rows = tier.rowCount();
    int slotIndex = 0;
    int chestSlotX = ChestGuiLayout.chestSlotStartX(columns);

    for (int row = 0; row < rows; row++) {
      int slotY = ChestGuiLayout.SLOT_SIZE + row * ChestGuiLayout.SLOT_SIZE;
      for (int column = 0; column < columns; column++) {
        this.addSlot(
            new Slot(
                container, slotIndex++, chestSlotX + column * ChestGuiLayout.SLOT_SIZE, slotY));
      }
    }

    int playerInventoryX = ChestGuiLayout.playerInventoryX(columns);
    int playerInventoryY = ChestGuiLayout.playerInventoryY(rows);
    for (int row = 0; row < 3; row++) {
      for (int column = 0; column < 9; column++) {
        this.addSlot(
            new Slot(
                playerInventory,
                column + row * 9 + 9,
                playerInventoryX + column * ChestGuiLayout.SLOT_SIZE,
                playerInventoryY + row * ChestGuiLayout.SLOT_SIZE));
      }
    }
    for (int column = 0; column < 9; column++) {
      this.addSlot(
          new Slot(
              playerInventory,
              column,
              playerInventoryX + column * ChestGuiLayout.SLOT_SIZE,
              playerInventoryY + ChestGuiLayout.HOTBAR_OFFSET));
    }
  }

  public int getChestRows() {
    return tier.rowCount();
  }

  public int getChestColumns() {
    return tier.rowLength();
  }

  public Container getBlockInventory() {
    return container;
  }

  public @Nullable BlockPos getChestBlockPos() {
    return chestBlockPos;
  }

  @Override
  public ItemStack quickMoveStack(Player player, int slotIndex) {
    ItemStack movedStack = ItemStack.EMPTY;
    Slot slot = this.slots.get(slotIndex);
    if (slot != null && slot.hasItem()) {
      ItemStack stackInSlot = slot.getItem();
      movedStack = stackInSlot.copy();
      int chestSlots = tier.size();
      if (slotIndex < chestSlots) {
        if (!this.moveItemStackTo(stackInSlot, chestSlots, this.slots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.moveItemStackTo(stackInSlot, 0, chestSlots, false)) {
        return ItemStack.EMPTY;
      }

      if (stackInSlot.isEmpty()) {
        slot.setByPlayer(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }
    }
    return movedStack;
  }

  @Override
  public boolean stillValid(Player player) {
    return container.stillValid(player);
  }

  @Override
  public void removed(Player player) {
    super.removed(player);
    container.stopOpen(player);
  }

  public static Container createClientContainer(TierSpec tier) {
    return new SimpleContainer(tier.size());
  }
}
