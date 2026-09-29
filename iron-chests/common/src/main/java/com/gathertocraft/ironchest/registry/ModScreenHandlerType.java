package com.gathertocraft.ironchest.registry;

import com.gathertocraft.ironchest.IronChestsCommon;
import com.gathertocraft.ironchest.blocks.ChestTypes;
import com.gathertocraft.ironchest.screenhandlers.ChestScreenHandler;
import com.gathertocraft.ironcore.SizedContainerMenu;
import com.gathertocraft.ironcore.platform.Platforms;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ModScreenHandlerType {
  private ModScreenHandlerType() {}

  public static void registerScreenHandlers() {
    for (ChestTypes type : ChestTypes.PLAYABLE) {
      type.bindMenuType(
          Platforms.registry()
              .register(
                  BuiltInRegistries.MENU,
                  Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, type.registryId),
                  () ->
                      new MenuType<>(
                          (syncId, inventory) ->
                              new ChestScreenHandler(
                                  type.getMenuType(),
                                  type,
                                  syncId,
                                  inventory,
                                  SizedContainerMenu.createClientContainer(type)),
                          FeatureFlags.VANILLA_SET)));
    }
  }
}
