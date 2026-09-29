package anner.ironcore;

import net.minecraft.resources.Identifier;

public final class ContainerGuiLayout {
  public static final int VANILLA_PANEL_WIDTH = 176;
  public static final int SLOT_SIZE = 18;
  public static final int LEFT_INSET = 8;
  public static final int RIGHT_INSET = 6;
  public static final int PLAYER_INVENTORY_GAP = 13;
  public static final int HOTBAR_OFFSET = 58;
  public static final int NARROW_CENTER_OFFSET = 9;

  public static final int TITLE_HEIGHT = 17;
  public static final int ROW_HEIGHT = 18;
  public static final int TEXTURE_SIZE = 256;
  public static final int PLAYER_PANEL_HEIGHT = 96;
  public static final int WIDE_FOOTER_FRAME_HEIGHT = 7;
  public static final float PLAYER_PANEL_V = 126.0F;
  public static final int VANILLA_ROWS = 6;
  public static final int VANILLA_COLUMNS = 9;

  public static final Identifier VANILLA_BACKGROUND =
      Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");
  private static final String WIDE_TEXTURE_BASE = "textures/gui/container/wide/";

  public enum LayoutKind {
    VANILLA_BLIT,
    TALL_VANILLA_ROWS,
    WIDE_STRIPS
  }

  private ContainerGuiLayout() {}

  public static LayoutKind layoutKind(int columns, int rows) {
    if (columns > VANILLA_COLUMNS) {
      return LayoutKind.WIDE_STRIPS;
    }
    if (rows > VANILLA_ROWS) {
      return LayoutKind.TALL_VANILLA_ROWS;
    }
    return LayoutKind.VANILLA_BLIT;
  }

  public static int panelWidth(int columns) {
    if (columns <= VANILLA_COLUMNS) {
      return Math.max(VANILLA_PANEL_WIDTH, LEFT_INSET + columns * SLOT_SIZE + RIGHT_INSET);
    }
    return LEFT_INSET + (columns - 1) * SLOT_SIZE + (SLOT_SIZE - 1) + RIGHT_INSET;
  }

  public static int screenHeight(int rows) {
    return 114 + rows * ROW_HEIGHT;
  }

  public static int containerSlotStartX(int columns) {
    return LEFT_INSET + (Math.max(VANILLA_COLUMNS, columns) - columns) * NARROW_CENTER_OFFSET;
  }

  public static int playerInventoryX(int columns) {
    int chestWidth = columns * SLOT_SIZE;
    int playerWidth = 9 * SLOT_SIZE;
    return containerSlotStartX(columns) + (chestWidth - playerWidth) / 2;
  }

  public static int playerInventoryY(int rows) {
    return SLOT_SIZE + rows * SLOT_SIZE + PLAYER_INVENTORY_GAP;
  }

  public static boolean usesVanillaRow(int columns, int row) {
    return columns <= VANILLA_COLUMNS && row < VANILLA_ROWS;
  }

  public static Identifier wideTitleTexture(String namespace, int columns) {
    return switch (columns) {
      case 12 -> wideTexture(namespace, "chest_title", 12);
      case 14 -> wideTexture(namespace, "chest_title", 14);
      default -> wideGuiTexture(namespace, "chest_title", columns);
    };
  }

  public static Identifier wideRowTexture(String namespace, int columns) {
    return switch (columns) {
      case 12 -> wideTexture(namespace, "chest_row", 12);
      case 14 -> wideTexture(namespace, "chest_row", 14);
      default -> wideGuiTexture(namespace, "chest_row", columns);
    };
  }

  public static Identifier widePlayerTexture(String namespace, int columns) {
    return switch (columns) {
      case 12 -> wideTexture(namespace, "chest_player", 12);
      case 14 -> wideTexture(namespace, "chest_player", 14);
      default -> wideGuiTexture(namespace, "chest_player", columns);
    };
  }

  private static Identifier wideTexture(String namespace, String baseName, int columns) {
    return Identifier.fromNamespaceAndPath(
        namespace, WIDE_TEXTURE_BASE + baseName + "_" + columns + ".png");
  }

  private static Identifier wideGuiTexture(String namespace, String baseName, int columns) {
    if (columns <= VANILLA_COLUMNS) {
      throw new IllegalArgumentException("Wide texture requires columns > 9, got " + columns);
    }
    return wideTexture(namespace, baseName, columns);
  }
}
