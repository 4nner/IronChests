package com.gathertocraft.ironchest.items;

import com.gathertocraft.ironchest.blocks.ChestTypes;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.Nullable;

public class ChestBlockItem extends BlockItem {
  private final ChestTypes tier;

  public ChestBlockItem(ChestTypes tier, Properties properties) {
    super(tier.getBlock(), properties);
    this.tier = tier;
  }

  @Override
  public void appendHoverText(
      ItemStack stack,
      TooltipContext context,
      TooltipDisplay display,
      Consumer<Component> lines,
      TooltipFlag flag) {
    super.appendHoverText(stack, context, display, lines, flag);
    lines.accept(
        Component.translatable(
            "tooltip.ironchest.chest.size",
            this.tier.size(),
            this.tier.rowCount(),
            this.tier.rowLength()));
    String trait = traitKey(this.tier);
    if (trait != null) {
      lines.accept(Component.translatable(trait).withStyle(ChatFormatting.GRAY));
    }
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
