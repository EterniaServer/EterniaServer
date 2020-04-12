package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportDeny implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tpa")) {
                if (Vars.tpa_requests.containsKey(player.getName())) {
                    Player target = Bukkit.getPlayer(Vars.tpa_requests.get(player.getName()));
                    assert target != null;
                    Messages.PlayerMessage("teleport.auto-deny", target.getName(), player);
                    Messages.PlayerMessage("teleport.deny", target);
                    Vars.tpa_requests.remove(player.getName());
                } else {
                    Messages.PlayerMessage("teleport.noask", player);
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }

}
