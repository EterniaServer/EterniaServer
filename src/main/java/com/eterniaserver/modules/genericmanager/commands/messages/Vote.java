package com.eterniaserver.modules.genericmanager.commands.messages;

import com.eterniaserver.configs.MVar;
import com.eterniaserver.configs.Vars;
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
                String sites_voto = Vars.getString("text.vote");
                assert sites_voto != null;
                String[] sites_lista = sites_voto.split("/split/");
                for (String s : sites_lista) {
                    sender.sendMessage(Vars.getColor(s));
                }
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        } else {
            String sites_voto = Vars.getString("text.vote");
            assert sites_voto != null;
            String[] sites_lista = sites_voto.split("/split/");
            for (String s : sites_lista) {
                Bukkit.getConsoleSender().sendMessage(Vars.getColor(s));
            }
        }
        return true;
    }
}