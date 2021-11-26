package io.github.cyberanner.ironchests.items;

import io.github.cyberanner.ironchests.IronChests;
import io.github.cyberanner.ironchests.blocks.ChestTypes;
import io.github.cyberanner.ironchests.blocks.blockentities.GenericChestEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class UpgradeItem extends Item {

    UpgradeTypes type;

    public UpgradeItem(UpgradeTypes type) {
        super(new Item.Settings().group(IronChests.TAB));
        this.type = type;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        World world = context.getWorld();
        if (world.isClient) { return ActionResult.PASS; }

        PlayerEntity entityPlayer = context.getPlayer();
        if (entityPlayer == null) { return ActionResult.PASS; }

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

        if (this.type.canUpgrade(ChestTypes.WOOD)) {
            if (!(blockEntity instanceof ChestBlockEntity)) {
                return ActionResult.PASS;
            }
        }

        ItemStack itemStack = context.getStack();
        Text customName;
        Direction chestFacing;

        if (blockEntity != null) {
            GenericChestEntity chest = (GenericChestEntity) blockEntity;

            if (ChestBlockEntity.getPlayersLookingInChestCount(world, blockPos) > 0) { return ActionResult.PASS; }
            if (!chest.canPlayerUse(entityPlayer)) { return ActionResult.PASS; }

            chestFacing = world.getBlockState(blockPos).get(ChestBlock.FACING);
            customName = chest.getCustomName();
            world.removeBlockEntity(blockPos);
            world.removeBlock(blockPos, false);

            BlockState blockState = ChestTypes.get(type.target).getDefaultState().with(ChestBlock.FACING, chestFacing).with(ChestBlock.WATERLOGGED, false);
            NbtCompound oldChestTag = new NbtCompound();
            chest.writeNbt(oldChestTag);
            world.setBlockState(blockPos, blockState, 3);
            world.updateListeners(blockPos, blockState, blockState, 3);

            world.getBlockEntity(blockPos).readNbt(oldChestTag);
            itemStack.decrement(1);

            if (customName != null) {
                ((ChestBlockEntity) world.getBlockEntity(blockPos)).setCustomName(customName);
            }
        }
        return ActionResult.PASS;
    }
}