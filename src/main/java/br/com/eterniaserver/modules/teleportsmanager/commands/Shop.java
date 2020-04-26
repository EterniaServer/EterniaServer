package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.teleportsmanager.TeleportsManager;

import br.com.eterniaserver.player.PlayerTeleport;
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
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (args.length == 0) {
                    final Location location = TeleportsManager.getWarp("shop");
                    if (player.hasPermission("eternia.warp.shop")) {
                        if (location != Vars.error) {
                            if (Vars.teleports.containsKey(player)) {
                                Messages.PlayerMessage("server.telep", player);
                            } else {
                                Vars.teleports.put(player, new PlayerTeleport(player, location, "warps.warp"));
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
                            if (Vars.teleports.containsKey(player)) {
                                Messages.PlayerMessage("server.telep", player);
                            } else {
                                Vars.teleports.put(player, new PlayerTeleport(player, location, "warps.shopp"));
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
            });
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}