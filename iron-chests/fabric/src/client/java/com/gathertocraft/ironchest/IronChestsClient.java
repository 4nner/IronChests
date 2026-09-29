package com.gathertocraft.ironchest;

import com.gathertocraft.ironchest.registry.ModBlockEntityRenderer;
import com.gathertocraft.ironchest.registry.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;

public class IronChestsClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    ModScreenHandlers.registerScreenHandlers();
    ModBlockEntityRenderer.registerBlockEntityRenderer();
  }
}
