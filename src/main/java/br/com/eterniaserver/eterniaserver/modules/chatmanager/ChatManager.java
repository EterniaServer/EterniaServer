package br.com.eterniaserver.eterniaserver.modules.chatmanager;


import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.AdvancedChatTorch;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.commands.*;
import br.com.eterniaserver.eterniaserver.player.PlayerManager;
import br.com.eterniaserver.eterniaserver.storages.Files;
import co.aikar.commands.PaperCommandManager;

public class ChatManager {

    public ChatManager(EterniaServer plugin, Messages messages, Strings strings, Vars vars, PlayerManager playerManager, Files files, PaperCommandManager manager) {
        if (plugin.serverConfig.getBoolean("modules.chat")) {
            files.loadChat();
            manager.registerCommand(new Channels(vars, messages));
            manager.registerCommand(new Mute(plugin, messages, vars, playerManager));
            manager.registerCommand(new Others(messages, strings, vars));
            new AdvancedChatTorch(plugin, messages, vars);
            messages.sendConsole("modules.enable", "%module%", "Chat");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Chat");
        }
    }

}
