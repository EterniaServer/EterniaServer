package com.eterniaserver.modules;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.modules.experiencemanager.commands.*;

import java.util.Objects;

public class ExperienceManager {
    public ExperienceManager(EterniaServer plugin) {
        if (CVar.getBool("modules.experience")) {
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
