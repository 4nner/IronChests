package io.github.cyberanner.ironchests.screenhandlers;


import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class CopperChestScreen extends CottonInventoryScreen<ChestScreenHandler> {
    public CopperChestScreen(ChestScreenHandler gui, PlayerEntity player, Text title) {
        super(gui, player, title);
    }
}