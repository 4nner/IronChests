package com.gathertocraft.ironcore.client;

import com.gathertocraft.ironcore.lock.KeyEditorMenu;
import com.gathertocraft.ironcore.lock.KeyEditorNet;
import com.gathertocraft.ironcore.lock.KeyEditorPayloads;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * Editor for a Container Key registry entry. Sends {@link KeyEditorPayloads.Add}/{@link
 * KeyEditorPayloads.Remove} to the server and renders the list from server-pushed {@link
 * KeyEditorPayloads.Sync} snapshots: the menu carries no slots, so vanilla inventory sync never
 * pushes changes back and re-reading the stack would stay stale.
 */
public class KeyEditorScreen extends Screen implements MenuAccess<KeyEditorMenu> {
  private static final int MAX_VISIBLE_ROWS = 12;
  private static final int MIN_VISIBLE_ROWS = 3;
  private static final int ROW_HEIGHT = 22;
  private static final int LIST_TOP = 58;

  private final KeyEditorMenu menu;
  private EditBox nameBox;
  private Button addButton;
  private Button doneButton;
  private final List<Button> removeButtons = new ArrayList<>();
  private Map<UUID, String> displayed = new LinkedHashMap<>();
  private int scrollOffset;

  public KeyEditorScreen(KeyEditorMenu menu, Inventory inventory, Component title) {
    super(title);
    this.menu = menu;
  }

  @Override
  protected void init() {
    int top = layoutTop();
    int visible = visibleRows(top);
    int left = (this.width - 200) / 2;
    this.displayed = new LinkedHashMap<>(this.menu.trustedSnapshot());
    this.scrollOffset = Math.min(this.scrollOffset, maxOffset());

    this.nameBox =
        new EditBox(
            this.font,
            left,
            top + 18,
            136,
            20,
            Component.translatable("menu.ironcore.key_editor.name"));
    this.nameBox.setMaxLength(16);
    this.addRenderableWidget(this.nameBox);

    this.addButton =
        Button.builder(Component.translatable("menu.ironcore.key_editor.add"), button -> sendAdd())
            .bounds(left + 140, top + 18, 60, 20)
            .build();
    this.addRenderableWidget(this.addButton);

    this.removeButtons.clear();
    for (int i = 0; i < visible; i++) {
      final int row = i;
      Button remove =
          Button.builder(Component.literal("x"), button -> sendRemove(row))
              .bounds(left + 176, top + LIST_TOP + i * ROW_HEIGHT, 24, 20)
              .build();
      this.removeButtons.add(remove);
      this.addRenderableWidget(remove);
    }
    updateRemoveButtons();

    this.doneButton =
        Button.builder(
                Component.translatable("menu.ironcore.key_editor.done"), button -> this.onClose())
            .bounds(left + 50, top + LIST_TOP + visible * ROW_HEIGHT + 8, 100, 20)
            .build();
    this.addRenderableWidget(this.doneButton);
  }

  /** Rows fitting the window. Two-pass so the panel stays centered on its real height. */
  private int layoutTop() {
    int guess = Math.max(8, (this.height - 340) / 2);
    int visible = visibleRows(guess);
    return Math.max(8, (this.height - (visible * ROW_HEIGHT + 100)) / 2);
  }

  private int visibleRows(int top) {
    return Math.max(
        MIN_VISIBLE_ROWS, Math.min(MAX_VISIBLE_ROWS, (this.height - top - 96) / ROW_HEIGHT));
  }

  private int visibleRows() {
    return visibleRows(layoutTop());
  }

  private int maxOffset() {
    return Math.max(0, this.displayed.size() - visibleRows());
  }

  private void updateRemoveButtons() {
    for (int i = 0; i < this.removeButtons.size(); i++) {
      this.removeButtons.get(i).visible = this.scrollOffset + i < this.displayed.size();
    }
  }

  private void sendAdd() {
    if (this.nameBox == null) {
      return;
    }
    String name = this.nameBox.getValue().trim();
    if (name.isEmpty()) {
      return;
    }
    KeyEditorNet.send(new KeyEditorPayloads.Add(name));
    this.nameBox.setValue("");
  }

  private void sendRemove(int row) {
    List<UUID> ids = new ArrayList<>(this.displayed.keySet());
    int index = this.scrollOffset + row;
    if (index < 0 || index >= ids.size()) {
      return;
    }
    KeyEditorNet.send(new KeyEditorPayloads.Remove(ids.get(index).toString()));
  }

  /** Applies a server-pushed snapshot. Safe to call from the network thread. */
  public void applySync(Map<UUID, String> snapshot) {
    this.displayed = new LinkedHashMap<>(snapshot);
    this.scrollOffset = Math.min(this.scrollOffset, maxOffset());
    updateRemoveButtons();
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
    if (maxOffset() > 0 && scrollY != 0) {
      int next =
          Math.min(maxOffset(), Math.max(0, this.scrollOffset - (int) Math.signum(scrollY) * 3));
      if (next != this.scrollOffset) {
        this.scrollOffset = next;
        updateRemoveButtons();
        return true;
      }
    }
    return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
  }

  @Override
  public boolean keyPressed(net.minecraft.client.input.KeyEvent event) {
    if (this.nameBox != null
        && this.nameBox.isFocused()
        && (event.key() == InputConstants.KEY_RETURN
            || event.key() == InputConstants.KEY_NUMPADENTER)) {
      sendAdd();
      return true;
    }
    return super.keyPressed(event);
  }

  @Override
  public void extractRenderState(
      GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
    super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    int left = (this.width - 200) / 2;
    int top = layoutTop();
    int visible = visibleRows(top);
    graphics.centeredText(this.font, this.title, this.width / 2, top + 4, 0xFFFFFFFF);
    graphics.text(
        this.font,
        Component.translatable("menu.ironcore.key_editor.hint"),
        left,
        top + 42,
        0xFF808080);
    List<String> names = new ArrayList<>(this.displayed.values());
    for (int i = 0; i < visible; i++) {
      int index = this.scrollOffset + i;
      if (index >= names.size()) {
        break;
      }
      graphics.text(
          this.font,
          Component.literal(names.get(index)),
          left + 4,
          top + LIST_TOP + 6 + i * ROW_HEIGHT,
          0xFFFFFFFF);
    }
    if (this.displayed.size() > visibleRows()) {
      int trackX = left + 202;
      int trackY = top + LIST_TOP;
      int trackH = visibleRows() * ROW_HEIGHT;
      graphics.fill(trackX, trackY, trackX + 4, trackY + trackH, 0xFF404040);
      int thumbH = Math.max(12, trackH * visibleRows() / this.displayed.size());
      int thumbY = trackY + (trackH - thumbH) * this.scrollOffset / maxOffset();
      graphics.fill(trackX, thumbY, trackX + 4, thumbY + thumbH, 0xFFA0A0A0);
    }
  }

  @Override
  public KeyEditorMenu getMenu() {
    return this.menu;
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }
}
