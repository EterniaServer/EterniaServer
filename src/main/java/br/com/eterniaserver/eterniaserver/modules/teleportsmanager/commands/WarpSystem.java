package br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.TeleportsManager;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

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
    private final EFiles eFiles;
    private final TeleportsManager teleportsManager;

    public WarpSystem(EterniaServer plugin, TeleportsManager teleportsManager) {
        this.plugin = plugin;
        this.eFiles = plugin.getEFiles();
        this.teleportsManager = teleportsManager;
    }

    @CommandAlias("spawn")
    @Syntax("<jogador>")
    @CommandPermission("eternia.spawn")
    public void onSpawn(Player player, @Optional OnlinePlayer target) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final Location location = teleportsManager.getWarp("spawn");
            if (target == null) {
                if (EterniaServer.teleports.containsKey(player)) {
                    eFiles.sendMessage("server.telep", player);
                } else {
                    if (location != EterniaServer.error) EterniaServer.teleports.put(player, new PlayerTeleport(player, location, "teleport.warp.done", plugin));
                    else eFiles.sendMessage("teleport.spawn.no-exists", player);
                }
            } else {
                if (player.hasPermission("eternia.spawn.other")) {
                    if (location != EterniaServer.error) {
                        PaperLib.teleportAsync(target.getPlayer(), location);
                        eFiles.sendMessage("teleport.warp.done", "%warp_name%", "Spawn", player);
                        eFiles.sendMessage("teleport.spawn.tp-target", "%target_name%", target.getPlayer().getName(), player);
                    } else {
                        eFiles.sendMessage("teleport.spawn.no-exists", player);
                    }
                } else {
                    eFiles.sendMessage("server.no-perm", player);
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
                   if (location != EterniaServer.error) {
                       if (EterniaServer.teleports.containsKey(player)) eFiles.sendMessage("server.telep", player);
                       else EterniaServer.teleports.put(player, new PlayerTeleport(player, location, "teleport.warp.done", plugin));
                   } else {
                       eFiles.sendMessage("teleport.warp.no-exists", "%warp_name%", "shop", player);
                   }
               } else {
                   eFiles.sendMessage("server.no-perm", player);
               }
           } else {
               final Location location = teleportsManager.getShop(target);
               if (location != EterniaServer.error) {
                   if (EterniaServer.teleports.containsKey(player)) eFiles.sendMessage("server.telep", player);
                   else EterniaServer.teleports.put(player, new PlayerTeleport(player, location, "teleport.shop.done", plugin));
               } else {
                   eFiles.sendMessage("teleport.shop.no-exists", player);
               }
           }
        });
    }

    @CommandAlias("setspawn")
    @CommandPermission("eternia.setspawn")
    public void onSetSpawn(Player player) {
        teleportsManager.setWarp(player.getLocation(), "spawn");
        eFiles.sendMessage("teleport.spawn.created", player);
    }

    @CommandAlias("setshop|setloja")
    @CommandPermission("eternia.setshop")
    public void onSetShop(Player player) {
        teleportsManager.setShop(player.getLocation(), player.getName().toLowerCase());
        eFiles.sendMessage("teleport.shop.created", player);
    }

    @CommandAlias("setwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.setwarp")
    public void onSetWarp(Player player, String nome) {
        teleportsManager.setWarp(player.getLocation(), nome.toLowerCase());
        eFiles.sendMessage("teleport.warp.created", "%warp_name%", nome, player);
    }

    @CommandAlias("delwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.delwarp")
    public void onDelWarp(Player player, String nome) {
        if (teleportsManager.existWarp(nome.toLowerCase())) {
            teleportsManager.delWarp(nome.toLowerCase());
            eFiles.sendMessage("teleport.warp.deleted", player);
        } else {
            eFiles.sendMessage("teleport.warp.no-exists", "%warp_name%", nome.toLowerCase(), player);
        }
    }

    @CommandAlias("listwarp|warplist|warps")
    @CommandPermission("eternia.listwarp")
    public void onListWarp(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-warp") + ";";
            final List<String> lista = EQueries.queryStringList(querie, "name");
            StringBuilder string = new StringBuilder();
            for (String home : lista) string.append(home).append("&8").append(", &3");
            eFiles.sendMessage("teleport.warp.list", "%warps%", eFiles.getColor(string.substring(0, string.length() - 1)), player);
        });
    }

    @CommandAlias("warp")
    @Syntax("<warp>")
    @CommandPermission("eternia.warp")
    public void onWarp(Player player, String nome) {
        if (player.hasPermission("eternia.warp." + nome.toLowerCase())) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                final Location location = teleportsManager.getWarp(nome.toLowerCase());
                if (location != EterniaServer.error) {
                    if (EterniaServer.teleports.containsKey(player)) eFiles.sendMessage("server.telep", player);
                    else EterniaServer.teleports.put(player, new PlayerTeleport(player, location, "teleport.warp.done", plugin));
                } else {
                    eFiles.sendMessage("teleport.warp.no-exists", "%warp_name%", nome, player);
                }
            });
        } else {
            eFiles.sendMessage("server.no-perm", player);
        }
    }

}
