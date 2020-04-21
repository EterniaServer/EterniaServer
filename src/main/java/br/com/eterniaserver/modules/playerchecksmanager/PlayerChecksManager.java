package br.com.eterniaserver.modules.playerchecksmanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.playerchecksmanager.tasks.Checks;

public class PlayerChecksManager {

    public PlayerChecksManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.playerchecks")) {
            new Checks().runTaskTimer(plugin, 20L, EterniaServer.configs.getInt("server.checks") * 20);
            Messages.ConsoleMessage("modules.enable", "Player-Checks");
        } else {
            Messages.ConsoleMessage("modules.disable", "Player-Checks");
        }
    }

}
