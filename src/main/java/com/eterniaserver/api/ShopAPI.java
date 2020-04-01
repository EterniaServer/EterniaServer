package com.eterniaserver.api;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ShopAPI {

    public static void setShop(Location loc, String shop) {
        Vars.warps.remove(shop);
        Vars.warps.put(shop, loc);
        String saveloc = Objects.requireNonNull(loc.getWorld()).getName() + ":" + ((int) loc.getX()) + ":" +
                ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
        if (existShop(shop)) {
            EterniaServer.sqlcon.Update("UPDATE shop SET location='" + saveloc + "' WHERE name='" + shop + "';");
        } else {
            EterniaServer.sqlcon.Update("INSERT INTO shop (name, location) VALUES ('" + shop + "', '" + saveloc + "')");
        }
    }

    public static Location getShop(String shop) {
        Location loc = Vars.error;
        if (Vars.warps.containsKey(shop)) {
            loc = Vars.warps.get(shop);
        } else {
            if (existShop(shop)) {
                try {
                    final ResultSet rs = EterniaServer.sqlcon.Query("SELECT * FROM shop WHERE name='" + shop + "';");
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

    private static boolean existShop(String shop) {
        try {
            final ResultSet rs = EterniaServer.sqlcon.Query("SELECT * FROM shop WHERE name='" + shop + "';");
            return rs.next() && rs.getString("name") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
