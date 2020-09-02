package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

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
        if (PluginVars.playerProfile.containsKey(uuid)) {
            return PluginVars.playerProfile.get(uuid).xp;
        } else {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);
            EQueries.executeQuery(PluginConstants.getQueryInsert(PluginConfigs.TABLE_PLAYER, "(uuid, player_name, time, last, hours, balance, xp)",
                    "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + PluginConfigs.BALANCE_START + "', '" + 0 + "')"));
            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.balance = PluginConfigs.BALANCE_START;
            PluginVars.playerProfile.put(uuid, playerProfile);
            return 0;
        }
    }

    /**
     * Defines the amount experience in player's database.
     * @param uuid to check
     * @param amount to set
     */
    public static void setExp(UUID uuid, int amount) {
        PluginVars.playerProfile.get(uuid).xp = amount;
        EQueries.executeQuery(PluginConstants.getQueryUpdate(PluginConfigs.TABLE_PLAYER, PluginConstants.XP_STR, amount, PluginConstants.UUID_STR, uuid.toString()));
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
