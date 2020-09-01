package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.strings.MSG;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;

public class EventPlayerCommandPreProcess implements Listener {

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;

        final Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();

        if (message.equalsIgnoreCase("/tps")) {
            player.sendMessage(MSG.MSG_TPS.replace(Constants.TPS, Arrays.toString(Bukkit.getServer().getTPS())));
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
