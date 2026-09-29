package com.gathertocraft.ironchest.client;

import com.gathertocraft.ironchest.IronChestsCommon;
import com.gathertocraft.ironchest.screenhandlers.ChestScreenHandler;
import com.gathertocraft.ironcore.ContainerGuiLayout;
import com.gathertocraft.ironcore.ContainerGuiLayout.LayoutKind;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
        ContainerGuiLayout.panelWidth(menu.getChestColumns()),
        ContainerGuiLayout.screenHeight(menu.getChestRows()));
    this.containerRows = menu.getChestRows();
    this.containerColumns = menu.getChestColumns();
    this.layoutKind = ContainerGuiLayout.layoutKind(this.containerColumns, this.containerRows);
    this.inventoryLabelY = this.imageHeight - 94;
    if (this.layoutKind == LayoutKind.WIDE_STRIPS) {
      this.inventoryLabelX = ContainerGuiLayout.playerInventoryX(this.containerColumns);
    }
  }

  @Override
  public void extractBackground(
      GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
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
    int bodyHeight = bodyHeight();
    blitVanillaRegion(graphics, xo, yo, this.imageWidth, bodyHeight, 0.0F, 0.0F);
    blitVanillaPlayerFooter(
        graphics, xo, yo + bodyHeight, this.imageWidth, this.imageHeight - bodyHeight);
  }

  private void renderTallVanillaLayout(GuiGraphicsExtractor graphics, int xo, int yo) {
    int panelWidth = this.imageWidth;
    blitVanillaRegion(graphics, xo, yo, panelWidth, ContainerGuiLayout.TITLE_HEIGHT, 0.0F, 0.0F);

    for (int row = 0; row < this.containerRows; row++) {
      int rowY = yo + ContainerGuiLayout.TITLE_HEIGHT + row * ContainerGuiLayout.ROW_HEIGHT;
      float textureV =
          ContainerGuiLayout.usesVanillaRow(this.containerColumns, row)
              ? ContainerGuiLayout.TITLE_HEIGHT + row * ContainerGuiLayout.ROW_HEIGHT
              : ContainerGuiLayout.TITLE_HEIGHT + 5 * ContainerGuiLayout.ROW_HEIGHT;
      blitVanillaRegion(
          graphics, xo, rowY, panelWidth, ContainerGuiLayout.ROW_HEIGHT, 0.0F, textureV);
    }

    blitTallPlayerFooter(graphics, xo, yo);
  }

  private void renderWideStripLayout(GuiGraphicsExtractor graphics, int xo, int yo) {
    int panelWidth = this.imageWidth;

    blitWideStrip(
        graphics,
        ContainerGuiLayout.wideTitleTexture(IronChestsCommon.MOD_ID, this.containerColumns),
        xo,
        yo,
        panelWidth,
        ContainerGuiLayout.TITLE_HEIGHT);

    for (int row = 0; row < this.containerRows; row++) {
      int rowY = yo + ContainerGuiLayout.TITLE_HEIGHT + row * ContainerGuiLayout.ROW_HEIGHT;
      blitWideStrip(
          graphics,
          ContainerGuiLayout.wideRowTexture(IronChestsCommon.MOD_ID, this.containerColumns),
          xo,
          rowY,
          panelWidth,
          ContainerGuiLayout.ROW_HEIGHT);
    }

    renderWidePlayerFooter(graphics, xo, yo + bodyHeight(), panelWidth);
  }

  private void renderWidePlayerFooter(
      GuiGraphicsExtractor graphics, int xo, int footerY, int panelWidth) {
    blitVanillaPlayerFooter(
        graphics,
        playerFooterX(xo),
        footerY,
        ContainerGuiLayout.VANILLA_PANEL_WIDTH,
        ContainerGuiLayout.PLAYER_PANEL_HEIGHT);
    blitWideStrip(
        graphics,
        ContainerGuiLayout.widePlayerTexture(IronChestsCommon.MOD_ID, this.containerColumns),
        xo,
        footerY,
        panelWidth,
        ContainerGuiLayout.WIDE_FOOTER_FRAME_HEIGHT);
  }

  private void blitTallPlayerFooter(GuiGraphicsExtractor graphics, int xo, int yo) {
    int bodyHeight = bodyHeight();
    blitVanillaPlayerFooter(
        graphics,
        playerFooterX(xo),
        yo + bodyHeight,
        ContainerGuiLayout.VANILLA_PANEL_WIDTH,
        this.imageHeight - bodyHeight);
  }

  private int bodyHeight() {
    return this.containerRows * ContainerGuiLayout.ROW_HEIGHT + ContainerGuiLayout.TITLE_HEIGHT;
  }

  private int playerFooterX(int xo) {
    return xo
        + ContainerGuiLayout.playerInventoryX(this.containerColumns)
        - ContainerGuiLayout.LEFT_INSET;
  }

  private static void blitWideStrip(
      GuiGraphicsExtractor graphics, Identifier texture, int x, int y, int width, int height) {
    graphics.blit(
        RenderPipelines.GUI_TEXTURED, texture, x, y, 0.0F, 0.0F, width, height, width, height);
  }

  private static void blitVanillaRegion(
      GuiGraphicsExtractor graphics, int x, int y, int width, int height, float u, float v) {
    graphics.blit(
        RenderPipelines.GUI_TEXTURED,
        ContainerGuiLayout.VANILLA_BACKGROUND,
        x,
        y,
        u,
        v,
        width,
        height,
        ContainerGuiLayout.TEXTURE_SIZE,
        ContainerGuiLayout.TEXTURE_SIZE);
  }

  private static void blitVanillaPlayerFooter(
      GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
    blitVanillaRegion(graphics, x, y, width, height, 0.0F, ContainerGuiLayout.PLAYER_PANEL_V);
  }
}
