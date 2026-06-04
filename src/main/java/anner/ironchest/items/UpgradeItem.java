package anner.ironchest.items;

import anner.ironchest.blocks.ChestTypes;
import anner.ironchest.blocks.blockentities.GenericChestEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.TagValueInput;

public class UpgradeItem extends Item {
    private final UpgradeTypes type;

    public UpgradeItem(UpgradeTypes type, Properties properties) {
        super(properties);
        this.type = type;
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

        if (!canUpgrade(state)) {
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
        Direction chestFacing = oldState.getValue(ChestBlock.FACING);
        CompoundTag oldChestTag = chest.saveWithoutMetadata(level.registryAccess());

        level.removeBlockEntity(blockPos);
        level.removeBlock(blockPos, false);
        level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(oldState));

        BlockState newState = this.type.target.getBlock().defaultBlockState()
            .setValue(ChestBlock.FACING, chestFacing)
            .setValue(ChestBlock.WATERLOGGED, oldState.getValue(ChestBlock.WATERLOGGED));
        level.setBlock(blockPos, newState, 3);
        level.sendBlockUpdated(blockPos, newState, newState, 3);

        BlockEntity newBlockEntity = level.getBlockEntity(blockPos);
        if (newBlockEntity != null) {
            ValueInput valueInput = TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), oldChestTag);
            newBlockEntity.loadWithComponents(valueInput);
            if (newBlockEntity instanceof GenericChestEntity genericChest) {
                genericChest.clampInventoryToCapacity();
            }
            newBlockEntity.setChanged();
        }

        level.playSound(null, blockPos, oldState.getSoundType().getBreakSound(), SoundSource.BLOCKS, oldState.getSoundType().getVolume(), oldState.getSoundType().getPitch());
        level.playSound(null, blockPos, newState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, newState.getSoundType().getVolume(), newState.getSoundType().getPitch());
        if (!player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    private boolean canUpgrade(BlockState state) {
        if (this.type.source == ChestTypes.WOOD) {
            return state.is(Blocks.CHEST) || state.is(Blocks.TRAPPED_CHEST);
        }
        return state.is(this.type.source.getBlock());
    }
}
