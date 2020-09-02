package br.com.eterniaserver.eterniaserver.generics;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class EventPlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;

        if (event.getTo().distanceSquared(event.getFrom()) != 0) {
            final Player player = event.getPlayer();
            final String playerName = player.getName();
            PluginVars.afkTime.put(playerName, System.currentTimeMillis());
            if (PluginVars.afk.contains(playerName)) {
                PluginVars.afk.remove(playerName);
                Bukkit.broadcastMessage(UtilInternMethods.putName(player, PluginMSGs.MSG_AFK_DISABLE));
            }
        }
    }

}
