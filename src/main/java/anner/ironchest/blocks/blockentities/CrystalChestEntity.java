package anner.ironchest.blocks.blockentities;

import anner.ironchest.blocks.ChestTypes;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class CrystalChestEntity extends GenericChestEntity {

    private final DefaultedList<ItemStack> topStacks = DefaultedList.ofSize(12, ItemStack.EMPTY);

    public CrystalChestEntity(BlockPos pos, BlockState state) {
        super(ChestTypes.CRYSTAL, pos, state);
    }

    public DefaultedList<ItemStack> getTopStacks() {
        topStacks.clear();
        int itemCount = 0;
        for (ItemStack stack : getHeldStacks()) {
            if (stack.isEmpty()) continue;
            topStacks.set(itemCount++, stack);
            if (itemCount >= 12) break;
        }
        return topStacks;
    }
}
