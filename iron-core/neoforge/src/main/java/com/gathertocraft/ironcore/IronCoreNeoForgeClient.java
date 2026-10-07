package com.gathertocraft.ironcore;

import com.gathertocraft.ironcore.client.KeyEditorScreen;
import com.gathertocraft.ironcore.lock.KeyEditorMenus;
import com.gathertocraft.ironcore.lock.KeyEditorNet;
import com.gathertocraft.ironcore.lock.KeyEditorPayloads;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(value = IronCoreCommon.MOD_ID, dist = Dist.CLIENT)
public class IronCoreNeoForgeClient {
  public IronCoreNeoForgeClient(IEventBus modBus) {
    modBus.addListener(
        (RegisterMenuScreensEvent event) ->
            event.register(KeyEditorMenus.getType(), KeyEditorScreen::new));
    KeyEditorNet.init(
        payload ->
            Minecraft.getInstance()
                .getConnection()
                .send(
                    new net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket(
                        payload)));
    modBus.addListener(
        (RegisterPayloadHandlersEvent event) ->
            event
                .registrar(IronCoreCommon.MOD_ID)
                .playToClient(
                    KeyEditorPayloads.Sync.TYPE,
                    KeyEditorPayloads.Sync.CODEC,
                    (payload, context) ->
                        Minecraft.getInstance()
                            .execute(
                                () -> {
                                  if (Minecraft.getInstance().gui.screen()
                                      instanceof KeyEditorScreen screen) {
                                    screen.applySync(payload.decode());
                                  }
                                })));
  }
}
