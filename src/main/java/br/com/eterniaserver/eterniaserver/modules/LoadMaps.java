package br.com.eterniaserver.eterniaserver.modules;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;

public class LoadMaps {

    public LoadMaps(EterniaServer plugin) {
        HashMap<String, String> temp;

        String query = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-homes") + ";";
        temp = EQueries.getMapString(query, "name", "location");

        temp.forEach((k, v) -> {
            System.out.println(k + "=" + v);
            final String[] split = v.split(":");
            final Location loc = new Location(Bukkit.getWorld(split[0]),
                    Double.parseDouble(split[1]),
                    (Double.parseDouble(split[2]) + 1),
                    Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]),
                    Float.parseFloat(split[5]));
            plugin.getHomes().put(k, loc);
        });

        query = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-shop") + ";";
        temp = EQueries.getMapString(query, "name", "location");
        
        temp.forEach((k, v) -> {
            final String[] split = v.split(":");
            final Location loc = new Location(Bukkit.getWorld(split[0]),
                    Double.parseDouble(split[1]),
                    (Double.parseDouble(split[2]) + 1),
                    Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]),
                    Float.parseFloat(split[5]));
            plugin.getShops().put(k, loc);
        });

        query = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-warp") + ";";
        temp = EQueries.getMapString(query, "name", "location");

        temp.forEach((k, v) -> {
            final String[] split = v.split(":");
            final Location loc = new Location(Bukkit.getWorld(split[0]),
                    Double.parseDouble(split[1]),
                    (Double.parseDouble(split[2]) + 1),
                    Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]),
                    Float.parseFloat(split[5]));
            plugin.getWarps().put(k, loc);
        });

        query = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-home") + ";";
        temp = EQueries.getMapString(query, "player_name", "homes");

        temp.forEach((k, v) -> {
            final String[] homess = v.split(":");
            plugin.getHome().put(k, homess);
        });

        query = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-money") + ";";
        temp = EQueries.getMapString(query, "player_name", "balance");

        temp.forEach((k, v) -> plugin.getBalances().put(k, Double.parseDouble(v)));

    }

}
