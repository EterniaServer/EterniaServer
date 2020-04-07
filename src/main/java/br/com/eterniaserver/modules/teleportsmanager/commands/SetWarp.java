package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.configs.methods.PlayerMessage;
import br.com.eterniaserver.modules.teleportsmanager.sql.QueriesW;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("eternia.setwarp")) {
                    QueriesW.setWarp(player.getLocation(), args[0].toLowerCase());
                    new PlayerMessage("warps.createwarp", args[0], player);
                } else {
                    new PlayerMessage("server.no-perm", player);
                }
            } else {
                new PlayerMessage("warps.use2", player);
            }
        } else {
            new ConsoleMessage("server.only-player");
        }
        return true;
    }
}
