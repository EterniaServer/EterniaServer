package br.com.eterniaserver.modules;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.homesmanager.commands.*;

import java.util.Objects;

public class HomesManager {
    public HomesManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.home")) {
            Objects.requireNonNull(plugin.getCommand("delhouse")).setExecutor(new DelHome());
            Objects.requireNonNull(plugin.getCommand("house")).setExecutor(new Home(plugin));
            Objects.requireNonNull(plugin.getCommand("houses")).setExecutor(new Homes(plugin));
            Objects.requireNonNull(plugin.getCommand("sethouse")).setExecutor(new SetHome());
            Messages.ConsoleMessage("modules.enable", "Homes");
        } else {
            Messages.ConsoleMessage("modules.disable", "Homes");
        }
    }
}
