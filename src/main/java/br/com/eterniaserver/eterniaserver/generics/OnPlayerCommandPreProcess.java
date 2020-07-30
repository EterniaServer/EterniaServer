package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.Strings;
import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnPlayerCommandPreProcess implements Listener {

    private final EFiles messages;

    public OnPlayerCommandPreProcess(EterniaServer plugin) {
        this.messages = plugin.getEFiles();
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;

        final Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();

        if (message.equalsIgnoreCase("/tps")) {
            String s = PlaceholderAPI.setPlaceholders((OfflinePlayer) player, Constants.TPS);
            messages.sendMessage(Strings.MSG_TPS, Constants.TPS, s.substring(0, s.length() - 2), player);
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
