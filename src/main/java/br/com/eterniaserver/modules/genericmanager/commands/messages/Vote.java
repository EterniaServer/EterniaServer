package br.com.eterniaserver.modules.genericmanager.commands.messages;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vote implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, java.lang.String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.vote")) {
                java.lang.String sites_voto = Strings.getMessage("text.vote");
                java.lang.String[] sites_lista = sites_voto.split("/split/");
                for (java.lang.String s : sites_lista) {
                    sender.sendMessage(Strings.getColor(s));
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            java.lang.String sites_voto = Strings.getMessage("text.vote");
            java.lang.String[] sites_lista = sites_voto.split("/split/");
            for (java.lang.String s : sites_lista) {
                Bukkit.getConsoleSender().sendMessage(Strings.getColor(s));
            }
        }
        return true;
    }
}