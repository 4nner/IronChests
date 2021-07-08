package io.github.cyberanner.ironchests.blocks.blockentities;

import io.github.cyberanner.ironchests.blocks.ChestTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class DiamondChestEntity extends GenericChestEntity {
    public DiamondChestEntity(BlockPos pos, BlockState state) {
        super(ChestTypes.DIAMOND, pos, state);
    }
}