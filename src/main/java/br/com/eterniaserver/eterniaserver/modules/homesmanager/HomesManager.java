package br.com.eterniaserver.eterniaserver.modules.homesmanager;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.homesmanager.commands.*;

import co.aikar.commands.PaperCommandManager;


public class HomesManager {


    public HomesManager(EterniaServer plugin) {

        final EFiles messages = plugin.getEFiles();
        final PaperCommandManager manager = plugin.getManager();

        if (plugin.serverConfig.getBoolean("modules.home")) {
            manager.registerCommand(new HomeSystem(plugin));
            messages.sendConsole("modules.enable", "%module%", "Homes");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Homes");
        }
    }

}
