package io.github.cyberanner.ironchests.blocks;

import io.github.cyberanner.ironchests.blocks.blockentities.GenericChestEntity;
import io.github.cyberanner.ironchests.registry.ModBlockEntityType;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class GenericChestBlock extends ChestBlock {

    private final ChestTypes type;

    public GenericChestBlock(FabricBlockSettings settings, ChestTypes type) {
        super(settings, type::getBlockEntityType);
        this.type = type;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return this.type.makeEntity(pos, state);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getPlayerFacing().getOpposite();
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return this.getDefaultState().with(FACING, direction).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    public ChestTypes getType() {
        return type;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? checkType(type, ModBlockEntityType.COPPER_CHEST, GenericChestEntity::clientTick) : null;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof GenericChestEntity) {
            ((GenericChestEntity)blockEntity).onScheduledTick();
        }
    }
}