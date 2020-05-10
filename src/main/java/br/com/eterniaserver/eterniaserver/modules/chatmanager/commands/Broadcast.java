package br.com.eterniaserver.eterniaserver.modules.chatmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Broadcast implements CommandExecutor {

    private final Messages messages;
    private final Strings strings;

    public Broadcast(Messages messages, Strings strings) {
        this.messages = messages;
        this.strings = strings;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, java.lang.String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.advice")) {
                if (args.length >= 1) {
                    StringBuilder sb = new StringBuilder();
                    for (String arg : args) {
                        sb.append(arg).append(" ");
                    }
                    sb.substring(0, sb.length() - 1);
                    messages.BroadcastMessage("chat.global-advice", "%advice%", strings.getColor(sb.toString()));
                } else {
                    messages.PlayerMessage("chat.use", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            if (args.length >= 1) {
                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                sb.substring(0, sb.length() - 1);
                messages.BroadcastMessage("chat.global-advice", "%advice%", strings.getColor(sb.toString()));
            } else {
                messages.ConsoleMessage("chat.use");
            }
        }
        return true;
    }
}
