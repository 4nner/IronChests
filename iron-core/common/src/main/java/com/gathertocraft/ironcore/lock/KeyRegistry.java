package com.gathertocraft.ironcore.lock;

import com.gathertocraft.ironcore.IronCoreCommon;
import com.gathertocraft.ironcore.LockPermissions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

/**
 * Server-side source of truth for Container Key links. Each physical key carries a key id; locked
 * containers store that id and resolve the live entry here on every check, so editing a key applies
 * to all linked containers instantly, including ones in unloaded chunks.
 */
public final class KeyRegistry extends SavedData {
  private final Map<UUID, Entry> entries = new LinkedHashMap<>();
  private int nextCode = 1;

  private record TrustedLine(UUID id, String name) {
    static final Codec<TrustedLine> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance
                    .group(
                        UUIDUtil.STRING_CODEC.fieldOf("id").forGetter(TrustedLine::id),
                        Codec.STRING.fieldOf("name").forGetter(TrustedLine::name))
                    .apply(instance, TrustedLine::new));
  }

  private static final Codec<Entry> ENTRY_CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      UUIDUtil.STRING_CODEC.fieldOf("id").forGetter(e -> e.id),
                      Codec.INT.fieldOf("code").forGetter(e -> e.code),
                      Codec.STRING.optionalFieldOf("name", "").forGetter(e -> e.name),
                      UUIDUtil.STRING_CODEC.fieldOf("owner").forGetter(e -> e.owner),
                      Codec.STRING.optionalFieldOf("ownerName", "").forGetter(e -> e.ownerName),
                      TrustedLine.CODEC.listOf().fieldOf("trusted").forGetter(Entry::trustedLines))
                  .apply(instance, Entry::fromCodec));

  private static final Codec<KeyRegistry> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      Codec.INT.fieldOf("nextCode").forGetter(r -> r.nextCode),
                      ENTRY_CODEC.listOf().fieldOf("keys").forGetter(KeyRegistry::entryList))
                  .apply(instance, KeyRegistry::fromCodec));

  public static final SavedDataType<KeyRegistry> TYPE =
      new SavedDataType<>(
          Identifier.fromNamespaceAndPath(IronCoreCommon.MOD_ID, "key_registry"),
          KeyRegistry::new,
          CODEC,
          // No dedicated fixer exists for mod data; command storage treats its content as an
          // opaque blob, which is the closest inert match. Rules only run across version bumps,
          // and vanilla fixers tolerate absent fields.
          DataFixTypes.SAVED_DATA_COMMAND_STORAGE);

  /** One key's live access list. Mutate only through {@link KeyRegistry} so saves stay dirty. */
  public static final class Entry {
    final UUID id;
    final int code;
    String name = "";
    UUID owner;
    String ownerName = "";
    final Map<UUID, String> trusted = new LinkedHashMap<>();

    private Entry(UUID id, int code) {
      this.id = id;
      this.code = code;
    }

    private static Entry fromCodec(
        UUID id, int code, String name, UUID owner, String ownerName, List<TrustedLine> lines) {
      Entry entry = new Entry(id, code);
      entry.name = name != null ? name : "";
      entry.owner = owner;
      entry.ownerName = ownerName != null ? ownerName : "";
      for (TrustedLine line : lines) {
        entry.trusted.putIfAbsent(line.id(), line.name());
      }
      return entry;
    }

    private List<TrustedLine> trustedLines() {
      List<TrustedLine> lines = new ArrayList<>(trusted.size());
      for (Map.Entry<UUID, String> e : trusted.entrySet()) {
        lines.add(new TrustedLine(e.getKey(), e.getValue()));
      }
      return lines;
    }

    public UUID id() {
      return id;
    }

    public int code() {
      return code;
    }

    public String name() {
      return name;
    }

    public UUID owner() {
      return owner;
    }

    public String ownerName() {
      return ownerName;
    }

    public Map<UUID, String> trustedView() {
      return Collections.unmodifiableMap(trusted);
    }

    public boolean isOwner(Player player) {
      return player != null && player.getUUID().equals(owner);
    }

    public boolean isAuthorized(Player player) {
      if (player == null) {
        return false;
      }
      if (player.getUUID().equals(owner) || trusted.containsKey(player.getUUID())) {
        return true;
      }
      // Name fallback: entries added while the player was offline (or on an offline-mode
      // server) may carry a different UUID than the live player.
      String name = player.getGameProfile().name();
      if (!name.isEmpty()) {
        for (String trustedName : trusted.values()) {
          if (trustedName.equalsIgnoreCase(name)) {
            return true;
          }
        }
      }
      return LockPermissions.isOperator(player);
    }

    public boolean canConfigure(Player player) {
      return isOwner(player) || LockPermissions.isOperator(player);
    }
  }

  private static KeyRegistry fromCodec(int nextCode, List<Entry> keys) {
    KeyRegistry registry = new KeyRegistry();
    registry.nextCode = Math.max(1, nextCode);
    for (Entry entry : keys) {
      registry.entries.putIfAbsent(entry.id, entry);
    }
    return registry;
  }

  private List<Entry> entryList() {
    return new ArrayList<>(entries.values());
  }

  /** Server-global instance; call only with a live server. */
  public static KeyRegistry get(MinecraftServer server) {
    return server.overworld().getDataStorage().computeIfAbsent(TYPE);
  }

  public Entry get(UUID id) {
    return id == null ? null : entries.get(id);
  }

  /** Mints a new entry for a fresh key. The minter becomes its owner. */
  public Entry create(Player minter) {
    return create(UUID.randomUUID(), minter.getUUID(), minter.getGameProfile().name());
  }

  /** Rebuilds an entry for an id whose record is missing (should not normally happen). */
  public Entry create(UUID id, UUID owner, String ownerName) {
    Entry entry = new Entry(id, nextCode++);
    entry.owner = owner;
    entry.ownerName = ownerName != null ? ownerName : "";
    entries.put(id, entry);
    setDirty();
    return entry;
  }

  /**
   * @return true if added, false if already trusted or is the owner.
   */
  public boolean trust(Entry entry, UUID id, String name) {
    if (entry == null || id == null || id.equals(entry.owner) || entry.trusted.containsKey(id)) {
      return false;
    }
    entry.trusted.put(id, name != null ? name : id.toString());
    setDirty();
    return true;
  }

  /**
   * @return true if an entry was removed.
   */
  public boolean untrust(Entry entry, UUID id) {
    if (entry != null && id != null && entry.trusted.remove(id) != null) {
      setDirty();
      return true;
    }
    return false;
  }

  public void rename(Entry entry, String name) {
    if (entry == null) {
      return;
    }
    String next = name != null ? name : "";
    if (!entry.name.equals(next)) {
      entry.name = next;
      setDirty();
    }
  }
}
