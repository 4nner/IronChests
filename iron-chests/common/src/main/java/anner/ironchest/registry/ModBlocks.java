package anner.ironchest.registry;

import anner.ironchest.IronChestsCommon;
import anner.ironchest.blocks.ChestTypes;
import anner.ironchest.blocks.GenericChestBlock;
import anner.ironchest.platform.Platforms;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
  private ModBlocks() {}

  public static void registerBlocks() {
    for (ChestTypes type : ChestTypes.PLAYABLE) {
      type.bindBlock(
          Platforms.registry()
              .register(
                  BuiltInRegistries.BLOCK,
                  Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, type.registryId),
                  () -> create(type)));
    }
  }

  private static Block create(ChestTypes type) {
    return new GenericChestBlock(blockProperties(type), type);
  }

  private static BlockBehaviour.Properties blockProperties(ChestTypes type) {
    return type.blockProperties()
        .setId(
            ResourceKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath(IronChestsCommon.MOD_ID, type.registryId)));
  }
}
