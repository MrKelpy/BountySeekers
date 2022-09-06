package com.mrkelpy.bountyseekers.v1_15;

import com.mrkelpy.bountyseekers.commons.carriers.Benefactor;
import com.mrkelpy.bountyseekers.commons.carriers.Bounty;
import com.mrkelpy.bountyseekers.commons.carriers.SimplePlayer;
import com.mrkelpy.bountyseekers.commons.commands.CommandRegistry;
import com.mrkelpy.bountyseekers.commons.commands.ICommandImplementations;
import com.mrkelpy.bountyseekers.commons.commands.PluginCommandHandler;
import com.mrkelpy.bountyseekers.commons.configuration.InternalConfigs;
import com.mrkelpy.bountyseekers.commons.configuration.UUIDCache;
import com.mrkelpy.bountyseekers.commons.gui.BountyRaiseGUI;
import com.mrkelpy.bountyseekers.commons.utils.ChatUtils;
import com.mrkelpy.bountyseekers.v1_15.gui.BountyListDisplayGUI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandImplementations implements ICommandImplementations {

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
            commandSender.sendMessage("§cUsage: /bounty setrewardlimit <amount>");
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
            commandSender.sendMessage("§cUsage: /bounty raise <target player>");
            return true;
        }

        // Prevents the target from raising their own bounty
        if (commandSender.getName().equalsIgnoreCase(args[0])) {
            ChatUtils.sendMessage((Player) commandSender, "You can't raise your own bounty!");
            return true;
        }

        SimplePlayer target = new SimplePlayer(args[0]);
        Player player = (Player) commandSender;

        if (target.getUniqueId() == null) {
            commandSender.sendMessage("§cThat player cannot found.");
            return true;
        }

        new BountyRaiseGUI(target, new Benefactor(player, false), BountySeekers.compatibility).openInventory();
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
            commandSender.sendMessage("§cUsage: /bounty silentraise <target player>");
            return true;
        }

        // Prevents the target from raising their own bounty
        if (commandSender.getName().equalsIgnoreCase(args[0])) {
            ChatUtils.sendMessage((Player) commandSender, "You can't raise your own bounty!");
            return true;
        }

        SimplePlayer target = new SimplePlayer(args[0]);
        Player player = (Player) commandSender;

        if (target.getUniqueId() == null) {
            commandSender.sendMessage("§cThat player cannot found.");
            return true;
        }

        new BountyRaiseGUI(target, new Benefactor(player, true), BountySeekers.compatibility).openInventory();
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
            commandSender.sendMessage("§cUsage: /bounty reset [target player]");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (UUIDCache.INSTANCE.getName(target.getUniqueId()) == null) {
            commandSender.sendMessage("§cThat player cannot found.");
            return true;
        }

        new Bounty(target.getUniqueId(), BountySeekers.compatibility).reset();
        ChatUtils.sendMessage(((Player) commandSender).getPlayer(), target.getName() + "'s bounty has been reset!");
        if (target.isOnline()) ChatUtils.sendMessage(target.getPlayer(), "Your bounty has been reset!");

        return true;
    }
}

