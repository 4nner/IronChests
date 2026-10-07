package com.gathertocraft.ironcore.lock;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/** Loader bridge for editor packets. Each loader's client init installs its sender here. */
public final class KeyEditorNet {
  private KeyEditorNet() {}

  public interface Sender {
    void send(CustomPacketPayload payload);
  }

  private static Sender SENDER = payload -> {};

  public static void init(Sender sender) {
    SENDER = sender;
  }

  public static void send(CustomPacketPayload payload) {
    SENDER.send(payload);
  }
}
