package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CustomCommands extends AbstractCommand {

    private final EterniaServer plugin;
    private final EFiles messages;
    private final String commandString;
    private final List<String> messagesStrings;
    private final List<String> commandsStrings;

    public CustomCommands(EterniaServer plugin, String command, String description, List<String> aliases, List<String> messages, List<String> commands) {
        super(command, description, aliases);
        this.messages = plugin.getEFiles();
        this.messagesStrings = messages;
        this.commandsStrings = commands;
        this.plugin = plugin;
        this.commandString = command;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (EterniaServer.serverConfig.getBoolean("modules.commands") && EterniaServer.cmdConfig.contains("commands." + commandString)) {
            checkCommands((Player) sender, commandString);
            return true;
        }
        return false;
    }

    private void checkCommands(final Player player, final String cmd) {
        if (player.hasPermission("eternia." + cmd)) {
            for (String line : PlaceholderAPI.setPlaceholders((OfflinePlayer) player, commandsStrings)) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), line);
            }
            for (String line : PlaceholderAPI.setPlaceholders((OfflinePlayer) player, messagesStrings)) {
                player.sendMessage(messages.getColor(line));
            }
        } else {
            messages.sendMessage(Strings.MSG_NO_PERM, player);
        }
    }

}
