package br.com.eterniaserver.modules.homesmanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.homesmanager.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class HomesManager {

    public HomesManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.home")) {
            Objects.requireNonNull(plugin.getCommand("delhouse")).setExecutor(new DelHome());
            Objects.requireNonNull(plugin.getCommand("house")).setExecutor(new Home(plugin));
            Objects.requireNonNull(plugin.getCommand("houses")).setExecutor(new Homes(plugin));
            Objects.requireNonNull(plugin.getCommand("sethouse")).setExecutor(new SetHome());
            Messages.ConsoleMessage("modules.enable", "%module%", "Homes");
        } else {
            Messages.ConsoleMessage("modules.disable", "%module%", "Homes");
        }
    }

    public static void setHome(Location loc, String home, String jogador) {
        Vars.homes.put(home, loc);
        boolean t = false;
        StringBuilder result = new StringBuilder();
        String[] values = getHomes(jogador);
        for (String line : values) {
            if (line.equals(home)) {
                result.append(line).append(":");
                t = true;
            } else {
                result.append(line).append(":");
            }
        }
        if (!t) {
            result.append(home).append(":");
            final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-home") + " SET homes='" + result + "' WHERE player_name='" + jogador + "';";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement sethome = connection.prepareStatement(querie);
                sethome.execute();
                sethome.close();
            }, true);
            values = result.toString().split(":");
            Vars.home.put(jogador, values);
            String saveloc = Objects.requireNonNull(loc.getWorld()).getName() + ":" + ((int) loc.getX()) + ":" +
                    ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
            final String querie2 = "INSERT INTO " + EterniaServer.configs.getString("sql.table-homes") + " (name, location) VALUES ('" + home + "." + jogador + "', '" + saveloc + "')";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement sethome = connection.prepareStatement(querie2);
                sethome.execute();
                sethome.close();
            }, true);
        } else {
            String saveloc = Objects.requireNonNull(loc.getWorld()).getName() + ":" + ((int) loc.getX()) + ":" +
                    ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
            final String querie3 = "UPDATE " + EterniaServer.configs.getString("sql.table-homes") + " SET location='" + saveloc + "' WHERE name='" + home + "." + jogador + "';";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement sethome = connection.prepareStatement(querie3);
                sethome.execute();
                sethome.close();
            }, true);
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
        values = nova.toString().split(":");
        Vars.home.put(jogador, values);
        if (t) {
            String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-home") + " SET homes=':' WHERE player_name='" + jogador + "';";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement delhome = connection.prepareStatement(querie);
                delhome.execute();
                delhome.close();
            }, true);
        } else {
            String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-home") + " SET homes='" + nova + "' WHERE player_name='" + jogador + "';";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement delhome = connection.prepareStatement(querie);
                delhome.execute();
                delhome.close();
            }, true);
        }
        String querie = "DELETE FROM " + EterniaServer.configs.getString("sql.table-homes") + " WHERE name='" + home + "." + jogador + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement delhome = connection.prepareStatement(querie);
            delhome.execute();
            delhome.close();
        }, true);
    }

    public static Location getHome(String home, String jogador) {
        Location loc = Vars.error;
        if (Vars.homes.containsKey(home + "." + jogador)) {
            loc = Vars.homes.get(home + "." + jogador);
        } else {
            if (existHome(home, jogador)) {
                AtomicReference<String> string = new AtomicReference<>("");
                final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-homes") + " WHERE name='" + home + "." + jogador + "';";
                EterniaServer.connection.executeSQLQuery(connection -> {
                    PreparedStatement gethome = connection.prepareStatement(querie);
                    ResultSet resultSet = gethome.executeQuery();
                    if (resultSet.next() && resultSet.getString("location") != null) {
                        string.set(resultSet.getString("location"));
                    }
                });
                String[] values = string.toString().split(":");
                loc = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), (Double.parseDouble(values[2]) + 1), Double.parseDouble(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                Vars.homes.put(home + "." + jogador, loc);
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
        if (Vars.home.containsKey(jogador)) {
            return Vars.home.get(jogador);
        }

        final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-home") + " WHERE player_name='" + jogador + "';";
        AtomicReference<String> string = new AtomicReference<>("");
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement gethomes = connection.prepareStatement(querie);
            ResultSet resultSet = gethomes.executeQuery();
            if (resultSet.next() && resultSet.getString("homes") != null) {
                string.set(resultSet.getString("homes"));
            }
        });

        String[] homess = string.toString().split(":");
        Vars.home.put(jogador, homess);
        return homess;
    }
}
