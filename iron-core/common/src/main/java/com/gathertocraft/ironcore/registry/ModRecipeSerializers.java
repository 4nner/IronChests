package com.gathertocraft.ironcore.registry;

import com.gathertocraft.ironcore.IronCoreCommon;
import com.gathertocraft.ironcore.lock.KeyDuplicationRecipe;
import com.gathertocraft.ironcore.platform.Platforms;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModRecipeSerializers {
  private ModRecipeSerializers() {}

  public static void registerSerializers() {
    Platforms.registry()
        .register(
            BuiltInRegistries.RECIPE_SERIALIZER,
            Identifier.fromNamespaceAndPath(IronCoreCommon.MOD_ID, "key_duplication"),
            () -> KeyDuplicationRecipe.SERIALIZER);
  }
}
