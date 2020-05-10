package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.messages;

import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Suicide implements CommandExecutor {

    private final Messages messages;

    public Suicide(Messages messages) {
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, java.lang.String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.suicide")) {
                if (args.length >= 1) {
                    StringBuilder sb = new StringBuilder();
                    for (java.lang.String arg : args) {
                        sb.append(arg).append(" ");
                    }
                    java.lang.String s = sb.toString();
                    player.setHealth(0);
                    messages.BroadcastMessage("text.suicide", "%message%", s, "%player_name%", player.getName());
                } else {
                    player.setHealth(0);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}