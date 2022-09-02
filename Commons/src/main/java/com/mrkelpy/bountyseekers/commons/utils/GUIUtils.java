package com.mrkelpy.bountyseekers.commons.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * This class implements utility methods for GUI creation.
 */
public class GUIUtils {
    
    /**
     * <font color="aqua">Since this method doesn't use extra data to go into the ItemStack, it is more suited for
     * 1.13+</font><br>
     * Creates an item meant to be used as a placeholder for a button inside a GUI.<br>
     *
     * @param itemType The item type of the item to be created.
     * @param itemName The item's name
     * @param itemLore (Optional) The item's lore.
     * @return The ItemStack with the custom item.
     */
    public static ItemStack createItemPlaceholder(Material itemType, String itemName, List<String> itemLore) {

        // Creates the item and obtains its metadata
        ItemStack placeholder = new ItemStack(itemType, 1);
        ItemMeta itemMeta = placeholder.getItemMeta();

        // Sets the custom data for the item
        itemMeta.setDisplayName("§f" + itemName);
        if (itemLore != null) itemMeta.setLore(itemLore);

        // Saves the data and returns the item
        placeholder.setItemMeta(itemMeta);
        return placeholder;
    }

    /**
     * Alternative for {@link GUIUtils#createItemPlaceholder(Material, String, List, short)}, suited for legacy
     * versions, where the extra data is used. <br>
     *
     * @param itemType The item type of the item to be created.
     * @param itemName The item's name
     * @param itemLore (Optional) The item's lore.
     * @param data The item's data modifier
     * @return The ItemStack with the custom item.
     */
    @SuppressWarnings("deprecation")
    public static ItemStack createItemPlaceholder(Material itemType, String itemName, List<String> itemLore, short data) {

        // Creates the item and obtains its metadata
        ItemStack placeholder = new ItemStack(itemType, 1, data);
        ItemMeta itemMeta = placeholder.getItemMeta();

        // Sets the custom data for the item
        itemMeta.setDisplayName("§f" + itemName);
        if (itemLore != null) itemMeta.setLore(itemLore);

        // Saves the data and returns the item
        placeholder.setItemMeta(itemMeta);
        return placeholder;
    }

    /**
     * Shortcut for {@link #createItemPlaceholder(Material, String, List)} with the lore set to null.
     * @param itemType The item type of the item to be created.
     * @param itemName The item's name
     * @return The ItemStack with the custom item.
     */
    public static ItemStack createItemPlaceholder(Material itemType, String itemName) {
        return createItemPlaceholder(itemType, itemName, null);
    }

}

