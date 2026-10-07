package com.gathertocraft.ironcore;

import com.gathertocraft.ironcore.client.KeyEditorScreen;
import com.gathertocraft.ironcore.lock.KeyEditorMenus;
import com.gathertocraft.ironcore.lock.KeyEditorNet;
import com.gathertocraft.ironcore.lock.KeyEditorPayloads;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;

public class IronCoreClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    MenuScreens.register(KeyEditorMenus.getType(), KeyEditorScreen::new);
    KeyEditorNet.init(ClientPlayNetworking::send);
    PayloadTypeRegistry.clientboundPlay()
        .register(KeyEditorPayloads.Sync.TYPE, KeyEditorPayloads.Sync.CODEC);
    ClientPlayNetworking.registerGlobalReceiver(
        KeyEditorPayloads.Sync.TYPE,
        (payload, context) -> {
          if (Minecraft.getInstance().gui.screen() instanceof KeyEditorScreen screen) {
            screen.applySync(payload.decode());
          }
        });
  }
}
