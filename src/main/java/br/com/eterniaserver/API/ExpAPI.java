package br.com.eterniaserver.API;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;

public class ExpAPI {

    /**
     *
     * @param playerName The player name in String.
     * @return The amount of exp of the player.
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
     *
     * @param playerName The player name in String.
     * @param valor the amount of exp to set for the player.
     */
    public static void setExp(String playerName, int valor) {
        Vars.xp.put(playerName, valor);
        final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-xp") + " SET xp='" + valor + "' WHERE player_name='" + playerName + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement setexp = connection.prepareStatement(querie);
            setexp.execute();
            setexp.close();
        }, true);
    }

    /**
     *
     * @param playerName The player name in String.
     * @param valor the amount of exp to add to a player.
     */
    public static void addExp(String playerName, int valor) {
        setExp(playerName, getExp(playerName) + valor);
    }

    /**
     *
     * @param playerName The player name in string.
     * @return all exp of the player.
     */
    public static Integer takeExp(String playerName) {
        int xp = getExp(playerName);
        setExp(playerName, 0);
        return xp;
    }

}
