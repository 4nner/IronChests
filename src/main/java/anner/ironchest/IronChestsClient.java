package anner.ironchest;

import anner.ironchest.blocks.blockentities.CrystalChestEntity;
import anner.ironchest.registry.ModBlockEntityRenderer;
import anner.ironchest.registry.ModScreenHandlers;
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
        ClientPlayNetworking.registerGlobalReceiver(IronChests.UpdateInventory.UPDATE_INV_PACKET_ID, (payload, context) -> {
            BlockPos pos = payload.blockPos();
            DefaultedList<ItemStack> inv = DefaultedList.ofSize(12, ItemStack.EMPTY);
            for (int i = 0; i < 12; i++) {
                inv.set(i, payload.itemStacks().get(i));
            }
            context.client().execute(() -> {
                CrystalChestEntity blockEntity = (CrystalChestEntity) MinecraftClient.getInstance().world.getBlockEntity(pos);
                blockEntity.setHeldStacks(inv);
            });
        });
    }
}
