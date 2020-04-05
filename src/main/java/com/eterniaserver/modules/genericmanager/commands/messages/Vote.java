package com.eterniaserver.modules.genericmanager.commands.messages;

import com.eterniaserver.configs.MVar;
import com.eterniaserver.configs.methods.PlayerMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vote implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.vote")) {
                String sites_voto = MVar.getMessage("text.vote");
                String[] sites_lista = sites_voto.split("/split/");
                for (String s : sites_lista) {
                    sender.sendMessage(MVar.getColor(s));
                }
            } else {
                new PlayerMessage("server.no-perm", player);
            }
        } else {
            String sites_voto = MVar.getMessage("text.vote");
            String[] sites_lista = sites_voto.split("/split/");
            for (String s : sites_lista) {
                Bukkit.getConsoleSender().sendMessage(MVar.getColor(s));
            }
        }
        return true;
    }
}