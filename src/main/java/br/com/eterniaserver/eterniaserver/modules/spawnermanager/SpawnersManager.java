package br.com.eterniaserver.eterniaserver.modules.spawnermanager;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.modules.spawnermanager.commands.SpawnerGive;
import co.aikar.commands.PaperCommandManager;

public class SpawnersManager {

    public SpawnersManager(EterniaServer plugin, Messages messages, PaperCommandManager manager) {
        if (plugin.serverConfig.getBoolean("modules.spawners")) {
            manager.registerCommand(new SpawnerGive(plugin, messages));
            messages.sendConsole("modules.enable", "%module%", "Spawners");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Spawners");
        }
    }

}
