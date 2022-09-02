package com.mrkelpy.bountyseekers.v1_15.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

/**
 * This class implements utility methods for GUI creation.
 */
public class GUIUtils {
    
    /**
     * Creates an item meant to be used as a placeholder for a button inside a GUI.
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
     * Creates a playerHead itemStack belonging to a player, and edits the item's name, and lore.
     * @param playerUUID The player's UUID.
     * @param customName The custom name of the item.
     * @param itemLore The item's lore.
     * @return The itemStack with the player's head.
     */
    public static ItemStack getPlayerHeadPlaceholder(UUID playerUUID, String customName, List<String> itemLore) {

        ItemStack placeholder = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta itemMeta = (SkullMeta) placeholder.getItemMeta();

        itemMeta.setOwningPlayer(Bukkit.getPlayer(playerUUID));
        if (customName != null) itemMeta.setDisplayName("§f" + customName);
        if (itemLore != null) itemMeta.setLore(itemLore);

        placeholder.setItemMeta(itemMeta);
        return placeholder;
    }

    /**
     * Shortcut for {@link #getPlayerHeadPlaceholder(UUID, String, List)} with no custom name or lore.
     */
    public static ItemStack getPlayerHeadPlaceholder(UUID playerUUID) {
        return getPlayerHeadPlaceholder(playerUUID, null, null);
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

