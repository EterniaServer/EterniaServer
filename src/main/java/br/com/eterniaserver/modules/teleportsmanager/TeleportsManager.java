package br.com.eterniaserver.modules.teleportsmanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.teleportsmanager.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class TeleportsManager {

    public TeleportsManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.teleports")) {
            Objects.requireNonNull(plugin.getCommand("back")).setExecutor(new Back(plugin));
            Objects.requireNonNull(plugin.getCommand("spawn")).setExecutor(new Spawn(plugin));
            Objects.requireNonNull(plugin.getCommand("setspawn")).setExecutor(new SetSpawn());
            Objects.requireNonNull(plugin.getCommand("warp")).setExecutor(new Warp(plugin));
            Objects.requireNonNull(plugin.getCommand("setwarp")).setExecutor(new SetWarp());
            Objects.requireNonNull(plugin.getCommand("delwarp")).setExecutor(new DelWarp());
            Objects.requireNonNull(plugin.getCommand("listwarp")).setExecutor(new ListWarp(plugin));
            Objects.requireNonNull(plugin.getCommand("shop")).setExecutor(new Shop(plugin));
            Objects.requireNonNull(plugin.getCommand("setshop")).setExecutor(new SetShop());
            Objects.requireNonNull(plugin.getCommand("teleportaccept")).setExecutor(new TeleportAccept(plugin));
            Objects.requireNonNull(plugin.getCommand("teleportdeny")).setExecutor(new TeleportDeny());
            Objects.requireNonNull(plugin.getCommand("teleporttoplayer")).setExecutor(new TeleportToPlayer());
            Objects.requireNonNull(plugin.getCommand("teleportall")).setExecutor(new TeleportAll());
            Messages.ConsoleMessage("modules.enable", "%module%", "Teleports");
        } else {
            Messages.ConsoleMessage("modules.disable", "%module%", "Teleports");
        }
    }

    public static void setWarp(Location loc, String warp) {
        Vars.warps.put(warp, loc);
        String saveloc = Objects.requireNonNull(loc.getWorld()).getName() + ":" + ((int) loc.getX()) + ":" +
                ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
        if (existWarp(warp)) {
            final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-warp") + " SET location='" + saveloc + "' WHERE name='" + warp + "';";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement setwarp = connection.prepareStatement(querie);
                setwarp.execute();
                setwarp.close();
            }, true);
        } else {
            final String querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-warp") + " (name, location) VALUES ('" + warp + "', '" + saveloc + "')";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement setwarp = connection.prepareStatement(querie);
                setwarp.execute();
                setwarp.close();
            }, true);
        }
    }

    public static void delWarp(String warp) {
        Vars.warps.remove(warp);
        final String querie = "DELETE FROM " + EterniaServer.configs.getString("sql.table-warp") + " WHERE name='" + warp + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement delwarp = connection.prepareStatement(querie);
            delwarp.execute();
            delwarp.close();
        }, true);
    }

    public static Location getWarp(String warp) {
        Location loc = Vars.error;
        if (Vars.warps.containsKey(warp)) {
            loc = Vars.warps.get(warp);
        } else {
            if (existWarp(warp)) {
                AtomicReference<String> string = new AtomicReference<>("");
                final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-warp") + " WHERE name='" + warp + "';";
                EterniaServer.connection.executeSQLQuery(connection -> {
                    PreparedStatement getwarp = connection.prepareStatement(querie);
                    ResultSet resultSet = getwarp.executeQuery();
                    if (resultSet.next() && resultSet.getString("location") != null) {
                        string.set(resultSet.getString("location"));
                    }
                });
                String[] values = string.toString().split(":");
                loc = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                Vars.warps.put(warp, loc);
            }
        }

        return loc;
    }

    public static boolean existWarp(String warp) {
        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-warp") + " WHERE name='" + warp + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement existwarp = connection.prepareStatement(querie);
            ResultSet resultSet = existwarp.executeQuery();
            if (resultSet.next() && resultSet.getString("name") != null) exist.set(true);
            resultSet.close();
            existwarp.close();
        });

        return exist.get();
    }

    public static void setShop(Location loc, String shop) {
        Vars.shops.put(shop, loc);
        String saveloc = Objects.requireNonNull(loc.getWorld()).getName() + ":" + ((int) loc.getX()) + ":" +
                ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
        if (existShop(shop)) {
            final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-shop") + " SET location='" + saveloc + "' WHERE name='" + shop + "';";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement setshop = connection.prepareStatement(querie);
                setshop.execute();
                setshop.close();
            }, true);
        } else {
            final String querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-shop") + " (name, location) VALUES ('" + shop + "', '" + saveloc + "')";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement setshop = connection.prepareStatement(querie);
                setshop.execute();
                setshop.close();
            }, true);
        }
    }

    public static Location getShop(String shop) {
        Location loc = Vars.error;
        if (Vars.shops.containsKey(shop)) {
            loc = Vars.shops.get(shop);
        } else {
            if (existShop(shop)) {
                AtomicReference<String> string = new AtomicReference<>("");
                final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-shop") + " WHERE name='" + shop + "';";
                EterniaServer.connection.executeSQLQuery(connection -> {
                    PreparedStatement getshop = connection.prepareStatement(querie);
                    ResultSet resultSet = getshop.executeQuery();
                    if (resultSet.next() && resultSet.getString("location") != null) {
                        string.set(resultSet.getString("location"));
                    }
                });
                String[] values = string.toString().split(":");
                loc = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                Vars.shops.put(shop, loc);
            }
        }
        return loc;
    }

    public static boolean existShop(String shop) {
        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-shop") + " WHERE name='" + shop + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement existshop = connection.prepareStatement(querie);
            ResultSet resultSet = existshop.executeQuery();
            if (resultSet.next() && resultSet.getString("name") != null) exist.set(true);
            resultSet.close();
            existshop.close();
        });

        return exist.get();
    }

}
