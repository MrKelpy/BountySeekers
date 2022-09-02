package com.mrkelpy.bountyseekers.v1_19.utils;

import org.apache.commons.lang.ObjectUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * <font color="yellow">This class ONLY contains the reflections for the 1.17+ mappings</font><br>
 * This class handles the needed classes related to NMS using reflection.
 */
public class ReNMS {

    /**
     * Uses reflection to get the given NMS class. This is possible because the class names
     * are the same across all versions.
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

        public static final Class<?> CLASS = getNMSClass("nbt.NBTCompressedStreamTools");

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
         * Reflection of the NBTCompressedStreamTools.a(DataInput) method.
         * <br>
         * This method reads the NBTTagCompound from the DataInput.
         *
         * @param dataInput The DataInput to read from.
         * @return The NBTTagCompound.
         */
        public static Object a(DataInput dataInput) {
            try {
                return CLASS.getMethod("a", new Class[]{DataInput.class}).invoke(CLASS, dataInput);

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

        public static final Class<?> CLASS = getNMSClass("world.item.ItemStack");

        /**
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
         * Reflection of the ItemStack.save(NBTTagCompound) method.
         * <br>
         * This method deposits the given NBTTagCompound data into the ItemStack.
         *
         * @param itemStack      The ItemStack to save the NBTTagCompound into.
         * @param nbtTagCompound The NBTTagCompound to deposit.
         * @return The NBT-Filled ItemStack.
         */
        public static Object b(Object itemStack, Object nbtTagCompound) {
            try {
                return itemStack.getClass().getMethod("b", new Class[]{NBTTagCompound.CLASS}).invoke(itemStack, nbtTagCompound);

            } catch (Exception e) {
                e.printStackTrace();
                return ObjectUtils.Null.class;
            }
        }

    }

}

