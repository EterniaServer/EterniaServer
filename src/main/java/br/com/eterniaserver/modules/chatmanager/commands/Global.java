package br.com.eterniaserver.modules.chatmanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Global implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.chat.global")) {
                if (args.length == 0) {
                    Vars.global.put(player.getName(), 1);
                } else {
                    int o = Vars.global.get(player.getName());
                    Vars.global.put(player.getName(), 1);
                    StringBuilder sb = new StringBuilder();
                    for (String arg : args) {
                        sb.append(arg).append(" ");
                    }
                    sb.substring(0, sb.length() - 1);
                    player.chat(sb.toString());
                    Vars.global.put(player.getName(), o);
                }
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}
