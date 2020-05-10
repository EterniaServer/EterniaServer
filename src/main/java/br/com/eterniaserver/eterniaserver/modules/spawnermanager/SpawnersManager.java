package br.com.eterniaserver.eterniaserver.modules.spawnermanager;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.modules.spawnermanager.commands.SpawnerGive;

public class SpawnersManager {

    public SpawnersManager(EterniaServer plugin, Messages messages, Strings strings) {
        if (plugin.serverConfig.getBoolean("modules.spawners")) {
           plugin.getCommand("spawnergive").setExecutor(new SpawnerGive(plugin, messages, strings));
            messages.ConsoleMessage("modules.enable", "%module%", "Spawners");
        } else {
            messages.ConsoleMessage("modules.disable", "%module%", "Spawners");
        }
    }

}
