package anner.ironchest.registry;

import anner.ironchest.IronChestsCommon;
import anner.ironchest.blocks.ChestTypes;
import anner.ironchest.blocks.blockentities.CrystalChestEntity;
import anner.ironchest.blocks.blockentities.GenericChestEntity;
import anner.ironchest.platform.Platforms;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntityType {
  public static final BlockEntityType<GenericChestEntity> COPPER_CHEST = create(ChestTypes.COPPER);
  public static final BlockEntityType<GenericChestEntity> IRON_CHEST = create(ChestTypes.IRON);
  public static final BlockEntityType<GenericChestEntity> GOLD_CHEST = create(ChestTypes.GOLD);
  public static final BlockEntityType<GenericChestEntity> DIAMOND_CHEST =
      create(ChestTypes.DIAMOND);
  public static final BlockEntityType<GenericChestEntity> EMERALD_CHEST =
      create(ChestTypes.EMERALD);
  public static final BlockEntityType<CrystalChestEntity> CRYSTAL_CHEST =
      Platforms.registry().blockEntityType(CrystalChestEntity::new, ModBlocks.CRYSTAL_CHEST);
  public static final BlockEntityType<GenericChestEntity> OBSIDIAN_CHEST =
      create(ChestTypes.OBSIDIAN);
  public static final BlockEntityType<GenericChestEntity> NETHERITE_CHEST =
      create(ChestTypes.NETHERITE);
  public static final BlockEntityType<GenericChestEntity> CHRISTMAS_CHEST =
      create(ChestTypes.CHRISTMAS);

  static {
    ChestTypes.COPPER.bindBlockEntityType(COPPER_CHEST);
    ChestTypes.IRON.bindBlockEntityType(IRON_CHEST);
    ChestTypes.GOLD.bindBlockEntityType(GOLD_CHEST);
    ChestTypes.DIAMOND.bindBlockEntityType(DIAMOND_CHEST);
    ChestTypes.EMERALD.bindBlockEntityType(EMERALD_CHEST);
    ChestTypes.CRYSTAL.bindBlockEntityType(CRYSTAL_CHEST);
    ChestTypes.OBSIDIAN.bindBlockEntityType(OBSIDIAN_CHEST);
    ChestTypes.NETHERITE.bindBlockEntityType(NETHERITE_CHEST);
    ChestTypes.CHRISTMAS.bindBlockEntityType(CHRISTMAS_CHEST);
  }

  private static BlockEntityType<GenericChestEntity> create(ChestTypes type) {
    return Platforms.registry()
        .blockEntityType(
            (pos, state) ->
                new GenericChestEntity(
                    type, type.getBlockEntityType(), type.getMenuType(), pos, state),
            type.getBlock());
  }

  public static void registerBlockEntities() {
    for (ChestTypes type : ChestTypes.PLAYABLE) {
      Registry.register(
          BuiltInRegistries.BLOCK_ENTITY_TYPE,
          Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, type.registryId),
          type.getBlockEntityType());
    }
  }
}
