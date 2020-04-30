package br.com.eterniaserver.modules.spawnermanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import br.com.eterniaserver.modules.spawnermanager.commands.SpawnerGive;

import java.util.Objects;

public class SpawnersManager {

    public SpawnersManager(EterniaServer plugin, Messages messages, Strings strings) {
        if (plugin.serverConfig.getBoolean("modules.spawners")) {
            Objects.requireNonNull(plugin.getCommand("spawnergive")).setExecutor(new SpawnerGive(plugin, messages, strings));
            messages.ConsoleMessage("modules.enable", "%module%", "Spawners");
        } else {
            messages.ConsoleMessage("modules.disable", "%module%", "Spawners");
        }
    }

}
