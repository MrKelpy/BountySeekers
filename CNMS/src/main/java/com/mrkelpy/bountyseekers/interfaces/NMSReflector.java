package com.mrkelpy.bountyseekers.interfaces;

import java.io.DataInputStream;
import java.io.DataOutput;

public interface NMSReflector {

    Class<?> getNMSClass(String name);
    NMSReflector.NBTTagCompound getNBTTagCompound();
    NMSReflector.NBTCompressedStreamTools getNBTCompressedStreamTools();
    NMSReflector.ItemStack getItemStack();


    abstract class NBTTagCompound {

        public abstract Object create(Object nmsItemStack);

        public abstract Object create();
    }

    abstract class NBTCompressedStreamTools {
        
        public abstract void write(Object nbtTagCompound, DataOutput dataOutput);

        public abstract Object read(DataInputStream dataInputStream);
    }

    abstract class ItemStack {
        
        public abstract Object create(Object nbtTagCompound);

        public abstract Object save(Object itemStack, Object nbtTagCompound);
    }


}

