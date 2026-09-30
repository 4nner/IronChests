package com.gathertocraft.ironchest;

import com.gathertocraft.ironchest.blocks.ChestTypes;
import com.gathertocraft.ironchest.client.ChestEntityRenderer;
import com.gathertocraft.ironchest.client.ChestScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

public class IronChestsClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    for (ChestTypes type : ChestTypes.PLAYABLE) {
      MenuScreens.register(type.getMenuType(), ChestScreen::new);
    }
    for (ChestTypes type : ChestTypes.PLAYABLE) {
      registerRenderer(type.getBlockEntityType());
    }
  }

  @SuppressWarnings("unchecked")
  private static <T extends ChestBlockEntity> void registerRenderer(
      BlockEntityType<? extends ChestBlockEntity> blockEntityType) {
    BlockEntityRenderers.register((BlockEntityType<T>) blockEntityType, ChestEntityRenderer::new);
  }
}
