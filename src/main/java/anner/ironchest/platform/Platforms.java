package anner.ironchest.platform;

import anner.ironchest.platform.fabric.FabricPlatformRegistry;

// Single lookup for the active loader's registry. Replaced by a ServiceLoader
// lookup once loader-specific modules exist; until then this is the only
// Fabric reference reachable from common code.
public final class Platforms {
  private Platforms() {}

  public static PlatformRegistry registry() {
    return FabricPlatformRegistry.INSTANCE;
  }
}
