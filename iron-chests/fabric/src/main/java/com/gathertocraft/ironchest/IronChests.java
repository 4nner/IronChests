package com.gathertocraft.ironchest;

import com.gathertocraft.ironchest.legacy.LegacyUpgrades;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class IronChests implements ModInitializer {
  @Override
  public void onInitialize() {
    IronChestsCommon.init(FabricLoader.getInstance().getConfigDir());
    LegacyUpgrades.registerAll();
  }
}
