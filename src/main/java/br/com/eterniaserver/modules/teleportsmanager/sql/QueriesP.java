package br.com.eterniaserver.modules.teleportsmanager.sql;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class QueriesP {

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

    private static boolean existShop(String shop) {
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
