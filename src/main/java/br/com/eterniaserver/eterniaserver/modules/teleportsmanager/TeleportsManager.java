package br.com.eterniaserver.eterniaserver.modules.teleportsmanager;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.commands.*;

import co.aikar.commands.PaperCommandManager;

public class TeleportsManager {

    public TeleportsManager(EterniaServer plugin) {

        final PaperCommandManager manager = plugin.getManager();
        final EFiles messages = plugin.getEFiles();

        if (plugin.serverConfig.getBoolean("modules.teleports")) {
            manager.registerCommand(new WarpSystem(plugin));
            manager.registerCommand(new TeleportSystem(plugin));
            messages.sendConsole("modules.enable", "%module%", "Teleports");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Teleports");
        }
    }

}
