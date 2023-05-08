package io.github.cyberanner.ironchests.blocks.blockentities;

import io.github.cyberanner.ironchests.blocks.ChestTypes;
import io.github.cyberanner.ironchests.screenhandlers.ChestScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;


public class GenericChestEntity extends ChestBlockEntity {
    ChestTypes type;
    int viewerCount = 0;

    public GenericChestEntity(ChestTypes type, BlockPos pos, BlockState state) {
        super(type.getBlockEntityType(), pos, state);
        this.type = type;
        this.setInvStackList(DefaultedList.ofSize(this.size(), ItemStack.EMPTY));
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory inventory) {
        return new ChestScreenHandler(type.getScreenHandlerType(), type, syncId, inventory, ScreenHandlerContext.create(world, pos));
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public int size() {
        return type.size;
    }

    public ChestTypes type() {
        return type;
    }

    @Environment(EnvType.CLIENT)
    public int countViewers() {
        return viewerCount;
    }

    public void onOpen(PlayerEntity player) {
        if (!player.isSpectator()) {
            ++this.viewerCount;
            markDirty();
        }
    }

    public void onClose(PlayerEntity player) {
        if (!player.isSpectator()) {
            --this.viewerCount;
            markDirty();
        }
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        this.viewerCount = tag.getInt("viewers");
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putInt("viewers", viewerCount);
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (this.getWorld() != null && !this.getWorld().isClient() && this.getWorld() instanceof ServerWorld) {
            ((ServerWorld) world).getChunkManager().markForUpdate(getPos());
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public float getAnimationProgress(float f) {
        return MathHelper.lerp(f, lastAnimationAngle, animationAngle);
    }

    private float animationAngle;
    private float lastAnimationAngle;

    @Environment(EnvType.CLIENT)
    public void clientTick() {
        if (world != null && world.isClient) {
            int viewerCount = countViewers();
            lastAnimationAngle = animationAngle;
            if (viewerCount > 0 && animationAngle == 0.0F) playSound(SoundEvents.BLOCK_CHEST_OPEN);
            if (viewerCount == 0 && animationAngle > 0.0F || viewerCount > 0 && animationAngle < 1.0F) {
                float float_2 = animationAngle;
                if (viewerCount > 0) animationAngle += 0.1F;
                else animationAngle -= 0.1F;
                animationAngle = MathHelper.clamp(animationAngle, 0, 1);
                if (animationAngle < 0.5F && float_2 >= 0.5F) playSound(SoundEvents.BLOCK_CHEST_CLOSE);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    private void playSound(SoundEvent soundEvent) {
        double d = (double) this.pos.getX() + 0.5D;
        double e = (double) this.pos.getY() + 0.5D;
        double f = (double) this.pos.getZ() + 0.5D;
        this.world.playSound(d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F, false);
    }
}