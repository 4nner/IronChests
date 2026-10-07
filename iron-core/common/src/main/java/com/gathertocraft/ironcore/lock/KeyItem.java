package com.gathertocraft.ironcore.lock;

import com.gathertocraft.ironcore.config.CoreConfig;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

/**
 * Configuration tool for key-linked locks. Each key carries a key id; locked containers store that
 * id and resolve the live registry entry on every check, so edits apply to all linked containers
 * instantly. Shift-click an unlocked container to lock and link it, shift-click a locked one with
 * the linked key to unlock, or with a different key to relink it.
 */
public class KeyItem extends Item {
  public KeyItem(Properties properties) {
    super(properties);
  }

  private static final String TAG_KEY_ID = "LockKeyId";
  private static final String TAG_KEY_CODE = "LockKeyCode";

  /** Key id carried by this stack, or null for a fresh (unminted) key. */
  public static @Nullable UUID readKeyId(ItemStack stack) {
    if (!(stack.getItem() instanceof KeyItem)) {
      return null;
    }
    CustomData data = stack.get(DataComponents.CUSTOM_DATA);
    if (data == null) {
      return null;
    }
    String raw = data.copyTag().getStringOr(TAG_KEY_ID, "");
    if (raw.isEmpty()) {
      return null;
    }
    try {
      return UUID.fromString(raw);
    } catch (IllegalArgumentException ignored) {
      return null;
    }
  }

  /** Short registry code cached on the stack for tooltips, or -1 when unminted. */
  public static int readKeyCode(ItemStack stack) {
    if (!(stack.getItem() instanceof KeyItem)) {
      return -1;
    }
    CustomData data = stack.get(DataComponents.CUSTOM_DATA);
    if (data == null) {
      return -1;
    }
    return data.copyTag().getIntOr(TAG_KEY_CODE, -1);
  }

  private static void writeKeyBinding(ItemStack stack, UUID id, int code) {
    CustomData.update(
        DataComponents.CUSTOM_DATA,
        stack,
        tag -> {
          tag.putString(TAG_KEY_ID, id.toString());
          tag.putInt(TAG_KEY_CODE, code);
        });
  }

  /**
   * Returns this key's registry entry, minting one (owned by the holder) for fresh keys. Null when
   * the stack is not a key or no server is available.
   */
  public static KeyRegistry.@Nullable Entry ensureEntry(ItemStack stack, Player holder) {
    if (!(stack.getItem() instanceof KeyItem) || holder == null) {
      return null;
    }
    if (!(holder instanceof ServerPlayer serverPlayer)
        || serverPlayer.level().getServer() == null) {
      return null;
    }
    KeyRegistry registry = KeyRegistry.get(serverPlayer.level().getServer());
    UUID id = readKeyId(stack);
    KeyRegistry.Entry entry = id == null ? null : registry.get(id);
    if (entry == null) {
      UUID fresh = id != null ? id : UUID.randomUUID();
      entry = registry.create(fresh, holder.getUUID(), holder.getGameProfile().name());
      writeKeyBinding(stack, fresh, entry.code());
    }
    syncNameFromStack(registry, stack, entry);
    return entry;
  }

  /** Copies an anvil rename on the stack into the entry. Unnamed stacks never blank a name. */
  static void syncNameFromStack(KeyRegistry registry, ItemStack stack, KeyRegistry.Entry entry) {
    if (registry == null || entry == null || !(stack.getItem() instanceof KeyItem)) {
      return;
    }
    if (stack.has(DataComponents.CUSTOM_NAME)) {
      Component custom = stack.get(DataComponents.CUSTOM_NAME);
      if (custom != null) {
        String name = custom.getString().trim();
        if (!name.isEmpty()) {
          registry.rename(entry, name);
        }
      }
    }
  }

