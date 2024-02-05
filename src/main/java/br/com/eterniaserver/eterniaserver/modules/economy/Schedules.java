package br.com.eterniaserver.eterniaserver.modules.economy;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

final class Schedules {

    private Schedules() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class BalanceTopSchedule extends BukkitRunnable {

        @Override
        public void run() {
            List<Entities.EcoBalance> balances = EterniaLib.getDatabase().listAll(Entities.EcoBalance.class);

            EterniaServer.getExtraEconomyAPI().refreshBalanceTop(balances);
        }
    }

    static class BankTaxSchedule extends BukkitRunnable {

        private final EterniaServer plugin;
        private final DatabaseInterface databaseInterface;

        public BankTaxSchedule(EterniaServer plugin) {
            this.plugin = plugin;
            this.databaseInterface = EterniaLib.getDatabase();
        }

        @Override
        public void run() {
            List<Entities.BankBalance> bankBalances = EterniaServer.getExtraEconomyAPI().getBankList();

            for (Entities.BankBalance bankBalance : bankBalances) {
                double taxValue = bankBalance.getTax() + plugin.getDouble(Doubles.ECO_BANK_TAX_VALUE);
                double balance = bankBalance.getBalance() + bankBalance.getBalance() * taxValue;

                bankBalance.setBalance(balance);

                databaseInterface.update(Entities.BankBalance.class, bankBalance);
            }
        }
    }
}