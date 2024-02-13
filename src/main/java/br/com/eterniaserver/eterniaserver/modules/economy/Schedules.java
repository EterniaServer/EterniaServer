package br.com.eterniaserver.eterniaserver.modules.economy;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.dtos.BalanceDTO;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.economy.Entities.EcoBalance;
import br.com.eterniaserver.eterniaserver.modules.economy.Entities.BankBalance;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

final class Schedules {

    private Schedules() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class BalanceTopSchedule extends BukkitRunnable {

        @Override
        public void run() {
            List<EcoBalance> balances = EterniaLib.getDatabase().listAll(EcoBalance.class);

            List<BalanceDTO> balanceDTOS = new ArrayList<>(balances.stream().map(b -> new BalanceDTO(b.getUuid(), b.getBalance())).toList());
            EterniaServer.getExtraEconomyAPI().refreshBalanceTop(balanceDTOS);
        }
    }

    static class BankTaxSchedule extends BukkitRunnable {

        private final EterniaServer plugin;

        public BankTaxSchedule(EterniaServer plugin) {
            this.plugin = plugin;
        }

        @Override
        public void run() {
            List<BankBalance> bankBalances = EterniaLib.getDatabase().listAll(BankBalance.class);

            for (BankBalance bankBalance : bankBalances) {
                double taxValue = bankBalance.getTax() + plugin.getDouble(Doubles.ECO_BANK_TAX_VALUE);
                double balance = bankBalance.getBalance() + bankBalance.getBalance() * taxValue;

                bankBalance.setBalance(balance);

                EterniaLib.getDatabase().update(BankBalance.class, bankBalance);
            }
        }
    }
}
