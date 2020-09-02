package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;

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
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - UtilInternMethods.getCooldown(playerName)) > 6) {
                PluginVars.bedCooldown.put(playerName, System.currentTimeMillis());
                Bukkit.broadcastMessage(UtilInternMethods.putName(player, PluginMSGs.MSG_PLAYER_SKIP));
            }
        }
    }

}
