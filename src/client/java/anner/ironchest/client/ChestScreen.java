package anner.ironchest.client;

import anner.ironchest.screenhandlers.ChestScreenHandler;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class ChestScreen extends CottonInventoryScreen<ChestScreenHandler> {
    private boolean slotBackgroundsApplied;

    public ChestScreen(ChestScreenHandler description, PlayerInventory inventory, Text title) {
        super(description, inventory, title);
    }

    @Override
    public void init() {
        super.init();
        applySlotBackgrounds();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!slotBackgroundsApplied) {
            applySlotBackgrounds();
        }
        super.render(context, mouseX, mouseY, delta);
    }

    private void applySlotBackgrounds() {
        if (description != null && description.getRootPanel() != null) {
            SlotBackgrounds.applyVanillaSlotBackgrounds(description.getRootPanel());
            slotBackgroundsApplied = true;
        }
    }
}
