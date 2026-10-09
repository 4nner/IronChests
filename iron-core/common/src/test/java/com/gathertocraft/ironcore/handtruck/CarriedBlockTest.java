package com.gathertocraft.ironcore.handtruck;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mojang.serialization.Dynamic;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Test;

class CarriedBlockTest {
  private static CarriedBlock sample(boolean spawnerMove) {
    CompoundTag state = new CompoundTag();
    state.putString("Name", "minecraft:chest");
    CompoundTag be = new CompoundTag();
    be.putString("id", "minecraft:chest");
    be.putInt("size", 27);
    return new CarriedBlock(Identifier.withDefaultNamespace("chest"), state, be, spawnerMove);
  }

  @Test
  void codecRoundTrip() {
    CarriedBlock original = sample(false);
    CompoundTag encoded =
        (CompoundTag) CarriedBlock.CODEC.encodeStart(NbtOps.INSTANCE, original).getOrThrow();
    CarriedBlock decoded =
        CarriedBlock.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, encoded)).result().orElseThrow();
    assertEquals(original, decoded);
  }

  @Test
  void spawnerFlagSurvivesCodec() {
    CarriedBlock original = sample(true);
    CompoundTag encoded =
        (CompoundTag) CarriedBlock.CODEC.encodeStart(NbtOps.INSTANCE, original).getOrThrow();
    CarriedBlock decoded =
        CarriedBlock.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, encoded)).result().orElseThrow();
    assertEquals(true, decoded.spawnerMove());
    assertEquals(original, decoded);
  }

  @Test
  void streamCodecRoundTrip() {
    CarriedBlock original = sample(true);
    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
    CarriedBlock.STREAM_CODEC.encode(buf, original);
    assertEquals(original, CarriedBlock.STREAM_CODEC.decode(buf));
  }
}
