package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ChannelCommand extends AbstractCommand {

    private final EterniaServer plugin;

    private final String commandStr;
    private final String perm;
    private final String orig;

    public ChannelCommand(final EterniaServer plugin, final String command, final String description, final String perm, final String orig) {
        super(command, description, List.of(String.valueOf(command.charAt(0))));
        this.plugin = plugin;
        this.commandStr = command;
        this.perm = perm;
        this.orig = orig;
    }

    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (commandSender.hasPermission(perm)) {
            Player player = (Player) commandSender;
            StringBuilder str = new StringBuilder(orig);
            str.append(" ").append(commandStr);
            for (String string : strings) {
                str.append(" ").append(string);
            }
            player.performCommand(str.toString());
            return true;
        }
        plugin.sendMessage(commandSender, Messages.SERVER_NO_PERM);
        return false;
    }
}
