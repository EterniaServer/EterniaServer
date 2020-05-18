package br.com.eterniaserver.eterniaserver.modules.economymanager;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.modules.economymanager.commands.*;
import co.aikar.commands.PaperCommandManager;

public class EconomyManager {

    public EconomyManager(EterniaServer plugin, Messages messages, br.com.eterniaserver.eterniaserver.API.Money money, PaperCommandManager manager) {
        if (plugin.serverConfig.getBoolean("modules.economy")) {
            manager.registerCommand(new Economy(plugin, messages, money));
            manager.registerCommand(new EcoChange(messages, money));
            messages.sendConsole("modules.enable", "%module%", "Economy");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Economy");
        }
    }

}
