package io.github.cyberanner.ironchests.blocks.blockentities;

import io.github.cyberanner.ironchests.blocks.ChestTypes;
import io.github.cyberanner.ironchests.screenhandlers.ChestScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ChestLidAnimator;
import net.minecraft.block.entity.ChestStateManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GenericChestEntity extends ChestBlockEntity {
    private static final int VIEWER_COUNT_UPDATE_EVENT = 1;
    ChestTypes type;
    /*
    private final ChestLidAnimator lidAnimator = new ChestLidAnimator();
    private final ChestStateManager stateManager = new ChestStateManager() {
        boolean isChestOpen;
        protected void onChestOpened(World world, BlockPos pos, BlockState state) {
            isChestOpen = true;
            playSound(world, pos, SoundEvents.BLOCK_CHEST_OPEN);
        }

        protected void onChestClosed(World world, BlockPos pos, BlockState state) {
            isChestOpen = false;
            playSound(world, pos, SoundEvents.BLOCK_CHEST_CLOSE);
        }

        protected void onInteracted(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
            world.addSyncedBlockEvent(GenericChestEntity.this.pos, ChestTypes.get(type), VIEWER_COUNT_UPDATE_EVENT, newViewerCount);
        }

        protected boolean isPlayerViewing(PlayerEntity player) {
            return isChestOpen;
        }
    };
*/
    public GenericChestEntity(ChestTypes type, BlockPos pos, BlockState state) {
        super(type.getBlockEntityType(), pos, state);
        this.type = type;
        this.setInvStackList(DefaultedList.ofSize(this.size(), ItemStack.EMPTY));
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
        return new ChestScreenHandler(type.getScreenHandlerType(), type, syncId, inventory, ScreenHandlerContext.create(world, pos));
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory inventory) {
        return new ChestScreenHandler(type.getScreenHandlerType(), type, syncId, inventory, ScreenHandlerContext.create(world, pos));
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public int size() {
        return type.size;
    }

    public ChestTypes type() {
        return type;
    }

/*
    public static void clientTick(World world, BlockPos pos, BlockState state, GenericChestEntity blockEntity) {
        blockEntity.lidAnimator.step();
    }
    public boolean onSyncedBlockEvent(int type, int data) {
        if (type == VIEWER_COUNT_UPDATE_EVENT) {
            this.lidAnimator.setOpen(data > 0);
            return true;
        } else {
            return super.onSyncedBlockEvent(type, data);
        }
    }

    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.openChest(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.closeChest(player, this.getWorld(), this.getPos(), this.getCachedState());
        }

    }

    public void onScheduledTick() {
        if (!this.removed) {
            this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    public float getAnimationProgress(float tickDelta) {
        return this.lidAnimator.getProgress(tickDelta);
    }

 */

    @Environment(EnvType.CLIENT)
    private void playSound(World world, BlockPos pos, SoundEvent sound) {
        world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, sound, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
    }

}