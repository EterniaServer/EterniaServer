package br.com.eterniaserver.API;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;

public class Exp {

    /**
     * Gets experience of a player on a database.
     * @param playerName to check
     * @return Amount currently held in player's database
     */
    public static Integer getExp(String playerName) {
        if (Vars.xp.containsKey(playerName)) {
            return Vars.xp.get(playerName);
        }

        AtomicReference<Integer> xp = new AtomicReference<>(0);
        final String querie = "SELECT xp FROM " + EterniaServer.configs.getString("sql.table-xp") + " WHERE player_name='" + playerName + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement getshop = connection.prepareStatement(querie);
            ResultSet resultSet = getshop.executeQuery();
            if (resultSet.next() && resultSet.getInt("xp") != 0) {
                xp.set(resultSet.getInt("xp"));
            }
        });

        Vars.xp.put(playerName, xp.get());
        return xp.get();
    }

    /**
     * Defines the amount experience in player's database.
     * @param playerName to check
     * @param amount to set
     */
    public static void setExp(String playerName, int amount) {
        Vars.xp.put(playerName, amount);
        final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-xp") + " SET xp='" + amount + "' WHERE player_name='" + playerName + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement setexp = connection.prepareStatement(querie);
            setexp.execute();
            setexp.close();
        }, true);
    }

    /**
     * Adds experience of player's database.
     * @param playerName to check
     * @param amount to add
     */
    public static void addExp(String playerName, int amount) {
        setExp(playerName, getExp(playerName) + amount);
    }

    /**
     * Removes experience of player's database.
     * @param playerName to check
     * @param amount to remove
     */
    public static void removeExp(String playerName, int amount) {
        setExp(playerName, getExp(playerName) - amount);
    }

}
