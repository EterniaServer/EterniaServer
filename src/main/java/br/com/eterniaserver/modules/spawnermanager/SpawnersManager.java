package br.com.eterniaserver.modules.spawnermanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.spawnermanager.commands.SpawnerGive;

import java.util.Objects;

public class SpawnersManager {

    public SpawnersManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.spawners")) {
            Objects.requireNonNull(plugin.getCommand("spawnergive")).setExecutor(new SpawnerGive());
            Messages.ConsoleMessage("modules.enable", "%module%", "Spawners");
        } else {
            Messages.ConsoleMessage("modules.disable", "%module%", "Spawners");
        }
    }

}
