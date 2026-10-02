package com.gathertocraft.ironcore;

import com.gathertocraft.ironcore.platform.Platforms;
import com.gathertocraft.ironcore.platform.neoforge.NeoForgePlatformRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(IronCoreCommon.MOD_ID)
public class IronCoreNeoForge {
  public IronCoreNeoForge(IEventBus modBus) {
    // The bus is only available here; the registry is discovered via ServiceLoader.
    ((NeoForgePlatformRegistry) Platforms.registry()).init(IronCoreCommon.MOD_ID, modBus);
    IronCoreCommon.init();
  }
}
