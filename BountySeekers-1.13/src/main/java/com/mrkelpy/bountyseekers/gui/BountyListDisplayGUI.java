package com.mrkelpy.bountyseekers.gui;

import com.mrkelpy.bountyseekers.BountySeekers;
import com.mrkelpy.bountyseekers.utils.FileUtils;
import com.mrkelpy.bountyseekers.utils.GUIUtils;
import com.mrkelpy.bountyseekers.utils.SerializationUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * This class implements a GUI capable of displaying all the currently available bounties.
 */
public class BountyListDisplayGUI extends PagedGUI {

    /**
     * The directory containing all the bounty files.
     */
    private final File bountiesDirectory = FileUtils.makeDirectory("bounties");

    /**
     * The player who is viewing the GUI.
     */
    private final Player player;

    /**
     * Main constructor for the BountyListDisplayGUI.
     */
    public BountyListDisplayGUI(Player player) {
        super("Active Bounties", 27);
        this.player = player;
        this.setItems(this.makeBountyItemList());
        this.reload();
    }

    /**
     * Open the GUI for the player.
     */
    public void openInventory() {
        this.player.openInventory(this.inventory);
    }


    /**
     * Displays the selected bounty.
     * @param event InventoryClickEvent
     */
    @Override
    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        super.onItemClick(event);
        if (event.getSlot() > this.storageSlots || event.getCurrentItem().getType() == Material.AIR) return;

        UUID playerUUID = BountySeekers.UUID_CACHE.getUUID(event.getCurrentItem().getItemMeta().getDisplayName().substring(4));
        String data = FileUtils.readFile(new File(this.bountiesDirectory, playerUUID + ".bounty"));
        new BountyDisplayGUI(this.player, SerializationUtils.itemStackArrayFromBase64(data)).openInventory();
    }

    /**
     * There's nothing to go back to, so leave the body empty.
     */
    @Override
    protected void goBack() {}

    /**
     * Creates an ItemStack list representing all the currently available bounties.
     * @return List<ItemStack> The list of ItemStacks representing the currently available bounties.
     */
    private List<ItemStack> makeBountyItemList() {

        File[] bountyFiles = bountiesDirectory.listFiles();
        List<ItemStack> bountyItemList = new ArrayList<>();
        if (bountyFiles == null) return bountyItemList;

        for (File bountyFile : bountyFiles) {

            // Creates the base item representing the bounty
            UUID playerUUID = UUID.fromString(bountyFile.getName().replace(".bounty", ""));

            String playername = BountySeekers.UUID_CACHE.getName(playerUUID);
            ItemStack item = GUIUtils.getPlayerHeadPlaceholder(
                    playerUUID, "§e" + playername, Collections.singletonList("§7Click here to view this bounty"));

            // Adds the item with the custom tag to the list, as a bukkit copy.
            bountyItemList.add(item);
        }

        return bountyItemList;
    }
}

