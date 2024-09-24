package anner.ironchest;

import anner.ironchest.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.List;


public class IronChests implements ModInitializer {

    public static final String MOD_ID = "ironchest";
    public static final RegistryKey<ItemGroup> TAB = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(MOD_ID, "general"));

    public record UpdateInventory(BlockPos blockPos, List<ItemStack> itemStacks) implements CustomPayload {
        public static final Id<UpdateInventory> UPDATE_INV_PACKET_ID = new Id<>(Identifier.of(MOD_ID, "update"));
        public static final PacketCodec<RegistryByteBuf, UpdateInventory> PACKET_CODEC = PacketCodec.tuple(
                BlockPos.PACKET_CODEC, UpdateInventory::blockPos,
                ItemStack.OPTIONAL_LIST_PACKET_CODEC, UpdateInventory::itemStacks,
                UpdateInventory::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return UPDATE_INV_PACKET_ID;
        }

        public static void registerPayloadTypes() {
            PayloadTypeRegistry.playS2C().register(UpdateInventory.UPDATE_INV_PACKET_ID, UpdateInventory.PACKET_CODEC);
            PayloadTypeRegistry.playC2S().register(UpdateInventory.UPDATE_INV_PACKET_ID, UpdateInventory.PACKET_CODEC);
        }
    }

    @Override
    public void onInitialize() {
        ModItems.registerItems();
        ModBlocks.registerBlocks();
        ModItemGroup.registerItemGroup();
        ModBlockEntityType.registerBlockEntities();
        ModScreenHandlerType.registerScreenHandlers();
        UpdateInventory.registerPayloadTypes();
    }
}
