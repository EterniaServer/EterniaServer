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
     * Get the singular name of current
     * @return the name
     */
    public static String singularName() {
        return Configs.BALANCE_SINGULAR_NAME;
    }

    /**
     * Get the plural name of current
     * @return the name
     */
    public static String pluralName() {
        return Configs.BALANCE_PLURAL_NAME;
    }

    /**
     * Check if player has account
     * @param uuid uuid of player
     */
    public static boolean hasAccount(UUID uuid) {
        return Vars.playerProfile.containsKey(uuid);
    }

    /**
     * Create a account for the player
     * @param uuid uuid of player
     */
    public static void createAccount(UUID uuid) {
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
    }

    /**
     * Get the balance of the player
     * @param uuid of player
     * @return the balance
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
     * Check if the player has money enough
     * @param uuid of player
     * @param amount the amount of money needed
     * @return if has or not
     */
    public static boolean hasMoney(UUID uuid, double amount) {
        return getMoney(uuid) >= amount;
    }

    /**
     * Defines the balance of the player
     * @param uuid of player
     * @param amount the amount of money to set
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
     * Give money to player account
     * @param uuid of player
     * @param amount the amount of money to give
     */
    public static void addMoney(UUID uuid, double amount) {
        setMoney(uuid, getMoney(uuid) + amount);
    }

    /**
     * Give money to player account
     * @param uuid of player
     * @param amount the amount of money to give
     */
    public static void removeMoney(UUID uuid, double amount) {
        setMoney(uuid, getMoney(uuid) - amount);
    }

}
