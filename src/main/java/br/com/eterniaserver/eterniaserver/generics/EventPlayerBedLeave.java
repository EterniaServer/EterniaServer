package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.util.concurrent.TimeUnit;

public class EventPlayerBedLeave implements Listener {

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        if (!EterniaServer.serverConfig.getBoolean("modules.bed")) return;

        final Player player = event.getPlayer();
        final String playerName = player.getName();
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - UtilInternMethods.getCooldown(playerName)) > 6) {
            Vars.bedCooldown.put(playerName, System.currentTimeMillis());
        }
    }

}
