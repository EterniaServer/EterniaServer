package br.com.eterniaserver.eterniaserver.api.interfaces;

import java.util.UUID;

public interface CashAPI {

    /**
     * Check if player has a cash account.
     * This is always return true if the player has
     * joined the server.
     * @param uuid of player to check
     * @return if the player has a cash account
     */
    boolean hasAccount(UUID uuid);

    /**
     * Gets the cash balance of a player.
     * @param uuid of player to check
     * @return the amount of cash
     */
    int getBalance(UUID uuid);

    /**
     * Checks if the player has at least the amount.
     * Don't use negative amounts.
     * @param uuid of player to check
     * @param amount the amount of cash to needed
     * @return if the player has at least that amount
     */
    boolean has(UUID uuid, int amount);

    /**
     * Deposit an amount of cash to a player.
     * Don't use negative amounts.
     * @param uuid of player to check
     * @param amount the amount of cash to deposit
     */
    void depositBalance(UUID uuid, int amount);

    /**
     * Withdraw an amount of cash from a player.
     * Don't use negative amounts.
     * @param uuid of player to check
     * @param amount the amount of cash to withdraw
     */
    void withdrawBalance(UUID uuid, int amount);

    /**
     * Set an amount of cash to a balance of a player.
     * @param uuid of player to check
     * @param amount the amount of cash to set
     */
    void setBalance(UUID uuid, int amount);

}
