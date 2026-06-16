package anner.ironchest.blocks.blockentities;

import anner.ironchest.blocks.ChestTypes;
import anner.ironchest.screenhandlers.ChestScreenHandler;
import anner.ironchest.util.ChestInventorySanitizer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.List;

public class GenericChestEntity extends ChestBlockEntity {
    private final ChestTypes type;
    private final List<ItemStack> pendingOverflow = new ArrayList<>();

    // setItems() resolves to ChestBlockEntity.setItems() — a plain field assignment,
    // no virtual dispatch into subclass code. Safe to call before subclass is fully initialized.
    @SuppressWarnings("this-escape")
    public GenericChestEntity(ChestTypes type, BlockPos pos, BlockState state) {
        super(type.getBlockEntityType(), pos, state);
        this.type = type;
        setItems(NonNullList.withSize(type.size, ItemStack.EMPTY));
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
        return new ChestScreenHandler(this.type.getMenuType(), this.type, syncId, inventory, this);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public int getContainerSize() {
        return this.type.size;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, ChestInventorySanitizer.sanitize(stack));
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.clampInventoryToCapacity();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        ChestInventorySanitizer.sanitize(this.getItems());
        super.saveAdditional(output);
    }

    public void clampInventoryToCapacity() {
        int capacity = this.getContainerSize();
        NonNullList<ItemStack> items = this.getItems();
        if (items.size() == capacity) {
            ChestInventorySanitizer.sanitize(items);
            this.setChanged();
            return;
        }

        NonNullList<ItemStack> clamped = NonNullList.withSize(capacity, ItemStack.EMPTY);
        for (int slot = 0; slot < Math.min(items.size(), capacity); slot++) {
            clamped.set(slot, items.get(slot));
        }

        Level level = this.getLevel();
        BlockPos pos = this.getBlockPos();
        for (int slot = capacity; slot < items.size(); slot++) {
            ItemStack overflow = items.get(slot);
            if (!overflow.isEmpty()) {
                this.placeOverflow(clamped, overflow, capacity, level, pos);
            }
        }

        this.setItems(clamped);
        ChestInventorySanitizer.sanitize(clamped);
        this.setChanged();
    }

    private void placeOverflow(NonNullList<ItemStack> inventory, ItemStack stack, int capacity, Level level, BlockPos pos) {
        for (int slot = 0; slot < capacity; slot++) {
            if (inventory.get(slot).isEmpty()) {
                inventory.set(slot, stack);
                return;
            }
        }
        if (level != null) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        } else {
            this.pendingOverflow.add(stack);
        }
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (level == null || level.isClientSide() || this.pendingOverflow.isEmpty()) {
            return;
        }
        BlockPos pos = this.getBlockPos();
        for (ItemStack stack : this.pendingOverflow) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
        this.pendingOverflow.clear();
    }
}
