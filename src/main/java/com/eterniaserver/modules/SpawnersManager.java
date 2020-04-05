package com.eterniaserver.modules;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.modules.spawnermanager.commands.*;

import java.util.Objects;

public class SpawnersManager {

    public SpawnersManager(EterniaServer plugin) {
        if (CVar.getBool("modules.spawners")) {
            Objects.requireNonNull(plugin.getCommand("spawnergive")).setExecutor(new SpawnerGive());
            new ConsoleMessage("modules.enable", "Spawners");
        } else {
            new ConsoleMessage("modules.disable", "Spawners");
        }
    }

}
