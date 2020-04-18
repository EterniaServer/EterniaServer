package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Checks;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.teleportsmanager.sql.WarpsAPI;
import br.com.eterniaserver.configs.Vars;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {

    private final EterniaServer plugin;

    public Spawn(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (Checks.isTp(player.getName())) {
                Messages.PlayerMessage("server.telep", player);
                return true;
            }
            final Location location = WarpsAPI.getWarp("spawn");
            if (location != Vars.error) {
                if (args.length == 0) {
                    if (player.hasPermission("eternia.spawn")) {
                        if (player.hasPermission("eternia.timing.bypass")) {
                            PaperLib.teleportAsync(player, location);
                            Messages.PlayerMessage("warps.warp", "Spawn", player);
                        } else {
                            Messages.PlayerMessage("teleport.timing", EterniaServer.configs.getInt("server.cooldown"), player);
                            Vars.playerposition.put(player.getName(), player.getLocation());
                            Vars.moved.put(player.getName(), false);
                            Vars.teleporting.put(player.getName(), System.currentTimeMillis());
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            {
                                if (!Vars.moved.get(player.getName())) {
                                    PaperLib.teleportAsync(player, location);
                                    Messages.PlayerMessage("warps.warp", "Spawn", player);
                                } else {
                                    Messages.PlayerMessage("warps.move", player);
                                }
                            }, 20 * EterniaServer.configs.getInt("server.cooldown"));
                        }
                    } else {
                        Messages.PlayerMessage("sem-permissao", player);
                    }
                } else if (args.length == 1) {
                    if (player.hasPermission("eternia.spawn.other")) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null && target.isOnline()) {
                            PaperLib.teleportAsync(target, location);
                            Messages.PlayerMessage("warps.warp", "Spawn", player);
                            Messages.PlayerMessage("warps.spawn-other", target.getName(), player);
                        } else {
                            Messages.PlayerMessage("server.player-offline", player);
                        }
                    } else {
                        Messages.PlayerMessage("server.no-perm", player);
                    }
                } else {
                    Messages.PlayerMessage("warps.spawn-use", player);
                }
            } else {
                Messages.PlayerMessage("warps.spawnno", "Spawn", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}