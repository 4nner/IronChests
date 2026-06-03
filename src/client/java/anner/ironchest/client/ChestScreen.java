package anner.ironchest.client;

import anner.ironchest.screenhandlers.ChestGuiLayout;
import anner.ironchest.screenhandlers.ChestGuiLayout.LayoutKind;
import anner.ironchest.screenhandlers.ChestScreenHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ChestScreen extends AbstractContainerScreen<ChestScreenHandler> {
    private final int containerRows;
    private final int containerColumns;
    private final LayoutKind layoutKind;

    public ChestScreen(ChestScreenHandler menu, Inventory inventory, Component title) {
        super(
            menu,
            inventory,
            title,
            ChestGuiLayout.panelWidth(menu.getChestColumns()),
            ChestGuiLayout.screenHeight(menu.getChestRows())
        );
        this.containerRows = menu.getChestRows();
        this.containerColumns = menu.getChestColumns();
        this.layoutKind = ChestGuiLayout.layoutKind(this.containerColumns, this.containerRows);
        this.inventoryLabelY = this.imageHeight - 94;
        if (this.layoutKind == LayoutKind.WIDE_STRIPS) {
            this.inventoryLabelX = ChestGuiLayout.playerInventoryX(this.containerColumns);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int xo = this.leftPos;
        int yo = this.topPos;

        switch (this.layoutKind) {
            case VANILLA_BLIT -> blitVanillaFullPanel(graphics, xo, yo);
            case TALL_VANILLA_ROWS -> renderTallVanillaLayout(graphics, xo, yo);
            case WIDE_STRIPS -> renderWideStripLayout(graphics, xo, yo);
        }
    }

    private void blitVanillaFullPanel(GuiGraphicsExtractor graphics, int xo, int yo) {
        int bodyHeight = this.containerRows * ChestGuiLayout.ROW_HEIGHT + ChestGuiLayout.TITLE_HEIGHT;
        blitVanillaRegion(graphics, xo, yo, this.imageWidth, bodyHeight, 0.0F, 0.0F);
        blitVanillaPlayerFooter(graphics, xo, yo + bodyHeight, this.imageWidth, this.imageHeight - bodyHeight);
    }

    private void renderTallVanillaLayout(GuiGraphicsExtractor graphics, int xo, int yo) {
        int panelWidth = this.imageWidth;
        blitVanillaRegion(graphics, xo, yo, panelWidth, ChestGuiLayout.TITLE_HEIGHT, 0.0F, 0.0F);

        for (int row = 0; row < this.containerRows; row++) {
            int rowY = yo + ChestGuiLayout.TITLE_HEIGHT + row * ChestGuiLayout.ROW_HEIGHT;
            float textureV = ChestGuiLayout.usesVanillaChestRow(this.containerColumns, row)
                ? ChestGuiLayout.TITLE_HEIGHT + row * ChestGuiLayout.ROW_HEIGHT
                : ChestGuiLayout.TITLE_HEIGHT + 5 * ChestGuiLayout.ROW_HEIGHT;
            blitVanillaRegion(graphics, xo, rowY, panelWidth, ChestGuiLayout.ROW_HEIGHT, 0.0F, textureV);
        }

        blitTallPlayerFooter(graphics, xo, yo);
    }

    private void renderWideStripLayout(GuiGraphicsExtractor graphics, int xo, int yo) {
        int panelWidth = this.imageWidth;
        int bodyHeight = this.containerRows * ChestGuiLayout.ROW_HEIGHT + ChestGuiLayout.TITLE_HEIGHT;
        int footerY = yo + bodyHeight;

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            ChestGuiLayout.wideTitleTexture(this.containerColumns),
            xo,
            yo,
            0.0F,
            0.0F,
            panelWidth,
            ChestGuiLayout.TITLE_HEIGHT,
            panelWidth,
            ChestGuiLayout.TITLE_HEIGHT
        );

        for (int row = 0; row < this.containerRows; row++) {
            int rowY = yo + ChestGuiLayout.TITLE_HEIGHT + row * ChestGuiLayout.ROW_HEIGHT;
            graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                ChestGuiLayout.wideRowTexture(this.containerColumns),
                xo,
                rowY,
                0.0F,
                0.0F,
                panelWidth,
                ChestGuiLayout.ROW_HEIGHT,
                panelWidth,
                ChestGuiLayout.ROW_HEIGHT
            );
        }

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            ChestGuiLayout.widePlayerTexture(this.containerColumns),
            xo,
            footerY,
            0.0F,
            0.0F,
            panelWidth,
            ChestGuiLayout.PLAYER_PANEL_HEIGHT,
            panelWidth,
            ChestGuiLayout.PLAYER_PANEL_HEIGHT
        );
    }

    private void blitTallPlayerFooter(GuiGraphicsExtractor graphics, int xo, int yo) {
        int bodyHeight = this.containerRows * ChestGuiLayout.ROW_HEIGHT + ChestGuiLayout.TITLE_HEIGHT;
        int playerBgX = xo + ChestGuiLayout.playerInventoryX(this.containerColumns) - ChestGuiLayout.LEFT_INSET;
        blitVanillaPlayerFooter(graphics, playerBgX, yo + bodyHeight, ChestGuiLayout.VANILLA_PANEL_WIDTH, this.imageHeight - bodyHeight);
    }

    private static void blitVanillaRegion(GuiGraphicsExtractor graphics, int x, int y, int width, int height, float u, float v) {
        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            ChestGuiLayout.VANILLA_BACKGROUND,
            x,
            y,
            u,
            v,
            width,
            height,
            ChestGuiLayout.TEXTURE_SIZE,
            ChestGuiLayout.TEXTURE_SIZE
        );
    }

    private static void blitVanillaPlayerFooter(GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            ChestGuiLayout.VANILLA_BACKGROUND,
            x,
            y,
            0.0F,
            ChestGuiLayout.PLAYER_PANEL_V,
            width,
            height,
            ChestGuiLayout.TEXTURE_SIZE,
            ChestGuiLayout.TEXTURE_SIZE
        );
    }
}
