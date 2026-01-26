package anner.ironchest.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.enums.ChestType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import net.minecraft.world.World;

public class GenericChestBlock extends ChestBlock {
    private final ChestTypes type;

    public GenericChestBlock(Settings settings, ChestTypes type) {
        super(type::getBlockEntityType, getOpenSound(type), getCloseSound(type), settings);
        this.type = type;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return this.type.makeEntity(pos, state);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getHorizontalPlayerFacing().getOpposite();
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return this.getDefaultState()
            .with(FACING, direction)
            .with(CHEST_TYPE, ChestType.SINGLE)
            .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    public ChestTypes getType() {
        return type;
    }

    private static SoundEvent getOpenSound(ChestTypes type) {
        return usesCopperChestSound(type) ? SoundEvents.BLOCK_COPPER_CHEST_OPEN : SoundEvents.BLOCK_CHEST_OPEN;
    }

    private static SoundEvent getCloseSound(ChestTypes type) {
        return usesCopperChestSound(type) ? SoundEvents.BLOCK_COPPER_CHEST_CLOSE : SoundEvents.BLOCK_CHEST_CLOSE;
    }

    private static boolean usesCopperChestSound(ChestTypes type) {
        return type == ChestTypes.COPPER
            || type == ChestTypes.IRON
            || type == ChestTypes.GOLD
            || type == ChestTypes.NETHERITE;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        BlockState updatedState = super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
        return updatedState.with(CHEST_TYPE, ChestType.SINGLE);
    }

    @Override
    public boolean canMergeWith(BlockState state) {
        return false;
    }

    @Override
    public DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> getBlockEntitySource(BlockState state, World world, BlockPos pos, boolean ignoreBlocked) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ChestBlockEntity chest) {
            return new DoubleBlockProperties.PropertySource.Single<>(chest);
        }
        return new DoubleBlockProperties.PropertySource<ChestBlockEntity>() {
            @Override
            public <T> T apply(DoubleBlockProperties.PropertyRetriever<? super ChestBlockEntity, T> retriever) {
                return retriever.getFallback();
            }
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return super.getTicker(world, state, type);
    }
}
