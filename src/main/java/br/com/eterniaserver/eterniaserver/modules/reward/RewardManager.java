package br.com.eterniaserver.eterniaserver.modules.reward;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.Entity;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.Module;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import java.util.logging.Level;


public class RewardManager implements Module {

    private final EterniaServer plugin;

    private Services.Rewards rewardsService;

    public RewardManager(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        Configurations.RewardConfiguration configuration = new Configurations.RewardConfiguration(plugin);

        EterniaLib.registerConfiguration("eterniaserver", "reward", configuration);

        configuration.executeConfig();
        configuration.executeCritical();
        configuration.saveConfiguration(true);

        loadCommandsLocale(configuration, Enums.Commands.class);


        try {
            Entity<Entities.RewardGroup> rewardGroupEntity = new Entity<>(Entities.RewardGroup.class);

            EterniaLib.addTableName("%eternia_server_reward%", plugin.getString(Strings.REVISION_TABLE_NAME));

            EterniaLib.getDatabase().register(Entities.RewardGroup.class, rewardGroupEntity);
        }
        catch (Exception exception) {
            EterniaLib.registerLog("EE-105-Reward");
            return;
        }

        this.rewardsService = new Services.Rewards(plugin);
    }

    @Override
    public void loadCommandsCompletions() {
        plugin.getLogger().log(Level.INFO, "Reward module: no completions");
    }

    @Override
    public void loadConditions() {
        plugin.getLogger().log(Level.INFO, "Reward module: no conditions");
    }

    @Override
    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Handlers(plugin), plugin);
    }

    @Override
    public void loadSchedules() {
        plugin.getLogger().log(Level.INFO, "Reward module: no schedules");
    }

    @Override
    public void loadCommands() {
        EterniaLib.getCmdManager().registerCommand(new Commands.Rewards(plugin, rewardsService));
    }

}
