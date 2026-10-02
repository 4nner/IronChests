package com.gathertocraft.ironcore;

import net.fabricmc.api.ModInitializer;

public class IronCore implements ModInitializer {
  @Override
  public void onInitialize() {
    IronCoreCommon.init();
  }
}
