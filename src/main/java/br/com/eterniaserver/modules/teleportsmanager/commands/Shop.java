package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.methods.Checks;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.teleportsmanager.TeleportsManager;

import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Shop implements CommandExecutor {

    private final EterniaServer plugin;

    public Shop(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                final Location location = TeleportsManager.getWarp("shop");
                if (player.hasPermission("eternia.warp.shop")) {
                    if (Checks.isTp(player.getName())) {
                        Messages.PlayerMessage("server.telep", player);
                        return true;
                    }
                    if (location != Vars.error) {
                        if (player.hasPermission("eternia.timing.bypass")) {
                            PaperLib.teleportAsync(player, location);
                            Messages.PlayerMessage("warps.warp", "%warp_name%", "Loja", player);
                        } else {
                            Messages.PlayerMessage("teleport.timing", "%cooldown%", EterniaServer.configs.getInt("server.cooldown"), player);
                            Vars.playerposition.put(player.getName(), player.getLocation());
                            Vars.teleporting.put(player.getName(), System.currentTimeMillis());
                            Vars.moved.put(player.getName(), false);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            {
                                if (!Vars.moved.get(player.getName())) {
                                    PaperLib.teleportAsync(player, location);
                                    Messages.PlayerMessage("warps.warp", "%warp_name%", "Loja", player);
                                } else {
                                    Messages.PlayerMessage("warps.move", player);
                                }
                            }, 20 * EterniaServer.configs.getInt("server.cooldown"));
                        }
                    } else {
                        Messages.PlayerMessage("warps.noexist", "%warp_name%", "shop", player);
                    }
                } else {
                    Messages.PlayerMessage("server.no-perm", player);
                }
            } else if (args.length == 1) {
                final Location location = TeleportsManager.getShop(args[0].toLowerCase());
                if (player.hasPermission("eternia.shop.player")) {
                    if (location != Vars.error) {
                        if (player.hasPermission("eternia.timing.bypass")) {
                            PaperLib.teleportAsync(player, location);
                            Messages.PlayerMessage("warps.shopp", "%player_name%", args[0], player);
                        } else {
                            Messages.PlayerMessage("teleport.timing", "%cooldown%", EterniaServer.configs.getInt("server.cooldown"), player);
                            Vars.playerposition.put(player.getName(), player.getLocation());
                            Vars.teleporting.put(player.getName(), System.currentTimeMillis());
                            Vars.moved.put(player.getName(), false);
                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            {
                                if (!Vars.moved.get(player.getName())) {
                                    PaperLib.teleportAsync(player, location);
                                    Messages.PlayerMessage("warps.shopp", "%player_name%", args[0], player);
                                } else {
                                    Messages.PlayerMessage("warps.move", player);
                                }
                            }, 20 * EterniaServer.configs.getInt("server.cooldown"));
                        }
                    } else {
                        Messages.PlayerMessage("warps.shopno", player);
                    }
                } else {
                    Messages.PlayerMessage("server.no-perm", player);
                }
            } else {
                Messages.PlayerMessage("warps.shopuse", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}