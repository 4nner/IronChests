package anner.ironchest.blocks.blockentities;

import anner.ironchest.IronChests;
import anner.ironchest.blocks.ChestTypes;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;

public class CrystalChestEntity extends GenericChestEntity {

    private DefaultedList<ItemStack> topStacks;
    private boolean inventoryTouched;

    public CrystalChestEntity(BlockPos pos, BlockState state) {
        super(ChestTypes.CRYSTAL, pos, state);
        topStacks = DefaultedList.ofSize(12, ItemStack.EMPTY);
        inventoryTouched = true;
    }


    public void tick() {
        if (world != null && !world.isClient && this.inventoryTouched) {
            Collection<ServerPlayerEntity> viewers = PlayerLookup.tracking(this);
            getTopStacks();
            DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(12, ItemStack.EMPTY);
            for (int i = 0; i < 12; i++) {
                itemStacks.set(i, topStacks.get(i));
            }
            IronChests.UpdateInventory payload = new IronChests.UpdateInventory(pos, itemStacks);
            viewers.forEach(player -> ServerPlayNetworking.send(player, payload));
            inventoryTouched = false;
            topStacks.clear();
        }
    }

    private void getTopStacks() {
        int startIndex = 0;
        for (int i = 0; i < 12; i++) {
            DefaultedList<ItemStack> inv = getInventory();
            for (int j = startIndex; j < inv.size(); j++) {
                ItemStack stack = inv.get(j);
                if (stack.getItem() != Items.AIR) {
                    startIndex = j + 1;
                    topStacks.set(i, stack);
                    if (startIndex > inv.size()) {
                        return;
                    }
                    break;
                }
            }
        }
    }

    private DefaultedList<ItemStack> getInventory() {
        return super.getHeldStacks();
    }

    @Override
    public DefaultedList<ItemStack> getHeldStacks() {
        this.inventoryTouched = true;
        return super.getHeldStacks();
    }

    @Override
    public void setHeldStacks(DefaultedList<ItemStack> list) {
        super.setHeldStacks(list);
        this.inventoryTouched = true;
    }

    @Override
    public void onClose(PlayerEntity player) {
        super.onClose(player);
        this.inventoryTouched = true;
    }
}
