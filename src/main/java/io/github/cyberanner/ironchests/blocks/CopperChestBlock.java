package io.github.cyberanner.ironchests.blocks;


import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class CopperChestBlock extends GenericChestBlock {

    public CopperChestBlock() {
        super(FabricBlockSettings.of(Material.METAL)
                .strength(3.0F, 6.0F)
                .breakByTool(FabricToolTags.PICKAXES, 1)
                .requiresTool()
                .sounds(BlockSoundGroup.COPPER));
    }
}
