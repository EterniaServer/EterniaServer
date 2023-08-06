package br.com.eterniaserver.eterniaserver.modules.cash;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.Entity;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.Module;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import java.util.logging.Level;


public class CashManager implements Module {

    private final EterniaServer plugin;
    private Services.Cash cashService;

    public CashManager(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        Configurations.Cash configuration = new Configurations.Cash(plugin);

        EterniaLib.registerConfiguration("eterniaserver", "cash", configuration);

        configuration.executeConfig();
        configuration.executeCritical();
        configuration.saveConfiguration(true);

        try {
            Entity<Entities.CashBalance> cashEntity = new Entity<>(Entities.CashBalance.class);

            EterniaLib.addTableName("%eternia_server_cash%", plugin.getString(Strings.CASH_TABLE_NAME));
            EterniaLib.addTableName("%eternia_server_profile%", plugin.getString(Strings.PROFILE_TABLE_NAME));

            EterniaLib.getDatabase().register(Entities.CashBalance.class, cashEntity);
        }
        catch (Exception exception) {
            EterniaLib.registerLog("EE-104-Cash");
        }

        this.plugin.setCashAPI(new Services.CraftCash());
        this.cashService = new Services.Cash(plugin);
    }

    @Override
    public void loadCommandsCompletions() {
        plugin.getLogger().log(Level.INFO, "Cash module: no commands completions");
    }

    @Override
    public void loadConditions() {
        plugin.getLogger().log(Level.INFO, "Cash module: no commands conditions");
    }

    @Override
    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Handlers(cashService), plugin);
    }

    @Override
    public void loadSchedules() {
        plugin.getLogger().log(Level.INFO, "Cash module: no schedules");
    }

    @Override
    public void loadCommands() {
        EterniaLib.getCmdManager().registerCommand(new Commands.Cash(plugin, cashService));
    }

}
