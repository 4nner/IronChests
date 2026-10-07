package com.gathertocraft.ironcore.lock;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Shared guards for key-linked containers. Any mod's block calls these; only its block entity must
 * implement {@link KeyLinkable}.
 */
public final class LockGuards {
  private LockGuards() {}

  /** True when the container at pos is locked and the player is outside its access list. */
  public static boolean isLockedFor(BlockGetter level, BlockPos pos, Player player) {
    if (level.getBlockEntity(pos) instanceof KeyLinkable link && link.isLocked()) {
      return !link.isAuthorized(player);
    }
    return false;
  }

  /** Denied-access feedback: message plus locked sound. Call server-side only. */
  public static void denyLocked(Level level, BlockPos pos, Player player) {
    String owner = "???";
    BlockEntity entity = level.getBlockEntity(pos);
    if (entity instanceof KeyLinkable link && link.getLockKeyId() != null) {
      if (level instanceof ServerLevel serverLevel) {
        MinecraftServer server = serverLevel.getServer();
        if (server != null) {
          KeyRegistry.Entry entry = KeyRegistry.get(server).get(link.getLockKeyId());
          if (entry != null && !entry.ownerName().isEmpty()) {
            owner = entry.ownerName();
          }
        }
      }
    }
    player.sendOverlayMessage(
        Component.translatable("message.ironcore.locked_by", owner).withStyle(ChatFormatting.RED));
    level.playSound(null, pos, SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 0.6F);
  }
}
