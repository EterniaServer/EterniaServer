package br.com.eterniaserver.modules.experiencemanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.experiencemanager.commands.*;

import java.util.Objects;

public class ExperienceManager {

    public ExperienceManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.experience")) {
            Objects.requireNonNull(plugin.getCommand("depositlvl")).setExecutor(new DepositLevel());
            Objects.requireNonNull(plugin.getCommand("withdrawlvl")).setExecutor(new WithdrawLevel());
            Objects.requireNonNull(plugin.getCommand("bottlelvl")).setExecutor(new BottleLevel());
            Objects.requireNonNull(plugin.getCommand("checklvl")).setExecutor(new CheckLevel());
            Messages.ConsoleMessage("modules.enable", "%module%", "Teleports");
        } else {
            Messages.ConsoleMessage("modules.disable", "%module%", "Teleports");
        }
    }

}
