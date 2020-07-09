package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.utils.PlayerTeleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class WarpSystem extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles eFiles;

    public WarpSystem(EterniaServer plugin) {
        this.plugin = plugin;
        this.eFiles = plugin.getEFiles();

        String query = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-shop") + ";";
        HashMap<String, String> temp = EQueries.getMapString(query, "name", "location");

        AtomicInteger x = new AtomicInteger(0);
        temp.forEach((k, v) -> {
            final String[] split = v.split(":");
            final Location loc = new Location(Bukkit.getWorld(split[0]),
                    Double.parseDouble(split[1]),
                    (Double.parseDouble(split[2]) + 1),
                    Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]),
                    Float.parseFloat(split[5]));
            Vars.shops.put(k, loc);
            x.getAndIncrement();
            eFiles.sendConsole("server.load-data",  "%module%", "Shops", "%amount%", x.get());
        });

        query = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-warp") + ";";
        temp = EQueries.getMapString(query, "name", "location");

        x.set(0);
        temp.forEach((k, v) -> {
            final String[] split = v.split(":");
            final Location loc = new Location(Bukkit.getWorld(split[0]),
                    Double.parseDouble(split[1]),
                    (Double.parseDouble(split[2]) + 1),
                    Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]),
                    Float.parseFloat(split[5]));
            Vars.warps.put(k, loc);
            x.getAndIncrement();
            eFiles.sendConsole("server.load-data",  "%module%", "Warps", "%amount%", x.get());
        });

    }

    @CommandAlias("spawn")
    @Syntax("<jogador>")
    @CommandPermission("eternia.spawn")
    public void onSpawn(Player player, @Optional OnlinePlayer target) {
        final Location location = getWarp("spawn");
        if (target == null) {
                if (Vars.teleports.containsKey(player)) {
                    eFiles.sendMessage("server.telep", player);
                } else {
                    if (location != plugin.error) Vars.teleports.put(player, new PlayerTeleport(player, location, "teleport.warp.done", plugin));
                    else eFiles.sendMessage("teleport.spawn.no-exists", player);
                }
        } else {
            if (player.hasPermission("eternia.spawn.other")) {
                if (location != plugin.error) {
                    Vars.teleports.put(target.getPlayer(), new PlayerTeleport(target.getPlayer(), location, "teleport.warp.done", plugin));
                    eFiles.sendMessage("teleport.spawn.tp-target", "%target_name%", target.getPlayer().getName(), player);
                } else {
                    eFiles.sendMessage("teleport.spawn.no-exists", player);
                }
            } else {
                eFiles.sendMessage("server.no-perm", player);
            }
        }
    }

    @CommandAlias("shop|loja")
    @Syntax("<jogador>")
    @CommandCompletion("@players")
    @CommandPermission("eternia.shop.player")
    public void onShop(Player player, @Optional String target) {
        if (target == null) {
               final Location location = getWarp("shop");
               if (player.hasPermission("eternia.warp.shop")) {
                   if (location != plugin.error) {
                       if (Vars.teleports.containsKey(player)) eFiles.sendMessage("server.telep", player);
                       else Vars.teleports.put(player, new PlayerTeleport(player, location, "teleport.warp.done", plugin));
                   } else {
                       eFiles.sendMessage("teleport.warp.no-exists", "%warp_name%", "shop", player);
                   }
               } else {
                   eFiles.sendMessage("server.no-perm", player);
               }
        } else {
            final Location location = getShop(target);
            if (location != plugin.error) {
                if (Vars.teleports.containsKey(player)) {
                    eFiles.sendMessage("server.telep", player);
                }
                else {
                    Vars.teleports.put(player, new PlayerTeleport(player, location, "teleport.shop.done", plugin));
                }
            } else {
                eFiles.sendMessage("teleport.shop.no-exists", player);
            }
        }
    }

    @CommandAlias("setspawn")
    @CommandPermission("eternia.setspawn")
    public void onSetSpawn(Player player) {
        setWarp(player.getLocation(), "spawn");
        eFiles.sendMessage("teleport.spawn.created", player);
    }

    @CommandAlias("setshop|setloja")
    @CommandPermission("eternia.setshop")
    public void onSetShop(Player player) {
        setShop(player.getLocation(), player.getName().toLowerCase());
        eFiles.sendMessage("teleport.shop.created", player);
    }

    @CommandAlias("setwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.setwarp")
    public void onSetWarp(Player player, String nome) {
        setWarp(player.getLocation(), nome.toLowerCase());
        eFiles.sendMessage("teleport.warp.created", "%warp_name%", nome, player);
    }

    @CommandAlias("delwarp")
    @Syntax("<warp>")
    @CommandPermission("eternia.delwarp")
    public void onDelWarp(Player player, String nome) {
        if (existWarp(nome.toLowerCase())) {
            delWarp(nome.toLowerCase());
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
            final Location location = getWarp(nome.toLowerCase());
            if (location != plugin.error) {
                if (Vars.teleports.containsKey(player)) {
                    eFiles.sendMessage("server.telep", player);
                } else {
                    Vars.teleports.put(player, new PlayerTeleport(player, location, "teleport.warp.done", plugin));
                }
            } else {
                eFiles.sendMessage("teleport.warp.no-exists", "%warp_name%", nome, player);
            }
        } else {
            eFiles.sendMessage("server.no-perm", player);
        }
    }

    public void setShop(Location loc, String shop) {
        Vars.shops.put(shop, loc);
        String saveloc = loc.getWorld().getName() +
                ":" + ((int) loc.getX()) +
                ":" + ((int) loc.getY()) +
                ":" + ((int) loc.getZ()) +
                ":" + ((int) loc.getYaw()) +
                ":" + ((int) loc.getPitch());
        CompletableFuture.runAsync(() -> {
            final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-shop") + " WHERE name='" + shop + "';";
            if (EQueries.queryBoolean(querie, "name")) {
                final String querieSave = "UPDATE " + plugin.serverConfig.getString("sql.table-shop") + " SET location='" + saveloc + "' WHERE name='" + shop + "';";
                EQueries.executeQuery(querieSave);
            } else {
                final String querieSave = "INSERT INTO " + plugin.serverConfig.getString("sql.table-shop") + " (name, location) VALUES ('" + shop + "', '" + saveloc + "')";
                EQueries.executeQuery(querieSave);
            }
        });
    }

    public Location getShop(String shop) {
        if (Vars.shops.containsKey(shop)) {
            return Vars.shops.get(shop);
        } else {
            return plugin.error;
        }
    }

    public void setWarp(Location loc, String warp) {
        Vars.warps.put(warp, loc);
        String saveloc = loc.getWorld().getName() +
                ":" + ((int) loc.getX()) +
                ":" + ((int) loc.getY()) +
                ":" + ((int) loc.getZ()) +
                ":" + ((int) loc.getYaw()) +
                ":" + ((int) loc.getPitch());
        if (existWarp(warp)) {
            final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-warp") + " SET location='" + saveloc + "' WHERE name='" + warp + "';";
            EQueries.executeQuery(querie);
        } else {
            final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-warp") + " (name, location) VALUES ('" + warp + "', '" + saveloc + "')";
            EQueries.executeQuery(querie);
        }
    }

    public void delWarp(String warp) {
        Vars.warps.remove(warp);
        final String querie = "DELETE FROM " + plugin.serverConfig.getString("sql.table-warp") + " WHERE name='" + warp + "';";
        EQueries.executeQuery(querie);
    }

    public Location getWarp(String warp) {
        if (Vars.warps.containsKey(warp)) {
            return Vars.warps.get(warp);
        } else {
            return plugin.error;
        }
    }

    public boolean existWarp(String warp) {
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-warp") + " WHERE name='" + warp + "';";
        return EQueries.queryBoolean(querie, "name");
    }

}
