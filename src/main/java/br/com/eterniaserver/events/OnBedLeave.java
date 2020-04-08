package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.methods.BroadcastMessage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.util.concurrent.TimeUnit;

public class OnBedLeave implements Listener {

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        if (!Vars.skipping_worlds.contains(player.getWorld())) {
            Bukkit.getScheduler().runTaskLater(EterniaServer.getMain(), () -> {
                if ((TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - getCooldown(player.getName())) > 100)) {
                    new BroadcastMessage("bed.player-leave", player.getName());
                    Vars.bed_cooldown.put(player.getName(), System.currentTimeMillis());
                }
            }, 1);
        }
    }

    private long getCooldown(final String name) {
        if (!Vars.bed_cooldown.containsKey(name)) {
            return 0;
        } else {
            return Vars.bed_cooldown.get(name);
        }
    }

}
