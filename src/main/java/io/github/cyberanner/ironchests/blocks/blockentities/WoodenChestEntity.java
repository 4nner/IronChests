package io.github.cyberanner.ironchests.blocks.blockentities;

import io.github.cyberanner.ironchests.blocks.ChestTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class WoodenChestEntity extends GenericChestEntity {
    public WoodenChestEntity(BlockPos pos, BlockState state) {
        super(ChestTypes.WOOD, pos, state);
    }
}
