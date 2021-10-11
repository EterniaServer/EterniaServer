package br.com.eterniaserver.eterniaserver.modules.elevator;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.Module;

import java.util.logging.Level;

public class ElevatorManager implements Module {

    private final EterniaServer plugin;

    public ElevatorManager(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        new Configurations.Configs(plugin);
    }

    @Override
    public void loadCommandsCompletions() {
        plugin.getLogger().log(Level.INFO, "Elevator module: no command completions");
    }

    @Override
    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Handlers(plugin), plugin);
    }

    @Override
    public void loadSchedules() {
        plugin.getLogger().log(Level.INFO, "Elevator module: no schedules");
    }

    @Override
    public void loadCommands() {
        plugin.getLogger().log(Level.INFO, "Elevator module: no commands");
    }

    @Override
    public void reloadConfigurations() {
        new Configurations.Configs(plugin);
    }


}
