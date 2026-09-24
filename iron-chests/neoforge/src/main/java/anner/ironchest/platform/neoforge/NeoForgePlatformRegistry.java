package anner.ironchest.platform.neoforge;

import anner.ironchest.IronChestsCommon;
import anner.ironchest.platform.PlatformRegistry;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class NeoForgePlatformRegistry implements PlatformRegistry {
  private final DeferredRegister<Block> blocks =
      DeferredRegister.create(BuiltInRegistries.BLOCK, IronChestsCommon.MOD_ID);
  private final DeferredRegister<Item> items =
      DeferredRegister.create(BuiltInRegistries.ITEM, IronChestsCommon.MOD_ID);
  private final DeferredRegister<BlockEntityType<?>> blockEntityTypes =
      DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, IronChestsCommon.MOD_ID);
  private final DeferredRegister<MenuType<?>> menuTypes =
      DeferredRegister.create(BuiltInRegistries.MENU, IronChestsCommon.MOD_ID);
  private final DeferredRegister<CreativeModeTab> tabs =
      DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, IronChestsCommon.MOD_ID);

  private IEventBus modBus;

  // Public no-arg constructor required by ServiceLoader; the event bus
  // arrives via init(IEventBus) from the mod entrypoint, before any registration.
  public NeoForgePlatformRegistry() {}

  public void init(IEventBus modBus) {
    this.modBus = modBus;
    this.blocks.register(modBus);
    this.items.register(modBus);
    this.blockEntityTypes.register(modBus);
    this.menuTypes.register(modBus);
    this.tabs.register(modBus);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Supplier<T> register(
      Registry<? super T> registry, Identifier id, Supplier<T> supplier) {
    if (registry == BuiltInRegistries.BLOCK) {
      return (Supplier<T>) this.blocks.register(id.getPath(), () -> (Block) supplier.get());
    }
    if (registry == BuiltInRegistries.ITEM) {
      return (Supplier<T>) this.items.register(id.getPath(), () -> (Item) supplier.get());
    }
    if (registry == BuiltInRegistries.BLOCK_ENTITY_TYPE) {
      return (Supplier<T>)
          this.blockEntityTypes.register(id.getPath(), () -> (BlockEntityType<?>) supplier.get());
    }
    if (registry == BuiltInRegistries.MENU) {
      return (Supplier<T>)
          this.menuTypes.register(id.getPath(), () -> (MenuType<?>) supplier.get());
    }
    if (registry == BuiltInRegistries.CREATIVE_MODE_TAB) {
      return (Supplier<T>) this.tabs.register(id.getPath(), () -> (CreativeModeTab) supplier.get());
    }
    throw new IllegalArgumentException("Unsupported registry: " + registry.key());
  }

  @Override
  public <T extends BlockEntity> BlockEntityType<T> blockEntityType(
      BiFunction<BlockPos, BlockState, T> factory, Block... validBlocks) {
    return new BlockEntityType<>((pos, state) -> factory.apply(pos, state), Set.of(validBlocks));
  }

  @Override
  public CreativeModeTab creativeTab(Supplier<ItemStack> icon, Component title) {
    return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).title(title).icon(icon).build();
  }

  @Override
  public void addTabItems(
      ResourceKey<CreativeModeTab> tab, Supplier<List<? extends ItemLike>> itemsSupplier) {
    IEventBus bus =
        Objects.requireNonNull(this.modBus, "init(IEventBus) must be called before registering");
    bus.addListener(
        (BuildCreativeModeTabContentsEvent event) -> {
          if (!event.getTabKey().equals(tab)) {
            return;
          }
          for (ItemLike item : itemsSupplier.get()) {
            event.accept(
                new ItemStack(item.asItem()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
          }
        });
  }
}
