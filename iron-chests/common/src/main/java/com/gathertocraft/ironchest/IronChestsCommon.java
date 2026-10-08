package com.gathertocraft.ironchest;

import com.gathertocraft.ironchest.blocks.ChestTypes;
import com.gathertocraft.ironchest.blocks.GenericChestBlock;
import com.gathertocraft.ironchest.config.ChestRows;
import com.gathertocraft.ironchest.registry.ModBlockEntityType;
import com.gathertocraft.ironchest.registry.ModBlocks;
import com.gathertocraft.ironchest.registry.ModItemGroup;
import com.gathertocraft.ironchest.registry.ModItems;
import com.gathertocraft.ironchest.registry.ModScreenHandlerType;
import com.gathertocraft.ironchest.registry.ModUpgradeBindings;
import com.gathertocraft.ironchest.support.CopperGolemSupport;
import com.gathertocraft.ironcore.config.JsonConfig;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.file.Path;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.slf4j.Logger;

public final class IronChestsCommon {
  private IronChestsCommon() {}

  private static final Logger LOGGER = LogUtils.getLogger();
  public static final String MOD_ID = "ironchest";
  public static final ResourceKey<CreativeModeTab> TAB =
      ResourceKey.create(
          Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MOD_ID, "general"));

  public static void init(Path configDir) {
    try {
      JsonConfig.LoadResult rows = ChestRows.load(configDir);
      for (String warning : rows.warnings()) {
        LOGGER.warn(warning);
      }
      ChestTypes.setConfiguredRows(rows.values());
      GenericChestBlock.setOpenUnderSolidBlocks(
          rows.flags().getOrDefault(ChestRows.OPEN_UNDER_SOLID_BLOCKS, false));
      CopperGolemSupport.setEnabled(
          rows.flags().getOrDefault(ChestRows.COPPER_GOLEM_INTERACTION, true));
    } catch (IOException e) {
      LOGGER.error("Failed to load ironchest.json; using default chest sizes", e);
    }
    ModBlocks.registerBlocks();
    ModUpgradeBindings.bindVanilla();
    ModItems.registerItems();
    ModItemGroup.registerItemGroup();
    ModItems.addTabItems(TAB);
    ModBlockEntityType.registerBlockEntities();
    ModScreenHandlerType.registerScreenHandlers();
  }
}
