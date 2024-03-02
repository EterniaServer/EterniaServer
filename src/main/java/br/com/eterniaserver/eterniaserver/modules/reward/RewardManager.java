package br.com.eterniaserver.eterniaserver.modules.reward;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.Entity;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.Module;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import java.util.List;
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
        Configurations.RewardMessagesConfiguration messagesConfiguration = new Configurations.RewardMessagesConfiguration(plugin.messages());
        Configurations.RewardCommandsConfiguration commandsConfiguration = new Configurations.RewardCommandsConfiguration();

        EterniaLib.getCfgManager().registerConfiguration("eterniaserver", "reward", true, configuration);
        EterniaLib.getCfgManager().registerConfiguration("eterniaserver", "reward_messages", true, messagesConfiguration);
        EterniaLib.getCfgManager().registerConfiguration("eterniaserver", "reward_commands", true, commandsConfiguration);

        try {
            Entity<Entities.RewardGroup> rewardGroupEntity = new Entity<>(Entities.RewardGroup.class);

            EterniaLib.getDatabase().addTableName("%eternia_server_reward%", plugin.getString(Strings.REWARD_TABLE_NAME));

            EterniaLib.getDatabase().register(Entities.RewardGroup.class, rewardGroupEntity);
        }
        catch (Exception exception) {
            plugin.getLogger().log(Level.SEVERE, "Error registering RewardGroup entity {0}", exception.getMessage());
            return;
        }

        this.rewardsService = new Services.Rewards(plugin);

        List<Entities.RewardGroup> rewardGroups = EterniaLib.getDatabase().listAll(Entities.RewardGroup.class);
        this.plugin.getLogger().log(Level.INFO, "Reward module: {0} rewards loaded", rewardGroups.size());
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
        EterniaLib.getCmdManager().registerCommand(new Commands.Rewards(plugin, rewardsService));
    }

}
