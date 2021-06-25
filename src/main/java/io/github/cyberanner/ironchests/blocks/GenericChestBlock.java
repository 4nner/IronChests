package io.github.cyberanner.ironchests.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;


public class GenericChestBlock extends Block {
    public GenericChestBlock(FabricBlockSettings settings) {
        super(settings);
    }

    // A side effect of extending BlockWithEntity is it changes the render type to INVISIBLE, so we have to revert this
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    // Avoids adjacent blocks from hiding their faces.
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
    }
}