  /** Entry display name, falling back to plain "Key" when never renamed. */
  public static String keyDisplayName(KeyRegistry.Entry entry) {
    if (entry != null && !entry.name().isEmpty()) {
      return entry.name();
    }
    return Component.translatable("title.ironcore.unnamed_key").getString();
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level level = context.getLevel();
    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }
    Player player = context.getPlayer();
    if (player == null) {
      return InteractionResult.PASS;
    }
    if (!CoreConfig.locksEnabled()) {
      player.sendOverlayMessage(Component.translatable("message.ironcore.locks_disabled"));
      return InteractionResult.SUCCESS_SERVER;
    }
    BlockEntity entity = level.getBlockEntity(context.getClickedPos());
    if (!(entity instanceof KeyLinkable target)) {
      return InteractionResult.PASS;
    }
    MinecraftServer server = level.getServer();
    if (server == null) {
      return InteractionResult.PASS;
    }

    if (!target.isLocked()) {
      if (!player.isSecondaryUseActive()) {
        return InteractionResult.PASS;
      }
      KeyRegistry.Entry entry = ensureEntry(context.getItemInHand(), player);
      if (entry == null) {
        return InteractionResult.PASS;
      }
      target.link(entry.id());
      level.playSound(
          null, context.getClickedPos(), SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
      player.sendOverlayMessage(Component.translatable("message.ironcore.locked_by_you"));
      return InteractionResult.SUCCESS_SERVER;
    }

    KeyRegistry.Entry entry =
        target.getLockKeyId() == null ? null : KeyRegistry.get(server).get(target.getLockKeyId());
    if (entry == null || !entry.canConfigure(player)) {
      String owner = entry != null && !entry.ownerName().isEmpty() ? entry.ownerName() : "???";
      player.sendOverlayMessage(
          Component.translatable("message.ironcore.locked_by", owner)
              .withStyle(ChatFormatting.RED));
      level.playSound(
          null, context.getClickedPos(), SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 0.6F);
      return InteractionResult.SUCCESS_SERVER;
    }

    if (player.isSecondaryUseActive()) {
      UUID heldId = readKeyId(context.getItemInHand());
      if (heldId != null && heldId.equals(target.getLockKeyId())) {
        target.unlink();
        level.playSound(
            null,
            context.getClickedPos(),
            SoundEvents.CHEST_LOCKED,
            SoundSource.BLOCKS,
            0.8F,
            1.2F);
        player.sendOverlayMessage(Component.translatable("message.ironcore.unlocked"));
        return InteractionResult.SUCCESS_SERVER;
      }
      KeyRegistry.Entry next = ensureEntry(context.getItemInHand(), player);
      if (next == null) {
        return InteractionResult.PASS;
      }
      target.link(next.id());
      level.playSound(
          null, context.getClickedPos(), SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
      player.sendOverlayMessage(
          Component.translatable("message.ironcore.relinked", keyDisplayName(next), next.code()));
      if (!target.isAuthorized(player)) {
        player.sendSystemMessage(
            Component.translatable("message.ironcore.relink_warning")
                .withStyle(ChatFormatting.RED));
      }
      return InteractionResult.SUCCESS_SERVER;
    }

    openChestEditor(player, target, context.getHand());
    return InteractionResult.SUCCESS_SERVER;
  }

  /** Air right-click opens the held key's editor. Block aims route to {@link #useOn} instead. */
  @Override
  public InteractionResult use(Level level, Player player, InteractionHand hand) {
    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }
    if (!CoreConfig.locksEnabled()) {
      player.sendOverlayMessage(Component.translatable("message.ironcore.locks_disabled"));
      return InteractionResult.SUCCESS_SERVER;
    }
    // pick() returns a BlockHitResult even for a miss (typed MISS): test the hit type, not the
    // class, or every air-click is silently swallowed.
    HitResult hit = player.pick(player.blockInteractionRange(), 0.0F, false);
    if (hit.getType() == HitResult.Type.BLOCK) {
      return InteractionResult.PASS;
    }
    ItemStack stack = player.getItemInHand(hand);
    if (!(stack.getItem() instanceof KeyItem)) {
      return InteractionResult.PASS;
    }
    openEditor(player, hand);
    return InteractionResult.SUCCESS_SERVER;
  }

  /** Editor for the held key. Only its owner (or an OP) may open it. */
  public static void openEditor(Player player, InteractionHand hand) {
    if (!(player instanceof ServerPlayer serverPlayer)
        || serverPlayer.level().getServer() == null) {
      return;
    }
    KeyRegistry.Entry entry = ensureEntry(serverPlayer.getItemInHand(hand), serverPlayer);
    if (entry == null) {
      return;
    }
    if (!entry.canConfigure(serverPlayer)) {
      serverPlayer.sendOverlayMessage(
          Component.translatable("message.ironcore.key_not_yours").withStyle(ChatFormatting.RED));
      return;
    }
    openEntryEditor(serverPlayer, entry.id());
  }

