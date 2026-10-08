package com.gathertocraft.ironcore.lock;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * One keyed key plus one blank key yields two copies of the keyed key. Two keyed keys never match,
 * so different ids cannot merge by accident.
 */
public class KeyDuplicationRecipe extends CustomRecipe {
  public static final MapCodec<KeyDuplicationRecipe> MAP_CODEC =
      MapCodec.unit(KeyDuplicationRecipe::new);
  public static final StreamCodec<RegistryFriendlyByteBuf, KeyDuplicationRecipe> STREAM_CODEC =
      new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buf, KeyDuplicationRecipe recipe) {}

        @Override
        public KeyDuplicationRecipe decode(RegistryFriendlyByteBuf buf) {
          return new KeyDuplicationRecipe();
        }
      };
  public static final RecipeSerializer<KeyDuplicationRecipe> SERIALIZER =
      new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

  public KeyDuplicationRecipe() {}

  @Override
  public boolean matches(CraftingInput input, Level level) {
    return keyed(input) != null;
  }

  @Override
  public ItemStack assemble(CraftingInput input) {
    ItemStack keyed = keyed(input);
    if (keyed == null) {
      return ItemStack.EMPTY;
    }
    return keyed.copyWithCount(2);
  }

  private static ItemStack keyed(CraftingInput input) {
    ItemStack keyed = null;
    boolean blank = false;
    for (ItemStack stack : input.items()) {
      if (stack.isEmpty()) {
        continue;
      }
      if (!(stack.getItem() instanceof KeyItem) || stack.getCount() != 1) {
        return null;
      }
      if (KeyItem.readKeyId(stack) != null) {
        if (keyed != null) {
          return null;
        }
        keyed = stack;
      } else {
        if (blank) {
          return null;
        }
        blank = true;
      }
    }
    return blank ? keyed : null;
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public boolean showNotification() {
    return true;
  }

  @Override
  public String group() {
    return "";
  }

  @Override
  public CraftingBookCategory category() {
    return CraftingBookCategory.MISC;
  }

  @Override
  public RecipeBookCategory recipeBookCategory() {
    return RecipeBookCategories.CRAFTING_MISC;
  }

  @Override
  public PlacementInfo placementInfo() {
    return PlacementInfo.NOT_PLACEABLE;
  }

  @Override
  public RecipeSerializer<KeyDuplicationRecipe> getSerializer() {
    return SERIALIZER;
  }
}
