package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.CVar;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.configs.methods.PlayerMessage;
import br.com.eterniaserver.modules.teleportsmanager.sql.QueriesW;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Warp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("eternia.warp." + args[0].toLowerCase())) {
                    final Location location = QueriesW.getWarp(args[0].toLowerCase());
                    if (location != Vars.error) {
                        if (player.hasPermission("eternia.timing.bypass")) {
                            player.teleport(location);
                            new PlayerMessage("warps.warp", args[0], player);
                        } else {
                            new PlayerMessage("teleport.timing", CVar.getInt("server.cooldown"), player);
                            Vars.playerposition.put(player.getName(), player.getLocation());
                            Vars.moved.put(player.getName(), false);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                            {
                                if (!Vars.moved.get(player.getName())) {
                                    player.teleport(location);
                                    new PlayerMessage("warps.warp", args[0], player);
                                } else {
                                    new PlayerMessage("warps.move", player);
                                }
                            }, 20 * CVar.getInt("server.cooldown"));
                        }
                    } else {
                        new PlayerMessage("warps.noexist", args[0], player);
                    }
                } else {
                    new PlayerMessage("server.no-perm", player);
                }
            } else {
                new PlayerMessage("warps.use", player);
            }
        } else {
            new ConsoleMessage("server.only-player");
        }
        return true;
    }
}
