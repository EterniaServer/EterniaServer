package br.com.eterniaserver.events;

import br.com.eterniaserver.configs.CVar;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.methods.BroadcastMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {
    @EventHandler
    public void OnLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (CVar.getBool("modules.playerchecks")) {
            Vars.afktime.remove(player.getName());
        }
        event.setQuitMessage(null);
        new BroadcastMessage("server.leave", player.getName());
    }
}