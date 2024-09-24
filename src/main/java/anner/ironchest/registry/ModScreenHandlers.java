package anner.ironchest.registry;

import anner.ironchest.screenhandlers.ChestScreenHandler;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ModScreenHandlers {
    public static void registerScreenHandlers() {
        HandledScreens.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.COPPER_CHEST, CottonInventoryScreen::new);
        HandledScreens.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.IRON_CHEST, CottonInventoryScreen::new);
        HandledScreens.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.GOLD_CHEST, CottonInventoryScreen::new);
        HandledScreens.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.DIAMOND_CHEST, CottonInventoryScreen::new);
        HandledScreens.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.EMERALD_CHEST, CottonInventoryScreen::new);
        HandledScreens.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.CRYSTAL_CHEST, CottonInventoryScreen::new);
        HandledScreens.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.OBSIDIAN_CHEST, CottonInventoryScreen::new);
        HandledScreens.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.NETHERITE_CHEST, CottonInventoryScreen::new);
        HandledScreens.<ChestScreenHandler, CottonInventoryScreen<ChestScreenHandler>>register(ModScreenHandlerType.CHRISTMAS_CHEST, CottonInventoryScreen::new);
    }
}
