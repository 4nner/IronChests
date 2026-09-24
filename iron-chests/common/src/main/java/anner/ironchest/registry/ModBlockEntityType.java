package anner.ironchest.registry;

import anner.ironchest.IronChestsCommon;
import anner.ironchest.blocks.ChestTypes;
import anner.ironchest.blocks.blockentities.CrystalChestEntity;
import anner.ironchest.blocks.blockentities.GenericChestEntity;
import anner.ironchest.platform.Platforms;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModBlockEntityType {
  private ModBlockEntityType() {}

  public static void registerBlockEntities() {
    for (ChestTypes type : ChestTypes.PLAYABLE) {
      if (type == ChestTypes.CRYSTAL) {
        type.bindBlockEntityType(
            Platforms.registry()
                .register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, type.registryId),
                    () ->
                        Platforms.registry()
                            .blockEntityType(CrystalChestEntity::new, type.getBlock())));
      } else {
        type.bindBlockEntityType(
            Platforms.registry()
                .register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, type.registryId),
                    () ->
                        Platforms.registry()
                            .blockEntityType(
                                (pos, state) ->
                                    new GenericChestEntity(
                                        type,
                                        type::getBlockEntityType,
                                        type::getMenuType,
                                        pos,
                                        state),
                                type.getBlock())));
      }
    }
  }
}
