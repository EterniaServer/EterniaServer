package br.com.eterniaserver.eterniaserver.modules.entity;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.Module;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.modules.entity.Configurations.EntityConfiguration;

public class EntityManager implements Module {

    private final EterniaServer plugin;

    public EntityManager(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        EntityConfiguration configuration = new EntityConfiguration(plugin);

        EterniaLib.getCfgManager().registerConfiguration("eterniaserver", "entity", true, configuration);
    }

    @Override
    public void loadCommandsCompletions() { }

    @Override
    public void loadConditions() { }

    @Override
    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Handlers(plugin), plugin);
    }

    @Override
    public void loadSchedules() {
        new Schedules.PluginClearSchedule(plugin).runTaskTimerAsynchronously(plugin, 20L, plugin.getInteger(Integers.CLEAR_TIMER) * 20L);
    }

    @Override
    public void loadCommands() { }
}
