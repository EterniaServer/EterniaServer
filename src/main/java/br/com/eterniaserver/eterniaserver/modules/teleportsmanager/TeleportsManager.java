package br.com.eterniaserver.eterniaserver.modules.teleportsmanager;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands.*;

import co.aikar.commands.PaperCommandManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class TeleportsManager {

    private final EterniaServer plugin;

    public TeleportsManager(EterniaServer plugin) {
        this.plugin = plugin;

        final PaperCommandManager manager = plugin.getManager();
        final EFiles messages = plugin.getEFiles();

        if (plugin.serverConfig.getBoolean("modules.teleports")) {
            manager.registerCommand(new WarpSystem(plugin, this));
            manager.registerCommand(new TeleportSystem(plugin));
            messages.sendConsole("modules.enable", "%module%", "Teleports");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Teleports");
        }
    }

    public void setWarp(Location loc, String warp) {
        EterniaServer.warps.put(warp, loc);
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
        EterniaServer.warps.remove(warp);
        final String querie = "DELETE FROM " + plugin.serverConfig.getString("sql.table-warp") + " WHERE name='" + warp + "';";
        EQueries.executeQuery(querie);
    }

    public Location getWarp(String warp) {
        Location loc = plugin.error;
        if (EterniaServer.warps.containsKey(warp)) {
            loc = EterniaServer.warps.get(warp);
        } else {
            if (existWarp(warp)) {
                final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-warp") + " WHERE name='" + warp + "';";
                String[] values = EQueries.queryString(querie, "location").split(":");
                loc = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                EterniaServer.warps.put(warp, loc);
            }
        }

        return loc;
    }

    public boolean existWarp(String warp) {
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-warp") + " WHERE name='" + warp + "';";
        return EQueries.queryBoolean(querie, "name");
    }

    public void setShop(Location loc, String shop) {
        EterniaServer.shops.put(shop, loc);
        String saveloc = loc.getWorld().getName() +
                ":" + ((int) loc.getX()) +
                ":" + ((int) loc.getY()) +
                ":" + ((int) loc.getZ()) +
                ":" + ((int) loc.getYaw()) +
                ":" + ((int) loc.getPitch());
        if (existShop(shop)) {
            final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-shop") + " SET location='" + saveloc + "' WHERE name='" + shop + "';";
            EQueries.executeQuery(querie);
        } else {
            final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-shop") + " (name, location) VALUES ('" + shop + "', '" + saveloc + "')";
            EQueries.executeQuery(querie);
        }
    }

    public Location getShop(String shop) {
        Location loc = plugin.error;
        if (EterniaServer.shops.containsKey(shop)) {
            loc = EterniaServer.shops.get(shop);
        } else {
            if (existShop(shop)) {
                final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-shop") + " WHERE name='" + shop + "';";
                String[] values = EQueries.queryString(querie, "location").split(":");
                loc = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                EterniaServer.shops.put(shop, loc);
            }
        }

        return loc;
    }

    public boolean existShop(String shop) {
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-shop") + " WHERE name='" + shop + "';";
        return EQueries.queryBoolean(querie, "name");
    }

}
