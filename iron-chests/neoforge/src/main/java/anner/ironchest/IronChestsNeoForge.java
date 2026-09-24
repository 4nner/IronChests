package anner.ironchest;

import anner.ironchest.platform.Platforms;
import anner.ironchest.platform.neoforge.NeoForgePlatformRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(IronChestsCommon.MOD_ID)
public class IronChestsNeoForge {
  public IronChestsNeoForge(IEventBus modBus) {
    // The bus is only available here; the registry is discovered via ServiceLoader.
    ((NeoForgePlatformRegistry) Platforms.registry()).init(modBus);
    IronChestsCommon.init();
  }
}
