package br.com.eterniaserver.eterniaserver.modules.experiencemanager;

import br.com.eterniaserver.eterniaserver.API.Exp;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.methods.Checks;
import br.com.eterniaserver.eterniaserver.modules.experiencemanager.commands.*;

public class ExperienceManager {

    public ExperienceManager(EterniaServer plugin, Messages messages, Checks checks, Exp exp) {
        if (plugin.serverConfig.getBoolean("modules.experience")) {
            plugin.getCommand("depositlvl").setExecutor(new DepositLevel(checks, messages, exp));
            plugin.getCommand("withdrawlvl").setExecutor(new WithdrawLevel(checks, messages, exp));
            plugin.getCommand("bottlelvl").setExecutor(new BottleLevel(checks, messages));
            plugin.getCommand("checklvl").setExecutor(new CheckLevel(messages, exp));
            messages.ConsoleMessage("modules.enable", "%module%", "Teleports");
        } else {
            messages.ConsoleMessage("modules.disable", "%module%", "Teleports");
        }
    }

}
