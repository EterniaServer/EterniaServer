package br.com.eterniaserver.modules.economymanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.economymanager.commands.*;

import java.util.Objects;

public class EconomyManager {

    public EconomyManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.economy")) {
            Objects.requireNonNull(plugin.getCommand("pay")).setExecutor(new Pay());
            Objects.requireNonNull(plugin.getCommand("money")).setExecutor(new Money());
            Objects.requireNonNull(plugin.getCommand("baltop")).setExecutor(new Baltop(plugin));
            Objects.requireNonNull(plugin.getCommand("eco")).setExecutor(new Eco());
            Messages.ConsoleMessage("modules.enable", "%module%", "Economy");
        } else {
            Messages.ConsoleMessage("modules.disable", "%module%", "Economy");
        }
    }

}
