package com.mrkelpy.bountyseekers.v1_16.utils;

import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This class handles the needed classes related to NMS using reflection.
 */
public class ReNMS {

    /**
     * Uses reflection to get the current NMS class for the given server version. This is possible since all that changes
     * between NMS versions is the package version name, and the class names stay mostly the same.
     *
     * @param name The name of the class to get.
     * @return The reflected class.
     */
    public static Class<?> getNMSClass(String name) {

        try {
            return Class.forName("net.minecraft.server." +
                    Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
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

        public static final Class<?> CLASS = getNMSClass("NBTTagCompound");

        /**
         * Returns an NBTTagCompound instance from the given NMS ItemStack.
         *
         * @param nmsItemStack The NMS ItemStack to get the NBTTagCompound from.
         * @return The NBTTagCompound.
         */
        public static Object create(Object nmsItemStack) {
            try {
                return nmsItemStack.getClass().getMethod("getTag").invoke(nmsItemStack);

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

        public static final Class<?> CLASS = getNMSClass("NBTCompressedStreamTools");

        /**
         * Reflection of the NBTCompressedStreamTools.a(NBTTagCompound, DataOutput) method.
         * <br>
         * This method writes the given NBTTagCompound into the DataOutputStream.
         */
        public static void a(Object nbtTagCompound, DataOutput dataOutput) {
            try {
                CLASS.getMethod("a", new Class[]{NBTTagCompound.CLASS, DataOutput.class}).invoke(CLASS, nbtTagCompound, dataOutput);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        /**
         * Reflection of the NBTCompressedStreamTools.a(DataInputStream) method.
         * <br>
         * This method reads the NBTTagCompound from the DataInputStream.
         *
         * @param dataInputStream The DataInputStream to read from.
         * @return The NBTTagCompound.
         */
        public static Object a(DataInputStream dataInputStream) {
            try {
                return CLASS.getMethod("a", new Class[]{DataInputStream.class}).invoke(CLASS, dataInputStream);

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

        public static final Class<?> CLASS = getNMSClass("ItemStack");

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
                Constructor<?> ctor = CLASS.getDeclaredConstructor(NBTTagCompound.CLASS);
                ctor.setAccessible(true);
                return ctor.newInstance(nbtTagCompound);

            } catch (Exception e) {
                e.printStackTrace();
                return ObjectUtils.Null.class;
            }
        }

        /**
         * <font color="#3498DB"> Compatible with Minecraft Version 1.7.x-1.10.x </font><br>
         * Reflection of the ItemStack.createStack(NBTTagCompound) method.
         * <br>
         * This method creates a new ItemStack from the given NBTTagCompound.
         *
         * @param nbtTagCompound The NBTTagCompound to create the ItemStack from.
         * @return The ItemStack with the given NBTTagCompound.
         */
        public static Object createStack(Object nbtTagCompound) {
            try {
                return CLASS.getMethod("createStack", new Class[]{NBTTagCompound.CLASS}).invoke(CLASS, nbtTagCompound);

            } catch (Exception e) {
                e.printStackTrace();
                return ObjectUtils.Null.class;
            }
        }

        /**
         * Reflection of the ItemStack.save(NBTTagCompound) method.
         * <br>
         * This method deposits the given NBTTagCompound data into the ItemStack.
         *
         * @param itemStack      The ItemStack to save the NBTTagCompound into.
         * @param nbtTagCompound The NBTTagCompound to deposit.
         * @return The NBT-Filled ItemStack.
         */
        public static Object save(Object itemStack, Object nbtTagCompound) {
            try {
                return itemStack.getClass().getMethod("save", new Class[]{NBTTagCompound.CLASS}).invoke(itemStack, nbtTagCompound);

            } catch (Exception e) {
                e.printStackTrace();
                return ObjectUtils.Null.class;
            }
        }

    }

}

