package com.mrkelpy.bountyseekers.vCR1_7_10.events;

import com.mrkelpy.bountyseekers.commons.carriers.Bounty;
import com.mrkelpy.bountyseekers.vCR1_7_10.BountySeekers;
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

        Bounty bounty = new Bounty(event.getEntity().getUniqueId(), BountySeekers.compatibility);
        if (bounty.getRewards().isEmpty()) return;

        Bukkit.broadcastMessage(BountySeekers.sendMessage(null, killer.getName() + " has claimed " + bounty.getTarget() + "'s bounty!"));
        bounty.claimBounty(killer);
    }

}

