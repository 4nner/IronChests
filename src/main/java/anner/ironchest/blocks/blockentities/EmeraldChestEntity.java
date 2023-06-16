package anner.ironchest.blocks.blockentities;

import anner.ironchest.blocks.ChestTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class EmeraldChestEntity extends GenericChestEntity {
    public EmeraldChestEntity(BlockPos pos, BlockState state) {
        super(ChestTypes.EMERALD, pos, state);
    }
}
