package com.gathertocraft.ironchest;

import com.gathertocraft.ironcore.platform.Platforms;
import com.gathertocraft.ironcore.platform.neoforge.NeoForgePlatformRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(IronChestsCommon.MOD_ID)
public class IronChestsNeoForge {
  public IronChestsNeoForge(IEventBus modBus) {
    // The bus is only available here; the registry is discovered via ServiceLoader.
    ((NeoForgePlatformRegistry) Platforms.registry()).init(IronChestsCommon.MOD_ID, modBus);
    IronChestsCommon.init();
  }
}
