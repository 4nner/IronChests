package anner.ironchest.registry;

import anner.ironchest.client.ChestScreen;
import anner.ironchest.screenhandlers.ChestScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ModScreenHandlers {
    public static void registerScreenHandlers() {
        HandledScreens.register(ModScreenHandlerType.COPPER_CHEST, ChestScreen::new);
        HandledScreens.register(ModScreenHandlerType.IRON_CHEST, ChestScreen::new);
        HandledScreens.register(ModScreenHandlerType.GOLD_CHEST, ChestScreen::new);
        HandledScreens.register(ModScreenHandlerType.DIAMOND_CHEST, ChestScreen::new);
        HandledScreens.register(ModScreenHandlerType.EMERALD_CHEST, ChestScreen::new);
        HandledScreens.register(ModScreenHandlerType.CRYSTAL_CHEST, ChestScreen::new);
        HandledScreens.register(ModScreenHandlerType.OBSIDIAN_CHEST, ChestScreen::new);
        HandledScreens.register(ModScreenHandlerType.NETHERITE_CHEST, ChestScreen::new);
        HandledScreens.register(ModScreenHandlerType.CHRISTMAS_CHEST, ChestScreen::new);
    }
}
