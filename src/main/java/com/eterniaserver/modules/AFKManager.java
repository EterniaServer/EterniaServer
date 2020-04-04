package com.eterniaserver.modules;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.modules.afkmanager.AFKTimer;

public class AFKManager {
    public AFKManager(EterniaServer plugin) {
        if (CVar.getBool("modules.afk")) {
            if (CVar.getBool("server.async-check")) {
                new AFKTimer().runTaskTimerAsynchronously(plugin, 20L, CVar.getInt("server.checks") * 200);
            } else {
                new AFKTimer().runTaskTimer(plugin, 20L, CVar.getInt("server.checks") * 200);
            }
        }
    }
}
