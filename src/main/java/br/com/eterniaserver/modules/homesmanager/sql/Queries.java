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
        Vars.homes.remove(home);
        Vars.homes.put(home, loc);
        boolean t = false;
        StringBuilder result = new StringBuilder();
        String[] values;
        String valor = "";
        try {
            String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-home") + " WHERE player_name='" + jogador + "';";
            ResultSet rsn = EterniaServer.connection.Query(querie);
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
            String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-home") + " SET homes='" + result + "' WHERE player_name='" + jogador + "';";
            EterniaServer.connection.Update(querie);
            String saveloc = Objects.requireNonNull(loc.getWorld()).getName() + ":" + ((int) loc.getX()) + ":" +
                    ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
            querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-homes") + " (name, location) VALUES ('" + home + "." + jogador + "', '" + saveloc + "')";
            EterniaServer.connection.Update(querie);
        } else {
            String saveloc = Objects.requireNonNull(loc.getWorld()).getName() + ":" + ((int) loc.getX()) + ":" +
                    ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
            String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-homes") + " SET location='" + saveloc + "' WHERE name='" + home + "." + jogador + "';";
            EterniaServer.connection.Update(querie);
        }
    }

    public static void delHome(String home, String jogador) {
        Vars.homes.remove(home + "." + jogador);
        StringBuilder nova = new StringBuilder();
        String[] values = getHomes(jogador);
        boolean t = true;
        for (String line : values) {
            if (!line.equals(home)) {
                nova.append(line).append(":");
                t = false;
            }
        }
        if (t) {
            String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-home") + " SET homes=':' WHERE player_name='" + jogador + "';";
            EterniaServer.connection.Update(querie);
        } else {
            String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-home") + " SET homes='" + nova + "' WHERE player_name='" + jogador + "';";
            EterniaServer.connection.Update(querie);
        }
        String querie = "DELETE FROM " + EterniaServer.configs.getString("sql.table-homes") + " WHERE name='" + home + "." + jogador + "';";
        EterniaServer.connection.Update(querie);
    }

    public static Location getHome(String home, String jogador) {
        Location loc = Vars.error;
        if (Vars.homes.containsKey(home + "." + jogador)) {
            loc = Vars.homes.get(home + "." + jogador);
        } else {
            try {
                if (existHome(home, jogador)) {
                    try {
                        String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-homes") + " WHERE name='" + home + "." + jogador + "';";
                        ResultSet rss = EterniaServer.connection.Query(querie);
                        if (rss.next()) {
                            rss.getString("location");
                        }
                        String[] values = rss.getString("location").split(":");
                        loc = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                        Vars.homes.put(home + "." + jogador, loc);
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
        String[] homes = getHomes(jogador);
        for (String line : homes) {
            if (line.equals(home)) {
                return true;
            }
        }
        return false;
    }

    public static int canHome(String jogador) {
        return getHomes(jogador).length;
    }

    public static String[] getHomes(String jogador) {
        String[] values = new String[0];
        try {
            String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-home") + " WHERE player_name='" + jogador + "';";
            ResultSet rs = EterniaServer.connection.Query(querie);
            if (rs.next()) {
                rs.getString("homes");
            }
            values = rs.getString("homes").split(":");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return values;
    }

}
