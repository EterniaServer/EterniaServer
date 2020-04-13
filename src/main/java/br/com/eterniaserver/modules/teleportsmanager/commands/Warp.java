package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Checks;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.teleportsmanager.sql.QueriesW;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Warp implements CommandExecutor {

    private final EterniaServer plugin;

    public Warp(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("eternia.warp." + args[0].toLowerCase())) {
                    if (Checks.isTp(player.getName())) {
                        Messages.PlayerMessage("server.telep", player);
                        return true;
                    }
                    final Location location = QueriesW.getWarp(args[0].toLowerCase());
                    if (location != Vars.error) {
                        if (player.hasPermission("eternia.timing.bypass")) {
                            player.teleport(location);
                            Messages.PlayerMessage("warps.warp", args[0], player);
                        } else {
                            Messages.PlayerMessage("teleport.timing", EterniaServer.configs.getInt("server.cooldown"), player);
                            Vars.playerposition.put(player.getName(), player.getLocation());
                            Vars.moved.put(player.getName(), false);
                            Vars.teleporting.put(player.getName(), System.currentTimeMillis());
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            {
                                if (!Vars.moved.get(player.getName())) {
                                    player.teleport(location);
                                    Messages.PlayerMessage("warps.warp", args[0], player);
                                } else {
                                    Messages.PlayerMessage("warps.move", player);
                                }
                            }, 20 * EterniaServer.configs.getInt("server.cooldown"));
                        }
                    } else {
                        Messages.PlayerMessage("warps.noexist", args[0], player);
                    }
                } else {
                    Messages.PlayerMessage("server.no-perm", player);
                }
            } else {
                Messages.PlayerMessage("warps.use", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }

}
