package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.CVar;
import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.configs.methods.PlayerMessage;
import br.com.eterniaserver.modules.teleportsmanager.sql.QueriesW;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            final Location location = QueriesW.getWarp("spawn");
            if (location != Vars.error) {
                if (args.length == 0) {
                    if (player.hasPermission("eternia.spawn")) {
                        if (player.hasPermission("eternia.timing.bypass")) {
                            player.teleport(location);
                            new PlayerMessage("warps.warp", "Spawn", player);
                        } else {
                            new PlayerMessage("teleport.timing", CVar.getInt("server.cooldown"), player);
                            Vars.playerposition.put(player.getName(), player.getLocation());
                            Vars.moved.put(player.getName(), false);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                            {
                                if (!Vars.moved.get(player.getName())) {
                                    player.teleport(location);
                                    new PlayerMessage("warps.warp", "Spawn", player);
                                } else {
                                    new PlayerMessage("warps.move", player);
                                }
                            }, 20 * CVar.getInt("server.cooldown"));
                        }
                    } else {
                        new PlayerMessage("sem-permissao", player);
                    }
                } else if (args.length == 1) {
                    if (player.hasPermission("eternia.spawn.other")) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null && target.isOnline()) {
                            target.teleport(location);
                            new PlayerMessage("warps.warp", "Spawn", player);
                            new PlayerMessage("warps.spawn-other", target.getName(), player);
                        } else {
                            new PlayerMessage("server.player-offline", player);
                        }
                    } else {
                        new PlayerMessage("server.no-perm", player);
                    }
                } else {
                    new PlayerMessage("warps.spawn-use", player);
                }
            } else {
                new PlayerMessage("warps.spawnno", "Spawn", player);
            }
        } else {
            new ConsoleMessage("server.only-player");
        }
        return true;
    }
}