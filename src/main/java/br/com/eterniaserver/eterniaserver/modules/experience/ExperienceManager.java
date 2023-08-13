package br.com.eterniaserver.eterniaserver.modules.experience;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.Module;

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
    }

    @Override
    public void loadCommandsCompletions() {
        plugin.getLogger().log(Level.INFO, "Experience module: no command completions");
    }

    @Override
    public void loadConditions() {
        plugin.getLogger().log(Level.INFO, "Experience module: no conditions");
    }

    @Override
    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Handlers(plugin), plugin);
    }

    @Override
    public void loadSchedules() {
        plugin.getLogger().log(Level.INFO, "Experience module: no schedules");
    }

    @Override
    public void loadCommands() {
        EterniaLib.getCmdManager().registerCommand(new Commands.Experience(plugin, experienceService));
    }

}
