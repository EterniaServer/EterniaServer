package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnPlayerCommandPreProcess implements Listener {

    private final EterniaServer plugin;
    private final EFiles messages;

    public OnPlayerCommandPreProcess(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
    }

    @EventHandler
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;

        final Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();

        if (message.equalsIgnoreCase("/tps") && plugin.hasPlaceholderAPI) {
            String s = PlaceholderAPI.setPlaceholders(player, "%server_tps%");
            messages.sendMessage("replaces.tps", "%server_tps%", s.substring(0, s.length() - 2), player);
            event.setCancelled(true);
            return;
        }
        final String cMessage = "command." + message;
        if (plugin.getServerConfig().getBoolean("modules.commands") && (plugin.cmdConfig.contains(cMessage))) {
            String cmd = message.replace("/", "");
            if (player.hasPermission("eternia." + cmd)) {
                for (String line : plugin.cmdConfig.getStringList(cMessage + ".command")) {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(player, line));
                }
                for (String line : plugin.cmdConfig.getStringList(cMessage + ".text")) {
                    player.sendMessage(messages.getColor(PlaceholderAPI.setPlaceholders(player, line)));
                }
            } else {
                messages.sendMessage("server.no-perm", player);
            }
            event.setCancelled(true);
        }
    }

}
