package br.com.eterniaserver.eterniaserver.modules;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.Module;
import br.com.eterniaserver.eterniaserver.modules.core.CoreManager;
import br.com.eterniaserver.eterniaserver.modules.spawner.SpawnerManager;

import java.util.ArrayList;
import java.util.List;

public class Manager {

    private final List<Module> modules = new ArrayList<>();

    public Manager(final EterniaServer plugin) {
        modules.add(new CoreManager(plugin));
        modules.add(new SpawnerManager(plugin));

        loadModules();
    }

    private void loadModules() {
        for (final Module module : modules) {
            module.loadConfigurations();
            module.loadCommandsLocales();
            module.loadCommandsCompletions();
            module.loadListeners();
            module.loadSchedules();
            module.loadCommands();
        }
    }

}
