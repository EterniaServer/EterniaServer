package br.com.eterniaserver.modules.homesmanager.sql;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Queries {

    public static void setHome(Location loc, String home, String jogador) {
        Vars.warps.remove(home);
        Vars.warps.put(home, loc);
        boolean t = false;
        StringBuilder result = new StringBuilder();
        String[] values;
        String valor = "";
        try {
            final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-home") + " WHERE name='" + jogador + "';";
            final ResultSet rsn = EterniaServer.sqlcon.Query(querie);
            if (rsn.next()) {
                rsn.getString("homes");
            }
            values = rsn.getString("homes").split(":");
            valor = rsn.getString("homes");
            for (String line : values) {
                if (line.equals(home)) {
                    t = true;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!t) {
            result.append(valor).append(home).append(":");
            String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-home") + " SET homes='" + result + "' WHERE name='" + jogador + "';";
            EterniaServer.sqlcon.Update(querie);
            String saveloc = Objects.requireNonNull(loc.getWorld()).getName() + ":" + ((int) loc.getX()) + ":" +
                    ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
            querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-homes") + " (name, location) VALUES ('" + home + "." + jogador + "', '" + saveloc + "')";
            EterniaServer.sqlcon.Update(querie);
        } else {
            String saveloc = Objects.requireNonNull(loc.getWorld()).getName() + ":" + ((int) loc.getX()) + ":" +
                    ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
            final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-homes") + " SET location='" + saveloc + "' WHERE name='" + home + "." + jogador + "';";
            EterniaServer.sqlcon.Update(querie);
        }
        final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-home") + " SET many='" + (canHome(jogador) + 1) + "' WHERE name='" + jogador + "';";
        EterniaServer.sqlcon.Update(querie);

    }

    public static void delHome(String home, String jogador) {
        Vars.homes.remove(home);
        try {
            String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-home") + " WHERE name='" + home + "';";
            final ResultSet rsd = EterniaServer.sqlcon.Query(querie);
            if (rsd.next()) {
                rsd.getString("homes");
            }
            final String[] values = rsd.getString("homes").split(":");
            StringBuilder nova = new StringBuilder();
            for (String line : values) {
                if (!line.equals(home)) {
                    nova.append(line).append(":");
                }
            }
            querie = "UPDATE " + EterniaServer.configs.getString("sql.table-home") + " SET homes='" + nova + "', SET many='" + (canHome(jogador) - 1) + "' WHERE name='" + jogador + "';";
            EterniaServer.sqlcon.Update(querie);
            querie = "DELETE FROM " + EterniaServer.configs.getString("sql.table-homes") + " WHERE name='" + home + "." + jogador + "';";
            EterniaServer.sqlcon.Update(querie);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Location getHome(String home, String jogador) {
        Location loc = Vars.error;
        if (Vars.homes.containsKey(home)) {
            loc = Vars.homes.get(home);
        } else {
            try {
                if (existHome(home, jogador)) {
                    try {
                        final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-homes") + " WHERE name='" + home + "." + jogador + "';";
                        final ResultSet rss = EterniaServer.sqlcon.Query(querie);
                        if (rss.next()) {
                            rss.getString("location");
                        }
                        String[] values = rss.getString("location").split(":");
                        loc = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                        Vars.homes.put(home, loc);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return loc;
    }

    public static boolean existHome(String home, String jogador) {
        try {
            final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-home") + " WHERE name='" + jogador + "';";
            final ResultSet rs = EterniaServer.sqlcon.Query(querie);
            if (rs.next()) {
                rs.getString("homes");
            }
            String[] values = rs.getString("homes").split(":");
            for (String line : values) {
                if (line.equals(home)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int canHome(String jogador) {
        int i = 0;
        try {
            final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-home") + " WHERE name='" + jogador + "';";
            final ResultSet rsv = EterniaServer.sqlcon.Query(querie);
            if (rsv.next()) {
                rsv.getInt("many");
            }
            i = rsv.getInt("many");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

}
