package br.com.eterniaserver.eterniaserver.api.interfaces;

import br.com.eterniaserver.eterniaserver.api.dtos.BalanceDTO;
import br.com.eterniaserver.eterniaserver.api.dtos.BankDTO;
import br.com.eterniaserver.eterniaserver.api.dtos.BankMemberDTO;

import java.util.List;
import java.util.UUID;

public interface ExtraEconomyAPI {

    /**
     * Check if player is the richest player.
     * @param uuid of player to check
     * @return if the player is the richest
     */
    boolean isBalanceTop(UUID uuid);

    /**
     * Get the top richest players.
     * @return the top richest players
     */
    List<BalanceDTO> getBalanceTop();

    /**
     * Get the list of banks.
     * @return the list of banks
     */
    List<BankDTO> getBankList();

    /**
     * Get the list of members from a bank.
     * @param bankName the name of the bank
     * @return the list of members
     */
    List<BankMemberDTO> getBankMembers(String bankName);

    /**
     * Refresh the top richest players.
     * @param balances the balances of the players
     */
    void refreshBalanceTop(List<BalanceDTO> balances);

}
