package br.com.eterniaserver.modules.genericmanager.commands.messages;

import br.com.eterniaserver.configs.MVar;
import br.com.eterniaserver.configs.methods.BroadcastMessage;
import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.configs.methods.PlayerMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Suicide implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.suicide")) {
                if (args.length >= 1) {
                    StringBuilder sb = new StringBuilder();
                    for (String arg : args) {
                        sb.append(arg).append(" ");
                    }
                    sb.append("&8- &3").append(player.getName());
                    String s = sb.toString();
                    player.setHealth(0);
                    new BroadcastMessage("text.suicide", MVar.getColor(s));
                } else {
                    player.setHealth(0);
                }
            } else {
                new PlayerMessage("server.no-perm", player);
            }
        } else {
            new ConsoleMessage("server.only-player");
        }
        return true;
    }
}