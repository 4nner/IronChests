package com.gathertocraft.ironchest;

import com.gathertocraft.ironchest.blocks.ChestTypes;
import com.gathertocraft.ironchest.client.ChestEntityRenderer;
import com.gathertocraft.ironchest.client.ChestScreen;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(value = IronChestsCommon.MOD_ID, dist = Dist.CLIENT)
public class IronChestsNeoForgeClient {
  public IronChestsNeoForgeClient(IEventBus modBus) {
    modBus.addListener(
        (RegisterMenuScreensEvent event) -> {
          for (ChestTypes type : ChestTypes.PLAYABLE) {
            event.register(type.getMenuType(), ChestScreen::new);
          }
        });
    modBus.addListener(
        (EntityRenderersEvent.RegisterRenderers event) -> {
          for (ChestTypes type : ChestTypes.PLAYABLE) {
            registerRenderer(event, type.getBlockEntityType());
          }
        });
  }

  @SuppressWarnings("unchecked")
  private static <T extends ChestBlockEntity> void registerRenderer(
      EntityRenderersEvent.RegisterRenderers event,
      BlockEntityType<? extends ChestBlockEntity> blockEntityType) {
    event.registerBlockEntityRenderer(
        (BlockEntityType<T>) blockEntityType, ChestEntityRenderer::new);
  }
}
