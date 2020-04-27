package br.com.eterniaserver.modules.chatmanager;


import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.chatmanager.act.AdvancedChatTorch;
import br.com.eterniaserver.modules.chatmanager.commands.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ChatManager {

    public ChatManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.chat")) {
            File chatFile = new File(plugin.getDataFolder(), "chat.yml");
            if (!chatFile.exists()) {
                plugin.saveResource("chat.yml", false);
            }
            EterniaServer.chat = new YamlConfiguration();
            try {
                EterniaServer.chat.load(chatFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            Objects.requireNonNull(plugin.getCommand("global")).setExecutor(new Global());
            Objects.requireNonNull(plugin.getCommand("local")).setExecutor(new Local());
            Objects.requireNonNull(plugin.getCommand("staff")).setExecutor(new Staff());
            Objects.requireNonNull(plugin.getCommand("broadcast")).setExecutor(new Broadcast());
            Objects.requireNonNull(plugin.getCommand("resp")).setExecutor(new Resp(plugin));
            Objects.requireNonNull(plugin.getCommand("tell")).setExecutor(new Tell(plugin));
            new AdvancedChatTorch(plugin);
            Messages.ConsoleMessage("modules.enable", "%module%", "Chat");
        } else {
            Messages.ConsoleMessage("modules.disable", "%module%", "Chat");
        }
    }

}
