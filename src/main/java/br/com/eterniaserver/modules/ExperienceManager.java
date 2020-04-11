package br.com.eterniaserver.modules;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.modules.experiencemanager.commands.*;
import br.com.eterniaserver.configs.methods.ConsoleMessage;

import java.util.Objects;

public class ExperienceManager {

    public ExperienceManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.experience")) {
            Objects.requireNonNull(plugin.getCommand("depositlvl")).setExecutor(new DepositLevel());
            Objects.requireNonNull(plugin.getCommand("withdrawlvl")).setExecutor(new WithdrawLevel());
            Objects.requireNonNull(plugin.getCommand("bottlexp")).setExecutor(new Bottlexp());
            Objects.requireNonNull(plugin.getCommand("checklevel")).setExecutor(new CheckLevel());
            new ConsoleMessage("modules.enable", "Teleports");
        } else {
            new ConsoleMessage("modules.disable", "Teleports");
        }
    }

}
