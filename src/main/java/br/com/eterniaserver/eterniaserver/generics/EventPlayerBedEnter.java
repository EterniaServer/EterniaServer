package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.strings.MSG;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import java.util.concurrent.TimeUnit;

public class EventPlayerBedEnter implements Listener {

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (!EterniaServer.serverConfig.getBoolean("modules.bed")) return;

        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            final Player player = event.getPlayer();
            final String playerName = player.getName();
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - InternMethods.getCooldown(playerName)) > 6) {
                Vars.bedCooldown.put(playerName, System.currentTimeMillis());
                Bukkit.broadcastMessage(InternMethods.putName(player, MSG.MSG_PLAYER_SKIP));
            }
        }
    }

}
