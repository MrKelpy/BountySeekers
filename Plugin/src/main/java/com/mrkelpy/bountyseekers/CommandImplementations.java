package com.mrkelpy.bountyseekers;

import com.mrkelpy.bountyseekers.commons.carriers.Benefactor;
import com.mrkelpy.bountyseekers.commons.carriers.Bounty;
import com.mrkelpy.bountyseekers.commons.carriers.SimplePlayer;
import com.mrkelpy.bountyseekers.commons.commands.ICommandImplementations;
import com.mrkelpy.bountyseekers.commons.commands.PluginCommandHandler;
import com.mrkelpy.bountyseekers.commons.configuration.ConfigurableTextHandler;
import com.mrkelpy.bountyseekers.commons.configuration.InternalConfigs;
import com.mrkelpy.bountyseekers.commons.configuration.UUIDCache;
import com.mrkelpy.bountyseekers.commons.enums.CommandRegistry;
import com.mrkelpy.bountyseekers.commons.utils.ChatUtils;
import com.mrkelpy.bountyseekers.gui.BountyDisplayGUI;
import com.mrkelpy.bountyseekers.gui.BountyListDisplayGUI;
import com.mrkelpy.bountyseekers.gui.BountyRaiseGUI;
import com.mrkelpy.bountyseekers.gui.RewardFilterGUI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CommandImplementations implements ICommandImplementations {

    /**
     * Changes the configured reward filters in the Internal Configurations.
     * @param commandSender The sender of the command
     * @param args          The arguments of the command
     * @return Boolean, feedback to the caller
     */
    @Override
    public boolean setRewardFiltersCommand(CommandSender commandSender, String[] args) {

        if (!PluginCommandHandler.checkPermission(CommandRegistry.SET_REWARD_FILTERS.getPermission(), commandSender))
            return true;

        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;

        new RewardFilterGUI(player).openInventory();
        return true;
    }

    /**
     * Changes the configured reward limit for bounties.
     *
     * @param commandSender The sender of the command
     * @param args          The arguments of the command
     * @return Boolean, feedback to the caller
     */
    @Override
    public boolean setRewardLimitCommand(CommandSender commandSender, String[] args) {

        if (!PluginCommandHandler.checkPermission(CommandRegistry.SET_REWARD_LIMIT.getPermission(), commandSender))
            return true;

        if (args.length != 1) {
            commandSender.sendMessage(ConfigurableTextHandler.INSTANCE.getValue("command.usage") + CommandRegistry.SET_REWARD_LIMIT.getUsage());
            return true;
        }

        try {
            // Changes the reward limit to the amount specified
            InternalConfigs.INSTANCE.getConfig().set("reward-limit", Integer.parseInt(args[0]));
            InternalConfigs.INSTANCE.save();
            commandSender.sendMessage(ChatUtils.sendMessage(null, "Reward limit set to " + Integer.parseInt(args[0])));
            return true;

        } catch (NumberFormatException e) {
            // If the argument is not a number, send an error message
            commandSender.sendMessage("Limit must be numeric.");
            return true;
        }
    }

    /**
     * Opens up a GUI to allow a player to raise someone's bounty.
     *
     * @param commandSender The sender of the command
     * @param args          The arguments of the command
     * @return Boolean, feedback to the caller
     */
    @Override
    public boolean listCommand(CommandSender commandSender, String[] args) {

        if (!PluginCommandHandler.checkPermission(CommandRegistry.BOUNTY_LIST.getPermission(), commandSender))
            return true;

        // Only players can use this command
        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;

        new BountyListDisplayGUI(player).openInventory();
        return true;
    }

    /**
     * Directly opens the GUI displaying the bounty of a player.
     * @param commandSender The sender of the command
     * @param args          The arguments of the command
     * @return Boolean, feedback to the caller
     */
    @Override
    public boolean checkCommand(CommandSender commandSender, String[] args) {

        if (!PluginCommandHandler.checkPermission(CommandRegistry.CHECK.getPermission(), commandSender))
            return true;

        // Only players can use this command
        if (!(commandSender instanceof Player)) return false;

        if (args.length != 1) {
            commandSender.sendMessage(ConfigurableTextHandler.INSTANCE.getValue("command.usage") + CommandRegistry.CHECK.getUsage());
            return true;
        }

        // Check if the player can be found
        Player player = (Player) commandSender;
        UUID targetUUID = UUIDCache.INSTANCE.getUUID(args[0]);
        if (targetUUID == null) {
            commandSender.sendMessage(ConfigurableTextHandler.INSTANCE.getValue("command.playernotfound"));
            return true;
        }

        // Check if the player has a bounty
        Bounty bounty = new Bounty(targetUUID);
        if (bounty.getRewards().isEmpty()) {
            ChatUtils.sendMessage(player, ConfigurableTextHandler.INSTANCE.getValue("command.nobounty"));
            return true;
        }

        // Open the GUI
        new BountyDisplayGUI(player, bounty.getRewards().toArray(new ItemStack[0])).openInventory();
        return true;
    }

    /**
     * Opens up a GUI to allow a player to raise someone's bounty.
     *
     * @param commandSender The sender of the command
     * @param args          The arguments of the command
     * @return Boolean, feedback to the caller
     */
    @Override
    public boolean raiseCommand(CommandSender commandSender, String[] args) {

        if (!PluginCommandHandler.checkPermission(CommandRegistry.BOUNTY_RAISE.getPermission(), commandSender))
            return true;

        // Only players can use this command
        if (!(commandSender instanceof Player)) return false;

        if (args.length == 0) {
            commandSender.sendMessage(ConfigurableTextHandler.INSTANCE.getValue("command.usage") + CommandRegistry.BOUNTY_RAISE.getUsage());
            return true;
        }

        // Prevents the target from raising their own bounty
        if (commandSender.getName().equalsIgnoreCase(args[0])) {
            ChatUtils.sendMessage((Player) commandSender, ConfigurableTextHandler.INSTANCE.getValue("command.ownbounty"));
            return true;
        }

        SimplePlayer target = new SimplePlayer(args[0]);
        Player player = (Player) commandSender;

        if (target.getUniqueId() == null) {
            commandSender.sendMessage(ConfigurableTextHandler.INSTANCE.getValue("command.playernotfound"));
            return true;
        }

        new BountyRaiseGUI(target, new Benefactor(player, false)).openInventory();
        return true;
    }

    /**
     * Opens up a GUI to allow a player to raise someone's bounty, hiding the player's indentity in the process.
     *
     * @param commandSender The sender of the command
     * @param args          The arguments of the command
     * @return Boolean, feedback to the caller
     */
    @Override
    public boolean silentRaiseCommand(CommandSender commandSender, String[] args) {

        if (!PluginCommandHandler.checkPermission(CommandRegistry.BOUNTY_SILENT_RAISE.getPermission(), commandSender))
            return true;

        // Only players can use this command
        if (!(commandSender instanceof Player)) return false;

        if (args.length == 0) {
            commandSender.sendMessage(ConfigurableTextHandler.INSTANCE.getValue("command.usage") + CommandRegistry.BOUNTY_SILENT_RAISE);
            return true;
        }

        // Prevents the target from raising their own bounty
        if (commandSender.getName().equalsIgnoreCase(args[0])) {
            ChatUtils.sendMessage((Player) commandSender, ConfigurableTextHandler.INSTANCE.getValue("command.ownbounty"));
            return true;
        }

        SimplePlayer target = new SimplePlayer(args[0]);
        Player player = (Player) commandSender;

        if (target.getUniqueId() == null) {
            commandSender.sendMessage(ConfigurableTextHandler.INSTANCE.getValue("command.playernotfound"));
            return true;
        }

        new BountyRaiseGUI(target, new Benefactor(player, true)).openInventory();
        return true;
    }

    /**
     * Resets a player's bounty.
     *
     * @param commandSender The sender of the command
     * @param args          The arguments of the command
     * @return Boolean, feedback to the caller
     */
    @Override
    public boolean resetCommand(CommandSender commandSender, String[] args) {

        if (!PluginCommandHandler.checkPermission(CommandRegistry.BOUNTY_RESET.getPermission(), commandSender))
            return true;

        if (args.length == 0) {
            commandSender.sendMessage(ConfigurableTextHandler.INSTANCE.getValue("command.usage") + CommandRegistry.BOUNTY_RESET.getUsage());
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (UUIDCache.INSTANCE.getName(target.getUniqueId()) == null) {
            commandSender.sendMessage(ConfigurableTextHandler.INSTANCE.getValue("command.playernotfound"));
            return true;
        }

        new Bounty(target.getUniqueId()).reset();
        ChatUtils.sendMessage(((Player) commandSender).getPlayer(), ConfigurableTextHandler.INSTANCE.getValueFormatted("bounty.reset.broadcast", null, target.getName()));
        if (target.isOnline()) ChatUtils.sendMessage(target.getPlayer(), ConfigurableTextHandler.INSTANCE.getValueFormatted("bounty.reset.user", null, target.getPlayer().getName()));

        return true;
    }
}

