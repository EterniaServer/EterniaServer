package br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.TeleportsManager;
import br.com.eterniaserver.eterniaserver.player.PlayerTeleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Shop implements CommandExecutor {

    private final EterniaServer plugin;
    private final Messages messages;
    private final TeleportsManager teleportsManager;
    private final Vars vars;

    public Shop(EterniaServer plugin, Messages messages, TeleportsManager teleportsManager, Vars vars) {
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
                if (args.length == 0) {
                    final Location location = teleportsManager.getWarp("shop");
                    if (player.hasPermission("eternia.warp.shop")) {
                        if (location != vars.error) {
                            if (vars.teleports.containsKey(player)) {
                                messages.PlayerMessage("server.telep", player);
                            } else {
                                vars.teleports.put(player, new PlayerTeleport(player, location, "warps.warp", plugin));
                            }
                        } else {
                            messages.PlayerMessage("warps.noexist", "%warp_name%", "shop", player);
                        }
                    } else {
                        messages.PlayerMessage("server.no-perm", player);
                    }
                } else if (args.length == 1) {
                    final Location location = teleportsManager.getShop(args[0].toLowerCase());
                    if (player.hasPermission("eternia.shop.player")) {
                        if (location != vars.error) {
                            if (vars.teleports.containsKey(player)) {
                                messages.PlayerMessage("server.telep", player);
                            } else {
                                vars.teleports.put(player, new PlayerTeleport(player, location, "warps.shopp", plugin));
                            }
                        } else {
                            messages.PlayerMessage("warps.shopno", player);
                        }
                    } else {
                        messages.PlayerMessage("server.no-perm", player);
                    }
                } else {
                    messages.PlayerMessage("warps.shopuse", player);
                }
            });
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}