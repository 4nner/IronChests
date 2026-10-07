package com.gathertocraft.ironcore.lock;

import com.gathertocraft.ironcore.LockableContainer;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

/**
 * A {@link LockableContainer} whose lock state is a link to a {@link KeyRegistry} entry. 
 */
public interface KeyLinkable extends LockableContainer {
  /** Linked key id, or null when unlocked (or legacy-locked, for containers that predate keys). */
  @Nullable UUID getLockKeyId();

  /**
   * Links this container to a registry entry, dropping any previous lock state.
   */
  void link(UUID keyId);

  /** Clears the key link, leaving the container unlocked. */
  void unlink();
}
