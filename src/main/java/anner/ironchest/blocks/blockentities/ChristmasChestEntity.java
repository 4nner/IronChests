package anner.ironchest.blocks.blockentities;

import anner.ironchest.blocks.ChestTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class ChristmasChestEntity extends GenericChestEntity {
    public ChristmasChestEntity(BlockPos pos, BlockState state) {
        super(ChestTypes.CHRISTMAS, pos, state);
    }
}