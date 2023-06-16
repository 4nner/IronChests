package anner.ironchest.blocks.blockentities;

import anner.ironchest.blocks.ChestTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class NetheriteChestEntity extends GenericChestEntity {
    public NetheriteChestEntity(BlockPos pos, BlockState state) {
        super(ChestTypes.NETHERITE, pos, state);
    }
}
