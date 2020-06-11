package br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.eternialib.sql.Queries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.TeleportsManager;
import br.com.eterniaserver.eterniaserver.player.PlayerTeleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import io.papermc.lib.PaperLib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class WarpSystem extends BaseCommand {

    private final EterniaServer plugin;
    private final Messages messages;
    private final TeleportsManager teleportsManager;
    private final Vars vars;
    private final Strings strings;

    public WarpSystem(EterniaServer plugin, TeleportsManager teleportsManager) {
        this.plugin = plugin;
        this.messages = plugin.getMessages();
        this.teleportsManager = teleportsManager;
        this.vars = plugin.getVars();
        this.strings = plugin.getStrings();
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
                    if (location != vars.error) vars.teleports.put(player, new PlayerTeleport(player, location, "teleport.warp.done", plugin));
                    else messages.sendMessage("teleport.spawn.no-exists", player);
                }
            } else {
                if (player.hasPermission("eternia.spawn.other")) {
                    if (location != vars.error) {
                        PaperLib.teleportAsync(target.getPlayer(), location);
                        messages.sendMessage("teleport.warp.done", "%warp_name%", "Spawn", player);
                        messages.sendMessage("teleport.spawn.tp-target", "%target_name%", target.getPlayer().getName(), player);
                    } else {
                        messages.sendMessage("teleport.spawn.no-exists", player);
                    }
                } else {
                    messages.sendMessage("server.no-perm", player);
                }
            }
        });
    }

    @CommandAlias("shop|loja")
    @Syntax("<jogador>")
    @CommandCompletion("@players")
    @CommandPermission("eternia.shop.player")
    public void onShop(Player player, @Optional String target) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
           if (target == null) {
               final Location location = teleportsManager.getWarp("shop");
               if (player.hasPermission("eternia.warp.shop")) {
                   if (location != vars.error) {
                       if (vars.teleports.containsKey(player)) messages.sendMessage("server.telep", player);
                       else vars.teleports.put(player, new PlayerTeleport(player, location, "teleport.warp.done", plugin));
                   } else {
                       messages.sendMessage("teleport.warp.no-exists", "%warp_name%", "shop", player);
                   }
               } else {
                   messages.sendMessage("server.no-perm", player);
               }
           } else {
               final Location location = teleportsManager.getShop(target);
               if (location != vars.error) {
                   if (vars.teleports.containsKey(player)) messages.sendMessage("server.telep", player);
                   else vars.teleports.put(player, new PlayerTeleport(player, location, "teleport.shop.done", plugin));
               } else {
                   messages.sendMessage("teleport.shop.no-exists", player);
               }
           }
        });
    }

    @CommandAlias("setspawn")
    @CommandPermission("eternia.setspawn")
    public void onSetSpawn(Player player) {
        teleportsManager.setWarp(player.getLocation(), "spawn");
        messages.sendMessage("teleport.spawn.created", player);
    }

    @CommandAlias("setshop|setloja")
    @CommandPermission("eternia.setshop")
    public void onSetShop(Player player) {
        teleportsManager.setShop(player.getLocation(), player.getName().toLowerCase());
        messages.sendMessage("teleport.shop.created", player);
    }

    @CommandAlias("setwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.setwarp")
    public void onSetWarp(Player player, String nome) {
        teleportsManager.setWarp(player.getLocation(), nome.toLowerCase());
        messages.sendMessage("teleport.warp.created", "%warp_name%", nome, player);
    }

    @CommandAlias("delwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.delwarp")
    public void onDelWarp(Player player, String nome) {
        if (teleportsManager.existWarp(nome.toLowerCase())) {
            teleportsManager.delWarp(nome.toLowerCase());
            messages.sendMessage("teleport.warp.deleted", player);
        } else {
            messages.sendMessage("teleport.warp.no-exists", "%warp_name%", nome.toLowerCase(), player);
        }
    }

    @CommandAlias("listwarp|warplist|warps")
    @CommandPermission("eternia.listwarp")
    public void onListWarp(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-warp") + ";";
            final List<String> lista = Queries.queryStringList(querie, "name");
            StringBuilder string = new StringBuilder();
            for (String home : lista) string.append(home).append("&8").append(", &3");
            messages.sendMessage("teleport.warp.list", "%warps%", strings.getColor(string.substring(0, string.length() - 1)), player);
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
                    if (vars.teleports.containsKey(player)) messages.sendMessage("server.telep", player);
                    else vars.teleports.put(player, new PlayerTeleport(player, location, "teleport.warp.done", plugin));
                } else {
                    messages.sendMessage("teleport.warp.no-exists", "%warp_name%", nome, player);
                }
            });
        } else {
            messages.sendMessage("server.no-perm", player);
        }
    }

}
