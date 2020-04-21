package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.API.WarpsAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelWarp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("eternia.delwarp")) {
                    if (WarpsAPI.existWarp(args[0])) {
                        WarpsAPI.delWarp(args[0]);
                        Messages.PlayerMessage("warps.delwarp", player);
                    } else {
                        Messages.PlayerMessage("warps.noexist", args[0], player);
                    }
                } else {
                    Messages.PlayerMessage("server.no-perm", player);
                }
            } else {
                Messages.PlayerMessage("warps.deluse", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}
