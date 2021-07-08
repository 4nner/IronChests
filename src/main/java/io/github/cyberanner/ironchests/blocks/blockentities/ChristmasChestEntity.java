package io.github.cyberanner.ironchests.blocks.blockentities;

import io.github.cyberanner.ironchests.blocks.ChestTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class ChristmasChestEntity extends GenericChestEntity {
    public ChristmasChestEntity(BlockPos pos, BlockState state) {
        super(ChestTypes.CHRISTMAS, pos, state);
    }
}