package com.mrkelpy.bountyseekers.v1_10;

import com.mrkelpy.bountyseekers.v1_8.configuration.InternalConfigs;
import com.mrkelpy.bountyseekers.v1_8.configuration.UUIDCache;
import com.mrkelpy.bountyseekers.v1_8.events.PlayerJoinListener;
import com.mrkelpy.bountyseekers.v1_8.events.PlayerKillListener;
import com.mrkelpy.bountyseekers.v1_8.events.PluginCommands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

/**
 * Main class for BountySeekers plugin, the entrypoint to registration and kickstarting of any needed
 * tasks.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class BountySeekers extends JavaPlugin {

    public static final String PLUGIN_NAME = "BountySeekers";
    public static final Logger LOGGER = Bukkit.getLogger();
    public static UUIDCache UUID_CACHE;
    public static InternalConfigs INTERNAL_CONFIGS;
    public static File DATA_FOLDER;

    @Override
    public void onEnable() {
        DATA_FOLDER = this.getDataFolder();
        if (!DATA_FOLDER.exists()) DATA_FOLDER.mkdirs();

        getCommand("bounty").setExecutor(PluginCommands.INSTANCE);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerKillListener(), this);

        INTERNAL_CONFIGS = InternalConfigs.INSTANCE;
        UUID_CACHE = UUIDCache.INSTANCE;
        LOGGER.info(String.format("Enabled %s v%s", PLUGIN_NAME, this.getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        LOGGER.info(String.format("Disabled %s v%s", PLUGIN_NAME, this.getDescription().getVersion()));
    }

    /**
     * Sends an ingame chat message to a player with the custom prefix and formatting
     * of the plugin.
     * @param player The player to send the message to.
     * @param message The message to send.
     * @return The message that was sent.
     */
    public static String sendMessage(Player player, String message) {

        String formattedMessage = String.format("§7[§c%s§7] §e" + message, PLUGIN_NAME);

        if (player != null) player.sendMessage(formattedMessage);
        return formattedMessage;
    }

}

