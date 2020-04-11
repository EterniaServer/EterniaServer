package br.com.eterniaserver.modules;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.modules.spawnermanager.commands.SpawnerGive;

import java.util.Objects;

public class SpawnersManager {

    public SpawnersManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.spawners")) {
            Objects.requireNonNull(plugin.getCommand("spawnergive")).setExecutor(new SpawnerGive());
            new ConsoleMessage("modules.enable", "Spawners");
        } else {
            new ConsoleMessage("modules.disable", "Spawners");
        }
    }

}
