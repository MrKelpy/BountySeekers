package com.mrkelpy.bountyseekers.v1_16.utils;

import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;

public class ReBukkit {

    /**
     * Uses reflection to get the current Bukkit class for the given server version.
     * @param name The name of the class to get.
     * @return The reflected class.
     */
    public static Class<?> getBukkitClass(String name)  {

        try {
            return Class.forName("org.bukkit.craftbukkit." +
                    Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        }
        catch (ClassNotFoundException e) {
            // If an error happens, print it into console and return null.
            e.printStackTrace();
            return ObjectUtils.Null.class;
        }
    }

    /**
     * This class holds every supported method for the reflected CraftItemStack class.
     */
    public static class CraftItemStack {

        private static final Class<?> CLASS = ReBukkit.getBukkitClass("inventory.CraftItemStack");

        /**
         * Reflection of the CraftItemStack.asNMSCopy(ItemStack) method.
         * <br>
         * This method creates a new NMS ItemStack from the given ItemStack.
         *
         * @param itemStack The Bukkit ItemStack to create the NMS ItemStack from.
         * @return The NMS ItemStack.
         */
        public static Object asNMSCopy(org.bukkit.inventory.ItemStack itemStack) {
            try {
                return CLASS.getMethod("asNMSCopy", new Class[] {org.bukkit.inventory.ItemStack.class}).invoke(CLASS, itemStack);

            } catch (Exception e) {
                e.printStackTrace();
                return ObjectUtils.Null.class;
            }
        }

        /**
         * Reflection of the CraftItemStack.asBukkitCopy(ItemStack) method.
         * <br>
         * This method creates a new Bukkit ItemStack from the given NMS ItemStack.
         *
         * @param nmsItemStack The NMS ItemStack to create the Bukkit ItemStack from.
         * @return The Bukkit ItemStack.
         */
        public static org.bukkit.inventory.ItemStack asBukkitCopy(Object nmsItemStack) {
            try {
                return (org.bukkit.inventory.ItemStack) CLASS.getMethod("asBukkitCopy", new Class[] {ReNMS.ItemStack.CLASS}).invoke(CLASS, nmsItemStack);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }
}

