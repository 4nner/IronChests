package com.gathertocraft.ironcore;

import com.gathertocraft.ironcore.client.KeyEditorScreen;
import com.gathertocraft.ironcore.handtruck.HandTruckItem;
import com.gathertocraft.ironcore.lock.KeyEditorMenus;
import com.gathertocraft.ironcore.lock.KeyEditorNet;
import com.gathertocraft.ironcore.lock.KeyEditorPayloads;
import com.gathertocraft.ironcore.lock.KeyItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;

public class IronCoreClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    MenuScreens.register(KeyEditorMenus.getType(), KeyEditorScreen::new);
    KeyEditorNet.init(ClientPlayNetworking::send);
    ItemTooltipCallback.EVENT.register(
        (stack, context, flag, lines) -> {
          if (stack.getItem() instanceof KeyItem) {
            lines.addAll(KeyItem.tooltipLines(stack));
          } else if (stack.getItem() instanceof HandTruckItem) {
            lines.addAll(HandTruckItem.tooltipLines(stack));
          }
        });
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
