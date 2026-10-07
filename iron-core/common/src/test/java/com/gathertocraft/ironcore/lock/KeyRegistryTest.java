package com.gathertocraft.ironcore.lock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.junit.jupiter.api.Test;

class KeyRegistryTest {
  private static final UUID OWNER = UUID.fromString("11111111-1111-1111-1111-111111111111");
  private static final UUID FRIEND = UUID.fromString("22222222-2222-2222-2222-222222222222");

  @Test
  void trustRejectsOwnerDuplicatesAndUntrustRemoves() {
    KeyRegistry registry = KeyRegistry.TYPE.constructor().get();
    KeyRegistry.Entry entry = registry.create(UUID.randomUUID(), OWNER, "Owner");

    assertFalse(registry.trust(entry, OWNER, "Owner"));
    assertTrue(registry.trust(entry, FRIEND, "Friend"));
    assertFalse(registry.trust(entry, FRIEND, "Friend"));
    assertEquals(Map.of(FRIEND, "Friend"), entry.trustedView());

    assertTrue(registry.untrust(entry, FRIEND));
    assertFalse(registry.untrust(entry, FRIEND));
    assertTrue(entry.trustedView().isEmpty());
  }

  @Test
  void codesAreUniqueAndMonotonic() {
    KeyRegistry registry = KeyRegistry.TYPE.constructor().get();
    int first = registry.create(UUID.randomUUID(), OWNER, "Owner").code();
    int second = registry.create(UUID.randomUUID(), OWNER, "Owner").code();
    assertEquals(1, first);
    assertEquals(2, second);
  }

  @Test
  void codecRoundTripPreservesEntries() {
    KeyRegistry registry = KeyRegistry.TYPE.constructor().get();
    UUID id = UUID.randomUUID();
    KeyRegistry.Entry created = registry.create(id, OWNER, "Owner");
    registry.rename(created, "Master Key");
    registry.trust(created, FRIEND, "Friend");

    CompoundTag encoded =
        (CompoundTag) KeyRegistry.TYPE.codec().encodeStart(NbtOps.INSTANCE, registry).getOrThrow();
    KeyRegistry decoded = KeyRegistry.TYPE.codec().parse(NbtOps.INSTANCE, encoded).getOrThrow();

    KeyRegistry.Entry entry = decoded.get(id);
    assertEquals("Master Key", entry.name());
    assertEquals(OWNER, entry.owner());
    assertEquals("Owner", entry.ownerName());
    assertEquals(Map.of(FRIEND, "Friend"), entry.trustedView());
    // Codes continue past the restored high-water mark, never reused.
    assertEquals(entry.code() + 1, decoded.create(UUID.randomUUID(), OWNER, "O").code());
  }
}
