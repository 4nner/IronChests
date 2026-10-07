package com.gathertocraft.ironcore.lock;

import com.gathertocraft.ironcore.IronCoreCommon;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/** Client-to-server packets for editing a key registry entry. The screen sends these. */
public final class KeyEditorPayloads {
  private KeyEditorPayloads() {}

  public record Add(String name) implements CustomPacketPayload {
    // createType(String) treats its arg as a path under minecraft:, so build the Type with an
    // explicit Identifier instead.
    public static final Type<Add> TYPE =
        new Type<>(Identifier.fromNamespaceAndPath(IronCoreCommon.MOD_ID, "key_trust_add"));
    public static final StreamCodec<ByteBuf, Add> CODEC =
        StreamCodec.composite(ByteBufCodecs.STRING_UTF8, Add::name, Add::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
      return TYPE;
    }
  }

  public record Remove(String uuid) implements CustomPacketPayload {
    public static final Type<Remove> TYPE =
        new Type<>(Identifier.fromNamespaceAndPath(IronCoreCommon.MOD_ID, "key_trust_remove"));
    public static final StreamCodec<ByteBuf, Remove> CODEC =
        StreamCodec.composite(ByteBufCodecs.STRING_UTF8, Remove::uuid, Remove::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
      return TYPE;
    }
  }

  /**
   * Server-to-client entry snapshot. The editor menu carries no slots, so vanilla inventory sync
   * never pushes changes back; without this the client's list would stay stale forever. One line
   * per entry, {@code uuid=name}.
   */
  public record Sync(String body) implements CustomPacketPayload {
    public static final Type<Sync> TYPE =
        new Type<>(Identifier.fromNamespaceAndPath(IronCoreCommon.MOD_ID, "key_trust_sync"));
    public static final StreamCodec<ByteBuf, Sync> CODEC =
        StreamCodec.composite(ByteBufCodecs.STRING_UTF8, Sync::body, Sync::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
      return TYPE;
    }

    public static Sync encode(java.util.Map<UUID, String> trusted) {
      StringBuilder out = new StringBuilder();
      for (java.util.Map.Entry<UUID, String> entry : trusted.entrySet()) {
        if (!out.isEmpty()) {
          out.append('\n');
        }
        out.append(entry.getKey()).append('=').append(entry.getValue());
      }
      return new Sync(out.toString());
    }

    public java.util.Map<UUID, String> decode() {
      java.util.Map<UUID, String> trusted = new java.util.LinkedHashMap<>();
      if (body == null || body.isEmpty()) {
        return trusted;
      }
      for (String line : body.split("\n")) {
        int sep = line.indexOf('=');
        if (sep <= 0) {
          continue;
        }
        try {
          UUID id = UUID.fromString(line.substring(0, sep));
          trusted.putIfAbsent(id, line.substring(sep + 1));
        } catch (IllegalArgumentException ignored) {
          // Corrupt entry; skip it.
        }
      }
      return trusted;
    }
  }

  /** Pushes the given entry to the player's open editor. */
  public static void sendSync(ServerPlayer player, java.util.Map<UUID, String> trusted) {
    player.connection.send(new ClientboundCustomPayloadPacket(Sync.encode(trusted)));
  }

  /** Resolves a typed name to a stable id: online players by live UUID, anyone else by name. */
  static UUID resolveId(MinecraftServer server, String rawName) {
    String name = rawName.trim();
    ServerPlayer target = server.getPlayerList().getPlayerByName(name);
    if (target != null) {
      return target.getUUID();
    }
    return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
  }

  public static void handleAdd(ServerPlayer player, Add payload) {
    if (!(player.containerMenu instanceof KeyEditorMenu menu)) {
      return;
    }
    MinecraftServer server = player.level().getServer();
    if (server == null) {
      return;
    }
    KeyRegistry registry = KeyRegistry.get(server);
    KeyRegistry.Entry entry = menu.keyId() == null ? null : registry.get(menu.keyId());
    if (entry == null) {
      return;
    }
    // Possession alone must not reconfigure a live-synced key: only its owner (or an OP).
    if (!entry.canConfigure(player)) {
      player.sendSystemMessage(
          Component.translatable("message.ironcore.key_not_yours").withStyle(ChatFormatting.RED));
      sendSync(player, entry.trustedView());
      return;
    }
    String name = payload.name() == null ? "" : payload.name().trim();
    if (name.isEmpty() || name.length() > 16) {
      return;
    }
    UUID id = resolveId(server, name);
    ServerPlayer target = server.getPlayerList().getPlayerByName(name);
    String display = target != null ? target.getGameProfile().name() : name;
    if (menu.trustedSnapshot().containsKey(id)) {
      player.sendSystemMessage(
          Component.translatable("message.ironcore.key_already_listed", display)
              .withStyle(ChatFormatting.YELLOW));
    } else if (menu.addEntry(display, id)) {
      player.sendSystemMessage(
          Component.translatable("message.ironcore.key_trusted_added", display)
              .withStyle(ChatFormatting.GREEN));
    }
    sendSync(player, menu.trustedSnapshot());
  }

  public static void handleRemove(ServerPlayer player, Remove payload) {
    if (!(player.containerMenu instanceof KeyEditorMenu menu)) {
      return;
    }
    MinecraftServer server = player.level().getServer();
    if (server == null) {
      return;
    }
    KeyRegistry registry = KeyRegistry.get(server);
    KeyRegistry.Entry entry = menu.keyId() == null ? null : registry.get(menu.keyId());
    if (entry == null || !entry.canConfigure(player)) {
      return;
    }
    UUID id;
    try {
      id = UUID.fromString(payload.uuid());
    } catch (IllegalArgumentException e) {
      return;
    }
    menu.removeEntry(id);
    sendSync(player, menu.trustedSnapshot());
  }
}
