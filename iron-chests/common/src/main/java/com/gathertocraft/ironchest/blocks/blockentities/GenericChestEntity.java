package com.gathertocraft.ironchest.blocks.blockentities;

import com.gathertocraft.ironchest.blocks.ChestTypes;
import com.gathertocraft.ironchest.config.ChestRows;
import com.gathertocraft.ironchest.screenhandlers.ChestScreenHandler;
import com.gathertocraft.ironcore.InventorySanitizer;
import com.gathertocraft.ironcore.LockableContainer;
import com.gathertocraft.ironcore.ResizingContainer;
import com.gathertocraft.ironcore.TierSpec;
import com.gathertocraft.ironcore.UpgradableContainer;
import com.gathertocraft.ironcore.lock.KeyItem;
import com.gathertocraft.ironcore.lock.KeyLinkable;
import com.gathertocraft.ironcore.lock.KeyRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class GenericChestEntity extends ChestBlockEntity
    implements ResizingContainer,
        UpgradableContainer,
        LockableContainer,
        KeyLinkable,
        WorldlyContainer {
  private final TierSpec tier;
  private final Supplier<MenuType<ChestScreenHandler>> menuType;
  private final List<ItemStack> pendingOverflow = new ArrayList<>();
  private boolean sizingForLoad;
  private UUID lockKeyId;

  // setItems() resolves to ChestBlockEntity.setItems() — a plain field assignment,
  // no virtual dispatch into subclass code. Safe to call before subclass is fully initialized.
  @SuppressWarnings("this-escape")
  public GenericChestEntity(
      TierSpec tier,
      Supplier<BlockEntityType<?>> blockEntityType,
      Supplier<MenuType<ChestScreenHandler>> menuType,
      BlockPos pos,
      BlockState state) {
    super(blockEntityType.get(), pos, state);
    this.tier = tier;
    this.menuType = menuType;
    setItems(NonNullList.withSize(tier.size(), ItemStack.EMPTY));
  }

  @Override
  public TierSpec tier() {
    return this.tier;
  }

  @Override
  public boolean isAvailableForUpgrade(Player player) {
    Level level = this.getLevel();
    if (level == null) {
      return false;
    }
    return ChestBlockEntity.getOpenCount(level, this.getBlockPos()) == 0 && this.stillValid(player);
  }

  @Override
  public UUID getLockKeyId() {
    return this.lockKeyId;
  }

  @Override
  public void link(UUID keyId) {
    this.lockKeyId = keyId;
    this.setChanged();
  }

  @Override
  public void unlink() {
    this.lockKeyId = null;
    this.setChanged();
  }

  /** Live registry entry, or null when unlinked, off-server, or missing. */
  public KeyRegistry.Entry linkedEntry() {
    if (this.lockKeyId == null) {
      return null;
    }
    Level level = getLevel();
    if (!(level instanceof ServerLevel serverLevel)) {
      return null;
    }
    MinecraftServer server = serverLevel.getServer();
    if (server == null) {
      return null;
    }
    return KeyRegistry.get(server).get(this.lockKeyId);
  }

  @Override
  public boolean isLocked() {
    return this.lockKeyId != null;
  }

  @Override
  public boolean isAuthorized(Player player) {
    if (!isLocked() || player == null) {
      return !isLocked();
    }
    KeyRegistry.Entry entry = linkedEntry();
    return entry != null && entry.isAuthorized(player);
  }

  public String getOwnerName() {
    KeyRegistry.Entry entry = linkedEntry();
    return entry != null ? entry.ownerName() : "";
  }

  @Override
  public boolean canOpen(Player player) {
    return isAuthorized(player);
  }

  @Override
  public boolean stillValid(Player player) {
    return super.stillValid(player) && isAuthorized(player);
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    int size = getContainerSize();
    int[] slots = new int[size];
    for (int i = 0; i < size; i++) {
      slots[i] = i;
    }
    return slots;
  }

  @Override
  public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction dir) {
    return !isLocked() && canPlaceItem(slot, stack);
  }

  @Override
  public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
    return !isLocked();
  }

  @Override
  protected AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
    if (this.lockKeyId != null) {
      KeyItem.refreshEntryName(inventory.player, this.lockKeyId);
    }
    return new ChestScreenHandler(this.menuType.get(), this.tier, syncId, inventory, this);
  }

  @Override
  protected Component getDefaultName() {
    return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
  }

  @Override
  public Component getDisplayName() {
    if (this.lockKeyId != null
        && getLevel() instanceof ServerLevel serverLevel
        && serverLevel.getServer() != null) {
      KeyRegistry.Entry entry = KeyRegistry.get(serverLevel.getServer()).get(this.lockKeyId);
      if (entry != null) {
        return Component.translatable(
            "title.ironcore.linked_container",
            super.getDisplayName(),
            KeyItem.keyDisplayName(entry),
            entry.code());
      }
    }
    return super.getDisplayName();
  }

  @Override
  public int getContainerSize() {
    if (sizingForLoad) {
      return maxCapacity();
    }
    return this.tier.size();
  }

  @Override
  public void setItem(int slot, ItemStack stack) {
    super.setItem(slot, InventorySanitizer.sanitize(stack));
  }

  /** The Dirt Chest 9000 only stores block under the {@code minecraft:dirt} tag. */
  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    if (this.tier == ChestTypes.DIRT && !stack.is(ItemTags.DIRT)) {
      return false;
    }
    return super.canPlaceItem(slot, stack);
  }

  /** Largest inventory this tier can ever hold; anything beyond is overflow by definition. */
  private int maxCapacity() {
    return ChestRows.MAX_ROWS * this.tier.rowLength();
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    // Vanilla sizes the item list from getContainerSize(); report the largest possible
    // inventory while loading so that shrinking rows keeps every saved stack for the clamp below
    // instead of silently dropping them.
    sizingForLoad = true;
    try {
      super.loadAdditional(input);
    } finally {
      sizingForLoad = false;
    }
    this.clampInventoryToCapacity();
    this.lockKeyId = null;
    input
        .getString("LockKeyId")
        .ifPresent(
            raw -> {
              try {
                this.lockKeyId = UUID.fromString(raw);
              } catch (IllegalArgumentException ignored) {
                this.lockKeyId = null;
              }
            });
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    InventorySanitizer.sanitize(this.getItems());
    super.saveAdditional(output);
    if (this.lockKeyId != null) {
      output.putString("LockKeyId", this.lockKeyId.toString());
    }
  }

  @Override
  public void clampInventoryToCapacity() {
    int capacity = this.getContainerSize();
    NonNullList<ItemStack> items = this.getItems();
    if (items.size() == capacity) {
      InventorySanitizer.sanitize(items);
      this.setChanged();
      return;
    }

    NonNullList<ItemStack> clamped = NonNullList.withSize(capacity, ItemStack.EMPTY);
    for (int slot = 0; slot < Math.min(items.size(), capacity); slot++) {
      clamped.set(slot, items.get(slot));
    }

    Level level = this.getLevel();
    BlockPos pos = this.getBlockPos();
    for (int slot = capacity; slot < items.size(); slot++) {
      ItemStack overflow = items.get(slot);
      if (!overflow.isEmpty()) {
        this.placeOverflow(clamped, overflow, capacity, level, pos);
      }
    }

    this.setItems(clamped);
    InventorySanitizer.sanitize(clamped);
    this.setChanged();
  }

  private void placeOverflow(
      NonNullList<ItemStack> inventory, ItemStack stack, int capacity, Level level, BlockPos pos) {
    for (int slot = 0; slot < capacity; slot++) {
      if (inventory.get(slot).isEmpty()) {
        inventory.set(slot, stack);
        return;
      }
    }
    if (level != null) {
      Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
    } else {
      this.pendingOverflow.add(stack);
    }
  }

  @Override
  public void setLevel(Level level) {
    super.setLevel(level);
    if (level == null || level.isClientSide() || this.pendingOverflow.isEmpty()) {
      return;
    }
    BlockPos pos = this.getBlockPos();
    for (ItemStack stack : this.pendingOverflow) {
      Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
    }
    this.pendingOverflow.clear();
  }
}
