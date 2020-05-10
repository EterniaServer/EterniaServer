package br.com.eterniaserver.eterniaserver.modules.homesmanager;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.homesmanager.commands.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;

public class HomesManager {

    private final EterniaServer plugin;
    private final Vars vars;

    public HomesManager(EterniaServer plugin, Messages messages, Strings strings, Vars vars) {
        this.plugin = plugin;
        this.vars = vars;
        if (plugin.serverConfig.getBoolean("modules.home")) {
            plugin.getCommand("delhouse").setExecutor(new DelHome(this, messages));
            plugin.getCommand("house").setExecutor(new Home(plugin, messages, this, vars));
            plugin.getCommand("houses").setExecutor(new Homes(plugin, messages, this, strings));
            plugin.getCommand("sethouse").setExecutor(new SetHome(messages, this));
            messages.ConsoleMessage("modules.enable", "%module%", "Homes");
        } else {
            messages.ConsoleMessage("modules.disable", "%module%", "Homes");
        }
    }

    public void setHome(Location loc, String home, String jogador) {
        vars.homes.put(home, loc);
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
            final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-home") + " SET homes='" + result + "' WHERE player_name='" + jogador + "';";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement sethome = connection.prepareStatement(querie);
                sethome.execute();
                sethome.close();
            }, true);
            values = result.toString().split(":");
            vars.home.put(jogador, values);
            String saveloc = loc.getWorld().getName() + ":" + ((int) loc.getX()) + ":" +
                    ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
            final String querie2 = "INSERT INTO " + plugin.serverConfig.getString("sql.table-homes") + " (name, location) VALUES ('" + home + "." + jogador + "', '" + saveloc + "')";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement sethome = connection.prepareStatement(querie2);
                sethome.execute();
                sethome.close();
            }, true);
        } else {
            String saveloc = loc.getWorld().getName() + ":" + ((int) loc.getX()) + ":" +
                    ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
            final String querie3 = "UPDATE " + plugin.serverConfig.getString("sql.table-homes") + " SET location='" + saveloc + "' WHERE name='" + home + "." + jogador + "';";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement sethome = connection.prepareStatement(querie3);
                sethome.execute();
                sethome.close();
            }, true);
        }
    }

    public void delHome(String home, String jogador) {
        vars.homes.remove(home + "." + jogador);
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
        vars.home.put(jogador, values);
        if (t) {
            String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-home") + " SET homes=':' WHERE player_name='" + jogador + "';";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement delhome = connection.prepareStatement(querie);
                delhome.execute();
                delhome.close();
            }, true);
        } else {
            String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-home") + " SET homes='" + nova + "' WHERE player_name='" + jogador + "';";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement delhome = connection.prepareStatement(querie);
                delhome.execute();
                delhome.close();
            }, true);
        }
        String querie = "DELETE FROM " + plugin.serverConfig.getString("sql.table-homes") + " WHERE name='" + home + "." + jogador + "';";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement delhome = connection.prepareStatement(querie);
            delhome.execute();
            delhome.close();
        }, true);
    }

    public Location getHome(String home, String jogador) {
        Location loc = vars.error;
        if (vars.homes.containsKey(home + "." + jogador)) {
            loc = vars.homes.get(home + "." + jogador);
        } else {
            if (existHome(home, jogador)) {
                AtomicReference<String> string = new AtomicReference<>("");
                final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-homes") + " WHERE name='" + home + "." + jogador + "';";
                plugin.connections.executeSQLQuery(connection -> {
                    PreparedStatement gethome = connection.prepareStatement(querie);
                    ResultSet resultSet = gethome.executeQuery();
                    if (resultSet.next() && resultSet.getString("location") != null) {
                        string.set(resultSet.getString("location"));
                    }
                });
                String[] values = string.toString().split(":");
                loc = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), (Double.parseDouble(values[2]) + 1), Double.parseDouble(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5]));
                vars.homes.put(home + "." + jogador, loc);
            }
        }
        return loc;
    }

    public boolean existHome(String home, String jogador) {
        String[] homes = getHomes(jogador);
        for (String line : homes) {
            if (line.equals(home)) {
                return true;
            }
        }
        return false;
    }

    public int canHome(String jogador) {
        return getHomes(jogador).length;
    }

    public String[] getHomes(String jogador) {
        if (vars.home.containsKey(jogador)) {
            return vars.home.get(jogador);
        }

        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-home") + " WHERE player_name='" + jogador + "';";
        AtomicReference<String> string = new AtomicReference<>("");
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement gethomes = connection.prepareStatement(querie);
            ResultSet resultSet = gethomes.executeQuery();
            if (resultSet.next() && resultSet.getString("homes") != null) {
                string.set(resultSet.getString("homes"));
            }
        });

        String[] homess = string.toString().split(":");
        vars.home.put(jogador, homess);
        return homess;
    }
}
