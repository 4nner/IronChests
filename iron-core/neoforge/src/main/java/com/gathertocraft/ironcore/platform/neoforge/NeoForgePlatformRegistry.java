package com.gathertocraft.ironcore.platform.neoforge;

import com.gathertocraft.ironcore.platform.PlatformRegistry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  // One register set per mod namespace; core serves multiple content mods,
  // each with its own namespace on its own mod event bus.
  private record Registers(
      DeferredRegister<Block> blocks,
      DeferredRegister<Item> items,
      DeferredRegister<BlockEntityType<?>> blockEntityTypes,
      DeferredRegister<MenuType<?>> menuTypes,
      DeferredRegister<CreativeModeTab> tabs) {}

  private final Map<String, Registers> registers = new HashMap<>();
  private final Map<String, IEventBus> buses = new HashMap<>();

  // Public no-arg constructor required by ServiceLoader; each content mod
  // calls init(namespace, bus) from its entrypoint before registering.
  public NeoForgePlatformRegistry() {}

  public void init(String namespace, IEventBus modBus) {
    if (this.registers.containsKey(namespace)) {
      return;
    }
    Registers created =
        new Registers(
            DeferredRegister.create(BuiltInRegistries.BLOCK, namespace),
            DeferredRegister.create(BuiltInRegistries.ITEM, namespace),
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, namespace),
            DeferredRegister.create(BuiltInRegistries.MENU, namespace),
            DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, namespace));
    created.blocks.register(modBus);
    created.items.register(modBus);
    created.blockEntityTypes.register(modBus);
    created.menuTypes.register(modBus);
    created.tabs.register(modBus);
    this.registers.put(namespace, created);
    this.buses.put(namespace, modBus);
  }

  private Registers registersFor(Identifier id) {
    Registers found = this.registers.get(id.getNamespace());
    if (found == null) {
      throw new IllegalStateException(
          "Namespace '"
              + id.getNamespace()
              + "' is not initialized;"
              + " call init(namespace, bus) from the mod entrypoint first");
    }
    return found;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Supplier<T> register(
      Registry<? super T> registry, Identifier id, Supplier<T> supplier) {
    Registers target = registersFor(id);
    if (registry == BuiltInRegistries.BLOCK) {
      return (Supplier<T>) target.blocks.register(id.getPath(), () -> (Block) supplier.get());
    }
    if (registry == BuiltInRegistries.ITEM) {
      return (Supplier<T>) target.items.register(id.getPath(), () -> (Item) supplier.get());
    }
    if (registry == BuiltInRegistries.BLOCK_ENTITY_TYPE) {
      return (Supplier<T>)
          target.blockEntityTypes.register(id.getPath(), () -> (BlockEntityType<?>) supplier.get());
    }
    if (registry == BuiltInRegistries.MENU) {
      return (Supplier<T>)
          target.menuTypes.register(id.getPath(), () -> (MenuType<?>) supplier.get());
    }
    if (registry == BuiltInRegistries.CREATIVE_MODE_TAB) {
      return (Supplier<T>)
          target.tabs.register(id.getPath(), () -> (CreativeModeTab) supplier.get());
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
    return new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0).title(title).icon(icon).build();
  }

  @Override
  public void addTabItems(
      ResourceKey<CreativeModeTab> tab, Supplier<List<? extends ItemLike>> itemsSupplier) {
    IEventBus bus =
        Objects.requireNonNull(
            this.buses.get(tab.identifier().getNamespace()),
            "Namespace '" + tab.identifier().getNamespace() + "' is not initialized");
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
