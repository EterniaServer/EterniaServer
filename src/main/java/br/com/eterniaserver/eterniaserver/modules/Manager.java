package br.com.eterniaserver.eterniaserver.modules;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.Module;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.modules.core.CoreManager;
import br.com.eterniaserver.eterniaserver.modules.elevator.ElevatorManager;
import br.com.eterniaserver.eterniaserver.modules.experience.ExperienceManager;
import br.com.eterniaserver.eterniaserver.modules.spawner.SpawnerManager;

import java.util.ArrayList;
import java.util.List;

public class Manager {

    private final List<Module> modules = new ArrayList<>();

    public Manager(final EterniaServer plugin) {
        loadModule(new CoreManager(plugin));

        if (plugin.getBoolean(Booleans.MODULE_SPAWNERS)) modules.add(new SpawnerManager(plugin));
        if (plugin.getBoolean(Booleans.MODULE_EXPERIENCE)) modules.add(new ExperienceManager(plugin));
        if (plugin.getBoolean(Booleans.MODULE_ELEVATOR)) modules.add(new ElevatorManager(plugin));

        loadModules();
    }

    private void loadModules() {
        modules.forEach(this::loadModule);
    }

    private void loadModule(Module module) {
        module.loadConfigurations();
        module.loadCommandsCompletions();
        module.loadListeners();
        module.loadSchedules();
        module.loadCommands();
    }

}
