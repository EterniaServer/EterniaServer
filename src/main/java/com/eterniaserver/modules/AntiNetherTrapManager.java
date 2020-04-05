package com.eterniaserver.modules;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.modules.antinethertrapmanager.NetherTimer;

public class AntiNetherTrapManager {
    public AntiNetherTrapManager(EterniaServer plugin) {
        if (CVar.getBool("modules.anti-nether-trap")) {
            new NetherTimer().runTaskTimer(plugin, 20L, CVar.getInt("server.checks") * 20);
            new ConsoleMessage("modules.enable", "Anti-Nether-Trap");
        } else {
            new ConsoleMessage("modules.disable", "Anti-Nether-Trap");
        }
    }
}
