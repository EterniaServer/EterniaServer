package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.Constants;
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
        if (EterniaServer.serverConfig.getBoolean("modules.playerchecks")) Vars.afkTime.remove(playerName);

        if (EterniaServer.serverConfig.getBoolean("modules.chat")) {
            plugin.getInternMethods().removeUUIF(player);
            if (player.hasPermission("eternia.spy")) Vars.spy.remove(player);
        }

        event.setQuitMessage(null);
        plugin.getEFiles().broadcastMessage("server.leave", Constants.PLAYER.get(), player.getDisplayName());

    }

}