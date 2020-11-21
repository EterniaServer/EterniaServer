package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomCommand extends AbstractCommand {

    private final EterniaServer plugin;
    private final String commandString;
    private final List<String> messagesStrings;
    private final List<String> commandsStrings;
    private final boolean console;

    public CustomCommand(EterniaServer plugin, String command, String description, List<String> aliases, List<String> messages, List<String> commands, boolean console) {

        super(command, description, aliases);

        this.messagesStrings = messages;
        this.commandsStrings = commands;
        this.plugin = plugin;
        this.commandString = command;
        this.console = console;

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (EterniaServer.getBoolean(Booleans.MODULE_COMMANDS)) {
            checkCommands((Player) sender, commandString);
            return true;
        }
        return false;
    }

    private void checkCommands(final Player player, final String cmd) {
        if (player.hasPermission(EterniaServer.getString(Strings.PERM_BASE_COMMAND) + cmd)) {
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
            EterniaServer.sendMessage(player, Messages.SERVER_NO_PERM);
        }
    }

}
