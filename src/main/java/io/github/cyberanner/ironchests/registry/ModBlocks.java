package io.github.cyberanner.ironchests.registry;

import io.github.cyberanner.ironchests.IronChests;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block COPPER_CHEST = new Block(FabricBlockSettings.of(Material.METAL).breakByTool(FabricToolTags.PICKAXES, 1).requiresTool().strength(3.0F, 6.0F).sounds(BlockSoundGroup.COPPER));


    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(IronChests.MOD_ID, "copper_chest"), COPPER_CHEST);
    }
}
