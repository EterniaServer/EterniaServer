package com.eterniaserver.modules;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.MVar;
import com.eterniaserver.modules.spawnermanager.commands.*;

import java.util.Objects;

public class SpawnersManager {

    public SpawnersManager(EterniaServer plugin) {
        if (CVar.getBool("modules.spawners")) {
            Objects.requireNonNull(plugin.getCommand("spawnergive")).setExecutor(new SpawnerGive());
            MVar.consoleReplaceMessage("modules.enable", "Spawners");
        } else {
            MVar.consoleReplaceMessage("modules.disable", "Spawners");
        }
    }

}
