package br.com.eterniaserver.eterniaserver.modules.economymanager;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.modules.economymanager.commands.*;

public class EconomyManager {

    public EconomyManager(EterniaServer plugin, Messages messages, br.com.eterniaserver.eterniaserver.API.Money money) {
        if (plugin.serverConfig.getBoolean("modules.economy")) {
            plugin.getCommand("pay").setExecutor(new Pay(messages, money));
            plugin.getCommand("money").setExecutor(new Money(messages, money));
            plugin.getCommand("baltop").setExecutor(new Baltop(plugin, messages, money));
            plugin.getCommand("eco").setExecutor(new Eco(messages, money));
            messages.ConsoleMessage("modules.enable", "%module%", "Economy");
        } else {
            messages.ConsoleMessage("modules.disable", "%module%", "Economy");
        }
    }

}
