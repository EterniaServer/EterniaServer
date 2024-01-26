package br.com.eterniaserver.eterniaserver.modules.economy;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.ExtraEconomyAPI;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.modules.Constants;

import java.util.*;

final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class ExtraEconomy implements ExtraEconomyAPI {

        private final EterniaServer plugin;
        private final DatabaseInterface databaseInterface;

        private final List<Entities.EcoBalance> topBalances = new ArrayList<>();

        public ExtraEconomy(EterniaServer plugin) {
            this.plugin = plugin;
            this.databaseInterface = EterniaLib.getDatabase();
        }

        @Override
        public boolean isBalanceTop(UUID uuid) {
            if (topBalances.isEmpty()) {
                return false;
            }

            return uuid.equals(topBalances.get(0).getUuid());
        }

        @Override
        public List<Entities.EcoBalance> getBalanceTop() {
            return topBalances;
        }

        @Override
        public List<Entities.BankBalance> getBankList() {
            return databaseInterface.listAll(Entities.BankBalance.class);
        }

        @Override
        public List<Entities.BankMember> getBankMembers(String bankName) {
            return databaseInterface.findAllBy(Entities.BankMember.class, "bankName", bankName);
        }

        @Override
        public void refreshBalanceTop(List<Entities.EcoBalance> balances) {
            balances.sort((b1, b2) -> Objects.compare(b1.getBalance(), b2.getBalance(), Comparator.reverseOrder()));

            topBalances.clear();
            for (int i = 0; i < plugin.getInteger(Integers.ECONOMY_BALANCE_TOP_SIZE) && i < balances.size(); i++) {
                topBalances.add(balances.get(i));
            }
        }
    }

}
