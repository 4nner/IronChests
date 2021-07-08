package io.github.cyberanner.ironchests.blocks.blockentities;

import io.github.cyberanner.ironchests.blocks.ChestTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class CopperChestEntity extends GenericChestEntity {
    public CopperChestEntity(BlockPos pos, BlockState state) {
        super(ChestTypes.COPPER, pos, state);
    }
}
