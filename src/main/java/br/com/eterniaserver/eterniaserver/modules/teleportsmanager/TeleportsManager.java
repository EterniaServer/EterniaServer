package br.com.eterniaserver.eterniaserver.modules.teleportsmanager;

import br.com.eterniaserver.eterniaserver.API.Money;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class TeleportsManager {

    private final EterniaServer plugin;
    private final Vars vars;

    public TeleportsManager(EterniaServer plugin, Messages messages, Strings strings, Vars vars, Money moneyx) {
        this.plugin = plugin;
        this.vars = vars;
        if (plugin.serverConfig.getBoolean("modules.teleports")) {
            plugin.getCommand("back").setExecutor(new Back(plugin, messages, moneyx, vars));
            plugin.getCommand("spawn").setExecutor(new Spawn(plugin, messages, this, vars));
            plugin.getCommand("setspawn").setExecutor(new SetSpawn(this, messages));
            plugin.getCommand("warp").setExecutor(new Warp(plugin, this, messages, vars));
            plugin.getCommand("setwarp").setExecutor(new SetWarp(this, messages));
            plugin.getCommand("delwarp").setExecutor(new DelWarp(this, messages));
            plugin.getCommand("listwarp").setExecutor(new ListWarp(plugin, messages, strings));
            plugin.getCommand("shop").setExecutor(new Shop(plugin, messages, this, vars));
            plugin.getCommand("setshop").setExecutor(new SetShop(this, messages));
            plugin.getCommand("teleportaccept").setExecutor(new TeleportAccept(plugin, messages, vars));
            plugin.getCommand("teleportdeny").setExecutor(new TeleportDeny(messages, vars));
            plugin.getCommand("teleporttoplayer").setExecutor(new TeleportToPlayer(messages, vars));
            plugin.getCommand("teleportall").setExecutor(new TeleportAll(messages));
            messages.sendConsole("modules.enable", "%module%", "Teleports");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Teleports");
        }
    }

    public void setWarp(Location loc, String warp) {
        vars.warps.put(warp, loc);
        String saveloc = loc.getWorld().getName() + ":" + ((int) loc.getX()) + ":" +
                ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
        if (existWarp(warp)) {
            final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-warp") + " SET location='" + saveloc + "' WHERE name='" + warp + "';";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement setwarp = connection.prepareStatement(querie);
                setwarp.execute();
                setwarp.close();
            }, true);
        } else {
            final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-warp") + " (name, location) VALUES ('" + warp + "', '" + saveloc + "')";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement setwarp = connection.prepareStatement(querie);
                setwarp.execute();
                setwarp.close();
            }, true);
        }
    }

    public void delWarp(String warp) {
        vars.warps.remove(warp);
        final String querie = "DELETE FROM " + plugin.serverConfig.getString("sql.table-warp") + " WHERE name='" + warp + "';";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement delwarp = connection.prepareStatement(querie);
            delwarp.execute();
            delwarp.close();
        }, true);
    }

    public Location getWarp(String warp) {
        Location loc = vars.error;
        if (vars.warps.containsKey(warp)) {
            loc = vars.warps.get(warp);
        } else {
            if (existWarp(warp)) {
                AtomicReference<String> string = new AtomicReference<>("");
                final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-warp") + " WHERE name='" + warp + "';";
                plugin.connections.executeSQLQuery(connection -> {
                    PreparedStatement getwarp = connection.prepareStatement(querie);
                    ResultSet resultSet = getwarp.executeQuery();
                    if (resultSet.next() && resultSet.getString("location") != null) {
                        string.set(resultSet.getString("location"));
                    }
                });
                String[] values = string.toString().split(":");
                loc = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                vars.warps.put(warp, loc);
            }
        }

        return loc;
    }

    public boolean existWarp(String warp) {
        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-warp") + " WHERE name='" + warp + "';";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement existwarp = connection.prepareStatement(querie);
            ResultSet resultSet = existwarp.executeQuery();
            if (resultSet.next() && resultSet.getString("name") != null) exist.set(true);
            resultSet.close();
            existwarp.close();
        });

        return exist.get();
    }

    public void setShop(Location loc, String shop) {
        vars.shops.put(shop, loc);
        String saveloc = loc.getWorld().getName() + ":" + ((int) loc.getX()) + ":" +
                ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
        if (existShop(shop)) {
            final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-shop") + " SET location='" + saveloc + "' WHERE name='" + shop + "';";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement setshop = connection.prepareStatement(querie);
                setshop.execute();
                setshop.close();
            }, true);
        } else {
            final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-shop") + " (name, location) VALUES ('" + shop + "', '" + saveloc + "')";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement setshop = connection.prepareStatement(querie);
                setshop.execute();
                setshop.close();
            }, true);
        }
    }

    public Location getShop(String shop) {
        Location loc = vars.error;
        if (vars.shops.containsKey(shop)) {
            loc = vars.shops.get(shop);
        } else {
            if (existShop(shop)) {
                AtomicReference<String> string = new AtomicReference<>("");
                final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-shop") + " WHERE name='" + shop + "';";
                plugin.connections.executeSQLQuery(connection -> {
                    PreparedStatement getshop = connection.prepareStatement(querie);
                    ResultSet resultSet = getshop.executeQuery();
                    if (resultSet.next() && resultSet.getString("location") != null) {
                        string.set(resultSet.getString("location"));
                    }
                });
                String[] values = string.toString().split(":");
                loc = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                vars.shops.put(shop, loc);
            }
        }

        return loc;
    }

    public boolean existShop(String shop) {
        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-shop") + " WHERE name='" + shop + "';";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement existshop = connection.prepareStatement(querie);
            ResultSet resultSet = existshop.executeQuery();
            if (resultSet.next() && resultSet.getString("name") != null) exist.set(true);
            resultSet.close();
            existshop.close();
        });

        return exist.get();
    }

}
