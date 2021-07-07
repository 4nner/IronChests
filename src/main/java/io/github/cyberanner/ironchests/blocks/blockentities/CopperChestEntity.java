package io.github.cyberanner.ironchests.blocks.blockentities;

import io.github.cyberanner.ironchests.blocks.ChestTypes;
import io.github.cyberanner.ironchests.registry.ModBlockEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class CopperChestEntity extends GenericChestEntity {
    public CopperChestEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityType.COPPER_CHEST, pos, state, ChestTypes.COPPER);
    }
}