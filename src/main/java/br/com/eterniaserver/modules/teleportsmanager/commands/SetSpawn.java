package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.API.WarpsAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.setspawn")) {
                WarpsAPI.setWarp(player.getLocation(), "spawn");
                Messages.PlayerMessage("warps.spawn-set", player);
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}