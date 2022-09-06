package com.mrkelpy.bountyseekers.commons.commands;

/**
 * This class holds all the existent commands, their needed permissions, the description, and
 * usage.
 */
public enum CommandRegistry {

    SET_REWARD_LIMIT("/bounty setrewardlimit <limit>", "bounty.admin.setrewardlimit", "Sets the maximum amount of rewards a player can receive for their bounty."),
    BOUNTY_LIST("/bounty list", "bounty.list", "Displays the list of active bounties."),
    BOUNTY_RAISE("/bounty raise <target player>", "bounty.raise", "Raises the bounty on a player by contributing to the player's bounty reward."),
    BOUNTY_SILENT_RAISE("/bounty silentraise <target player>", "bounty.raise.silent", "Raises the bounty for a player, hiding the identity of the benefactor."),
    BOUNTY_RESET("/bounty reset <target player>", "bounty.reset", "Resets the bounty on a player."),
    HELP("/bounty help", "bounty.help", "Displays the help menu.");

    private final String usage;
    private final String permission;
    private final String description;

    /**
     * Main constructor for the CommandRegistry enum.
     *
     * @param usage       The usage of the command.
     * @param permission  The permission needed to execute the command.
     * @param description The command description.
     */
    CommandRegistry(String usage, String permission, String description) {
        this.usage = usage;
        this.permission = permission;
        this.description = description;
    }

    public String getUsage() {
        return usage;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }

}

