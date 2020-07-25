package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.Strings;

public class APIExperience {

    private APIExperience() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Gets experience of a player on a database.
     * @param playerName to check
     * @return Amount currently held in player's database
     */
    public static Integer getExp(String playerName) {
        if (Vars.xp.containsKey(playerName)) {
            return Vars.xp.get(playerName);
        } else {
            Vars.xp.put(playerName, 0);
            EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_XP, Strings.PNAME, playerName, Strings.XP, 0));
            return 0;
        }
    }

    /**
     * Defines the amount experience in player's database.
     * @param playerName to check
     * @param amount to set
     */
    public static void setExp(String playerName, int amount) {
        Vars.xp.put(playerName, amount);
        EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_XP, Strings.XP, amount, Strings.PNAME, playerName));
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
