package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.configs.methods.PlayerMessage;
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
                    new PlayerMessage("teleport.auto-deny", target.getName(), player);
                    new PlayerMessage("teleport.deny", target);
                    Vars.tpa_requests.remove(player.getName());
                } else {
                    new PlayerMessage("teleport.noask", player);
                }
            } else {
                new PlayerMessage("server.no-perm", player);
            }
        } else {
            new ConsoleMessage("server.only-player");
        }
        return true;
    }
}
