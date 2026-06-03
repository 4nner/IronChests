package anner.ironchest.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class GenericChestBlock extends ChestBlock {
    private final ChestTypes type;

    public GenericChestBlock(BlockBehaviour.Properties properties, ChestTypes type) {
        super(type::getBlockEntityType, getOpenSound(type), getCloseSound(type), properties);
        this.type = type;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.type.createBlockEntity(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
            .setValue(FACING, direction)
            .setValue(TYPE, ChestType.SINGLE)
            .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    public ChestTypes getType() {
        return type;
    }

    private static SoundEvent getOpenSound(ChestTypes type) {
        return usesCopperChestSound(type) ? SoundEvents.COPPER_CHEST_OPEN : SoundEvents.CHEST_OPEN;
    }

    private static SoundEvent getCloseSound(ChestTypes type) {
        return usesCopperChestSound(type) ? SoundEvents.COPPER_CHEST_CLOSE : SoundEvents.CHEST_CLOSE;
    }

    private static boolean usesCopperChestSound(ChestTypes type) {
        return type == ChestTypes.COPPER
            || type == ChestTypes.IRON
            || type == ChestTypes.GOLD
            || type == ChestTypes.NETHERITE;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, net.minecraft.world.level.ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return super.updateShape(state, level, tickView, pos, direction, neighborPos, neighborState, random)
            .setValue(TYPE, ChestType.SINGLE);
    }
}
