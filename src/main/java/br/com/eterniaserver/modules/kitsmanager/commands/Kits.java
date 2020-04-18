package br.com.eterniaserver.modules.kitsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kits implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.kits")) {
                Messages.PlayerMessage("kits.kits", Strings.getColor(EterniaServer.kits.getString("kits.nameofkits")), player);
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
