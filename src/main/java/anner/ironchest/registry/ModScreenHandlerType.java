package anner.ironchest.registry;

import anner.ironchest.IronChests;
import anner.ironchest.blocks.ChestTypes;
import anner.ironchest.screenhandlers.ChestScreenHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ModScreenHandlerType {
    @SuppressWarnings("unchecked")
    public static void registerScreenHandlers() {
        for (ChestTypes type : ChestTypes.PLAYABLE) {
            MenuType<ChestScreenHandler>[] holder = new MenuType[1];
            holder[0] = Registry.register(
                BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(IronChests.MOD_ID, type.registryId),
                new MenuType<>((syncId, inventory) -> new ChestScreenHandler(
                    holder[0], type, syncId, inventory, ChestScreenHandler.createClientContainer(type)
                ), FeatureFlags.VANILLA_SET)
            );
            type.bindMenuType(holder[0]);
        }
    }
}
