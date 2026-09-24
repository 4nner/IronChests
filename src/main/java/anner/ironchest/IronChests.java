package anner.ironchest;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class IronChests implements ModInitializer {
  public static final String MOD_ID = "ironchest";
  public static final ResourceKey<CreativeModeTab> TAB =
      ResourceKey.create(
          Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MOD_ID, "general"));

  @Override
  public void onInitialize() {
    IronChestsCommon.init();
  }
}
