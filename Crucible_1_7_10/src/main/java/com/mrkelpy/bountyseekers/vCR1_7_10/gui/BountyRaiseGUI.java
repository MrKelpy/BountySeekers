package com.mrkelpy.bountyseekers.vCR1_7_10.gui;

import com.mrkelpy.bountyseekers.commons.carriers.Benefactor;
import com.mrkelpy.bountyseekers.commons.carriers.Bounty;
import com.mrkelpy.bountyseekers.commons.carriers.SimplePlayer;
import com.mrkelpy.bountyseekers.commons.gui.ConfirmationGUI;
import com.mrkelpy.bountyseekers.commons.utils.ItemStackUtils;
import com.mrkelpy.bountyseekers.vCR1_7_10.BountySeekers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * This class creates a GUI capable of raising a player's bounty intuitively.
 */
public class BountyRaiseGUI extends ConfirmationGUI {

    private final Bounty bounty;
    private final Benefactor benefactor;

    /**
     * Main constructor for the ConfirmationGUI class.
     */
    public BountyRaiseGUI(SimplePlayer target, Benefactor benefactor) {
        super("Raise " + target.getName() + "'s Bounty", 27);
        this.bounty = new Bounty(target.getUniqueId(), BountySeekers.compatibility);
        this.benefactor = benefactor;
    }

    /**
     * Open the GUI for the player.
     */
    public void openInventory() {
        this.benefactor.getPlayer().openInventory(this.inventory);
    }

    /**
     * Adds all the rewards currently inside the GUI as rewards inside the bounty.
     *
     * @param player The player that pressed the confirm button.
     */
    @Override
    public void onConfirm(Player player) {

        // Cancels the raising if the player didn't contribute to the bounty
        if (Arrays.stream(this.inventory.getContents()).filter(Objects::nonNull).count() == 2) {
            this.onCancel(player);
            return;
        }

        int rewardLimit = BountySeekers.INTERNAL_CONFIGS.getConfig().getInt("reward-limit");

        // Adds all the rewards inside the GUI to the bounty, and adds the benefactor.
        for (int i = 0; this.storageSlots > i; i++) {

            // Prevents the player from raising the target's bounty over the maximum amount.
            if (this.inventory.getItem(i) != null && this.bounty.getRewards().size() >= rewardLimit && rewardLimit != -1) {

                // Check if the item will be compressed, and if so, if the bounty rewards post-compression will overflow the limit.
                if (!ItemStackUtils.willCompress(this.inventory.getItem(i), this.bounty.getRewards())) continue;

                ArrayList<ItemStack> compressedOverflow = new ArrayList<>(this.bounty.getRewards());
                compressedOverflow.add(this.inventory.getItem(i));

                // If the bounty size doesn't overflow the reward limit, add it.
                if (ItemStackUtils.compress(compressedOverflow).size() <= rewardLimit) {
                    this.bounty.addReward(this.inventory.getItem(i));
                    this.inventory.setItem(i, null);
                }

                continue;
            }

            this.bounty.addReward(this.inventory.getItem(i));
            this.inventory.setItem(i, null);
        }

        // Sends the "items returned" warning message in case there are still items left inside the GUI to be returned to the player.
        if (Arrays.stream(this.inventory.getContents()).filter(Objects::nonNull).count() > 2)
            player.sendMessage(BountySeekers.sendMessage(null, "Some items were returned to you because they would overflow the maximum reward limit for that target."));

        // Returns any leftover items to the player.
        for (int i = 0; this.storageSlots > i; i++) {

            if (this.inventory.getItem(i) == null) continue;
            this.benefactor.getPlayer().getInventory().addItem(this.inventory.getItem(i));
            this.inventory.setItem(i, null);
        }
        this.bounty.save();

        // Announces the bounty raise, incase it was raised, hiding the benefactor if they're anonymous.
        if (this.benefactor.toString() != null && this.bounty.getAdditionCount() > 0)
            Bukkit.broadcastMessage(BountySeekers.sendMessage(null, this.benefactor.getPlayer().getName() + " has raised " + this.bounty.getTarget() + "'s bounty!"));

        else if (this.benefactor.toString() == null && this.bounty.getAdditionCount() > 0)
            Bukkit.broadcastMessage(BountySeekers.sendMessage(null, "A player has raised " + this.bounty.getTarget() + "'s bounty!"));

        // Unregisters the event handlers and closes the inventory so no items are returned.
        HandlerList.unregisterAll(this);
        this.benefactor.getPlayer().closeInventory();
    }

    /**
     * Returns the benefactor's inventory as of before the GUI was opened.
     *
     * @param player The player that pressed the cancel button.
     */
    @Override
    public void onCancel(Player player) {

        // Unregisters the event handlers and closes the inventory so there's no recursion
        HandlerList.unregisterAll(this);
        this.benefactor.getPlayer().closeInventory();

        Bukkit.getScheduler().runTaskLater(
                Bukkit.getPluginManager().getPlugin(BountySeekers.PLUGIN_NAME),
                () -> this.benefactor.getPlayer().getInventory().setContents(this.benefactor.getInventory().getContents()), 2L);
    }

    /**
     * Prevents the player from picking up the confirmation button items.
     * @param event InventoryClickEvent
     */
    @Override
    @EventHandler
    public void onItemClick(InventoryClickEvent event) {

        if (event.getRawSlot() > this.storageSlots && event.getRawSlot() < this.inventory.getSize())
            super.onItemClick(event);
    }

    /**
     * Allows the player to drag stuff around by limiting the superclass code
     * to the 9 bottom slots.
     *
     * @param event InventoryDragEvent
     */
    @Override
    @EventHandler
    public void onItemDrag(InventoryDragEvent event) {

        if (event.getNewItems().keySet().stream().anyMatch(slot -> slot > this.storageSlots))
            super.onItemDrag(event);

    }

    /**
     * Count closing the inventory as a cancel.
     *
     * @param event InventoryCloseEvent
     */
    @Override
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        super.onInventoryClose(event);
        this.onCancel((Player) event.getPlayer());
    }

    /**
     * While this GUI is opened, the benefactor can't pick up any items, to prevent losses.
     *
     * @param event The event that is being handled.
     */
    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {

        if (event.getPlayer() == this.benefactor.getPlayer())
            event.setCancelled(true);
    }

}

