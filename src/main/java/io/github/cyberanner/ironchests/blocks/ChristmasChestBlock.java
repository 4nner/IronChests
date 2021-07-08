package io.github.cyberanner.ironchests.blocks;


import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class ChristmasChestBlock {
    public static FabricBlockSettings settings() {
        return(FabricBlockSettings.of(Material.WOOD)
                .hardness(3.0F)
                .resistance(3.0F)
                .sounds(BlockSoundGroup.WOOD)
                .breakByTool(FabricToolTags.AXES, 0)
                .requiresTool());
    }
}
