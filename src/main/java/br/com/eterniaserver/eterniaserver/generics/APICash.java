package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.Strings;

public class APICash {

    private APICash() {
        throw new IllegalStateException("Utility class");
    }

    public static int getCash(String playerName) {
        if (Vars.cash.containsKey(playerName)) {
            return Vars.cash.get(playerName);
        } else {
            EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_CASH, Strings.PLAYER_NAME, playerName, Strings.BALANCE, 0));
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
            EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_CASH, Strings.BALANCE, amount, Strings.PLAYER_NAME, playerName));
        } else {
            EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_CASH, Strings.PLAYER_NAME, playerName, Strings.BALANCE, 0));
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
