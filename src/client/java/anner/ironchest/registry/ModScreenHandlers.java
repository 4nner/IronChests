package anner.ironchest.registry;

import anner.ironchest.blocks.ChestTypes;
import anner.ironchest.client.ChestScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public class ModScreenHandlers {
    public static void registerScreenHandlers() {
        for (ChestTypes type : ChestTypes.PLAYABLE) {
            MenuScreens.register(type.getMenuType(), ChestScreen::new);
        }
    }
}
