package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.configs.Configs;
import br.com.eterniaserver.eterniaserver.configs.Constants;

import java.util.UUID;

public class APIExperience implements Constants {

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
            EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_XP, UUID_STR, uuid.toString(), XP_STR, 0));
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
        EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_XP, XP_STR, amount, UUID_STR, uuid.toString()));
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
