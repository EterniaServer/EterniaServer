package br.com.eterniaserver.eterniaserver.modules.experiencemanager;

import br.com.eterniaserver.eterniaserver.API.Exp;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.methods.Checks;
import br.com.eterniaserver.eterniaserver.modules.experiencemanager.commands.*;
import co.aikar.commands.PaperCommandManager;

public class ExperienceManager {

    public ExperienceManager(EterniaServer plugin, Messages messages, Checks checks, Exp exp, PaperCommandManager manager) {
        if (plugin.serverConfig.getBoolean("modules.experience")) {
            manager.registerCommand(new Experience(checks, messages, exp));
            messages.sendConsole("modules.enable", "%module%", "Teleports");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Teleports");
        }
    }

}
