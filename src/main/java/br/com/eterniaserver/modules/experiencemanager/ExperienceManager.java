package br.com.eterniaserver.modules.experiencemanager;

import br.com.eterniaserver.API.Exp;
import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.methods.Checks;
import br.com.eterniaserver.modules.experiencemanager.commands.*;

import java.util.Objects;

public class ExperienceManager {

    public ExperienceManager(EterniaServer plugin, Messages messages, Checks checks, Exp exp) {
        if (plugin.serverConfig.getBoolean("modules.experience")) {
            Objects.requireNonNull(plugin.getCommand("depositlvl")).setExecutor(new DepositLevel(checks, messages, exp));
            Objects.requireNonNull(plugin.getCommand("withdrawlvl")).setExecutor(new WithdrawLevel(checks, messages, exp));
            Objects.requireNonNull(plugin.getCommand("bottlelvl")).setExecutor(new BottleLevel(checks, messages));
            Objects.requireNonNull(plugin.getCommand("checklvl")).setExecutor(new CheckLevel(messages, exp));
            messages.ConsoleMessage("modules.enable", "%module%", "Teleports");
        } else {
            messages.ConsoleMessage("modules.disable", "%module%", "Teleports");
        }
    }

}
