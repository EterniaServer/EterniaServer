package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.teleportsmanager.TeleportsManager;
import br.com.eterniaserver.player.PlayerTeleport;
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
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                final Location location = TeleportsManager.getWarp("spawn");
                if (location != Vars.error) {
                    if (args.length == 0) {
                        if (player.hasPermission("eternia.spawn")) {
                            if (Vars.teleports.containsKey(player)) {
                                Messages.PlayerMessage("server.telep", player);
                            } else {
                                Vars.teleports.put(player, new PlayerTeleport(player, location, "warps.warp"));
                            }
                        } else {
                            Messages.PlayerMessage("sem-permissao", player);
                        }
                    } else if (args.length == 1) {
                        if (player.hasPermission("eternia.spawn.other")) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (target != null && target.isOnline()) {
                                PaperLib.teleportAsync(target, location);
                                Messages.PlayerMessage("warps.warp", "%warp_name%", "Spawn", player);
                                Messages.PlayerMessage("warps.spawn-other", "%target_name%", target.getName(), player);
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
                    Messages.PlayerMessage("warps.spawnno", player);
                }
            });
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}