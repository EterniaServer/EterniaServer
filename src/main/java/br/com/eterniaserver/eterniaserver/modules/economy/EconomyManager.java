package br.com.eterniaserver.eterniaserver.modules.economy;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.Entity;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.dtos.BalanceDTO;
import br.com.eterniaserver.eterniaserver.modules.Module;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.economy.Configurations.EconomyConfiguration;
import br.com.eterniaserver.eterniaserver.modules.economy.Configurations.MessagesConfiguration;
import br.com.eterniaserver.eterniaserver.modules.economy.Configurations.CommandsConfiguration;
import br.com.eterniaserver.eterniaserver.modules.economy.Entities.EcoBalance;
import br.com.eterniaserver.eterniaserver.modules.economy.Entities.BankBalance;
import br.com.eterniaserver.eterniaserver.modules.economy.Entities.BankMember;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.ServicePriority;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class EconomyManager implements Module {

    private final EterniaServer plugin;

    public EconomyManager(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {

        EterniaLib.getCfgManager().registerConfiguration(
                "eterniaserver",
                "economy_messages",
                true,
                new MessagesConfiguration(plugin)
        );
        EterniaLib.getCfgManager().registerConfiguration(
                "eterniaserver",
                "economy",
                true,
                new EconomyConfiguration(plugin)
        );
        EterniaLib.getCfgManager().registerConfiguration(
                "eterniaserver",
                "economy_commands",
                true,
                new CommandsConfiguration()
        );

        try {
            Entity<EcoBalance> balanceEntity = new Entity<>(EcoBalance.class);
            Entity<BankBalance> bankBalanceEntity = new Entity<>(BankBalance.class);
            Entity<BankMember> bankMemberEntity = new Entity<>(BankMember.class);

            EterniaLib.getDatabase().addTableName("%eternia_server_economy%", plugin.getString(Strings.ECO_TABLE_NAME_BALANCE));
            EterniaLib.getDatabase().addTableName("%eternia_server_bank%", plugin.getString(Strings.ECO_TABLE_NAME_BANK));
            EterniaLib.getDatabase().addTableName("%eternia_server_bank_member%", plugin.getString(Strings.ECO_TABLE_NAME_BANK_MEMBER));

            EterniaLib.getDatabase().register(EcoBalance.class, balanceEntity);
            EterniaLib.getDatabase().register(BankBalance.class, bankBalanceEntity);
            EterniaLib.getDatabase().register(BankMember.class, bankMemberEntity);
        }
        catch (Exception exception) {
            //todo EterniaLib.registerLog("EE-117-Economy");
            return;
        }

        //noinspection deprecation
        plugin.getServer().getServicesManager().register(
                Economy.class,
                new VaultEconomyManager(plugin),
                plugin,
                ServicePriority.Highest
        );

        EterniaServer.setExtraEconomyAPI(new Services.ExtraEconomy(plugin));

        List<EcoBalance> balances = EterniaLib.getDatabase().listAll(EcoBalance.class);
        plugin.getLogger().log(Level.INFO, "Economy module: {0} player balance loaded", balances.size());

        List<BankBalance> bankBalances = EterniaLib.getDatabase().listAll(BankBalance.class);
        plugin.getLogger().log(Level.INFO, "Economy module: {0} bank balance loaded", bankBalances.size());

        List<BalanceDTO> balanceDTOS = new ArrayList<>(balances.stream().map(b -> new BalanceDTO(b.getUuid(), b.getBalance())).toList());
        EterniaServer.getExtraEconomyAPI().refreshBalanceTop(balanceDTOS);
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
