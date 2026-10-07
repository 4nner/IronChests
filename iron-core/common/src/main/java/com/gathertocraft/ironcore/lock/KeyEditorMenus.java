package com.gathertocraft.ironcore.lock;

import com.gathertocraft.ironcore.IronCoreCommon;
import com.gathertocraft.ironcore.platform.Platforms;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

/** Menu type for the Container Key trusted-list editor (zero slots, entry-bound). */
public class KeyEditorMenus {
  private KeyEditorMenus() {}

  private static Supplier<MenuType<KeyEditorMenu>> TYPE;

  public static void registerScreenHandlers() {
    TYPE =
        Platforms.registry()
            .register(
                BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(IronCoreCommon.MOD_ID, "key_editor"),
                () ->
                    new MenuType<>(
                        (syncId, inventory) -> new KeyEditorMenu(getType(), syncId, inventory),
                        FeatureFlags.VANILLA_SET));
  }

  public static MenuType<KeyEditorMenu> getType() {
    return TYPE.get();
  }
}
