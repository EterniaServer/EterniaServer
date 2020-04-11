package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.methods.BroadcastMessage;
import br.com.eterniaserver.player.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {

    @EventHandler
    public void OnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (EterniaServer.configs.getBoolean("modules.experience")) {
            if (!PlayerManager.PlayerExist(player.getName())) {
                PlayerManager.CreatePlayer(player.getName());
            }
        }
        if (EterniaServer.configs.getBoolean("modules.playerchecks")) {
            Vars.afktime.put(player.getName(), System.currentTimeMillis());
        }
        if (EterniaServer.configs.getBoolean("modules.home")) {
            if (!PlayerManager.PlayerExistH(player.getName())) {
                PlayerManager.CreateHome(player.getName());
            }
        }
        event.setJoinMessage(null);
        new BroadcastMessage("server.join", player.getName());
    }

}