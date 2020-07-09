package br.com.eterniaserver.eterniaserver.modules.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {

    private final EterniaServer plugin;

    public OnPlayerLeave(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {

        final Player player = event.getPlayer();
        final String playerName = player.getName();
        if (plugin.serverConfig.getBoolean("modules.playerchecks")) Vars.afktime.remove(playerName);

        if (plugin.serverConfig.getBoolean("modules.chat")) {
            plugin.getChecks().removeUUIF(player);
            if (player.hasPermission("eternia.spy")) EterniaServer.spy.remove(player);
        }

        event.setQuitMessage(null);
        plugin.getEFiles().broadcastMessage("server.leave", "%player_name%", playerName);

    }

}