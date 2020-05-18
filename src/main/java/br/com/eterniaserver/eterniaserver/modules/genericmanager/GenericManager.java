package br.com.eterniaserver.eterniaserver.modules.genericmanager;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.dependencies.papi.PlaceHolders;
import br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.*;
import br.com.eterniaserver.eterniaserver.player.PlayerFlyState;
import br.com.eterniaserver.eterniaserver.storages.Files;
import co.aikar.commands.PaperCommandManager;

public class GenericManager {

    public GenericManager(EterniaServer plugin, Messages messages, Strings strings, PlayerFlyState playerFlyState, Files files, Vars vars, PlaceHolders placeHolders, PaperCommandManager manager) {
        if (plugin.serverConfig.getBoolean("modules.generic")) {
            manager.registerCommand(new Gamemode(messages));
            manager.registerCommand(new Inventory(messages));
            manager.registerCommand(new Others(messages, files, placeHolders, strings, vars, playerFlyState));
            manager.registerCommand(new Replaces(plugin, messages, strings, vars));
            manager.registerCommand(new Simplifications(messages));
            messages.sendConsole("modules.enable", "%module%", "Generic");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Generic");
        }
    }

}
