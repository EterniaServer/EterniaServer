package br.com.eterniaserver.eterniaserver.modules.teleportsmanager;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands.*;

import co.aikar.commands.PaperCommandManager;

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
        plugin.getWarps().put(warp, loc);
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
        plugin.getWarps().remove(warp);
        final String querie = "DELETE FROM " + plugin.serverConfig.getString("sql.table-warp") + " WHERE name='" + warp + "';";
        EQueries.executeQuery(querie);
    }

    public Location getWarp(String warp) {
        if (plugin.getWarps().containsKey(warp)) {
            return plugin.getWarps().get(warp);
        } else {
            return plugin.error;
        }
    }

    public boolean existWarp(String warp) {
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-warp") + " WHERE name='" + warp + "';";
        return EQueries.queryBoolean(querie, "name");
    }

    public void setShop(Location loc, String shop) {
        plugin.getShops().put(shop, loc);
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
        if (plugin.getShops().containsKey(shop)) {
            return plugin.getShops().get(shop);
        } else {
            return plugin.error;
        }
    }

    public boolean existShop(String shop) {
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-shop") + " WHERE name='" + shop + "';";
        return EQueries.queryBoolean(querie, "name");
    }

}
