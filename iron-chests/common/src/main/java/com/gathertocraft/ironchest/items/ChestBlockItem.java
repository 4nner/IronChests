package com.gathertocraft.ironchest.items;

import com.gathertocraft.ironchest.blocks.ChestTypes;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import org.jspecify.annotations.Nullable;

public class ChestBlockItem extends BlockItem {
  public ChestBlockItem(ChestTypes tier, Properties properties) {
    super(tier.getBlock(), properties);
  }

  public static List<Component> tooltipLines(ChestTypes tier) {
    List<Component> lines = new ArrayList<>();
    lines.add(
        Component.translatable(
            "tooltip.ironchest.chest.size", tier.size(), tier.rowCount(), tier.rowLength()));
    String trait = traitKey(tier);
    if (trait != null) {
      lines.add(Component.translatable(trait).withStyle(ChatFormatting.GRAY));
    }
    return lines;
  }

  private static @Nullable String traitKey(ChestTypes tier) {
    return switch (tier) {
      case DIRT -> "tooltip.ironchest.dirt_only";
      case CRYSTAL -> "tooltip.ironchest.see_through";
      case OBSIDIAN, NETHERITE -> "tooltip.ironchest.blast_resistant";
      case CHRISTMAS -> "tooltip.ironchest.festive";
      default -> null;
    };
  }
}
