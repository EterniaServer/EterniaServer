package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.Strings;

import java.util.UUID;

public class APIExperience {

    private APIExperience() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Gets experience of a player on a database.
     * @param uuid to check
     * @return Amount currently held in player's database
     */
    public static Integer getExp(UUID uuid) {
        if (Vars.xp.containsKey(uuid)) {
            return Vars.xp.get(uuid);
        } else {
            Vars.xp.put(uuid, 0);
            EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_XP, Strings.UUID, uuid.toString(), Strings.XP, 0));
            return 0;
        }
    }

    /**
     * Defines the amount experience in player's database.
     * @param uuid to check
     * @param amount to set
     */
    public static void setExp(UUID uuid, int amount) {
        Vars.xp.put(uuid, amount);
        EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_XP, Strings.XP, amount, Strings.UUID, uuid.toString()));
    }

    /**
     * Adds experience of player's database.
     * @param uuid to check
     * @param amount to add
     */
    public static void addExp(UUID uuid, int amount) {
        setExp(uuid, getExp(uuid) + amount);
    }

    /**
     * Removes experience of player's database.
     * @param uuid to check
     * @param amount to remove
     */
    public static void removeExp(UUID uuid, int amount) {
        setExp(uuid, getExp(uuid) - amount);
    }

}
