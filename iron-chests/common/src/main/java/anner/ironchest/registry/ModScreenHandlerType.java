package anner.ironchest.registry;

import anner.ironchest.IronChestsCommon;
import anner.ironchest.blocks.ChestTypes;
import anner.ironchest.screenhandlers.ChestScreenHandler;
import anner.ironchest.screenhandlers.SizedContainerMenu;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ModScreenHandlerType {
  public static void registerScreenHandlers() {
    for (ChestTypes type : ChestTypes.PLAYABLE) {
      MenuType<ChestScreenHandler> menuType =
          new MenuType<>(
              (syncId, inventory) ->
                  new ChestScreenHandler(
                      type.getMenuType(),
                      type,
                      syncId,
                      inventory,
                      SizedContainerMenu.createClientContainer(type)),
              FeatureFlags.VANILLA_SET);

      MenuType<ChestScreenHandler> registered =
          Registry.register(
              BuiltInRegistries.MENU,
              Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, type.registryId),
              menuType);

      type.bindMenuType(registered);
    }
  }
}
