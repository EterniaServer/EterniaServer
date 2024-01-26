package br.com.eterniaserver.eterniaserver.modules.experience;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.Entity;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.Module;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import java.util.List;
import java.util.logging.Level;

public class ExperienceManager implements Module {

    private final EterniaServer plugin;

    private final Services.Experience experienceService;

    public ExperienceManager(final EterniaServer plugin) {
        this.plugin = plugin;
        this.experienceService = new Services.Experience(plugin);
    }

    @Override
    public void loadConfigurations() {
        Configurations.ExperienceConfiguration configuration = new Configurations.ExperienceConfiguration(plugin);

        EterniaLib.registerConfiguration("eterniaserver", "experience", configuration);

        configuration.executeConfig();
        configuration.executeCritical();
        configuration.saveConfiguration(true);

        loadCommandsLocale(configuration, Enums.Commands.class);

        try {
            Entity<Entities.ExpBalance> expBalanceEntity = new Entity<>(Entities.ExpBalance.class);

            EterniaLib.addTableName("%eternia_server_xp%", plugin.getString(Strings.EXP_TABLE_NAME));

            EterniaLib.getDatabase().register(Entities.ExpBalance.class, expBalanceEntity);
        }
        catch (Exception exception) {
            EterniaLib.registerLog("EE-104-Cash");
        }

        List<Entities.ExpBalance> expBalances = EterniaLib.getDatabase().listAll(Entities.ExpBalance.class);
        this.plugin.getLogger().log(Level.INFO, "experience module: {0} exp balances loaded", expBalances.size());
    }

    @Override
    public void loadCommandsCompletions() {}

    @Override
    public void loadConditions() {}

    @Override
    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Handlers(plugin), plugin);
    }

    @Override
    public void loadSchedules() {}

    @Override
    public void loadCommands() {
        EterniaLib.getCmdManager().registerCommand(new Commands.Experience(plugin, experienceService));
    }

}
