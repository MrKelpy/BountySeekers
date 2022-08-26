package com.mrkelpy.bountyseekers.events;

import com.mrkelpy.bountyseekers.BountySeekers;
import com.mrkelpy.bountyseekers.common.Benefactor;
import com.mrkelpy.bountyseekers.common.Bounty;
import com.mrkelpy.bountyseekers.gui.BountyListDisplayGUI;
import com.mrkelpy.bountyseekers.gui.BountyRaiseGUI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This class handles all the plugin commands that are sent to the server.
 */
public class PluginCommands implements CommandExecutor {

    public final static PluginCommands INSTANCE = new PluginCommands();

    /**
     * Listens for commands sent to the server.
     * @param commandSender The sender of the command.
     * @param command The command that was sent.
     * @param s The command as a string
     * @param args The arguments of the command.
     * @return (Boolean) Feedback to the caller.
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (Objects.equals(command.getName(), "bounty")) {
            this.parseCommands(commandSender, args);
        }

        return true;
    }

    /**
     * Checks if a player has permission to use a command. If not, send a message to the player telling
     * them they do not have permission.
     * @param permission The permission to check for
     * @param sender The sender to check for the permission
     * @return Whether the player has permission or not
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean checkPermission(String permission, CommandSender sender) {
        if (sender.hasPermission("aos.all") || sender.isOp() || sender.hasPermission(permission))
            return true;

        sender.sendMessage("§cYou do not have permission to use this command");
        return false;
    }

    /**
     * Since there's a base command, this method takes in the first argument after that prefix and parses the command
     * normally from there. This serves to prevent the command from being used by other plugins, and to have a "command space"
     * for the plugin.
     * The "args" array will also be modified, removing the first element, because that's the command to be called.
     * @param commandSender The sender of the command
     * @param args The arguments of the command
     * @return Boolean, feedback to the caller
     */
    private boolean parseCommands(CommandSender commandSender, String[] args) {

        // Using just the base command only shows the help menu
        if (args.length == 0) return helpCommand(commandSender);

        // Process the arguments and remove the first element
        String command = args[0];
        List<String> argumentProcessing = new LinkedList<>(Arrays.asList(args));
        argumentProcessing.remove(0);
        args = Arrays.copyOf(argumentProcessing.toArray(), args.length - 1, String[].class);

        // Check which command was meant to be fired given the command argument, and fire it
        if (command.equalsIgnoreCase("raise")) {
            if (!this.checkPermission("bounty.raise", commandSender)) return true;
            return bountyRaiseCommand(commandSender, args, false);
        }

        if (command.equalsIgnoreCase("silentraise")) {
            if (!this.checkPermission("bounty.raise.silent", commandSender)) return true;
            return bountyRaiseCommand(commandSender, args, true);
        }

        if (command.equalsIgnoreCase("reset")) {
            if (!this.checkPermission("bounty.reset", commandSender)) return true;
            return bountyResetCommand(commandSender, args);
        }

        if (command.equalsIgnoreCase("list")) {
            if (!this.checkPermission("bounty.bountylist", commandSender)) return true;
            return bountyListCommand(commandSender);
        }

        if (command.equalsIgnoreCase("help")) {
            if (!this.checkPermission("bounty.help", commandSender)) return true;
            return helpCommand(commandSender);
        }

        commandSender.sendMessage("§cUnknown command. Use /bounty help for a list of available commands");
        return true;
    }

    /**
     * Displays the list of active bounties.
     * @param commandSender The sender of the command
     * @return Boolean, feedback to the caller
     */
    private boolean bountyListCommand(CommandSender commandSender) {

        // Only players can use this command
        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;

        new BountyListDisplayGUI(player).openInventory();
        return true;
    }

    /**
     * Opens up a GUI to allow a player to raise someone's bounty.
     * @param commandSender The sender of the command
     * @param args The arguments of the command
     * @return Boolean, feedback to the caller
     */
    private boolean bountyRaiseCommand(CommandSender commandSender, String[] args, boolean silent) {

        // Only players can use this command
        if (!(commandSender instanceof Player)) return false;

        if (args.length == 0) {
            commandSender.sendMessage("§cUsage: /bounty raise [target player] OR /bounty silentraise [target player]");
            return true;
        }

        // Prevents the target from raising their own bounty
        if (commandSender.getName().equalsIgnoreCase(args[0])) {
            BountySeekers.sendMessage((Player) commandSender, "You can't raise your own bounty!");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        Player player = (Player) commandSender;

        if (BountySeekers.UUID_CACHE.get(target.getUniqueId()) == null) {
            commandSender.sendMessage("§cThat player cannot found.");
            return true;
        }

        new BountyRaiseGUI(target, new Benefactor(player, silent)).openInventory();
        return true;
    }

    /**
     * Resets a player's bounty.
     * @param commandSender The sender of the command
     * @param args The arguments of the command
     * @return Boolean, feedback to the caller
     */
    private boolean bountyResetCommand(CommandSender commandSender, String[] args) {

        // Only players can use this command
        if (!(commandSender instanceof Player)) return false;

        if (args.length == 0) {
            commandSender.sendMessage("§cUsage: /bounty reset [target player]");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (BountySeekers.UUID_CACHE.get(target.getUniqueId()) == null) {
            commandSender.sendMessage("§cThat player cannot found.");
            return true;
        }

        new Bounty(target.getUniqueId()).reset();
        BountySeekers.sendMessage(((Player) commandSender).getPlayer(), target.getName() + "'s bounty has been reset!");
        if (target.isOnline()) BountySeekers.sendMessage(target.getPlayer(), "Your bounty has been reset!");

        return true;
    }

    /**
     * Displays a list of all the available commands to the player.
     * @param commandSender The sender of the command
     * @return Boolean, feedback to the caller
     */
    private boolean helpCommand(CommandSender commandSender) {

        commandSender.sendMessage(String.format("§e----- §c%s Command List§e-----", BountySeekers.PLUGIN_NAME));
        commandSender.sendMessage("§e> §f/bounty list §7- §aDisplays a list of all the active bounties");
        commandSender.sendMessage("§e> §f/bounty raise [target player] §7-> Raises a player's bounty.");
        commandSender.sendMessage("§e> §f/bounty silentraise [target player] §7-> Raises a player's bounty, hiding the benefactor's identity.");
        commandSender.sendMessage("§e> §f/bounty reset [target player] §7-> Resets a player's bounty.");
        commandSender.sendMessage("§e> §f/bounty help §7-> Displays this list of commands.");

        return true;

    }


}

