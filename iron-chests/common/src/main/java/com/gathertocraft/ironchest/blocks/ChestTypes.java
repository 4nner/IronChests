package com.gathertocraft.ironchest.blocks;

import com.gathertocraft.ironchest.IronChestsCommon;
import com.gathertocraft.ironchest.blocks.blockentities.CrystalChestEntity;
import com.gathertocraft.ironchest.blocks.blockentities.GenericChestEntity;
import com.gathertocraft.ironchest.screenhandlers.ChestScreenHandler;
import com.gathertocraft.ironcore.TierSpec;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public enum ChestTypes implements TierSpec {
  NETHERITE(
      126,
      14,
      "netherite_chest",
      Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, "entity/chest/netherite_chest")),
  OBSIDIAN(
      108,
      12,
      "obsidian_chest",
      Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, "entity/chest/obsidian_chest")),
  CRYSTAL(
      108,
      12,
      "crystal_chest",
      Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, "entity/chest/crystal_chest")),
  DIAMOND(
      108,
      12,
      "diamond_chest",
      Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, "entity/chest/diamond_chest")),
  EMERALD(
      108,
      12,
      "emerald_chest",
      Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, "entity/chest/emerald_chest")),
  GOLD(
      81,
      9,
      "gold_chest",
      Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, "entity/chest/gold_chest")),
  IRON(
      54,
      9,
      "iron_chest",
      Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, "entity/chest/iron_chest")),
  COPPER(
      45,
      9,
      "copper_chest",
      Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, "entity/chest/copper_chest")),
  CHRISTMAS(27, 9, "christmas_chest", Identifier.withDefaultNamespace("entity/chest/christmas")),
  DIRT(
      126,
      14,
      "dirt_chest",
      Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, "entity/chest/dirt_chest")),
  WOOD(27, 9, null, Identifier.withDefaultNamespace("entity/chest/normal"));

  public static final ChestTypes[] PLAYABLE = {
    DIRT, COPPER, IRON, GOLD, DIAMOND, EMERALD, CRYSTAL, OBSIDIAN, NETHERITE, CHRISTMAS
  };

  public final int size;
  public final int rowLength;
  public final String registryId;
  public final Identifier texture;

  private static final Map<ChestTypes, Integer> CONFIGURED_ROWS = new EnumMap<>(ChestTypes.class);

  private @Nullable Supplier<Block> block;
  private @Nullable Supplier<? extends BlockEntityType<? extends ChestBlockEntity>> blockEntityType;
  private @Nullable Supplier<MenuType<ChestScreenHandler>> menuType;

  ChestTypes(int size, int rowLength, String registryId, Identifier texture) {
    this.size = size;
    this.rowLength = rowLength;
    this.registryId = registryId;
    this.texture = texture;
  }

  @Override
  public int size() {
    return rowCount() * this.rowLength;
  }

  @Override
  public int rowLength() {
    return this.rowLength;
  }

  @Override
  public int rowCount() {
    return CONFIGURED_ROWS.getOrDefault(this, defaultRowCount());
  }

  /** Rows baked into the enum; the config defaults to these. */
  public int defaultRowCount() {
    return this.size / this.rowLength;
  }

  /** Lowercase tier name used as the key in ironchest.json. */
  public String configKey() {
    return name().toLowerCase(Locale.ROOT);
  }

  /** Applies row counts from the config; missing keys leave defaults in place. */
  public static void setConfiguredRows(Map<String, Integer> rowsByKey) {
    CONFIGURED_ROWS.clear();
    for (ChestTypes type : PLAYABLE) {
      Integer rows = rowsByKey.get(type.configKey());
      if (rows != null) {
        CONFIGURED_ROWS.put(type, rows);
      }
    }
  }

  public void bindBlock(Supplier<Block> block) {
    this.block = block;
  }

  public void bindBlockEntityType(
      Supplier<? extends BlockEntityType<? extends ChestBlockEntity>> blockEntityType) {
    this.blockEntityType = blockEntityType;
  }

  public void bindMenuType(Supplier<MenuType<ChestScreenHandler>> menuType) {
    this.menuType = menuType;
  }

  public @Nullable MenuType<ChestScreenHandler> getMenuType() {
    return this.menuType != null ? this.menuType.get() : null;
  }

  public Block getBlock() {
    return this.block != null ? this.block.get() : Blocks.CHEST;
  }

  public ChestBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    if (this == CRYSTAL) {
      return new CrystalChestEntity(pos, state);
    }
    return new GenericChestEntity(this, this::getBlockEntityType, this::getMenuType, pos, state);
  }

  public BlockEntityType<? extends ChestBlockEntity> getBlockEntityType() {
    return this.blockEntityType != null ? this.blockEntityType.get() : BlockEntityTypes.CHEST;
  }

  public BlockBehaviour.Properties blockProperties() {
    return switch (this) {
      case COPPER, GOLD ->
          BlockBehaviour.Properties.of()
              .strength(3.0F, 6.0F)
              .sound(SoundType.COPPER)
              .requiresCorrectToolForDrops();
      case IRON ->
          BlockBehaviour.Properties.of()
              .strength(5.0F, 6.0F)
              .sound(SoundType.METAL)
              .requiresCorrectToolForDrops();
      case DIAMOND, EMERALD ->
          BlockBehaviour.Properties.of()
              .strength(5.0F, 6.0F)
              .sound(SoundType.STONE)
              .requiresCorrectToolForDrops();
      case CRYSTAL ->
          BlockBehaviour.Properties.of()
              .strength(0.3F, 0.3F)
              .sound(SoundType.AMETHYST)
              .noOcclusion()
              .requiresCorrectToolForDrops();
      case OBSIDIAN ->
          BlockBehaviour.Properties.of()
              .strength(50.0F, 1200.0F)
              .sound(SoundType.STONE)
              .requiresCorrectToolForDrops();
      case NETHERITE ->
          BlockBehaviour.Properties.of()
              .strength(50.0F, 1200.0F)
              .sound(SoundType.NETHERITE_BLOCK)
              .requiresCorrectToolForDrops();
      case WOOD, CHRISTMAS ->
          BlockBehaviour.Properties.of().strength(3.0F, 3.0F).sound(SoundType.WOOD);
      case DIRT -> BlockBehaviour.Properties.of().strength(2.5F, 3.0F).sound(SoundType.GRAVEL);
      default -> BlockBehaviour.Properties.of();
    };
  }
}
