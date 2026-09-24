package anner.ironchest.platform.fabric;

import anner.ironchest.platform.PlatformRegistry;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public final class FabricPlatformRegistry implements PlatformRegistry {
  // Public no-arg constructor required by ServiceLoader.
  public FabricPlatformRegistry() {}

  @Override
  public <T extends BlockEntity> BlockEntityType<T> blockEntityType(
      BiFunction<BlockPos, BlockState, T> factory, Block... validBlocks) {
    return FabricBlockEntityTypeBuilder.create(
            (pos, state) -> factory.apply(pos, state), validBlocks)
        .build();
  }

  @Override
  public CreativeModeTab creativeTab(Supplier<ItemStack> icon, Component title) {
    return FabricCreativeModeTab.builder().icon(icon).title(title).build();
  }

  @Override
  public void addTabItems(ResourceKey<CreativeModeTab> tab, List<? extends ItemLike> items) {
    CreativeModeTabEvents.modifyOutputEvent(tab)
        .register(
            output -> {
              for (ItemLike item : items) {
                output.accept(item);
              }
            });
  }
}
