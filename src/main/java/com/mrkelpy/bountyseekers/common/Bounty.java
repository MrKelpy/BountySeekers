package com.mrkelpy.bountyseekers.common;

import com.mrkelpy.bountyseekers.BountySeekers;
import com.mrkelpy.bountyseekers.utils.FileUtils;
import com.mrkelpy.bountyseekers.utils.SerializationUtils;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

/**
 * This class creates a convenient method to hold and perform operations
 * centered around a player's bounty.
 */
public class Bounty {

    /**
     * The player who has the bounty.
     */
    private final String target;

    /**
     * The target's UUID
     */
    private final UUID targetUUID;

    /**
     * The items offered to the one who claims the bounty.
     */
    private final List<ItemStack> rewards;

    /**
     * The .bounty file that holds the bounty information
     */
    private final File bountyFile;

    /**
     * Main constructor for the Bounty class. From a player's name, go to their bounty file and extract the needed
     * information for the Bounty class. This automatically handles the base64 to List conversion for the rewards.
     *
     * @param playerUUID The target player's UUID.
     */
    public Bounty(UUID playerUUID) {
        this.target = Bukkit.getOfflinePlayer(playerUUID).getName();
        this.targetUUID = playerUUID;

        this.bountyFile = new File(FileUtils.makeDirectory("bounties"), playerUUID + ".bounty");
        String bountyInformation = this.bountyFile.exists() ? FileUtils.readFile(this.bountyFile) : null;

        ItemStack[] rewardArray = bountyInformation != null ? SerializationUtils.itemStackArrayFromBase64(bountyInformation) : null;
        this.rewards = bountyInformation != null && rewardArray != null ? new ArrayList<>(Arrays.asList(rewardArray)): new ArrayList<>();
    }

    @Getter
    public String getTarget() {
        return target;
    }

    @Getter
    public List<ItemStack> getRewards() {
        return rewards;
    }

    /**
     * Raises the bounty for the target by adding the reward to the bounty.
     * @param reward The reward to add to the bounty.
     */
    public void addReward(ItemStack reward) {
        if (reward != null && reward.getType() != Material.AIR)
            this.rewards.add(reward);
    }

    /**
     * Adds all the items present in the rewards List to the given hunter's inventory.
     * This will also reset the bounty for the hunter's inventory
     * @param hunter The bounty hunter that claimed the bounty.
     */
    public void claimBounty(Player hunter) {

        for (ItemStack reward : this.rewards) {

            if (!Arrays.stream(hunter.getInventory().getContents()).allMatch(Objects::nonNull))
                hunter.getInventory().addItem(reward);

            // If the inventory is full, start dropping the rewards on the ground.
            else { hunter.getWorld().dropItem(hunter.getLocation(), reward); }
        }

        this.reset();
    }

    /**
     * Resets a player's bounty. This will delete the bounty file and null out the current instance's fields.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void reset() {

        this.rewards.clear();
        this.bountyFile.delete();
    }

    /**
     * Save the bounty to the .bounty file.
     */
    public void save() {
        List<ItemStack> compressedRewards = this.compress(this.rewards);
        FileUtils.writeFile(this.bountyFile, SerializationUtils.itemStackArrayToBase64(compressedRewards.toArray(new ItemStack[0])));
        BountySeekers.UUID_CACHE.set(this.targetUUID, this.target);
    }

    /**
     * Iterates over every item in the given List, tracks the amount of items in each,
     * and generates a compressed list of ItemStacks.
     * @param list The list to compress.
     * @return The compressed list.
     */
    public List<ItemStack> compress(List<ItemStack> list) {
        Map<ItemStack, Integer> generationMap = new HashMap<>();  // The map holding the stacks and their amount.

        // Iterate over every item in the list and count the amount of each.
        for (ItemStack item : list) {

            // Creates a pivot ItemStack to use for comparison.
            ItemStack pivot = item.clone();
            pivot.setAmount(1);

            if (generationMap.containsKey(pivot))
                generationMap.put(pivot, generationMap.get(pivot) + item.getAmount());

            else { generationMap.put(pivot, item.getAmount()); }
        }

        // Generates the compressed ItemStacks given the generationMap.
        List<ItemStack> compressedList = new ArrayList<>();
        for (ItemStack item : generationMap.keySet()) {

            do {
                int amount = item.getMaxStackSize();  // Defaults the itemstack amount to the max stack size.

                // If the amount left is greater than the stack size, diminish the amount left by the stack size.
                if (generationMap.get(item) > amount)
                    generationMap.put(item, generationMap.get(item) - amount);

                else {
                    // If not, change the amount to what's left, and do the same.
                    amount = generationMap.get(item);
                    generationMap.put(item, generationMap.get(item) - amount);
                }

                // Create a clone of the itemstack, change the item count, and add it to the compressed list.
                ItemStack itemClone = item.clone();
                itemClone.setAmount(amount);
                compressedList.add(itemClone);

            }
            // Keep repeating this until the amount of items left is 0.
            while (generationMap.get(item) != 0);
        }

        return compressedList;
    }

}

