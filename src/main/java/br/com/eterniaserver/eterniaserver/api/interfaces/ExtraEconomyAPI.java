package br.com.eterniaserver.eterniaserver.api.interfaces;

import br.com.eterniaserver.eterniaserver.modules.economy.Entities;

import java.util.List;
import java.util.UUID;

public interface ExtraEconomyAPI {

    boolean isBalanceTop(UUID uuid);

    List<Entities.EcoBalance> getBalanceTop();

    List<Entities.BankBalance> getBankList();

    List<Entities.BankMember> getBankMembers(String bankName);

    void refreshBalanceTop(List<Entities.EcoBalance> balances);

}
