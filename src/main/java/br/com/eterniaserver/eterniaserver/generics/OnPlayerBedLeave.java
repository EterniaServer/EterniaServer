package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.util.concurrent.TimeUnit;

public class OnPlayerBedLeave implements Listener {

    private final EterniaServer plugin;

    public OnPlayerBedLeave(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        final Player player = event.getPlayer();
        final String playerName = player.getName();
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - plugin.getInternMethods().getCooldown(playerName)) > 6) {
            Vars.bedCooldown.put(playerName, System.currentTimeMillis());
        }
    }

}
