package com.mrkelpy.bountyseekers.v1_7.events;

import com.mrkelpy.bountyseekers.commons.carriers.Bounty;
import com.mrkelpy.bountyseekers.reflectors.v1_7.ReBukkit;
import com.mrkelpy.bountyseekers.reflectors.v1_7.ReNMS;
import com.mrkelpy.bountyseekers.v1_7.BountySeekers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerKillListener implements Listener {

    /**
     * Handles the bounty claiming when a player is killed.
     * A bounty will be available for claiming if the killed player has a bounty
     * and the killer has the permission to claim bounties.
     * @param event The event to handle.
     */
    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {

        Player killer = event.getEntity().getKiller();
        if (killer == null || !PluginCommands.checkPermission("bounty.claim", killer)) return;
        if (killer.getUniqueId() == event.getEntity().getUniqueId()) return;

        Bounty<ReBukkit, ReNMS> bounty = new Bounty<>(event.getEntity().getUniqueId());
        if (bounty.getRewards().isEmpty()) return;

        Bukkit.broadcastMessage(BountySeekers.sendMessage(null, killer.getName() + " has claimed " + bounty.getTarget() + "'s bounty!"));
        bounty.claimBounty(killer);
    }

}

