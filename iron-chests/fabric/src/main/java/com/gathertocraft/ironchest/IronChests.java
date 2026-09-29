package com.gathertocraft.ironchest;

import net.fabricmc.api.ModInitializer;

public class IronChests implements ModInitializer {
  @Override
  public void onInitialize() {
    IronChestsCommon.init();
  }
}
