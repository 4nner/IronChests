package anner.ironchest.blocks.blockentities;

import anner.ironchest.blocks.ChestTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class DiamondChestEntity extends GenericChestEntity {
    public DiamondChestEntity(BlockPos pos, BlockState state) {
        super(ChestTypes.DIAMOND, pos, state);
    }
}