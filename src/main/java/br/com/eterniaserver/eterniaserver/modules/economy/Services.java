package br.com.eterniaserver.eterniaserver.modules.economy;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.dtos.BalanceDTO;
import br.com.eterniaserver.eterniaserver.api.dtos.BankDTO;
import br.com.eterniaserver.eterniaserver.api.dtos.BankMemberDTO;
import br.com.eterniaserver.eterniaserver.api.interfaces.ExtraEconomyAPI;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.economy.Entities.BankMember;
import br.com.eterniaserver.eterniaserver.modules.economy.Entities.BankBalance;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class ExtraEconomy implements ExtraEconomyAPI {

        private final EterniaServer plugin;

        private final List<BalanceDTO> topBalances = new ArrayList<>();

        public ExtraEconomy(EterniaServer plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean isBalanceTop(UUID uuid) {
            if (topBalances.isEmpty()) {
                return false;
            }

            return uuid.equals(topBalances.get(0).playerUUID());
        }

        @Override
        public List<BalanceDTO> getBalanceTop() {
            return topBalances;
        }

        @Override
        public String getBalanceTop(int position) {
            if (topBalances.isEmpty() || position < 1 || position > topBalances.size()) {
                return "";
            }

            BalanceDTO balanceDTO = topBalances.get(position - 1);
            if (balanceDTO.playerUUID() == null) {
                return "";
            }

            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, balanceDTO.playerUUID());
            if (playerProfile.getUuid() == null) {
                return "";
            }

            return playerProfile.getPlayerName();
        }

        @Override
        public List<BankDTO> getBankList() {
            List<BankBalance> balances = EterniaLib.getDatabase().listAll(BankBalance.class);
            List<String> bankListName = plugin.bankListName();

            bankListName.clear();

            for (BankBalance balance : balances) {
                bankListName.add(balance.getName());
            }

            return balances.stream().map(b -> new BankDTO(b.getName(), b.getBalance(), b.getTax())).toList();
        }

        @Override
        public List<BankMemberDTO> getBankMembers(String bankName) {
            List<BankMember> members = EterniaLib.getDatabase().findAllBy(BankMember.class, "bankName", bankName);
            return members.stream().map(m -> new BankMemberDTO(m.getBankName(), m.getUuid(), m.getRole())).toList();
        }

        @Override
        public void refreshBalanceTop(List<BalanceDTO> balances) {
            balances.sort((b1, b2) -> Objects.compare(b1.balance(), b2.balance(), Comparator.reverseOrder()));

            topBalances.clear();
            for (int i = 0; i < plugin.getInteger(Integers.ECONOMY_BALANCE_TOP_SIZE) && i < balances.size(); i++) {
                topBalances.add(balances.get(i));
            }
        }
    }

}
