package br.com.eterniaserver.eterniaserver.modules.chatmanager;


import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.AdvancedChatTorch;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.commands.*;
import br.com.eterniaserver.eterniaserver.player.PlayerManager;
import br.com.eterniaserver.eterniaserver.storages.Files;

public class ChatManager {

    public ChatManager(EterniaServer plugin, Messages messages, Strings strings, Vars vars, PlayerManager playerManager, Files files) {
        if (plugin.serverConfig.getBoolean("modules.chat")) {
            files.loadChat();
            plugin.getCommand("global").setExecutor(new Global(messages, vars));
            plugin.getCommand("local").setExecutor(new Local(messages, vars));
            plugin.getCommand("staff").setExecutor(new Staff(messages, vars));
            plugin.getCommand("broadcast").setExecutor(new Broadcast(messages, strings));
            plugin.getCommand("resp").setExecutor(new Resp(plugin, messages, strings, vars));
            plugin.getCommand("tell").setExecutor(new Tell(plugin, messages, strings, vars));
            plugin.getCommand("spy").setExecutor(new Spy(messages, vars));
            plugin.getCommand("mute").setExecutor(new Mute(plugin, messages, vars, playerManager));
            plugin.getCommand("tempmute").setExecutor(new TempMute(plugin, messages, vars, playerManager));
            plugin.getCommand("unmute").setExecutor(new UnMute(plugin, messages, vars, playerManager));
            plugin.getCommand("nickname").setExecutor(new Nickname(messages, strings));
            plugin.getCommand("mutechannels").setExecutor(new MuteChannels(plugin, messages));
            plugin.getCommand("clearchat").setExecutor(new ClearChat(messages));
            new AdvancedChatTorch(plugin, messages, vars);
            messages.sendConsole("modules.enable", "%module%", "Chat");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Chat");
        }
    }

}
