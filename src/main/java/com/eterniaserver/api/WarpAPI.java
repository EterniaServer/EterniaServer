package com.eterniaserver.api;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class WarpAPI {

    public static void setWarp(Location loc, String warp) {
        Vars.warps.remove(warp);
        Vars.warps.put(warp, loc);
        String saveloc = Objects.requireNonNull(loc.getWorld()).getName() + ":" + ((int) loc.getX()) + ":" +
                ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
        if (existWarp(warp)) {
            EterniaServer.sqlcon.Update("UPDATE warp SET location='" + saveloc + "' WHERE name='" + warp + "';");
        } else {
            EterniaServer.sqlcon.Update("INSERT INTO warp (name, location) VALUES ('" + warp + "', '" + saveloc + "')");
        }
    }

    public static void delWarp(String warp) {
        Vars.warps.remove(warp);
        EterniaServer.sqlcon.Update("DELETE FROM warp WHERE name='" + warp + "';");
    }

    public static Location getWarp(String warp) {
        Location loc = Vars.error;
        if (Vars.warps.containsKey(warp)) {
            loc = Vars.warps.get(warp);
        } else {
            if (existWarp(warp)) {
                try {
                    final ResultSet rs = EterniaServer.sqlcon.Query("SELECT * FROM warp WHERE name='" + warp + "';");
                    if (rs.next()) {
                        rs.getString("location");
                    }
                    String[] values = rs.getString("location").split(":");
                    loc = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return loc;
    }

    public static boolean existWarp(String warp) {
        try {
            final ResultSet rs = EterniaServer.sqlcon.Query("SELECT * FROM warp WHERE name='" + warp + "';");
            return rs.next() && rs.getString("name") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
