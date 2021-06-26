/*
package io.github.cyberanner.ironchests.blocks.blockentities;

import io.github.cyberanner.ironchests.blocks.ChestTypes;
import io.github.cyberanner.ironchests.blocks.GenericChestBlock;
import io.github.cyberanner.ironchests.implementations.ImplementedInventory;
import io.github.cyberanner.ironchests.registry.ModBlockEntityType;
import io.github.cyberanner.ironchests.registry.ModScreenHandlerType;
import io.github.cyberanner.ironchests.screenhandlers.ChestScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.api.EnvironmentInterfaces;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;


@EnvironmentInterfaces({@EnvironmentInterface(
        value = EnvType.CLIENT,
        itf = ChestAnimationProgress.class
)})
public class GenericChestEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory, ChestAnimationProgress {

    private DefaultedList<ItemStack> chestContents;
    protected float lidAngle;
    protected float prevLidAngle;
    public int numPlayersUsing;
    private final int inventorySize;
    ChestTypes type;


    public GenericChestEntity(BlockPos pos, BlockState state, ChestTypes type) {
        super(ModBlockEntityType.COPPER_CHEST, pos, state);
        inventorySize = type.size;
        this.type = type;
        chestContents = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
        return new ChestScreenHandler(ModScreenHandlerType.IRON_CHEST, type, syncId, inventory, ScreenHandlerContext.create(world, pos));
    }
    //From the ImplementedInventory Interface
    @Override
    public DefaultedList<ItemStack> getItems() {
        return chestContents;

    }

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);

    //These Methods are from the NamedScreenHandlerFactory Interface
    //createMenu creates the ScreenHandler itself
    //getDisplayName will Provide its name which is normally shown at the top
    @Override
    public Text getDisplayName() {
        // Using the block name as the screen title
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }


    @Override
    public int size() {
        return inventorySize;
    }

    @Environment(EnvType.CLIENT)
    public float getAnimationProgress(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevLidAngle, this.lidAngle);
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.chestContents) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.chestContents);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.chestContents);
        return nbt;
    }


    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if (type == 1) {
            this.numPlayersUsing = data;
            return true;
        } else {
            return super.onSyncedBlockEvent(type, data);
        }
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }
            ++this.numPlayersUsing;
            this.onInvOpenOrClose();
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!player.isSpectator()) {
            --this.numPlayersUsing;
            this.onInvOpenOrClose();
        }
    }

    protected void onInvOpenOrClose() {
        Block block = this.getCachedState().getBlock();
        if (block instanceof GenericChestBlock) {
            this.world.addSyncedBlockEvent(this.pos, block, 1, this.numPlayersUsing);
            this.world.updateNeighborsAlways(this.pos, block);
        }
    }
}
 */