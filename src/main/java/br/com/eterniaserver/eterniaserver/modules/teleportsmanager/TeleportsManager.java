package br.com.eterniaserver.eterniaserver.modules.teleportsmanager;

import br.com.eterniaserver.eterniaserver.API.Money;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands.*;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class TeleportsManager {

    private final EterniaServer plugin;
    private final Vars vars;

    public TeleportsManager(EterniaServer plugin, Messages messages, Strings strings, Vars vars, Money moneyx, PaperCommandManager manager) {
        this.plugin = plugin;
        this.vars = vars;
        if (plugin.serverConfig.getBoolean("modules.teleports")) {
            manager.registerCommand(new WarpSystem(plugin, messages, this, vars, strings));
            manager.registerCommand(new TeleportSystem(plugin, messages, moneyx, vars));
            messages.sendConsole("modules.enable", "%module%", "Teleports");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Teleports");
        }
    }

    public void setWarp(Location loc, String warp) {
        vars.warps.put(warp, loc);
        String saveloc = loc.getWorld().getName() +
                ":" + ((int) loc.getX()) +
                ":" + ((int) loc.getY()) +
                ":" + ((int) loc.getZ()) +
                ":" + ((int) loc.getYaw()) +
                ":" + ((int) loc.getPitch());
        if (existWarp(warp)) {
            final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-warp") + " SET location='" + saveloc + "' WHERE name='" + warp + "';";
            plugin.executeQuery(querie);
        } else {
            final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-warp") + " (name, location) VALUES ('" + warp + "', '" + saveloc + "')";
            plugin.executeQuery(querie);
        }
    }

    public void delWarp(String warp) {
        vars.warps.remove(warp);
        final String querie = "DELETE FROM " + plugin.serverConfig.getString("sql.table-warp") + " WHERE name='" + warp + "';";
        plugin.executeQuery(querie);
    }

    public Location getWarp(String warp) {
        Location loc = vars.error;
        if (vars.warps.containsKey(warp)) {
            loc = vars.warps.get(warp);
        } else {
            if (existWarp(warp)) {
                final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-warp") + " WHERE name='" + warp + "';";
                String[] values = plugin.executeQueryString(querie, "location").toString().split(":");
                loc = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                vars.warps.put(warp, loc);
            }
        }

        return loc;
    }

    public boolean existWarp(String warp) {
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-warp") + " WHERE name='" + warp + "';";
        return plugin.executeQueryBoolean(querie, "name").get();
    }

    public void setShop(Location loc, String shop) {
        vars.shops.put(shop, loc);
        String saveloc = loc.getWorld().getName() +
                ":" + ((int) loc.getX()) +
                ":" + ((int) loc.getY()) +
                ":" + ((int) loc.getZ()) +
                ":" + ((int) loc.getYaw()) +
                ":" + ((int) loc.getPitch());
        if (existShop(shop)) {
            final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-shop") + " SET location='" + saveloc + "' WHERE name='" + shop + "';";
            plugin.executeQuery(querie);
        } else {
            final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-shop") + " (name, location) VALUES ('" + shop + "', '" + saveloc + "')";
            plugin.executeQuery(querie);
        }
    }

    public Location getShop(String shop) {
        Location loc = vars.error;
        if (vars.shops.containsKey(shop)) {
            loc = vars.shops.get(shop);
        } else {
            if (existShop(shop)) {
                final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-shop") + " WHERE name='" + shop + "';";
                String[] values = plugin.executeQueryString(querie, "location").toString().split(":");
                loc = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                vars.shops.put(shop, loc);
            }
        }

        return loc;
    }

    public boolean existShop(String shop) {
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-shop") + " WHERE name='" + shop + "';";
        return plugin.executeQueryBoolean(querie, "name").get();
    }

}
