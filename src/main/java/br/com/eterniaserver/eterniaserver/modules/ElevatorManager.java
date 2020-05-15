package br.com.eterniaserver.eterniaserver.modules;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;

public class ElevatorManager {

    public ElevatorManager(EterniaServer plugin, Messages messages) {
        if (plugin.serverConfig.getBoolean("modules.elevator")) {
            messages.sendConsole("modules.enable", "%module%", "Elevator");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Elevator");
        }
    }
}
