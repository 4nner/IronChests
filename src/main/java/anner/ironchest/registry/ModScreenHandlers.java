package anner.ironchest.registry;

import anner.ironchest.screenhandlers.ChestScreenHandler;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class ModScreenHandlers {
    public static void registerScreenHandlers() {
        ScreenRegistry.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.COPPER_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));
        ScreenRegistry.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.IRON_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));
        ScreenRegistry.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.GOLD_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));
        ScreenRegistry.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.DIAMOND_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));
        ScreenRegistry.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.EMERALD_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));
        ScreenRegistry.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.CRYSTAL_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));
        ScreenRegistry.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.OBSIDIAN_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));
        ScreenRegistry.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.NETHERITE_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));
        ScreenRegistry.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.CHRISTMAS_CHEST, (desc, inventory, title) -> new CottonInventoryScreen<>(desc, inventory.player, title));
    }
}
