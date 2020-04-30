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
    private final Messages messages;
    private final TeleportsManager teleportsManager;
    private final Vars vars;

    public Spawn(EterniaServer plugin, Messages messages, TeleportsManager teleportsManager, Vars vars) {
        this.plugin = plugin;
        this.messages = messages;
        this.teleportsManager = teleportsManager;
        this.vars = vars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                final Location location = teleportsManager.getWarp("spawn");
                if (location != vars.error) {
                    if (args.length == 0) {
                        if (player.hasPermission("eternia.spawn")) {
                            if (vars.teleports.containsKey(player)) {
                                messages.PlayerMessage("server.telep", player);
                            } else {
                                vars.teleports.put(player, new PlayerTeleport(player, location, "warps.warp", plugin));
                            }
                        } else {
                            messages.PlayerMessage("sem-permissao", player);
                        }
                    } else if (args.length == 1) {
                        if (player.hasPermission("eternia.spawn.other")) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (target != null && target.isOnline()) {
                                PaperLib.teleportAsync(target, location);
                                messages.PlayerMessage("warps.warp", "%warp_name%", "Spawn", player);
                                messages.PlayerMessage("warps.spawn-other", "%target_name%", target.getName(), player);
                            } else {
                                messages.PlayerMessage("server.player-offline", player);
                            }
                        } else {
                            messages.PlayerMessage("server.no-perm", player);
                        }
                    } else {
                        messages.PlayerMessage("warps.spawn-use", player);
                    }
                } else {
                    messages.PlayerMessage("warps.spawnno", player);
                }
            });
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}