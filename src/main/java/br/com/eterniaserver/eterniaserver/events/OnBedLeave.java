package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.util.concurrent.TimeUnit;

public class OnBedLeave implements Listener {

    private final EterniaServer plugin;

    public OnBedLeave(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent event) {
        final Player player = event.getPlayer();
        final String playerName = player.getName();
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - plugin.getChecks().getCooldown(playerName)) > 6) {
            plugin.getVars().bed_cooldown.put(playerName, System.currentTimeMillis());
        }
    }

}
