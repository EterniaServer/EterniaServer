package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class EventPlayerLeave implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {

        final Player player = event.getPlayer();
        final String playerName = player.getName();
        PluginVars.afkTime.remove(playerName);
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        EQueries.executeQuery(PluginConstants.getQueryUpdate(PluginConfigs.TABLE_PLAYER, PluginConstants.HOURS_STR, PluginVars.playerProfile.get(uuid).updateTimePlayed(), PluginConstants.UUID_STR, uuid.toString()));

        if (EterniaServer.serverConfig.getBoolean("modules.chat")) {
            UtilInternMethods.removeUUIF(player);
            if (player.hasPermission("eternia.spy")) PluginVars.spy.remove(playerName);
        }

        event.setQuitMessage(null);
        Bukkit.broadcastMessage(UtilInternMethods.putName(player, PluginMSGs.MSG_LEAVE));

    }

}