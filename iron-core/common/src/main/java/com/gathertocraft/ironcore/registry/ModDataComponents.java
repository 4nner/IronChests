package com.gathertocraft.ironcore.registry;

import com.gathertocraft.ironcore.IronCoreCommon;
import com.gathertocraft.ironcore.handtruck.CarriedBlock;
import com.gathertocraft.ironcore.lock.KeyData;
import com.gathertocraft.ironcore.platform.Platforms;
import java.util.function.Supplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModDataComponents {
  private ModDataComponents() {}

  private static Supplier<DataComponentType<KeyData>> LOCK_DATA;
  private static Supplier<DataComponentType<CarriedBlock>> CARRIED_BLOCK;

  public static void registerComponents() {
    LOCK_DATA =
        Platforms.registry()
            .register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                Identifier.fromNamespaceAndPath(IronCoreCommon.MOD_ID, "lock_data"),
                () ->
                    DataComponentType.<KeyData>builder()
                        .persistent(KeyData.CODEC)
                        .networkSynchronized(KeyData.STREAM_CODEC)
                        .build());
    CARRIED_BLOCK =
        Platforms.registry()
            .register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                Identifier.fromNamespaceAndPath(IronCoreCommon.MOD_ID, "carried_block"),
                () ->
                    DataComponentType.<CarriedBlock>builder()
                        .persistent(CarriedBlock.CODEC)
                        .networkSynchronized(CarriedBlock.STREAM_CODEC)
                        .build());
  }

  public static DataComponentType<KeyData> lockData() {
    return LOCK_DATA.get();
  }

  public static DataComponentType<CarriedBlock> carriedBlock() {
    return CARRIED_BLOCK.get();
  }
}
