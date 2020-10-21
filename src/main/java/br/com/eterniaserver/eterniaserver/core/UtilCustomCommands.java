package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UtilCustomCommands extends AbstractCommand {

    private final EterniaServer plugin;
    private final String commandString;
    private final List<String> messagesStrings;
    private final List<String> commandsStrings;
    private final boolean console;

    public UtilCustomCommands(EterniaServer plugin, String command, String description, List<String> aliases, List<String> messages, List<String> commands, boolean console) {
        super(command, description, aliases);
        this.messagesStrings = messages;
        this.commandsStrings = commands;
        this.plugin = plugin;
        this.commandString = command;
        this.console = console;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (EterniaServer.configs.moduleCommands) {
            checkCommands((Player) sender, commandString);
            return true;
        }
        return false;
    }

    private void checkCommands(final Player player, final String cmd) {
        if (player.hasPermission("eternia." + cmd)) {
            for (String line : commandsStrings) {
                if (console) {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), APIServer.setPlaceholders(player, line));
                } else {
                    player.performCommand(APIServer.setPlaceholders(player, line));
                }
            }
            for (String line : messagesStrings) {
                player.sendMessage(APIServer.getColor(APIServer.setPlaceholders(player, line)));
            }
        } else {
            EterniaServer.msg.sendMessage(player, Messages.SERVER_NO_PERM);
        }
    }

}
