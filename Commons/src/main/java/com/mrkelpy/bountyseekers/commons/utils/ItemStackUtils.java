package com.mrkelpy.bountyseekers.commons.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStackUtils {

    /**
     * Iterates over every item in the given List, tracks the amount of items in each,
     * and generates a compressed list of ItemStacks.
     *
     * @param list The list to compress.
     * @return The compressed list.
     */
    public static List<ItemStack> compress(List<ItemStack> list) {
        Map<ItemStack, Integer> generationMap = new HashMap<>();  // The map holding the stacks and their amount.

        // Iterate over every item in the list and count the amount of each.
        for (ItemStack item : list) {

            if (item == null || item.getType() == Material.AIR) continue;

            // Creates a pivot ItemStack to use for comparison.
            ItemStack pivot = item.clone();
            pivot.setAmount(1);

            if (generationMap.containsKey(pivot))
                generationMap.put(pivot, generationMap.get(pivot) + item.getAmount());

            else {
                generationMap.put(pivot, item.getAmount());
            }
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

    /**
     * Iterates over the given list and checks if there's any itemStack inside with less
     * than its max stack size that the given itemStack will be compressed into.
     * @param stack The itemStack to check.
     * @param list The list to check in.
     * @return True if the itemStack can be compressed, false otherwise.
     */
    public static boolean willCompress(ItemStack stack, List<ItemStack> list) {

        for (ItemStack item : list) {
            if (item.isSimilar(stack) && item.getAmount() < item.getMaxStackSize())
                return true;
        }

        return false;
    }

}

