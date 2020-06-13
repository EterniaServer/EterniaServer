package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.Vars;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {

    private final EterniaServer plugin;
    private final Vars vars;

    public OnPlayerLeave(EterniaServer plugin) {
        this.plugin = plugin;
        this.vars = plugin.getVars();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {

        final Player player = event.getPlayer();
        final String playerName = player.getName();
        if (plugin.serverConfig.getBoolean("modules.playerchecks")) vars.afktime.remove(playerName);

        if (plugin.serverConfig.getBoolean("modules.chat")) {
            plugin.getChecks().removeUUIF(player);
            if (player.hasPermission("eternia.spy")) vars.spy.remove(player);
        }

        event.setQuitMessage(null);
        plugin.getEFiles().broadcastMessage("server.leave", "%player_name%", playerName);

    }

}