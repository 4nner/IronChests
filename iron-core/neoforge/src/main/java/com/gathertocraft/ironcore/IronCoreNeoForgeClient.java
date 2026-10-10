package com.gathertocraft.ironcore;

import com.gathertocraft.ironcore.client.KeyEditorScreen;
import com.gathertocraft.ironcore.handtruck.HandTruckItem;
import com.gathertocraft.ironcore.lock.KeyEditorMenus;
import com.gathertocraft.ironcore.lock.KeyEditorNet;
import com.gathertocraft.ironcore.lock.KeyItem;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.common.NeoForge;

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
    NeoForge.EVENT_BUS.addListener(
        (RenderTooltipEvent.GatherComponents event) -> {
          if (event.getItemStack().getItem() instanceof KeyItem) {
            for (Component line : KeyItem.tooltipLines(event.getItemStack())) {
              event.getTooltipElements().add(Either.left(line));
            }
          } else if (event.getItemStack().getItem() instanceof HandTruckItem) {
            for (Component line : HandTruckItem.tooltipLines(event.getItemStack())) {
              event.getTooltipElements().add(Either.left(line));
            }
          }
        });
  }
}
