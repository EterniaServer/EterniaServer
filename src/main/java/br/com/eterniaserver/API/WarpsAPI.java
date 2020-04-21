package br.com.eterniaserver.API;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class WarpsAPI {

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

}
