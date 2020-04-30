package br.com.eterniaserver.modules.economymanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.economymanager.commands.*;

import java.util.Objects;

public class EconomyManager {

    public EconomyManager(EterniaServer plugin, Messages messages, br.com.eterniaserver.API.Money money) {
        if (plugin.serverConfig.getBoolean("modules.economy")) {
            Objects.requireNonNull(plugin.getCommand("pay")).setExecutor(new Pay(messages, money));
            Objects.requireNonNull(plugin.getCommand("money")).setExecutor(new Money(messages, money));
            Objects.requireNonNull(plugin.getCommand("baltop")).setExecutor(new Baltop(plugin, messages, money));
            Objects.requireNonNull(plugin.getCommand("eco")).setExecutor(new Eco(messages, money));
            messages.ConsoleMessage("modules.enable", "%module%", "Economy");
        } else {
            messages.ConsoleMessage("modules.disable", "%module%", "Economy");
        }
    }

}
