package io.github.cyberanner.ironchests.screenhandlers;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cyberanner.ironchests.blocks.ChestTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class ChestScreenHandler extends SyncedGuiDescription {
    Inventory inventory;

    public ChestScreenHandler(ScreenHandlerType<?> type, ChestTypes chestType, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, getBlockInventory(context, chestType.size), null);
        inventory = blockInventory;
        inventory.onOpen(playerInventory.player);
        int rows = chestType.getRowCount();
        int length = chestType.rowLength;

        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);

        WItemSlot itemSlot;
        int counter = 0;
        if (chestType.rowLength == 1) {
            itemSlot = WItemSlot.of(blockInventory, 0);
            itemSlot.setFilter(stack -> stack.getItem() == Items.DIRT);
            root.add(itemSlot, (18 * 4), 12);
        } else {
            for (int j = 0; j < rows; j++) {
                for (int i = 0; i < length; i++) {
                    itemSlot = WItemSlot.of(blockInventory, counter);
                    root.add(itemSlot, (18 * i), 12 + (18 * j));
                    counter++;
                }
            }
        }

        // Sets the correct GUI Size
        //root.setSize(chestType.xSize, chestType.ySize);
        root.setInsets(Insets.ROOT_PANEL);

        int height = 15;
        height += 18 * (chestType.size / length);

        int width = 0;
        if (chestType.rowLength > 9) {
            width = 9 * (chestType.rowLength - 9);
        }

        root.add(this.createPlayerInventoryPanel(), width, height);

        root.validate(this);
    }


    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return stack.getItem() == Items.DIRT;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.inventory.onClose(player);
    }
}
