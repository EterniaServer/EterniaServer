package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.configs.Strings;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;

        if (event.getTo().distanceSquared(event.getFrom()) != 0) {
            final Player player = event.getPlayer();
            final String playerName = player.getName();
            Vars.afkTime.put(playerName, System.currentTimeMillis());
            if (Vars.afk.contains(playerName)) {
                Vars.afk.remove(playerName);
                Bukkit.broadcastMessage(Strings.MSG_AFK_DISABLE.replace(Constants.PLAYER, player.getDisplayName()));
            }
        }
    }

}
