package br.com.eterniaserver.modules;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.antinethertrapmanager.NetherTimer;

public class AntiNetherTrapManager {

    public AntiNetherTrapManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.playerchecks")) {
            new NetherTimer().runTaskTimer(plugin, 20L, EterniaServer.configs.getInt("server.checks") * 20);
            Messages.ConsoleMessage("modules.enable", "Player-Checks");
        } else {
            Messages.ConsoleMessage("modules.disable", "Player-Checks");
        }
    }

}
