package br.com.eterniaserver.eterniaserver.modules.economy;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.Entity;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.Module;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;

import java.util.List;
import java.util.logging.Level;

public class EconomyManager implements Module {

    private final EterniaServer plugin;

    public EconomyManager(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        Configurations.EconomyConfiguration configuration = new Configurations.EconomyConfiguration(plugin);

        EterniaLib.registerConfiguration("eterniaserver", "economy", configuration);

        configuration.executeConfig();
        configuration.executeCritical();
        configuration.saveConfiguration(true);

        loadCommandsLocale(configuration, Enums.Commands.class);

        try {
            Entity<Entities.EcoBalance> balanceEntity = new Entity<>(Entities.EcoBalance.class);
            Entity<Entities.BankBalance> bankBalanceEntity = new Entity<>(Entities.BankBalance.class);
            Entity<Entities.BankMember> bankMemberEntity = new Entity<>(Entities.BankMember.class);

            EterniaLib.addTableName("%eternia_server_economy%", plugin.getString(Strings.ECO_TABLE_NAME_BALANCE));
            EterniaLib.addTableName("%eternia_server_bank%", plugin.getString(Strings.ECO_TABLE_NAME_BANK));
            EterniaLib.addTableName("%eternia_server_bank_member%", plugin.getString(Strings.ECO_TABLE_NAME_BANK_MEMBER));

            EterniaLib.getDatabase().register(Entities.EcoBalance.class, balanceEntity);
            EterniaLib.getDatabase().register(Entities.BankBalance.class, bankBalanceEntity);
            EterniaLib.getDatabase().register(Entities.BankMember.class, bankMemberEntity);
        }
        catch (Exception exception) {
            EterniaLib.registerLog("EE-117-Economy");
            return;
        }

        plugin.getServer().getServicesManager().register(
                Economy.class,
                new VaultEconomyManager(plugin),
                plugin,
                ServicePriority.Highest
        );

        EterniaServer.setExtraEconomyAPI(new Services.ExtraEconomy(plugin));

        List<Entities.EcoBalance> balances = EterniaLib.getDatabase().listAll(Entities.EcoBalance.class);
        plugin.getLogger().log(Level.INFO, "Economy module: {0} player balance loaded", balances.size());

        List<Entities.BankBalance> bankBalances = EterniaServer.getExtraEconomyAPI().getBankList();
        plugin.getLogger().log(Level.INFO, "Economy module: {0} bank balance loaded", bankBalances.size());

        EterniaServer.getExtraEconomyAPI().refreshBalanceTop(balances);
    }

    @Override
    public void loadCommandsCompletions() {
        EterniaLib.getCmdManager().getCommandCompletions().registerCompletion("banks", banks -> plugin.bankListName());
    }

    @Override
    public void loadConditions() {

    }

    @Override
    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Handlers(plugin), plugin);
    }

    @Override
    public void loadSchedules() {
        new Schedules.BalanceTopSchedule().runTaskTimerAsynchronously(
                plugin,
                plugin.getInteger(Integers.ECONOMY_BALANCE_TOP_REFRESH_TIME) * 20L,
                plugin.getInteger(Integers.ECONOMY_BALANCE_TOP_REFRESH_TIME) * 20L
        );
        new Schedules.BankTaxSchedule(plugin).runTaskTimerAsynchronously(
                plugin,
                plugin.getInteger(Integers.ECONOMY_BANK_TAX_REFRESH_TIME) * 20L,
                plugin.getInteger(Integers.ECONOMY_BANK_TAX_REFRESH_TIME) * 20L
        );
    }

    @Override
    public void loadCommands() {
        EterniaLib.getCmdManager().registerCommand(new Commands.EconomyGeneric(plugin));
        EterniaLib.getCmdManager().registerCommand(new Commands.EconomyBank(plugin));
    }
}
