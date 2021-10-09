package br.com.eterniaserver.eterniaserver.modules;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.Module;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.modules.core.CoreManager;
import br.com.eterniaserver.eterniaserver.modules.spawner.SpawnerManager;

import java.util.HashMap;
import java.util.Map;

public class Manager {

    private final EterniaServer plugin;

    private final Map<Booleans, Module> modules = new HashMap<>();

    public Manager(final EterniaServer plugin) {
        this.plugin = plugin;

        loadModule(new CoreManager(plugin));

        modules.put(Booleans.MODULE_SPAWNERS, new SpawnerManager(plugin));

        loadModules();
    }

    private void loadModules() {
        modules.forEach((moduleEnum, module) -> {
            if (plugin.getBoolean(moduleEnum)) {
                loadModule(module);
            }
        });
    }

    private void loadModule(Module module) {
        module.loadConfigurations();
        module.loadCommandsLocales();
        module.loadCommandsCompletions();
        module.loadListeners();
        module.loadSchedules();
        module.loadCommands();
    }

}
