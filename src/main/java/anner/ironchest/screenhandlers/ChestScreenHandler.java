package anner.ironchest.screenhandlers;

import anner.ironchest.blocks.ChestTypes;
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

public class ChestScreenHandler extends AbstractContainerMenu {
    private final Container container;
    private final ChestTypes chestType;
    private final @Nullable BlockPos chestBlockPos;

    public ChestScreenHandler(MenuType<?> menuType, ChestTypes chestType, int syncId, Inventory playerInventory, Container container) {
        super(menuType, syncId);
        checkContainerSize(container, chestType.size);
        this.chestType = chestType;
        this.container = container;
        this.chestBlockPos = container instanceof ChestBlockEntity blockEntity ? blockEntity.getBlockPos() : null;
        container.startOpen(playerInventory.player);

        int columns = chestType.rowLength;
        int rows = chestType.getRowCount();
        int slotIndex = 0;
        int chestSlotX = ChestGuiLayout.chestSlotStartX(columns);

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                this.addSlot(new Slot(
                    container,
                    slotIndex++,
                    chestSlotX + column * ChestGuiLayout.SLOT_SIZE,
                    ChestGuiLayout.SLOT_SIZE + row * ChestGuiLayout.SLOT_SIZE
                ));
            }
        }

        int playerInventoryX = ChestGuiLayout.playerInventoryX(columns);
        int playerInventoryY = ChestGuiLayout.playerInventoryY(rows);
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(
                    playerInventory,
                    column + row * 9 + 9,
                    playerInventoryX + column * ChestGuiLayout.SLOT_SIZE,
                    playerInventoryY + row * ChestGuiLayout.SLOT_SIZE
                ));
            }
        }
        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(
                playerInventory,
                column,
                playerInventoryX + column * ChestGuiLayout.SLOT_SIZE,
                playerInventoryY + ChestGuiLayout.HOTBAR_OFFSET
            ));
        }
    }

    public int getChestRows() {
        return chestType.getRowCount();
    }

    public int getChestColumns() {
        return chestType.rowLength;
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
            int chestSlots = chestType.size;
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

    public static Container createClientContainer(ChestTypes chestType) {
        return new SimpleContainer(chestType.size);
    }
}
