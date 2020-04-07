package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.configs.methods.PlayerMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportToPlayer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tpa")) {
                if (args.length == 1) {
                    try {
                        Player target = Bukkit.getPlayer(args[0]);
                        assert target != null;
                        if (target.isOnline()) {
                            if (target != player) {
                                Vars.tpa_requests.remove(target.getName());
                                Vars.tpa_requests.put(target.getName(), player.getName());
                                new PlayerMessage("teleport.receiver", player.getName(), target);
                                new PlayerMessage("teleport.send", target.getName(), player);
                            } else {
                                new PlayerMessage("teleport.auto", player);
                            }
                        } else {
                            new PlayerMessage("server.player-offline", player);
                        }
                    } catch (Exception e) {
                        new PlayerMessage("server.player-offline", player);
                    }
                } else {
                    new PlayerMessage("teleport.use", player);
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