package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.Strings;
import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnPlayerCommandPreProcess implements Listener {

    private final EterniaServer plugin;
    private final EFiles messages;

    public OnPlayerCommandPreProcess(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;

        final Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();

        if (message.equalsIgnoreCase("/tps")) {
            String s = PlaceholderAPI.setPlaceholders(player, Constants.TPS);
            messages.sendMessage(Strings.M_TPS, Constants.TPS, s.substring(0, s.length() - 2), player);
            event.setCancelled(true);
            return;
        }

        for (String line : EterniaServer.serverConfig.getStringList("blocked-commands")) {
            if (message.startsWith(line)) {
                event.setCancelled(true);
                // TODO
                return;
            }
        }

        final String commandsConfig = "commands.";
        if (EterniaServer.serverConfig.getBoolean("modules.commands") && EterniaServer.cmdConfig.contains(commandsConfig + message)) {
            checkCommands(player, message.replace("/", ""), message);
            event.setCancelled(true);
        }
    }

    private void checkCommands(final Player player, final String cmd, final String message) {
        final String commandsConfig = "commands.";
        if (player.hasPermission("eternia." + cmd)) {
            for (String line : EterniaServer.cmdConfig.getStringList(commandsConfig + message + ".command")) {
                final String modifiedCommand = PlaceholderAPI.setPlaceholders(player, line);
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
            }
            for (String line : EterniaServer.cmdConfig.getStringList(commandsConfig + message + ".text")) {
                final String modifiedText = PlaceholderAPI.setPlaceholders(player, line);
                player.sendMessage(messages.getColor(modifiedText));
            }
        } else {
            messages.sendMessage(Strings.M_NO_PERM, player);
        }
    }

}
