package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class OnAsyncPlayerPreLogin implements Listener {

    private final EterniaServer plugin;
    private final PlayerManager playerManager;

    public OnAsyncPlayerPreLogin(EterniaServer plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        final String playerName = event.getName();
        if (plugin.serverConfig.getBoolean("modules.experience")) {
            if (!playerManager.playerXPExist(playerName)) playerManager.playerXPCreate(playerName);
        }
        if (plugin.serverConfig.getBoolean("modules.playerchecks")) {
            EterniaServer.afktime.put(playerName, System.currentTimeMillis());
            if (!playerManager.playerProfileExist(playerName)) playerManager.playerProfileCreate(playerName);
        }
        if (plugin.serverConfig.getBoolean("modules.home")) {
            if (!playerManager.playerHomeExist(playerName)) playerManager.playerHomeCreate(playerName);
        }
    }

}
