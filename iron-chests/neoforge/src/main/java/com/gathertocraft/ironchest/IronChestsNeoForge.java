package com.gathertocraft.ironchest;

import com.gathertocraft.ironcore.lock.LockGuards;
import com.gathertocraft.ironcore.platform.Platforms;
import com.gathertocraft.ironcore.platform.neoforge.NeoForgePlatformRegistry;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

@Mod(IronChestsCommon.MOD_ID)
public class IronChestsNeoForge {
  public IronChestsNeoForge(IEventBus modBus) {
    // The bus is only available here; the registry is discovered via ServiceLoader.
    ((NeoForgePlatformRegistry) Platforms.registry()).init(IronChestsCommon.MOD_ID, modBus);
    IronChestsCommon.init(FMLPaths.CONFIGDIR.get());
    NeoForge.EVENT_BUS.addListener(
        (BreakBlockEvent event) -> {
          if (event.getLevel() instanceof Level level
              && LockGuards.isLockedFor(level, event.getPos(), event.getPlayer())) {
            event.setCanceled(true);
          }
        });
  }
}
