package com.mrkelpy.bountyseekers.commons.enums;

import com.mrkelpy.bountyseekers.interfaces.IBukkitReflector;
import com.mrkelpy.bountyseekers.interfaces.INMSReflector;

/**
 * This enum allows for any version of the plugin to declare which NMS and Bukkit
 * version it is compatible with. Every different compatibility version will be represented
 * in this enum, mapped to the two Reflector objects that it should use.
 * <br>
 * This is mostly just an accessibility class, for handy implementation of version compartibility.
 */
public enum CompatibilityMode {

    /**
     * ENUM DECLARATION
     */
    v1_7(com.mrkelpy.bountyseekers.reflectors.v1_7.ReBukkit.INSTANCE,
            com.mrkelpy.bountyseekers.reflectors.v1_7.ReNMS.INSTANCE),  // v1_7 Bukkit and NMS reflectors

    vCR1_7_10(com.mrkelpy.bountyseekers.reflectors.vCR1_7_10.ReBukkit.INSTANCE,
            com.mrkelpy.bountyseekers.reflectors.vCR1_7_10.ReCrucibleNMS.INSTANCE);  // vCR1_7_10 Bukkit and NMS Reflectors

    private final IBukkitReflector bukkitReflector;
    private final INMSReflector nmsReflector;

    CompatibilityMode(IBukkitReflector bukkitReflector, INMSReflector nmsReflector) {
        this.bukkitReflector = bukkitReflector;
        this.nmsReflector = nmsReflector;
    }

    /**
     * Gets the IBukkitReflector object that should be used for the compatibility mode.
     */
    public IBukkitReflector getBukkitReflector() {
        return bukkitReflector;
    }

    /**
     * Gets the INMSReflector object that should be used for the compatibility mode.
     */
    public INMSReflector getNMSReflector() {
        return nmsReflector;
    }

}
