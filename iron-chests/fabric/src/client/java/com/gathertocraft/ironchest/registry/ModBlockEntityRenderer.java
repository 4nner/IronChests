package com.gathertocraft.ironchest.registry;

import com.gathertocraft.ironchest.blocks.ChestTypes;
import com.gathertocraft.ironchest.client.ChestEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

public class ModBlockEntityRenderer {
  public static void registerBlockEntityRenderer() {
    for (ChestTypes type : ChestTypes.PLAYABLE) {
      register(type.getBlockEntityType());
    }
  }

  @SuppressWarnings("unchecked")
  private static <T extends ChestBlockEntity> void register(
      BlockEntityType<? extends ChestBlockEntity> blockEntityType) {
    BlockEntityRenderers.register((BlockEntityType<T>) blockEntityType, ChestEntityRenderer::new);
  }
}
