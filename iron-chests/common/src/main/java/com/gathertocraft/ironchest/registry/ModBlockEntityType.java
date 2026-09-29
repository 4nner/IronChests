package com.gathertocraft.ironchest.registry;

import com.gathertocraft.ironchest.IronChestsCommon;
import com.gathertocraft.ironchest.blocks.ChestTypes;
import com.gathertocraft.ironchest.blocks.blockentities.CrystalChestEntity;
import com.gathertocraft.ironchest.blocks.blockentities.GenericChestEntity;
import com.gathertocraft.ironcore.platform.Platforms;
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
