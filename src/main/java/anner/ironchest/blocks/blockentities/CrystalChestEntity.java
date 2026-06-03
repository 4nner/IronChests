package anner.ironchest.blocks.blockentities;

import anner.ironchest.blocks.ChestTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

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
            topStacks.set(itemCount++, stack);
            if (itemCount >= 12) {
                break;
            }
        }
        return topStacks;
    }
}
