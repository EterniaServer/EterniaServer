package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnPlayerCommandPreProcessEvent implements Listener {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Strings strings;

    public OnPlayerCommandPreProcessEvent(EterniaServer plugin, Messages messages, Strings strings) {
        this.plugin = plugin;
        this.messages = messages;
        this.strings = strings;
    }

    @EventHandler
    public void onPlayerCommandPreProcessEvent(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getMessage().equalsIgnoreCase("/tps") && plugin.hasPlaceholderAPI) {
            Player player = event.getPlayer();
            String s = PlaceholderAPI.setPlaceholders(player, "%server_tps%");
            messages.PlayerMessage("replaces.tps", "%tps%", s.substring(0, s.length() - 2), player);
            event.setCancelled(true);
        }
        if (plugin.serverConfig.getBoolean("modules.commands")) {
            Player player = event.getPlayer();
            if (plugin.cmdConfig.contains("commands." + event.getMessage().toLowerCase())) {
                final String cmd = event.getMessage().toLowerCase().replace("/", "");
                if (player.hasPermission("eternia." + cmd)) {
                    for (String line : plugin.cmdConfig.getStringList("commands." + event.getMessage() + ".command")) {
                        if (plugin.hasPlaceholderAPI) {
                            String modifiedCommand = messages.putPAPI(player, line);
                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                        } else {
                            String modifiedCommand = line.replace("%player_name%", player.getName());
                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                        }
                    }
                    for (String line : plugin.cmdConfig.getStringList("commands." + event.getMessage() + ".text")) {
                        if (plugin.hasPlaceholderAPI) {
                            String modifiedText = messages.putPAPI(player, line);
                            player.sendMessage(strings.getColor(modifiedText));
                        } else {
                            String modifiedText = line.replace("%player_name%", player.getName());
                            player.sendMessage(strings.getColor(modifiedText));
                        }
                    }
                } else {
                    messages.PlayerMessage("server.no-perm", player);
                }
                event.setCancelled(true);
            }
        }
    }

}
