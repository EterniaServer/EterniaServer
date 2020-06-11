package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import java.util.concurrent.TimeUnit;

public class OnBedEnter implements Listener {

    private final EterniaServer plugin;

    public OnBedEnter(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        if (event.isCancelled()) return;

        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            final String playerName = event.getPlayer().getName();
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - plugin.getChecks().getCooldown(playerName)) > 6) {
                plugin.getVars().bed_cooldown.put(playerName, System.currentTimeMillis());
                plugin.getMessages().broadcastMessage("bed.player-s", "%player_name%", playerName);
            }
        }
    }

}
