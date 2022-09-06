package com.mrkelpy.bountyseekers.commons.commands;

import com.mrkelpy.bountyseekers.commons.utils.PluginConstants;
import org.bukkit.command.CommandSender;

/**
 * This interface allows for an explicit declaration of the methods that will contain the actual method implementations,
 * and is needed to ensure that they exist, and for them to be called (in their actual implementation) through Reflection in the PluginCommandHandler.
 */
public interface ICommandImplementations {

    default boolean setRewardLimitCommand(CommandSender commandSender, String[] args) {
        return true;
    }

    default boolean listCommand(CommandSender commandSender, String[] args) {
        return true;
    }

    default boolean raiseCommand(CommandSender commandSender, String[] args) {
        return true;
    }

    default boolean silentRaiseCommand(CommandSender commandSender, String[] args) {
        return true;
    }

    default boolean resetCommand(CommandSender commandSender, String[] args) {
        return true;
    }

    /**
     * The default implementation of this method iterates through every registered command and
     * builds a help menu command with the command usage, the description, and the needed permissions for every command.
     *
     * @param commandSender The sender of the command.
     * @param args          The command arguments.
     * @return Boolean, Feedback to the sender.
     */
    default boolean helpCommand(CommandSender commandSender, String[] args) {

        commandSender.sendMessage(String.format("§e----- §c%s Command List§e-----", PluginConstants.PLUGIN_NAME));

        for (CommandRegistry command : CommandRegistry.values()) {
            commandSender.sendMessage(String.format("§e%s §7-> §f%s", command.getUsage(), command.getDescription()) + "\n");
        }

        return true;
    }

}

