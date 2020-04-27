package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {

    @EventHandler
    public void OnLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (EterniaServer.configs.getBoolean("modules.playerchecks")) {
            Vars.afktime.remove(player.getName());
        }
        if (EterniaServer.configs.getBoolean("modules.chat")) {
            remove(event.getPlayer());
        }
        event.setQuitMessage(null);
        Messages.BroadcastMessage("server.leave", "%player_name%", player.getName());
    }

    public static void remove(Player p) {
        Vars.uufi.remove(p.getUniqueId());
    }

}