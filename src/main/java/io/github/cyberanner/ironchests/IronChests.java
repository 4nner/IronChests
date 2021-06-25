package io.github.cyberanner.ironchests;

import io.github.cyberanner.ironchests.registry.ModBlocks;
import io.github.cyberanner.ironchests.registry.ModItems;
import net.fabricmc.api.ModInitializer;

public class IronChests implements ModInitializer {

    public static final String MOD_ID = "ironchests";

    @Override
    public void onInitialize() {
        ModItems.registerItems();
        ModBlocks.registerBlocks();
    }
}
