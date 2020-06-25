package br.com.eterniaserver.eterniaserver.modules.homesmanager;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.homesmanager.commands.*;

import co.aikar.commands.PaperCommandManager;

import org.bukkit.Location;

import java.util.HashMap;

public class HomesManager {

    private final EterniaServer plugin;
    private final HashMap<String, Location> homes;
    private final HashMap<String, String[]> home;

    public HomesManager(EterniaServer plugin) {
        this.plugin = plugin;
        this.home = plugin.getHome();
        this.homes = plugin.getHomes();

        final EFiles messages = plugin.getEFiles();
        final PaperCommandManager manager = plugin.getManager();

        if (plugin.serverConfig.getBoolean("modules.home")) {
            manager.registerCommand(new HomeSystem(plugin, this));
            messages.sendConsole("modules.enable", "%module%", "Homes");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Homes");
        }
    }

    public void setHome(Location loc, String home, String jogador) {
        homes.put(home + "." + jogador, loc);
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
            EQueries.executeQuery(querie);
            values = result.toString().split(":");
            this.home.put(jogador, values);
            String saveloc = loc.getWorld().getName() +
                    ":" + ((int) loc.getX()) +
                    ":" + ((int) loc.getY()) +
                    ":" + ((int) loc.getZ()) +
                    ":" + ((int) loc.getYaw()) +
                    ":" + ((int) loc.getPitch());
            final String querie2 = "INSERT INTO " + plugin.serverConfig.getString("sql.table-homes") + " (name, location) VALUES ('" + home + "." + jogador + "', '" + saveloc + "')";
            EQueries.executeQuery(querie2);
        } else {
            String saveloc = loc.getWorld().getName() +
                    ":" + ((int) loc.getX()) +
                    ":" + ((int) loc.getY()) +
                    ":" + ((int) loc.getZ()) +
                    ":" + ((int) loc.getYaw()) +
                    ":" + ((int) loc.getPitch());
            final String querie3 = "UPDATE " + plugin.serverConfig.getString("sql.table-homes") + " SET location='" + saveloc + "' WHERE name='" + home + "." + jogador + "';";
            EQueries.executeQuery(querie3);
        }
    }

    public void delHome(String home, String jogador) {
        homes.remove(home + "." + jogador);
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
        this.home.put(jogador, values);
        String querie;
        if (t) {
            querie = "UPDATE " + plugin.serverConfig.getString("sql.table-home") + " SET homes=':' WHERE player_name='" + jogador + "';";
        } else {
            querie = "UPDATE " + plugin.serverConfig.getString("sql.table-home") + " SET homes='" + nova + "' WHERE player_name='" + jogador + "';";
        }
        EQueries.executeQuery(querie);
        querie = "DELETE FROM " + plugin.serverConfig.getString("sql.table-homes") + " WHERE name='" + home + "." + jogador + "';";
        EQueries.executeQuery(querie);
    }

    public Location getHome(String home, String jogador) {
        if (homes.containsKey(home + "." + jogador)) {
            return homes.get(home + "." + jogador);
        } else {
            return plugin.error;
        }
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
        return home.get(jogador);
    }

}