  /**
   * Pushes an anvil rename into the entry by finding the linked key anywhere in the player's
   * inventory. Lets chest titles pick up renames on open without requiring a key gesture first.
   */
  public static void refreshEntryName(Player player, UUID keyId) {
    if (!(player instanceof ServerPlayer serverPlayer)
        || serverPlayer.level().getServer() == null
        || keyId == null) {
      return;
    }
    KeyRegistry registry = KeyRegistry.get(serverPlayer.level().getServer());
    KeyRegistry.Entry entry = registry.get(keyId);
    if (entry == null) {
      return;
    }
    var inventory = serverPlayer.getInventory();
    for (int i = 0; i < inventory.getContainerSize(); i++) {
      ItemStack stack = inventory.getItem(i);
      if (stack.getItem() instanceof KeyItem && keyId.equals(readKeyId(stack))) {
        syncNameFromStack(registry, stack, entry);
        return;
      }
    }
  }

  /**
   * Editor for a locked container's linked entry. Unlinked targets fall back to a deny: their lock
   * belongs to another system.
   */
  public static void openChestEditor(Player player, KeyLinkable target, InteractionHand hand) {
    if (!(player instanceof ServerPlayer serverPlayer)
        || serverPlayer.level().getServer() == null) {
      return;
    }
    UUID entryId = target.getLockKeyId();
    if (entryId == null) {
      serverPlayer.sendOverlayMessage(
          Component.translatable("message.ironcore.locked_by", "???")
              .withStyle(ChatFormatting.RED));
      return;
    }
    refreshEntryName(serverPlayer, entryId);
    openEntryEditor(serverPlayer, entryId);
  }

  private static void openEntryEditor(ServerPlayer player, UUID entryId) {
    KeyRegistry registry = KeyRegistry.get(player.level().getServer());
    KeyRegistry.Entry entry = registry.get(entryId);
    if (entry == null) {
      return;
    }
    player.openMenu(
        new SimpleMenuProvider(
            (syncId, inventory, opener) ->
                new KeyEditorMenu(
                    KeyEditorMenus.getType(), syncId, inventory, handOf(player, entryId), entryId),
            Component.translatable(
                "menu.ironcore.key_editor_named", keyDisplayName(entry), entry.code())));
    // The menu carries no slots, so vanilla inventory sync never pushes changes back. Push the
    // entry explicitly so the fresh screen paints current data.
    KeyEditorPayloads.sendSync(player, entry.trustedView());
  }

  private static InteractionHand handOf(ServerPlayer player, UUID entryId) {
    for (InteractionHand hand : InteractionHand.values()) {
      if (entryId.equals(readKeyId(player.getItemInHand(hand)))) {
        return hand;
      }
    }
    return InteractionHand.MAIN_HAND;
  }

  @Override
  public void appendHoverText(
      ItemStack stack,
      TooltipContext context,
      TooltipDisplay display,
      Consumer<Component> lines,
      TooltipFlag flag) {
    super.appendHoverText(stack, context, display, lines, flag);
    if (!CoreConfig.locksEnabled()) {
      lines.accept(
          Component.translatable("tooltip.ironcore.locks_disabled").withStyle(ChatFormatting.RED));
      return;
    }
    int code = readKeyCode(stack);
    if (code >= 0) {
      lines.accept(
          Component.translatable("tooltip.ironcore.container_key.code", code)
              .withStyle(ChatFormatting.GOLD));
    }
    lines.accept(
        Component.translatable("tooltip.ironcore.container_key.edit")
            .withStyle(ChatFormatting.GRAY));
    lines.accept(
        Component.translatable("tooltip.ironcore.container_key.lock")
            .withStyle(ChatFormatting.GRAY));
    lines.accept(
        Component.translatable("tooltip.ironcore.container_key.rename")
            .withStyle(ChatFormatting.DARK_GRAY));
  }
}
