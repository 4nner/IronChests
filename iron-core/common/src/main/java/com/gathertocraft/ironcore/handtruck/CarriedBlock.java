package com.gathertocraft.ironcore.handtruck;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

/**
 * A lifted block riding on a hand truck stack: which block it was, its blockstate, its block entity
 * data, and whether lifting it counted as a spawner move (for the enhanced truck's proportional
 * durability cost).
 */
public record CarriedBlock(
    Identifier blockId, CompoundTag stateTag, CompoundTag beTag, boolean spawnerMove) {
  public static final Codec<CarriedBlock> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      Identifier.CODEC.fieldOf("block").forGetter(CarriedBlock::blockId),
                      CompoundTag.CODEC.fieldOf("state").forGetter(CarriedBlock::stateTag),
                      CompoundTag.CODEC.fieldOf("block_entity").forGetter(CarriedBlock::beTag),
                      Codec.BOOL.fieldOf("spawner_move").forGetter(CarriedBlock::spawnerMove))
                  .apply(instance, CarriedBlock::new));

  public static final StreamCodec<ByteBuf, CarriedBlock> STREAM_CODEC =
      StreamCodec.composite(
          Identifier.STREAM_CODEC,
          CarriedBlock::blockId,
          ByteBufCodecs.COMPOUND_TAG,
          CarriedBlock::stateTag,
          ByteBufCodecs.COMPOUND_TAG,
          CarriedBlock::beTag,
          ByteBufCodecs.BOOL,
          CarriedBlock::spawnerMove,
          CarriedBlock::new);
}
