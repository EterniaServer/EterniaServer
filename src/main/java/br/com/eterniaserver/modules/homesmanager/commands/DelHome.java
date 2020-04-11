package br.com.eterniaserver.modules.homesmanager.commands;

import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.configs.methods.PlayerMessage;
import br.com.eterniaserver.modules.homesmanager.sql.Queries;
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
                    if (Queries.existHome(args[0], player.getName())) {
                        Queries.delHome(args[0], player.getName());
                        new PlayerMessage("home.del", player);
                    } else {
                        new PlayerMessage("home.no", player);
                    }
                } else {
                    new PlayerMessage("server.no-perm", player);
                }
            } else {
                new PlayerMessage("home.use3", player);
            }
        } else {
            new ConsoleMessage("server.only-player");
        }
        return true;
    }

}
