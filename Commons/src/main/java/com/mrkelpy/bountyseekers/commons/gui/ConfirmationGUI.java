package com.mrkelpy.bountyseekers.commons.gui;

import com.mrkelpy.bountyseekers.commons.utils.GUIUtils;
import com.mrkelpy.bountyseekers.commons.utils.PluginConstants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * This abstract class adds confirm/cancel buttons to a GUI.
 */
public abstract class ConfirmationGUI implements Listener {

    // This inventory instance should be used by any classes inheriting from the class.
    protected final Inventory inventory;
    protected final int storageSlots;

    /**
     * Main constructor for the ConfirmationGUI class. This constructor automatically
     * adds the extra space needed for the confirm/cancel buttons to the inventory.
     * @param title The title of the inventory.
     * @param inventorySize The size of the inventory.
     */
    public ConfirmationGUI(String title, int inventorySize) {
        this.storageSlots = inventorySize - 1;
        this.inventory = Bukkit.createInventory(null, inventorySize + 9, title);
        this.addConfirmationButtons();
        this.registerListeners();
    }

    /**
     * This method is called when the player clicks the confirm button.
     */
    public abstract void onConfirm(Player player);

    /**
     * This method is called when the player clicks the cancel button.
     */
    public abstract void onCancel(Player player);

    /**
     * Prevents an item from being clicked on, to prevent exploits, and checks if the slot clicked
     * was any of the paging button ones. If so, handle them accordingly.
     * <br>
     * This method can, and should be overriden to process clicks on GUIs that extend PagedGUI, but the super
     * should be called first.
     * @param event InventoryClickEvent
     */
    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        if (event.getInventory().equals(this.inventory)) event.setCancelled(true);
        else return;

        if (event.isShiftClick()) event.setCancelled(true);
        if (event.getSlot() == this.storageSlots + 1) this.onCancel((Player) event.getWhoClicked());
        if (event.getSlot() == this.storageSlots + 9) this.onConfirm((Player) event.getWhoClicked());
    }

    /**
     * Prevents an item from being dragged on, to prevent exploits.
     * @param event InventoryDragEvent
     */
    @EventHandler
    public void onItemDrag(InventoryDragEvent event) {
        if (event.getInventory().equals(this.inventory)) event.setCancelled(true);
    }

    /**
     * Unregisters all event listeners present in an instance of this GUI to save resources.
     * @param event InventoryCloseEvent
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().equals(this.inventory))
            HandlerList.unregisterAll(this);
    }

    /**
     * Adds the confirm and cancel buttons to the inventory.
     */
    private void addConfirmationButtons() {

        ItemStack redWool = Material.getMaterial("RED_WOOL") != null ?
                GUIUtils.createItemPlaceholder(Material.getMaterial("RED_WOOL"), "§cCancel") :
                GUIUtils.createItemPlaceholder(Material.getMaterial("WOOL"), "§cCancel", null, (short) 14);

        ItemStack limeWool = Material.getMaterial("LIME_WOOL") != null ?
                GUIUtils.createItemPlaceholder(Material.getMaterial("LIME_WOOL"), "§eConfirm") :
                GUIUtils.createItemPlaceholder(Material.getMaterial("WOOL"), "§eConfirm", null, (short) 5);

        this.inventory.setItem(this.storageSlots + 1, redWool);

        this.inventory.setItem(this.storageSlots + 9, limeWool);
    }

    /**
     * Registers all event listeners used by an instance of this GUI.
     */
    private void registerListeners() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(PluginConstants.PLUGIN_NAME);
        assert plugin != null;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

}

