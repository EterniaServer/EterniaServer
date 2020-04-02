package com.eterniaserver.modules.teleportsmanager.commands;

import com.eterniaserver.configs.MVar;
import com.eterniaserver.configs.Vars;
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
                                Vars.tpa_requests.put(target, player);
                                MVar.playerReplaceMessage("teleport.receiver", player.getName(), target);
                                MVar.playerReplaceMessage("teleport.send", target.getName(), player);
                            } else {
                                MVar.playerMessage("teleport.auto", player);
                            }
                        } else {
                            MVar.playerMessage("server.player-offline", player);
                        }
                    } catch (Exception e) {
                        MVar.playerMessage("server.player-offline", player);
                    }
                } else {
                    MVar.playerMessage("teleport.use", player);
                }
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }
}