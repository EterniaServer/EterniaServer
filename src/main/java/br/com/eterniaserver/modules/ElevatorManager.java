package br.com.eterniaserver.modules;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;

public class ElevatorManager {
    public ElevatorManager() {
        if (EterniaServer.configs.getBoolean("modules.elevator")) {
            Messages.ConsoleMessage("modules.enable", "Elevator");
        } else {
            Messages.ConsoleMessage("modules.disable", "Elevator");
        }
    }
}
