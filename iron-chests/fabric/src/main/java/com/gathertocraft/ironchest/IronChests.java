package com.gathertocraft.ironchest;

import com.gathertocraft.ironchest.legacy.LegacyUpgrades;
import net.fabricmc.api.ModInitializer;

public class IronChests implements ModInitializer {
  @Override
  public void onInitialize() {
    IronChestsCommon.init();
    LegacyUpgrades.registerAll();
  }
}
