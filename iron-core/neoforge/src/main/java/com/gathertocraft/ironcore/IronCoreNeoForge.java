package com.gathertocraft.ironcore;

import com.gathertocraft.ironcore.handtruck.HandTruckEffects;
import com.gathertocraft.ironcore.lock.KeyEditorPayloads;
import com.gathertocraft.ironcore.platform.Platforms;
import com.gathertocraft.ironcore.platform.neoforge.NeoForgePlatformRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(IronCoreCommon.MOD_ID)
public class IronCoreNeoForge {
  public IronCoreNeoForge(IEventBus modBus) {
    // The bus is only available here; the registry is discovered via ServiceLoader.
    ((NeoForgePlatformRegistry) Platforms.registry()).init(IronCoreCommon.MOD_ID, modBus);
    IronCoreCommon.init(FMLPaths.CONFIGDIR.get());
    NeoForge.EVENT_BUS.addListener(
        (ServerTickEvent.Post event) -> {
          for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            HandTruckEffects.tickPlayer(player);
          }
        });
    modBus.addListener(
        (RegisterPayloadHandlersEvent event) -> {
          var registrar = event.registrar(IronCoreCommon.MOD_ID);
          registrar.playToServer(
              KeyEditorPayloads.Add.TYPE,
              KeyEditorPayloads.Add.CODEC,
              (payload, context) -> {
                if (context.player() instanceof ServerPlayer player) {
                  context.enqueueWork(() -> KeyEditorPayloads.handleAdd(player, payload));
                }
              });
          registrar.playToServer(
              KeyEditorPayloads.Remove.TYPE,
              KeyEditorPayloads.Remove.CODEC,
              (payload, context) -> {
                if (context.player() instanceof ServerPlayer player) {
                  context.enqueueWork(() -> KeyEditorPayloads.handleRemove(player, payload));
                }
              });
        });
  }
}
