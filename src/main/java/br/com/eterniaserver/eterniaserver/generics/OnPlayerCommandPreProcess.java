package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Strings;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnPlayerCommandPreProcess implements Listener, Constants{

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;

        final Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();

        if (message.equalsIgnoreCase("/tps")) {
            String s = InternMethods.setPlaceholders(player, TPS);
            player.sendMessage(Strings.MSG_TPS.replace(TPS, s.substring(0, s.length() - 2)));
            event.setCancelled(true);
            return;
        }

        for (String line : EterniaServer.serverConfig.getStringList("blocked-commands")) {
            if (message.startsWith(line)) {
                event.setCancelled(true);
                return;
            }
        }
    }

}
