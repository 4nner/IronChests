package anner.ironchest.client;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;

public final class SlotBackgrounds {
    private static final BackgroundPainter VANILLA_SLOT = (context, left, top, panel) -> {
        if (!(panel instanceof WItemSlot slot)) {
            return;
        }

        int slotsWide = slot.getWidth() / 18;
        int slotsHigh = slot.getHeight() / 18;
        int index = 0;

        for (int y = 0; y < slotsHigh; y++) {
            for (int x = 0; x < slotsWide; x++) {
                int sx = left + (x * 18);
                int sy = top + (y * 18);
                ScreenDrawing.drawBeveledPanel(context, sx, sy, 18, 18);
                index++;
            }
        }
    };

    private SlotBackgrounds() {
    }

    public static void applyVanillaSlotBackgrounds(WWidget widget) {
        if (widget instanceof WItemSlot slot) {
            slot.setBackgroundPainter(VANILLA_SLOT);
        }

        if (widget instanceof WPanel panel) {
            panel.streamChildren().forEach(SlotBackgrounds::applyVanillaSlotBackgrounds);
        }
    }
}
