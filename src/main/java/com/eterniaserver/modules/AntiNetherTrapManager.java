package com.eterniaserver.modules;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.MVar;
import com.eterniaserver.modules.antinethertrapmanager.NetherTimer;

public class AntiNetherTrapManager {
    public AntiNetherTrapManager(EterniaServer plugin) {
        if (CVar.getBool("modules.anti-nether-trap")) {
            new NetherTimer().runTaskTimer(EterniaServer.getMain(), 20L, plugin.getConfig().getInt("server.nether-check") * 20);
            MVar.consoleReplaceMessage("modules.enable", "Anti-Nether-Trap");
        } else {
            MVar.consoleReplaceMessage("modules.disable", "Anti-Nether-Trap");
        }
    }
}
