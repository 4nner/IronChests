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
  private final int playerRows;
  private final boolean extendedInventory;
  private final LayoutKind layoutKind;

  // Segments of generic_54.png's 96px player panel (V=126): 14px top padding, 18px rows,
  // 4px gap, 18px hotbar, 6px bottom padding. Extra rows repeat the first inventory row.
  private static final int PLAYER_TOP_HEIGHT = 14;
  private static final int PLAYER_GAP_HEIGHT = 4;
  private static final int PLAYER_HOTBAR_HEIGHT = 18;
  private static final int PLAYER_BOTTOM_HEIGHT = 6;

  public ChestScreen(ChestScreenHandler menu, Inventory inventory, Component title) {
    super(
        menu,
        inventory,
        title,
        ContainerGuiLayout.panelWidth(menu.getChestColumns()),
        ContainerGuiLayout.screenHeight(menu.getChestRows(), menu.getPlayerRows()));
    this.containerRows = menu.getChestRows();
    this.containerColumns = menu.getChestColumns();
    this.playerRows = menu.getPlayerRows();
    this.extendedInventory = menu.isExtendedInventory();
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
    blitPlayerFooter(graphics, xo, yo + bodyHeight, this.imageWidth);
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
    blitPlayerFooter(
        graphics, playerFooterX(xo), yo + bodyHeight, ContainerGuiLayout.VANILLA_PANEL_WIDTH);
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

  private void blitPlayerFooter(GuiGraphicsExtractor graphics, int x, int y, int width) {
    if (!this.extendedInventory) {
      blitVanillaPlayerFooter(
          graphics, x, y, width, ContainerGuiLayout.playerPanelHeight(this.playerRows));
      return;
    }
    float panelV = ContainerGuiLayout.PLAYER_PANEL_V;
    float rowV = panelV + PLAYER_TOP_HEIGHT;
    float gapV = rowV + 3 * ContainerGuiLayout.ROW_HEIGHT;
    blitVanillaRegion(graphics, x, y, width, PLAYER_TOP_HEIGHT, 0.0F, panelV);
    int rowY = y + PLAYER_TOP_HEIGHT;
    for (int row = 0; row < this.playerRows; row++) {
      blitVanillaRegion(
          graphics,
          x,
          rowY + row * ContainerGuiLayout.ROW_HEIGHT,
          width,
          ContainerGuiLayout.ROW_HEIGHT,
          0.0F,
          rowV);
    }
    int afterRows = rowY + this.playerRows * ContainerGuiLayout.ROW_HEIGHT;
    blitVanillaRegion(graphics, x, afterRows, width, PLAYER_GAP_HEIGHT, 0.0F, gapV);
    blitVanillaRegion(
        graphics,
        x,
        afterRows + PLAYER_GAP_HEIGHT,
        width,
        PLAYER_HOTBAR_HEIGHT,
        0.0F,
        gapV + PLAYER_GAP_HEIGHT);
    blitVanillaRegion(
        graphics,
        x,
        afterRows + PLAYER_GAP_HEIGHT + PLAYER_HOTBAR_HEIGHT,
        width,
        PLAYER_BOTTOM_HEIGHT,
        0.0F,
        gapV + PLAYER_GAP_HEIGHT + PLAYER_HOTBAR_HEIGHT);
  }

  private static void blitVanillaPlayerFooter(
      GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
    blitVanillaRegion(graphics, x, y, width, height, 0.0F, ContainerGuiLayout.PLAYER_PANEL_V);
  }
}
