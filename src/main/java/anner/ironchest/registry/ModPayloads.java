package anner.ironchest.registry;

import anner.ironchest.IronChests;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class ModPayloads {
    public record CrystalChestBlockPayload(BlockPos blockPos,
                                           List<ItemStack> itemStacks) implements CustomPayload {
        public static final Id<CrystalChestBlockPayload> ID = new Id<>(Identifier.of(IronChests.MOD_ID, "crystal_chest_block"));
        public static final PacketCodec<RegistryByteBuf, CrystalChestBlockPayload> PACKET_CODEC = PacketCodec.tuple(
                BlockPos.PACKET_CODEC, CrystalChestBlockPayload::blockPos,
                ItemStack.OPTIONAL_LIST_PACKET_CODEC, CrystalChestBlockPayload::itemStacks,
                CrystalChestBlockPayload::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(CrystalChestBlockPayload.ID, CrystalChestBlockPayload.PACKET_CODEC);
    }
}
