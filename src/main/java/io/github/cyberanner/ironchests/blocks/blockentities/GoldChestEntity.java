package io.github.cyberanner.ironchests.blocks.blockentities;

import io.github.cyberanner.ironchests.blocks.ChestTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class GoldChestEntity extends GenericChestEntity {
    public GoldChestEntity(BlockPos pos, BlockState state) {
        super(ChestTypes.GOLD, pos, state);
    }
}
