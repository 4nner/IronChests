package anner.ironchest.items;

import anner.ironchest.blocks.blockentities.ResizingContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;

public class UpgradeItem extends Item {
  private final UpgradeStrategy strategy;

  public UpgradeItem(UpgradeStrategy strategy, Properties properties) {
    super(properties);
    this.strategy = strategy;
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level level = context.getLevel();
    BlockPos blockPos = context.getClickedPos();
    BlockState state = level.getBlockState(blockPos);

    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    Player player = context.getPlayer();
    if (player == null) {
      return InteractionResult.PASS;
    }

    if (!player.mayBuild()) {
      return InteractionResult.PASS;
    }

    if (!this.strategy.canUpgrade(state)) {
      return InteractionResult.PASS;
    }

    BlockEntity blockEntity = level.getBlockEntity(blockPos);
    if (!(blockEntity instanceof ChestBlockEntity chest)) {
      return InteractionResult.PASS;
    }

    if (ChestBlockEntity.getOpenCount(level, blockPos) > 0 || !chest.stillValid(player)) {
      return InteractionResult.PASS;
    }

    BlockState oldState = state;
    CompoundTag oldChestTag = chest.saveWithoutMetadata(level.registryAccess());

    level.removeBlockEntity(blockPos);
    level.removeBlock(blockPos, false);
    level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(oldState));

    BlockState newState = this.strategy.resultState(oldState);
    level.setBlock(blockPos, newState, 3);
    level.sendBlockUpdated(blockPos, newState, newState, 3);

    BlockEntity newBlockEntity = level.getBlockEntity(blockPos);
    if (newBlockEntity != null) {
      ValueInput valueInput =
          TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), oldChestTag);
      newBlockEntity.loadWithComponents(valueInput);
      if (newBlockEntity instanceof ResizingContainer resizing) {
        resizing.clampInventoryToCapacity();
      }
      newBlockEntity.setChanged();
    }

    level.playSound(
        null,
        blockPos,
        oldState.getSoundType().getBreakSound(),
        SoundSource.BLOCKS,
        oldState.getSoundType().getVolume(),
        oldState.getSoundType().getPitch());
    level.playSound(
        null,
        blockPos,
        newState.getSoundType().getPlaceSound(),
        SoundSource.BLOCKS,
        newState.getSoundType().getVolume(),
        newState.getSoundType().getPitch());
    if (!player.getAbilities().instabuild) {
      context.getItemInHand().shrink(1);
    }
    return InteractionResult.SUCCESS;
  }
}
