package com.gathertocraft.ironcore.lock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/** Key binding carried on the stack: registry id plus the short code shown in tooltips. */
public record KeyData(UUID id, int code) {
  public static final Codec<KeyData> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      UUIDUtil.STRING_CODEC.fieldOf("id").forGetter(KeyData::id),
                      Codec.INT.fieldOf("code").forGetter(KeyData::code))
                  .apply(instance, KeyData::new));

  public static final StreamCodec<ByteBuf, KeyData> STREAM_CODEC =
      StreamCodec.composite(
          UUIDUtil.STREAM_CODEC, KeyData::id, ByteBufCodecs.INT, KeyData::code, KeyData::new);
}
