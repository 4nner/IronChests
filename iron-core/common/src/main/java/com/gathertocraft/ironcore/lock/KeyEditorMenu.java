package com.gathertocraft.ironcore.lock;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

/** Server menu for a key registry entry editor. */
public final class KeyEditorMenu extends AbstractContainerMenu {
  private final InteractionHand hand;
  private final Player player;
  private final @Nullable UUID keyId;

  public KeyEditorMenu(MenuType<?> menuType, int syncId, Inventory playerInventory) {
    this(menuType, syncId, playerInventory, InteractionHand.MAIN_HAND, null);
  }

  public KeyEditorMenu(
      MenuType<?> menuType,
      int syncId,
      Inventory playerInventory,
      InteractionHand hand,
      @Nullable UUID keyId) {
    super(menuType, syncId);
    this.hand = hand;
    this.player = playerInventory.player;
    this.keyId = keyId;
  }

  public InteractionHand hand() {
    return this.hand;
  }

  public @Nullable UUID keyId() {
    return this.keyId;
  }

  private KeyRegistry.@Nullable Entry entry() {
    if (this.keyId == null || !(this.player instanceof ServerPlayer serverPlayer)) {
      return null;
    }
    MinecraftServer server = serverPlayer.level().getServer();
    if (server == null) {
      return null;
    }
    return KeyRegistry.get(server).get(this.keyId);
  }

  public Map<UUID, String> trustedSnapshot() {
    KeyRegistry.Entry entry = entry();
    return entry != null ? entry.trustedView() : Collections.emptyMap();
  }

  /** True if the entry was added. */
  public boolean addEntry(String name, UUID id) {
    KeyRegistry.Entry entry = entry();
    if (entry == null || id == null) {
      return false;
    }
    if (!(this.player instanceof ServerPlayer serverPlayer)
        || serverPlayer.level().getServer() == null) {
      return false;
    }
    return KeyRegistry.get(serverPlayer.level().getServer())
        .trust(entry, id, name != null ? name : id.toString());
  }

  /** True if an entry was removed. */
  public boolean removeEntry(UUID id) {
    KeyRegistry.Entry entry = entry();
    if (entry == null || id == null) {
      return false;
    }
    if (!(this.player instanceof ServerPlayer serverPlayer)
        || serverPlayer.level().getServer() == null) {
      return false;
    }
    return KeyRegistry.get(serverPlayer.level().getServer()).untrust(entry, id);
  }

  @Override
  public ItemStack quickMoveStack(Player player, int slotIndex) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean stillValid(Player player) {
    return player.getItemInHand(this.hand).getItem() instanceof KeyItem;
  }
}
