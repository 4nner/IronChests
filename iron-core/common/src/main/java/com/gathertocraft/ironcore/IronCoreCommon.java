package com.gathertocraft.ironcore;

import com.gathertocraft.ironcore.config.CoreConfig;
import com.gathertocraft.ironcore.lock.KeyEditorMenus;
import com.gathertocraft.ironcore.registry.ModCoreItemGroup;
import com.gathertocraft.ironcore.registry.ModCoreItems;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.file.Path;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.slf4j.Logger;

public final class IronCoreCommon {
  private IronCoreCommon() {}

  public static final String MOD_ID = "ironcore";
  public static final ResourceKey<CreativeModeTab> TAB =
      ResourceKey.create(
          Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MOD_ID, "general"));

  private static final Logger LOGGER = LogUtils.getLogger();

  public static void init(Path configDir) {
    try {
      for (String warning : CoreConfig.load(configDir).warnings()) {
        LOGGER.warn(warning);
      }
    } catch (IOException e) {
      LOGGER.error("Failed to load ironcore.json; locks stay enabled", e);
    }
    ModCoreItems.registerItems();
    ModCoreItemGroup.registerItemGroup();
    ModCoreItems.addTabItems(TAB);
    KeyEditorMenus.registerScreenHandlers();
  }
}
