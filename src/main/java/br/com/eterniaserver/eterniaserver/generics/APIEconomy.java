package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import java.util.UUID;

public class APIEconomy {

    private APIEconomy() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Gets the money in player account
     * @param uuid to check
     * @return Amount currently held in player's account
     */
    public static double getMoney(UUID uuid) {
        if (Vars.playerProfile.containsKey(uuid)) {
            return Vars.playerProfile.get(uuid).balance;
        } else {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);
            EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_PLAYER, "(uuid, player_name, time, last, hours, balance)",
                    "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + Configs.BALANCE_START + "')"));
            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.balance = Configs.BALANCE_START;
            Vars.playerProfile.put(uuid, playerProfile);
            return Configs.BALANCE_START;
        }
    }

    /**
     * Return a boolean that indicate if the player has money enough.
     * @param uuid to check
     * @param amount to check
     * @return the boolean
     */
    public static boolean hasMoney(UUID uuid, double amount) {
        return getMoney(uuid) >= amount;
    }

    /**
     * Defines the amount money in player's account.
     * @param uuid to check
     * @param amount to set
     */
    public static void setMoney(UUID uuid, double amount) {
        if (Vars.playerProfile.containsKey(uuid)) {
            Vars.playerProfile.get(uuid).balance = amount;
            EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_PLAYER, Constants.BALANCE_STR, amount, Constants.UUID_STR, uuid.toString()));
        } else {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);
            EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_PLAYER, "(uuid, player_name, time, last, hours, balance)",
                    "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + Configs.BALANCE_START + "')"));
            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.balance = Configs.BALANCE_START;
            Vars.playerProfile.put(uuid, playerProfile);
            setMoney(uuid, amount);
        }
    }

    /**
     * Adds money to player account.
     * @param uuid to check
     * @param amount to add
     */
    public static void addMoney(UUID uuid, double amount) {
        setMoney(uuid, getMoney(uuid) + amount);
    }

    /**
     * Removes money to player account.
     * @param uuid to check
     * @param amount to remove
     */
    public static void removeMoney(UUID uuid, double amount) {
        setMoney(uuid, getMoney(uuid) - amount);
    }

}
