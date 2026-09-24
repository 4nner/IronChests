package anner.ironchest.platform;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface PlatformRegistry {
  <T> Supplier<T> register(Registry<? super T> registry, Identifier id, Supplier<T> supplier);

  <T extends BlockEntity> BlockEntityType<T> blockEntityType(
      BiFunction<BlockPos, BlockState, T> factory, Block... validBlocks);

  CreativeModeTab creativeTab(Supplier<ItemStack> icon, Component title);

  void addTabItems(
      ResourceKey<CreativeModeTab> tab, Supplier<List<? extends ItemLike>> itemsSupplier);
}
