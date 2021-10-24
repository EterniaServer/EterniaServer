package br.com.eterniaserver.eterniaserver.modules.reward;

import br.com.eterniaserver.eternialib.CommandManager;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.Module;

import java.util.logging.Level;


public class RewardManager implements Module {

    private final EterniaServer plugin;

    private final Services.Rewards rewardsService;

    public RewardManager(final EterniaServer plugin) {
        this.plugin = plugin;
        this.rewardsService = new Services.Rewards(plugin);
    }

    @Override
    public void loadConfigurations() {
        new Configurations.Configs(plugin);
        new Configurations.Locales(plugin);

        loadCommandsLocales(new Configurations.CommandsLocales(), Enums.Commands.class);
    }

    @Override
    public void loadCommandsCompletions() {
        plugin.getLogger().log(Level.INFO, "Reward module: no completions");
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
        CommandManager.registerCommand(new Commands.Rewards(plugin, rewardsService));
    }

    @Override
    public void reloadConfigurations() {
        new Configurations.Configs(plugin);
        new Configurations.Locales(plugin);
    }
}
