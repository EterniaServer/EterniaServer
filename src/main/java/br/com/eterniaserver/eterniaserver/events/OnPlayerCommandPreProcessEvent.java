package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;

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
        if (event.isCancelled()) return;

        final Player player = event.getPlayer();
        final String playerName = player.getName();
        String message = event.getMessage().toLowerCase();

        if (message.equalsIgnoreCase("/tps") && plugin.hasPlaceholderAPI) {
            String s = PlaceholderAPI.setPlaceholders(player, "%server_tps%");
            messages.sendMessage("replaces.tps", "%tps%", s.substring(0, s.length() - 2), player);
            event.setCancelled(true);
            return;
        }
        if (plugin.serverConfig.getBoolean("modules.commands")) {
            if (plugin.cmdConfig.contains("commands." + message)) {
                String cmd = message.replace("/", "");
                if (player.hasPermission("eternia." + cmd)) {
                    for (String line : plugin.cmdConfig.getStringList("commands." + message + ".command")) {
                        String modifiedCommand;

                        if (plugin.hasPlaceholderAPI) modifiedCommand = messages.putPAPI(player, line);
                        else modifiedCommand = line.replace("%player_name%", playerName);

                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                    }
                    for (String line : plugin.cmdConfig.getStringList("commands." + playerName + ".text")) {
                        String modifiedText;

                        if (plugin.hasPlaceholderAPI) modifiedText = messages.putPAPI(player, line);
                        else modifiedText = line.replace("%player_name%", playerName);

                        player.sendMessage(strings.getColor(modifiedText));
                    }
                } else {
                    messages.sendMessage("server.no-perm", player);
                }
                event.setCancelled(true);
            }
        }
    }

}
