package io.github.cyberanner.ironchests;

import io.github.cyberanner.ironchests.blocks.blockentities.CrystalChestEntity;
import io.github.cyberanner.ironchests.registry.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;


@Environment(EnvType.CLIENT)
public class IronChestsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModScreenHandlers.registerScreenHandlers();
        ModBlockEntityRenderer.registerBlockEntityRenderer();

        // Crystal Chest Rendering
        ClientPlayNetworking.registerGlobalReceiver(IronChests.UPDATE_INV_PACKET_ID, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            DefaultedList<ItemStack> inv = DefaultedList.ofSize(12, ItemStack.EMPTY);
            for (int i = 0; i < 12; i++) {
                inv.set(i, buf.readItemStack());
            }
            client.execute(() -> {
                CrystalChestEntity blockEntity = (CrystalChestEntity) MinecraftClient.getInstance().world.getBlockEntity(pos);
                blockEntity.setInvStackList(inv);
            });
        });
    }
}
