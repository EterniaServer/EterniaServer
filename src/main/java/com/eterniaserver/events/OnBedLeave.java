package com.eterniaserver.events;

import com.eterniaserver.configs.Vars;
import com.eterniaserver.configs.methods.BroadcastMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class OnBedLeave implements Listener {
    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent event) {
        if (!Vars.skipping_worlds.contains(event.getPlayer().getWorld())) {
            long cooldownTime = 100;
            long secondsLeft = ((Vars.bed_cooldown.get(event.getPlayer().getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
            if (secondsLeft <= 0) {
                Vars.bed_cooldown.put(event.getPlayer().getName(), System.currentTimeMillis());
                new BroadcastMessage("bed.player-leave", event.getPlayer().getName());
            }
        }
    }

}
