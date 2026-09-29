package com.gathertocraft.ironchest.registry;

import com.gathertocraft.ironchest.blocks.ChestTypes;
import com.gathertocraft.ironchest.client.ChestScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public class ModScreenHandlers {
  public static void registerScreenHandlers() {
    for (ChestTypes type : ChestTypes.PLAYABLE) {
      MenuScreens.register(type.getMenuType(), ChestScreen::new);
    }
  }
}
