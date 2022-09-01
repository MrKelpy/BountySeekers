package com.mrkelpy.bountyseekers.utils;

import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.lang.reflect.InvocationTargetException;

/**
 * <font color="yellow"> This class uses the Crucible:Forge mappings instead of the spigot ones. </font><br>
 * This class handles the needed classes related to NMS using reflection.
 */
public class ReCrucibleNMS {

    /**
     * Uses reflection to get the current NMS class for the given server version. This is possible since all that changes
     * between NMS versions is the package version name, and the class names stay mostly the same.
     *
     * @param name The name of the class to get.
     * @return The reflected class.
     */
    public static Class<?> getNMSClass(String name) {

        try {
            return Class.forName("net.minecraft." + name);
        } catch (ClassNotFoundException e) {
            // If an error happens, print it into console and return null.
            e.printStackTrace();
            return ObjectUtils.Null.class;
        }
    }

    /**
     * This class holds every supported method for the reflected NBTTagCompound class.
     */
    public static class NBTTagCompound {

        public static final Class<?> CLASS = getNMSClass("nbt.NBTTagCompound");

        /**
         * Returns an NBTTagCompound instance from the given NMS ItemStack.
         *
         * @param nmsItemStack The NMS ItemStack to get the NBTTagCompound from.
         * @return The NBTTagCompound.
         */
        public static Object create(Object nmsItemStack) {
            try {
                return nmsItemStack.getClass().getMethod("getTagCompound").invoke(nmsItemStack);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return ObjectUtils.Null.class;
            }
        }

        /**
         * @return A Reflected  empty NBTTagCompound instance.
         */
        public static Object create() {
            try {
                return CLASS.getConstructor().newInstance();

            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                e.printStackTrace();
                return ObjectUtils.Null.class;
            }
        }

    }

    /**
     * This class holds every supported method for the reflected NBTCompressionStreamTools class.
     */
    public static class NBTCompressedStreamTools {

        public static final Class<?> CLASS = getNMSClass("nbt.CompressedStreamTools");

        /**
         * Reflection of the NBTCompressedStreamTools.func_74800_a(NBTTagCompound, DataOutput) method.
         * <br>
         * This method writes the given NBTTagCompound into the DataOutputStream.
         */
        public static void a(Object nbtTagCompound, DataOutput dataOutput) {
            try {
                CLASS.getMethod("func_74800_a", new Class[]{NBTTagCompound.CLASS, DataOutput.class}).invoke(CLASS, nbtTagCompound, dataOutput);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        /**
         * Reflection of the NBTCompressedStreamTools.func_74794_a(DataInputStream) method.
         * <br>
         * This method reads the NBTTagCompound from the DataInputStream.
         *
         * @param dataInputStream The DataInputStream to read from.
         * @return The NBTTagCompound.
         */
        public static Object a(DataInputStream dataInputStream) {
            try {
                return CLASS.getMethod("func_74794_a", new Class[]{DataInputStream.class}).invoke(CLASS, dataInputStream);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return ObjectUtils.Null.class;
            }
        }
    }

    /**
     * This class holds every supported method for the reflected ItemStack class.
     */
    public static class ItemStack {

        public static final Class<?> CLASS = getNMSClass("item.ItemStack");

        /**
         * <font color="#3498DB"> Compatible with Minecraft Version 1.11.x+ </font><br>
         * Reflection of the ItemStack(NBTTagCompound) constructor.
         * <br>
         * This method creates a new ItemStack from the given NBTTagCompound.
         *
         *
         * @param nbtTagCompound The NBTTagCompound to create the ItemStack from.
         * @return The ItemStack with the given NBTTagCompound.
         */
        public static Object create(Object nbtTagCompound) {
            try {
                return CLASS.getConstructor(NBTTagCompound.CLASS).newInstance(nbtTagCompound);

            } catch (Exception e) {
                e.printStackTrace();
                return ObjectUtils.Null.class;
            }
        }

        /**
         * <font color="#3498DB"> Compatible with Minecraft Version 1.7.x-1.10.x </font><br>
         * Reflection of the ItemStack.func_77949_a(NBTTagCompound) method.
         * <br>
         * This method creates a new ItemStack from the given NBTTagCompound.
         *
         * @param nbtTagCompound The NBTTagCompound to create the ItemStack from.
         * @return The ItemStack with the given NBTTagCompound.
         */
        public static Object createStack(Object nbtTagCompound) {
            try {
                return CLASS.getMethod("func_77949_a", new Class[]{NBTTagCompound.CLASS}).invoke(CLASS, nbtTagCompound);

            } catch (Exception e) {
                e.printStackTrace();
                return ObjectUtils.Null.class;
            }
        }

        /**
         * Reflection of the ItemStack.func_77955_b(NBTTagCompound) method.
         * <br>
         * This method deposits the given NBTTagCompound data into the ItemStack.
         *
         * @param itemStack      The ItemStack to save the NBTTagCompound into.
         * @param nbtTagCompound The NBTTagCompound to deposit.
         * @return The NBT-Filled ItemStack.
         */
        public static Object save(Object itemStack, Object nbtTagCompound) {
            try {
                return itemStack.getClass().getMethod("func_77955_b", new Class[]{NBTTagCompound.CLASS}).invoke(itemStack, nbtTagCompound);

            } catch (Exception e) {
                e.printStackTrace();
                return ObjectUtils.Null.class;
            }
        }

    }

}

