package br.com.eterniaserver.events;

import br.com.eterniaserver.configs.CVar;
import br.com.eterniaserver.configs.Vars;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class OnPlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (Objects.requireNonNull(event.getTo()).distanceSquared(event.getFrom()) != 0) {
            Player player = event.getPlayer();
            if (Vars.playerposition.get(player.getName()) != player.getLocation()) {
                if (CVar.getBool("modules.playerchecks")) {
                    Vars.afktime.put(player.getName(), System.currentTimeMillis());
                }
                if (CVar.getBool("modules.teleports")) {
                    Vars.moved.put(player.getName(), true);
                }
            }
        }
    }

}
