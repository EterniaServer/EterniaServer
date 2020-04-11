package br.com.eterniaserver.modules.teleportsmanager.sql;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class QueriesW {

    public static void setWarp(Location loc, String warp) {
        Vars.warps.remove(warp);
        Vars.warps.put(warp, loc);
        String saveloc = Objects.requireNonNull(loc.getWorld()).getName() + ":" + ((int) loc.getX()) + ":" +
                ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
        if (existWarp(warp)) {
            final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-warp") + " SET location='" + saveloc + "' WHERE name='" + warp + "';";
            EterniaServer.connection.Update(querie);
        } else {
            final String querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-warp") + " (name, location) VALUES ('" + warp + "', '" + saveloc + "')";
            EterniaServer.connection.Update(querie);
        }
    }

    public static void delWarp(String warp) {
        Vars.warps.remove(warp);
        final String querie = "DELETE FROM " + EterniaServer.configs.getString("sql.table-warp") + " WHERE name='" + warp + "';";
        EterniaServer.connection.Update(querie);
    }

    public static Location getWarp(String warp) {
        Location loc = Vars.error;
        if (Vars.warps.containsKey(warp)) {
            loc = Vars.warps.get(warp);
        } else {
            if (existWarp(warp)) {
                try {
                    final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-warp") + " WHERE name='" + warp + "';";
                    final ResultSet rs = EterniaServer.connection.Query(querie);
                    if (rs.next()) {
                        rs.getString("location");
                    }
                    String[] values = rs.getString("location").split(":");
                    loc = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                    Vars.warps.put(warp, loc);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return loc;
    }

    public static boolean existWarp(String warp) {
        try {
            final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-warp") + " WHERE name='" + warp + "';";
            final ResultSet rs = EterniaServer.connection.Query(querie);
            return rs.next() && rs.getString("name") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
