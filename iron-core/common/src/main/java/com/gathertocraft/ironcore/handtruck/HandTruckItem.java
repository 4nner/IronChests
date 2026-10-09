package com.gathertocraft.ironcore.handtruck;

import com.gathertocraft.ironcore.ResizingContainer;
import com.gathertocraft.ironcore.config.CoreConfig;
import com.gathertocraft.ironcore.lock.LockGuards;
import com.gathertocraft.ironcore.registry.ModDataComponents;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;

/**
 * Sneak-right-click a container to lift it onto the truck; right-click the ground to set it back
 * down with contents (and lock) intact. The basic truck never lifts spawners.
 */
public class HandTruckItem extends Item {
  private final boolean enhanced;

  public HandTruckItem(Properties properties, boolean enhanced) {
    super(properties);
    this.enhanced = enhanced;
  }

  /**
   * Hand trucks never fit inside container items (bundles, shulker-box items). The method takes no
   * stack, so empty trucks are excluded too; block inventories are unaffected and handled by the
   * loaded-truck guards instead.
   */
  @Override
  public boolean canFitInsideContainerItems() {
    return false;
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level level = context.getLevel();
    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }
    Player player = context.getPlayer();
    if (player == null) {
      return InteractionResult.PASS;
    }
    ItemStack stack = context.getItemInHand();
    CarriedBlock carried = stack.get(ModDataComponents.carriedBlock());
    if (carried != null) {
      return place(context, level, player, stack, carried);
    }
    if (!player.isSecondaryUseActive()) {
      return InteractionResult.PASS;
    }
    return pickup(context, level, player, stack);
  }

  private InteractionResult pickup(
      UseOnContext context, Level level, Player player, ItemStack stack) {
    BlockPos pos = context.getClickedPos();
    BlockState state = level.getBlockState(pos);
    BlockEntity entity = level.getBlockEntity(pos);
    if (entity == null) {
      return InteractionResult.PASS;
    }
    if (!player.mayBuild() || !level.mayInteract(player, pos)) {
      return InteractionResult.PASS;
    }
    boolean spawner = HandTruckSupport.isSpawner(entity);
    boolean container = entity instanceof Container;
    HandTruckSupport.Deny deny =
        HandTruckSupport.check(
            spawner,
            container,
            !spawner && HandTruckSupport.isDoubleChest(state),
            !spawner && HandTruckSupport.isLockedFor(level, pos, player),
            !spawner && HandTruckSupport.isInUse(entity, player),
            !spawner
                && entity instanceof Container picked
                && HandTruckSupport.containsLoadedTruck(picked),
            this.enhanced,
            CoreConfig.handTruckMovesSpawners());
    if (deny == HandTruckSupport.Deny.LOCKED) {
      LockGuards.denyLocked(level, pos, player);
      return InteractionResult.FAIL;
    }
    if (deny != HandTruckSupport.Deny.ALLOW) {
      player.sendOverlayMessage(Component.translatable(denyKey(deny)));
      return InteractionResult.FAIL;
    }

    CompoundTag beTag = entity.saveWithoutMetadata(level.registryAccess());
    CompoundTag stateTag = NbtUtils.writeBlockState(state);
    Identifier blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());

    level.removeBlockEntity(pos);
    level.removeBlock(pos, false);
    level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
    level.playSound(
        null,
        pos,
        state.getSoundType().getBreakSound(),
        SoundSource.BLOCKS,
        state.getSoundType().getVolume(),
        state.getSoundType().getPitch());

    stack.set(
        ModDataComponents.carriedBlock(), new CarriedBlock(blockId, stateTag, beTag, spawner));
    return InteractionResult.SUCCESS_SERVER;
  }

  private static InteractionResult place(
      UseOnContext context, Level level, Player player, ItemStack stack, CarriedBlock carried) {
    if (!player.mayBuild()) {
      return InteractionResult.PASS;
    }
    BlockPos clicked = context.getClickedPos();
    BlockPlaceContext placeContext = new BlockPlaceContext(context);
    BlockPos target =
        level.getBlockState(clicked).canBeReplaced(placeContext)
            ? clicked
            : clicked.relative(context.getClickedFace());
    if (!level.mayInteract(player, target)
        || !level.getBlockState(target).canBeReplaced(placeContext)) {
      player.sendOverlayMessage(Component.translatable("message.ironcore.hand_truck.no_room"));
      return InteractionResult.FAIL;
    }

    BlockState stored;
    try {
      stored =
          NbtUtils.readBlockState(
              level.registryAccess().lookupOrThrow(Registries.BLOCK), carried.stateTag());
    } catch (RuntimeException malformed) {
      return InteractionResult.FAIL;
    }
    BlockState oriented = orient(stored, player, level, target);
    if (!oriented.canSurvive(level, target)) {
      player.sendOverlayMessage(Component.translatable("message.ironcore.hand_truck.no_room"));
      return InteractionResult.FAIL;
    }

    level.setBlock(target, oriented, 3);
    level.sendBlockUpdated(target, oriented, oriented, 3);
    BlockEntity fresh = level.getBlockEntity(target);
    if (fresh != null) {
      ValueInput input =
          TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), carried.beTag());
      fresh.loadWithComponents(input);
      if (fresh instanceof ResizingContainer resizing) {
        resizing.clampInventoryToCapacity();
      }
      fresh.setChanged();
    }
    level.playSound(
        null,
        target,
        oriented.getSoundType().getPlaceSound(),
        SoundSource.BLOCKS,
        oriented.getSoundType().getVolume(),
        oriented.getSoundType().getPitch());

    stack.remove(ModDataComponents.carriedBlock());
    if (!player.getAbilities().instabuild) {
      stack.hurtAndBreak(HandTruckSupport.moveCost(carried), player, context.getHand());
    }
    return InteractionResult.SUCCESS_SERVER;
  }

  /** Keeps the lifted facing but turns horizontal facings toward the placer's view. */
  private static BlockState orient(BlockState stored, Player player, Level level, BlockPos target) {
    BlockState oriented = stored;
    Direction facing = player.getDirection().getOpposite();
    if (oriented.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
      oriented = oriented.setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
    } else if (oriented.hasProperty(BlockStateProperties.FACING)
        && BlockStateProperties.FACING.getPossibleValues().contains(facing)) {
      oriented = oriented.setValue(BlockStateProperties.FACING, facing);
    }
    if (oriented.hasProperty(BlockStateProperties.WATERLOGGED)) {
      oriented =
          oriented.setValue(
              BlockStateProperties.WATERLOGGED,
              level.getFluidState(target).getType() == Fluids.WATER);
    }
    return oriented;
  }

  private static String denyKey(HandTruckSupport.Deny deny) {
    return switch (deny) {
      case NOT_MOVABLE -> "message.ironcore.hand_truck.not_movable";
      case DOUBLE_CHEST -> "message.ironcore.hand_truck.double_chest";
      case IN_USE -> "message.ironcore.hand_truck.in_use";
      case NESTED -> "message.ironcore.hand_truck.nested";
      case SPAWNER_WRONG_TRUCK -> "message.ironcore.hand_truck.spawner_basic";
      case SPAWNER_DISABLED -> "message.ironcore.hand_truck.spawner_disabled";
      default -> "message.ironcore.hand_truck.not_movable";
    };
  }

  /** Tooltip lines: hint when empty, carried block name when loaded. */
  public static List<Component> tooltipLines(ItemStack stack) {
    CarriedBlock carried = stack.get(ModDataComponents.carriedBlock());
    if (carried == null) {
      return List.of(
          Component.translatable("tooltip.ironcore.hand_truck.empty")
              .withStyle(ChatFormatting.GRAY));
    }
    Block block = BuiltInRegistries.BLOCK.getValue(carried.blockId());
    return List.of(
        Component.translatable("tooltip.ironcore.hand_truck.carrying", block.getName())
            .withStyle(ChatFormatting.GOLD));
  }
}
