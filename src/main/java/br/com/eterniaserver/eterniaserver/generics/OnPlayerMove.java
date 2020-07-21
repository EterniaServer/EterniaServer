package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMove implements Listener {

    private final EterniaServer plugin;

    public OnPlayerMove(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;

        if (event.getTo().distanceSquared(event.getFrom()) != 0) {
            final Player player = event.getPlayer();
            final String playerName = player.getName();
            if (EterniaServer.serverConfig.getBoolean("modules.playerchecks")) {
                Vars.afkTime.put(playerName, System.currentTimeMillis());
                if (Vars.afk.contains(playerName)) {
                    Vars.afk.remove(playerName);
                    plugin.getEFiles().broadcastMessage("generic.afk.disabled", Constants.PLAYER.get(), player.getDisplayName());
                }
            }
        }
    }

}