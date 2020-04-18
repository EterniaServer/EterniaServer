package br.com.eterniaserver.modules.homesmanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.homesmanager.sql.HomesAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHome implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("eternia.delhome")) {
                    if (HomesAPI.existHome(args[0].toLowerCase(), player.getName())) {
                        HomesAPI.delHome(args[0].toLowerCase(), player.getName());
                        Messages.PlayerMessage("home.del", player);
                    } else {
                        Messages.PlayerMessage("home.no", player);
                    }
                } else {
                    Messages.PlayerMessage("server.no-perm", player);
                }
            } else {
                Messages.PlayerMessage("home.use3", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }

}
