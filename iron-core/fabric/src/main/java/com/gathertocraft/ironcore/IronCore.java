package com.gathertocraft.ironcore;

import com.gathertocraft.ironcore.lock.KeyEditorPayloads;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;

public class IronCore implements ModInitializer {
  @Override
  public void onInitialize() {
    IronCoreCommon.init(FabricLoader.getInstance().getConfigDir());
    PayloadTypeRegistry.serverboundPlay()
        .register(KeyEditorPayloads.Add.TYPE, KeyEditorPayloads.Add.CODEC);
    PayloadTypeRegistry.serverboundPlay()
        .register(KeyEditorPayloads.Remove.TYPE, KeyEditorPayloads.Remove.CODEC);
    ServerPlayNetworking.registerGlobalReceiver(
        KeyEditorPayloads.Add.TYPE,
        (payload, context) -> KeyEditorPayloads.handleAdd(context.player(), payload));
    ServerPlayNetworking.registerGlobalReceiver(
        KeyEditorPayloads.Remove.TYPE,
        (payload, context) -> KeyEditorPayloads.handleRemove(context.player(), payload));
  }
}
