package br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.TeleportsManager;
import br.com.eterniaserver.eterniaserver.player.PlayerTeleport;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WarpSystem extends BaseCommand {

    private final EterniaServer plugin;
    private final Messages messages;
    private final TeleportsManager teleportsManager;
    private final Vars vars;
    private final Strings strings;

    public WarpSystem(EterniaServer plugin, Messages messages, TeleportsManager teleportsManager, Vars vars, Strings strings) {
        this.plugin = plugin;
        this.messages = messages;
        this.teleportsManager = teleportsManager;
        this.vars = vars;
        this.strings = strings;
    }

    @CommandAlias("spawn")
    @Syntax("<jogador>")
    @CommandPermission("eternia.spawn")
    public void onSpawn(Player player, @Optional OnlinePlayer target) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final Location location = teleportsManager.getWarp("spawn");
            if (target == null) {
                if (vars.teleports.containsKey(player)) {
                    messages.sendMessage("server.telep", player);
                } else {
                    vars.teleports.put(player, new PlayerTeleport(player, location, "warps.warp", plugin));
                }
            } else {
                if (player.hasPermission("eternia.spawn.other")) {
                    PaperLib.teleportAsync(target.getPlayer(), location);
                    messages.sendMessage("warps.warp", "%warp_name%", "Spawn", player);
                    messages.sendMessage("warps.spawn-other", "%target_name%", target.getPlayer().getName(), player);
                } else {
                    messages.sendMessage("server.no-perm", player);
                }
            }
        });
    }

    @CommandAlias("shop|loja")
    @Syntax("<jogador>")
    @CommandPermission("eternia.shop.player")
    public void onShop(Player player, @Optional String target) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
           if (target == null) {
               final Location location = teleportsManager.getWarp("shop");
               if (player.hasPermission("eternia.warp.shop")) {
                   if (location != vars.error) {
                       if (vars.teleports.containsKey(player)) {
                           messages.sendMessage("server.telep", player);
                       } else {
                           vars.teleports.put(player, new PlayerTeleport(player, location, "warps.warp", plugin));
                       }
                   } else {
                       messages.sendMessage("warps.noexist", "%warp_name%", "shop", player);
                   }
               } else {
                   messages.sendMessage("server.no-perm", player);
               }
           } else {
               final Location location = teleportsManager.getShop(target);
               if (location != vars.error) {
                   if (vars.teleports.containsKey(player)) {
                       messages.sendMessage("server.telep", player);
                   } else {
                       vars.teleports.put(player, new PlayerTeleport(player, location, "warps.shopp", plugin));
                   }
               } else {
                   messages.sendMessage("warps.shopno", player);
               }
           }
        });
    }

    @CommandAlias("setspawn")
    @CommandPermission("eternia.setspawn")
    public void onSetSpawn(Player player) {
        teleportsManager.setWarp(player.getLocation(), "spawn");
        messages.sendMessage("warps.spawn-set", player);
    }

    @CommandAlias("setshop|setloja")
    @CommandPermission("eternia.setshop")
    public void onSetShop(Player player) {
        teleportsManager.setShop(player.getLocation(), player.getName().toLowerCase());
        messages.sendMessage("warps.shopd", player);
    }

    @CommandAlias("setwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.setwarp")
    public void onSetWarp(Player player, String nome) {
        teleportsManager.setWarp(player.getLocation(), nome.toLowerCase());
        messages.sendMessage("warps.createwarp", "%warp_name%", nome, player);
    }

    @CommandAlias("delwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.delwarp")
    public void onDelWarp(Player player, String nome) {
        if (teleportsManager.existWarp(nome.toLowerCase())) {
            teleportsManager.delWarp(nome.toLowerCase());
            messages.sendMessage("warps.delwarp", player);
        } else {
            messages.sendMessage("warps.noexist", "%warp_name%", nome.toLowerCase(), player);
        }
    }

    @CommandAlias("listwarp|warplist")
    @CommandPermission("eternia.listwarp")
    public void onListWarp(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-warp") + ";";
            StringBuilder accounts = new StringBuilder();
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement getHome = connection.prepareStatement(querie);
                ResultSet resultSet = getHome.executeQuery();
                while (resultSet.next()) {
                    final String warpname = resultSet.getString("name");
                    accounts.append(warpname).append("&8, &3");
                }
                resultSet.close();
                getHome.close();
            });
            messages.sendMessage("warps.list", "%warps%", strings.getColor(accounts.toString()), player);
        });
    }

    @CommandAlias("warp")
    @Syntax("<warp>")
    @CommandPermission("eternia.warp")
    public void onWarp(Player player, String nome) {
        if (player.hasPermission("eternia.warp." + nome.toLowerCase())) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                final Location location = teleportsManager.getWarp(nome.toLowerCase());
                if (location != vars.error) {
                    if (vars.teleports.containsKey(player)) {
                        messages.sendMessage("server.telep", player);
                    } else {
                        vars.teleports.put(player, new PlayerTeleport(player, location, "warps.warp", plugin));
                    }
                } else {
                    messages.sendMessage("warps.noexist", "%warp_name%", nome, player);
                }
            });
        } else {
            messages.sendMessage("server.no-perm", player);
        }
    }

}
