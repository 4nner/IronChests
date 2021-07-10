package io.github.cyberanner.ironchests.blocks.blockentities;

import io.github.cyberanner.ironchests.blocks.ChestTypes;
import io.github.cyberanner.ironchests.blocks.CopperChestBlock;
import io.github.cyberanner.ironchests.implementations.ImplementedInventory;
import io.github.cyberanner.ironchests.registry.ModScreenHandlerType;
import io.github.cyberanner.ironchests.screenhandlers.ChestScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;


public abstract class GenericChestEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private DefaultedList<ItemStack> inventory;
    private final ChestTypes chestType;
    private final int inventorySize;
    public int numPlayersUsing;


    public GenericChestEntity(BlockEntityType<? extends GenericChestEntity> entity, BlockPos pos, BlockState state, ChestTypes type) {
        super(entity, pos, state);
        chestType = type;
        inventorySize = type.size;
        inventory = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
    }

    //From the ImplementedInventory Interface
    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    //These Methods are from the NamedScreenHandlerFactory Interface
    //createMenu creates the ScreenHandler itself
    //getDisplayName will Provide its name which is normally shown at the top
    @Override
    public Text getDisplayName() {
        // Using the block name as the screen title
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
        return new ChestScreenHandler(ModScreenHandlerType.COPPER_CHEST, chestType, syncId, inventory, ScreenHandlerContext.create(world, pos));
    }



    // Reads and Saves Inventory Content
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        return nbt;
    }

    // Minecraft Stats Integration "Number of Chests opened"
    @Override
    public void onOpen(PlayerEntity player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }
            ++this.numPlayersUsing;
            this.onInvOpenOrClose();
            this.playSound(SoundEvents.BLOCK_CHEST_OPEN);
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!player.isSpectator()) {
            --this.numPlayersUsing;
            this.onInvOpenOrClose();
            this.playSound(SoundEvents.BLOCK_CHEST_CLOSE);
            //System.out.println(inventorySize);
        }
    }

    protected void onInvOpenOrClose() {
        Block block = this.getCachedState().getBlock();
        if (block instanceof CopperChestBlock) {
            this.world.addSyncedBlockEvent(this.pos, block, 1, this.numPlayersUsing);
            this.world.updateNeighborsAlways(this.pos, block);
        }
    }

    private void playSound(SoundEvent soundEvent) {
        double d0 = (double) this.pos.getX() + 0.5D;
        double d1 = (double) this.pos.getY() + 0.5D;
        double d2 = (double) this.pos.getZ() + 0.5D;
        this.world.playSound((PlayerEntity) null, d0, d1, d2, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }

    // Upgrade Items Methods
    @Override
    public int size() {
        return inventorySize;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.inventory) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = DefaultedList.<ItemStack>ofSize(inventorySize, ItemStack.EMPTY);

        for (int i = 0; i < list.size(); i++) {
            if (i < this.inventory.size()) {
                this.inventory.set(i, list.get(i));
            }
        }
    }

    public DefaultedList<ItemStack> getInvStackList() {
        return inventory;
    }

    public static int countViewers(World world, int x, int y, int z) {
        int i = 0;
        for (PlayerEntity playerentity : world.getNonSpectatingEntities(PlayerEntity.class, new Box((double) ((float) x - 5.0F), (double) ((float) y - 5.0F), (double) ((float) z - 5.0F), (double) ((float) (x + 1) + 5.0F), (double) ((float) (y + 1) + 5.0F), (double) ((float) (z + 1) + 5.0F)))) {
            if (playerentity.currentScreenHandler instanceof ChestScreenHandler) {
                ++i;
            }
        }
        return i;
    }
}