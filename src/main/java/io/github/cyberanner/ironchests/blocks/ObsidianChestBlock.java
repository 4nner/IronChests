package io.github.cyberanner.ironchests.blocks;


import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class ObsidianChestBlock {
    public static FabricBlockSettings settings() {
        return(FabricBlockSettings.of(Material.STONE)
                .hardness(50.0F)
                .resistance(1200.0F)
                .sounds(BlockSoundGroup.STONE)
                .breakByTool(FabricToolTags.PICKAXES, 3)
                .requiresTool());
    }
}