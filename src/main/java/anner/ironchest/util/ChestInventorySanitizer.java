package anner.ironchest.util;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

public final class ChestInventorySanitizer {
    private ChestInventorySanitizer() {
    }

    public static void sanitize(NonNullList<ItemStack> items) {
        for (int i = 0; i < items.size(); i++) {
            ItemStack sanitized = sanitize(items.get(i));
            if (sanitized != items.get(i)) {
                items.set(i, sanitized);
            }
        }
    }

    public static ItemStack sanitize(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack copy = stack.copy();
        if (copy.getCount() <= 0) {
            return ItemStack.EMPTY;
        }

        if (copy.getCount() > copy.getMaxStackSize()) {
            copy.setCount(copy.getMaxStackSize());
        }

        if (copy.has(DataComponents.DAMAGE)) {
            int damage = copy.getOrDefault(DataComponents.DAMAGE, 0);
            if (damage < 0) {
                copy.setDamageValue(0);
            } else if (copy.getMaxDamage() > 0 && damage > copy.getMaxDamage()) {
                copy.setDamageValue(copy.getMaxDamage());
            }
        }

        return ItemStack.validateStrict(copy).isError() ? ItemStack.EMPTY : copy;
    }
}
