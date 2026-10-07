package com.gathertocraft.ironchest.blocks;

import com.gathertocraft.ironcore.lock.KeyItem;
import com.gathertocraft.ironcore.lock.LockGuards;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

public class GenericChestBlock extends ChestBlock {
  private final ChestTypes type;
  private static boolean openUnderSolidBlocks;

  public GenericChestBlock(BlockBehaviour.Properties properties, ChestTypes type) {
    super(type::getBlockEntityType, getOpenSound(type), getCloseSound(type), properties);
    this.type = type;
  }

  /** Applies the openUnderSolidBlocks config flag; chests open under solid blocks when true. */
  public static void setOpenUnderSolidBlocks(boolean allow) {
    openUnderSolidBlocks = allow;
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

  @Override
  protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
    if (openUnderSolidBlocks && isBlockedByBlockOnly(level, pos)) {
      // Our chests are always single, so the block entity itself is the menu provider,
      // mirroring vanilla's acceptSingle path without the solid-block gate.
      BlockEntity entity = level.getBlockEntity(pos);
      if (entity instanceof MenuProvider provider) {
        return provider;
      }
    }
    return super.getMenuProvider(state, level, pos);
  }

  /** Deny opening for players outside the lock. Key holders defer to the key item. */
  @Override
  protected InteractionResult useWithoutItem(
      BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
    if (!level.isClientSide() && LockGuards.isLockedFor(level, pos, player)) {
      LockGuards.denyLocked(level, pos, player);
      return InteractionResult.FAIL;
    }
    return super.useWithoutItem(state, level, pos, player, hit);
  }

  @Override
  protected InteractionResult useItemOn(
      ItemStack stack,
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult hit) {
    if (stack.getItem() instanceof KeyItem) {
      return InteractionResult.PASS;
    }
    if (!level.isClientSide() && LockGuards.isLockedFor(level, pos, player)) {
      LockGuards.denyLocked(level, pos, player);
      return InteractionResult.FAIL;
    }
    return super.useItemOn(stack, state, level, pos, player, hand, hit);
  }

  /** Stall survival mining for outsiders. Creative breaks stop at the loader break events. */
  @Override
  protected float getDestroyProgress(
      BlockState state, Player player, BlockGetter level, BlockPos pos) {
    if (LockGuards.isLockedFor(level, pos, player)) {
      return 0.0F;
    }
    return super.getDestroyProgress(state, player, level, pos);
  }

  /**
   * True when a solid block sits above the chest but no cat is sitting on it. Mirrors {@code
   * ChestBlock} internals (the {@code isBlockedChestByBlock} half of {@code isChestBlockedAt} plus
   * its sitting-cat check), both private in vanilla.
   */
  private static boolean isBlockedByBlockOnly(Level level, BlockPos pos) {
    BlockPos above = pos.above();
    if (!level.getBlockState(above).isRedstoneConductor(level, above)) {
      return false;
    }
    List<Cat> cats = level.getEntitiesOfClass(Cat.class, new AABB(above));
    for (Cat cat : cats) {
      if (cat.isInSittingPose()) {
        return false;
      }
    }
    return true;
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
  protected BlockState updateShape(
      BlockState state,
      LevelReader level,
      net.minecraft.world.level.ScheduledTickAccess tickView,
      BlockPos pos,
      Direction direction,
      BlockPos neighborPos,
      BlockState neighborState,
      RandomSource random) {
    return super.updateShape(
            state, level, tickView, pos, direction, neighborPos, neighborState, random)
        .setValue(TYPE, ChestType.SINGLE);
  }
}
