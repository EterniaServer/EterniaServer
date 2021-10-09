package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.Module;

import java.util.logging.Level;


public class CoreManager implements Module {

    private final EterniaServer plugin;

    public CoreManager(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        new Configurations.Configs(plugin);
    }

    @Override
    public void loadCommandsLocales() {
        plugin.getLogger().log(Level.INFO, "Core module: no commands locales");
    }

    @Override
    public void loadCommandsCompletions() {
        plugin.getLogger().log(Level.INFO, "Core module: no commands completions");
    }

    @Override
    public void loadListeners() {
        plugin.getLogger().log(Level.INFO, "Core module: no listeners");
    }

    @Override
    public void loadSchedules() {
        plugin.getLogger().log(Level.INFO, "Core module: no schedules");
    }

    @Override
    public void loadCommands() {
        plugin.getLogger().log(Level.INFO, "Core module: no commands");
    }

    @Override
    public void reloadConfigurations() {

    }

}
