package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class OnPlayerCommandPreProcessEvent implements Listener {

    private final EterniaServer plugin;

    public OnPlayerCommandPreProcessEvent(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandPreProcessEvent(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/tps") && Strings.papi) {
            if (event.isCancelled()) {
                return;
            }
            Player player = event.getPlayer();
            String s = PlaceholderAPI.setPlaceholders(player, "%server_tps%");
            Messages.PlayerMessage("replaces.tps", "%tps%", s.substring(0, s.length() - 2), player);
            event.setCancelled(true);
        }
        if (EterniaServer.configs.getBoolean("modules.commands")) {
            if (event.isCancelled()) {
                return;
            }
            Player player = event.getPlayer();
            if (EterniaServer.commands.contains("commands." + event.getMessage().toLowerCase())) {
                final String cmd = event.getMessage().toLowerCase().replace("/", "");
                if (player.hasPermission("eternia." + cmd)) {
                    List<String> commandList = EterniaServer.commands.getStringList("commands." + event.getMessage() + ".command");
                    for (String line : commandList) {
                        if (Strings.papi) {
                            String modifiedCommand = Messages.putPAPI(player, line);
                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                        } else {
                            String modifiedCommand = line.replace("%player_name%", player.getName());
                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                        }
                    }
                    List<String> textList = EterniaServer.commands.getStringList("commands." + event.getMessage() + ".text");
                    for (String line : textList) {
                        if (Strings.papi) {
                            String modifiedText = Messages.putPAPI(player, line);
                            player.sendMessage(Strings.getColor(modifiedText));
                        } else {
                            String modifiedText = line.replace("%player_name%", player.getName());
                            player.sendMessage(Strings.getColor(modifiedText));
                        }
                    }
                } else {
                    Messages.PlayerMessage("server.no-perm", player);
                }
                event.setCancelled(true);
            }
        }
    }

}
