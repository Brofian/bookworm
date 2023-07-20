package de.fabianholzwarth;

import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;

abstract public class AbstractBookAnalyzer {

    /**
     * Generate a description of the item inside a chiseled bookshelf at the given slot
     */
    protected Component[] getSlotDescription(Inventory inventory, int slotId) {
        return this.getSlotDescription(inventory, slotId, "");
    }

    protected Component[] getSlotDescription(Inventory inventory, int slotId, String prefix) {
        ItemStack[] itemStack = inventory.getContents();

        if (slotId < 0 || itemStack.length <= slotId || itemStack[slotId] == null) {
            return new Component[0];
        }

        Component[] description = this.getTitleFromItemStack(itemStack[slotId]);
        Component[] contents = new Component[description.length + 1];
        contents[0] = ChatHelper.text(prefix);
        System.arraycopy(description, 0, contents, 1, description.length);

        return contents;
    }

    /**
     * Generate an array of Components as a description of the given itemstack
     */
    protected Component[] getTitleFromItemStack(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();

        switch (stack.getType()) {
            case WRITTEN_BOOK -> {
                BookMeta bookMeta = (BookMeta) meta;
                if (meta.hasDisplayName()) {
                    Component displayName = meta.displayName();
                    if (displayName != null) {
                        return new Component[]{
                                displayName,
                                ChatHelper.text(" ("),
                                ChatHelper.translated("book.byAuthor", new Component[]{ChatHelper.text(bookMeta.getAuthor())}),
                                ChatHelper.text(")")
                        };
                    }
                }
                return new Component[]{
                        ChatHelper.text(bookMeta.getTitle() + " ("),
                        ChatHelper.translated("book.byAuthor", new Component[]{ChatHelper.text(bookMeta.getAuthor())}),
                        ChatHelper.text(")")
                };
            }
            case WRITABLE_BOOK -> {
                if (meta.hasDisplayName()) {
                    Component displayName = meta.displayName();
                    if (displayName != null) {
                        return new Component[]{displayName};
                    }
                }
                return new Component[]{ChatHelper.translated("item.minecraft.writable_book")};
            }
            case BOOK -> {
                if (meta.hasDisplayName()) {
                    Component displayName = meta.displayName();
                    if (displayName != null) {
                        return new Component[]{displayName};
                    }
                }
                return new Component[]{
                        ChatHelper.translated("item.minecraft.book"),
                };
            }
            case ENCHANTED_BOOK -> {
                EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) meta;
                Set<Enchantment> enchants = enchantmentStorageMeta.getStoredEnchants().keySet();
                if (enchants.size() == 0) {
                    return new Component[]{ChatHelper.translated("item.minecraft.enchanted_book")};
                }
                Enchantment firstEnchantment = enchants.stream().findFirst().get();
                return new Component[]{
                        ChatHelper.translated("item.minecraft.enchanted_book"),
                        ChatHelper.text(" ("),
                        ChatHelper.translated(firstEnchantment.translationKey()),
                        (enchants.size() > 1 ? ChatHelper.text(", ...") : null),
                        ChatHelper.text(")")
                };
            }
        }

        System.out.println("[Bookworm] Encountered unknown book type: " + stack.getType().name());
        return new Component[]{ChatHelper.text("Suspicious book")};
    }

}
