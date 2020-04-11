package br.com.eterniaserver.modules.teleportsmanager.sql;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class QueriesP {

    public static void setShop(Location loc, String shop) {
        Vars.shops.remove(shop);
        Vars.shops.put(shop, loc);
        String saveloc = Objects.requireNonNull(loc.getWorld()).getName() + ":" + ((int) loc.getX()) + ":" +
                ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
        if (existShop(shop)) {
            final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-shop") + " SET location='" + saveloc + "' WHERE name='" + shop + "';";
            EterniaServer.sqlcon.Update(querie);
        } else {
            final String querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-shop") + " (name, location) VALUES ('" + shop + "', '" + saveloc + "')";
            EterniaServer.sqlcon.Update(querie);
        }
    }

    public static Location getShop(String shop) {
        Location loc = Vars.error;
        if (Vars.shops.containsKey(shop)) {
            loc = Vars.shops.get(shop);
        } else {
            if (existShop(shop)) {
                try {
                    final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-shop") + " WHERE name='" + shop + "';";
                    final ResultSet rs = EterniaServer.sqlcon.Query(querie);
                    if (rs.next()) {
                        rs.getString("location");
                    }
                    String[] values = rs.getString("location").split(":");
                    loc = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                    Vars.shops.put(shop, loc);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return loc;
    }

    private static boolean existShop(String shop) {
        try {
            final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-shop") + " WHERE name='" + shop + "';";
            final ResultSet rs = EterniaServer.sqlcon.Query(querie);
            return rs.next() && rs.getString("name") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
