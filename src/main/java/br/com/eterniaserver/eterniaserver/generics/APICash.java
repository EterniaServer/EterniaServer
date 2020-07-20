package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;

public class APICash {

    private APICash() {
        throw new IllegalStateException("Utility class");
    }

    public static int getCash(String playerName) {
        if (Vars.cash.containsKey(playerName)) {
            return Vars.cash.get(playerName);
        } else {
            EQueries.executeQuery("INSERT INTO " + EterniaServer.serverConfig.getString("sql.table-cash") + " (player_name, balance) VALUES('" + playerName + "', '" + 0 + "');");
            Vars.cash.put(playerName, 0);
            return 0;
        }
    }

    public static boolean hasCash(String playerName, int amount) {
        return getCash(playerName) >= amount;
    }

    public static void setCash(String playerName, int amount) {
        if (Vars.cash.containsKey(playerName)) {
            Vars.cash.put(playerName, amount);
            EQueries.executeQuery("UPDATE " + EterniaServer.serverConfig.getString("sql.table-cash") + " SET balance='" + amount + "' WHERE player_name='" + playerName + "';");
        } else {
            EQueries.executeQuery("INSERT INTO " + EterniaServer.serverConfig.getString("sql.table-cash") + " (player_name, balance) VALUES('" + playerName + "', '" + 0 + "');");
            Vars.cash.put(playerName, 0);
            setCash(playerName, amount);
        }
    }

    public static void addCash(String playerName, int amount) {
        setCash(playerName, getCash(playerName) + amount);
    }

    public static void removeCash(String playerName, int amount) {
        setCash(playerName, getCash(playerName) - amount);
    }

}
