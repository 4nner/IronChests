package anner.ironchest.items;

import anner.ironchest.IronChests;
import anner.ironchest.blocks.ChestTypes;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldEvents;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.storage.NbtReadView;
import net.minecraft.world.World;

public class UpgradeItem extends Item {

    UpgradeTypes type;

    public UpgradeItem(UpgradeTypes type, Item.Settings settings) {
        super(settings);
        ItemGroupEvents.modifyEntriesEvent(IronChests.TAB).register(entries -> entries.add(this));
        this.type = type;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        World world = context.getWorld();
        if (world.isClient()) {
            return ActionResult.PASS;
        }

        PlayerEntity entityPlayer = context.getPlayer();
        if (entityPlayer == null) {
            return ActionResult.PASS;
        }

        BlockPos blockPos = context.getBlockPos();
        if (this.type.canUpgrade(ChestTypes.WOOD)) {
            if (!(world.getBlockState(blockPos).getBlock() instanceof ChestBlock)) {
                return ActionResult.PASS;
            }
        } else {
            if (world.getBlockState(blockPos).getBlock().getDefaultState() != ChestTypes.get(this.type.source).getDefaultState()) {
                return ActionResult.PASS;
            }
        }

        BlockEntity blockEntity = world.getBlockEntity(blockPos);

        ItemStack itemStack = context.getStack();
        Direction chestFacing;

        if (blockEntity != null) {
            ChestBlockEntity chest = (ChestBlockEntity) blockEntity;

            if (ChestBlockEntity.getPlayersLookingInChestCount(world, blockPos) > 0) {
                return ActionResult.PASS;
            }
            if (!chest.canPlayerUse(entityPlayer)) {
                return ActionResult.PASS;
            }

            BlockState oldState = world.getBlockState(blockPos);
            chestFacing = world.getBlockState(blockPos).get(ChestBlock.FACING);
            world.removeBlockEntity(blockPos);
            world.removeBlock(blockPos, false);
            world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(oldState));

            BlockState blockState = ChestTypes.get(type.target).getDefaultState().with(ChestBlock.FACING, chestFacing).with(ChestBlock.WATERLOGGED, false);
            NbtCompound oldChestTag = chest.createNbt(world.getRegistryManager());
            world.setBlockState(blockPos, blockState, 3);
            world.updateListeners(blockPos, blockState, blockState, 3);
            BlockEntity newBlockEntity = world.getBlockEntity(blockPos);
            if (newBlockEntity != null) {
                newBlockEntity.readComponentlessData(
                    NbtReadView.create(ErrorReporter.EMPTY, world.getRegistryManager(), oldChestTag)
                );
                newBlockEntity.markDirty();
            }
            BlockSoundGroup oldSounds = oldState.getSoundGroup();
            BlockSoundGroup newSounds = blockState.getSoundGroup();
            world.playSound(null, blockPos, oldSounds.getBreakSound(), SoundCategory.BLOCKS, oldSounds.getVolume(), oldSounds.getPitch());
            world.playSound(null, blockPos, newSounds.getPlaceSound(), SoundCategory.BLOCKS, newSounds.getVolume(), newSounds.getPitch());
            itemStack.decrement(1);
        }
        return ActionResult.PASS;
    }

}
