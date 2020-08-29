package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.strings.Strings;
import br.com.eterniaserver.eterniaserver.objects.AbstractCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomCommands extends AbstractCommand {

    private final EterniaServer plugin;
    private final String commandString;
    private final List<String> messagesStrings;
    private final List<String> commandsStrings;

    public CustomCommands(EterniaServer plugin, String command, String description, List<String> aliases, List<String> messages, List<String> commands) {
        super(command, description, aliases);
        this.messagesStrings = messages;
        this.commandsStrings = commands;
        this.plugin = plugin;
        this.commandString = command;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (EterniaServer.serverConfig.getBoolean("modules.commands") && EterniaServer.cmdConfig.contains("commands." + commandString)) {
            checkCommands((Player) sender, commandString);
            return true;
        }
        return false;
    }

    private void checkCommands(final Player player, final String cmd) {
        if (player.hasPermission("eternia." + cmd)) {
            for (String line : commandsStrings) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), InternMethods.setPlaceholders(player, line));
            }
            for (String line : messagesStrings) {
                player.sendMessage(Strings.getColor(InternMethods.setPlaceholders(player, line)));
            }
        } else {
            player.sendMessage(Strings.MSG_NO_PERM);
        }
    }

}
