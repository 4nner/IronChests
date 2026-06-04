package anner.ironchest.blocks.blockentities;

import anner.ironchest.blocks.ChestTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class CrystalChestEntity extends GenericChestEntity {

    public CrystalChestEntity(BlockPos pos, BlockState state) {
        super(ChestTypes.CRYSTAL, pos, state);
    }

    public NonNullList<ItemStack> getTopStacks() {
        NonNullList<ItemStack> topStacks = NonNullList.withSize(12, ItemStack.EMPTY);
        int itemCount = 0;
        for (ItemStack stack : this.getItems()) {
            if (stack.isEmpty()) {
                continue;
            }
            topStacks.set(itemCount++, stack.copy());
            if (itemCount >= 12) {
                break;
            }
        }
        return topStacks;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().blockChanged(this.worldPosition);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }
}
