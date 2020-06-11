package br.com.eterniaserver.eterniaserver.modules.homesmanager;

import br.com.eterniaserver.eternialib.sql.Queries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.homesmanager.commands.*;

import co.aikar.commands.PaperCommandManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class HomesManager {

    private final EterniaServer plugin;
    private final Vars vars;

    public HomesManager(EterniaServer plugin) {
        this.plugin = plugin;
        this.vars = plugin.getVars();

        final Messages messages = plugin.getMessages();
        final PaperCommandManager manager = plugin.getManager();

        if (plugin.serverConfig.getBoolean("modules.home")) {
            manager.registerCommand(new HomeSystem(plugin, this));
            messages.sendConsole("modules.enable", "%module%", "Homes");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Homes");
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
            Queries.executeQuery(querie);
            values = result.toString().split(":");
            vars.home.put(jogador, values);
            String saveloc = loc.getWorld().getName() +
                    ":" + ((int) loc.getX()) +
                    ":" + ((int) loc.getY()) +
                    ":" + ((int) loc.getZ()) +
                    ":" + ((int) loc.getYaw()) +
                    ":" + ((int) loc.getPitch());
            final String querie2 = "INSERT INTO " + plugin.serverConfig.getString("sql.table-homes") + " (name, location) VALUES ('" + home + "." + jogador + "', '" + saveloc + "')";
            Queries.executeQuery(querie2);
        } else {
            String saveloc = loc.getWorld().getName() +
                    ":" + ((int) loc.getX()) +
                    ":" + ((int) loc.getY()) +
                    ":" + ((int) loc.getZ()) +
                    ":" + ((int) loc.getYaw()) +
                    ":" + ((int) loc.getPitch());
            final String querie3 = "UPDATE " + plugin.serverConfig.getString("sql.table-homes") + " SET location='" + saveloc + "' WHERE name='" + home + "." + jogador + "';";
            Queries.executeQuery(querie3);
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
        String querie;
        if (t) {
            querie = "UPDATE " + plugin.serverConfig.getString("sql.table-home") + " SET homes=':' WHERE player_name='" + jogador + "';";
        } else {
            querie = "UPDATE " + plugin.serverConfig.getString("sql.table-home") + " SET homes='" + nova + "' WHERE player_name='" + jogador + "';";
        }
        Queries.executeQuery(querie);
        querie = "DELETE FROM " + plugin.serverConfig.getString("sql.table-homes") + " WHERE name='" + home + "." + jogador + "';";
        Queries.executeQuery(querie);
    }

    public Location getHome(String home, String jogador) {
        Location loc = vars.error;
        if (vars.homes.containsKey(home + "." + jogador)) {
            loc = vars.homes.get(home + "." + jogador);
        } else {
            if (existHome(home, jogador)) {
                final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-homes") + " WHERE name='" + home + "." + jogador + "';";
                String[] values = Queries.queryString(querie, "location").split(":");
                loc = new Location(Bukkit.getWorld(values[0]),
                        Double.parseDouble(values[1]),
                        (Double.parseDouble(values[2]) + 1),
                        Double.parseDouble(values[3]),
                        Float.parseFloat(values[4]),
                        Float.parseFloat(values[5]));
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
        String[] homess = Queries.queryString(querie, "homes").split(":");
        vars.home.put(jogador, homess);
        return homess;
    }
}
