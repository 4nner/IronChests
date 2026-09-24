package anner.ironchest.platform;

import java.util.ServiceLoader;

// Single lookup for the active loader's registry. Loader modules provide the
// implementation via META-INF/services; common code never names a loader class.
public final class Platforms {
  private static final PlatformRegistry REGISTRY = load();

  private Platforms() {}

  private static PlatformRegistry load() {
    return ServiceLoader.load(PlatformRegistry.class)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No PlatformRegistry implementation found"));
  }

  public static PlatformRegistry registry() {
    return REGISTRY;
  }
}
