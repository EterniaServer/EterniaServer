package br.com.eterniaserver.modules.chatmanager;


import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.chatmanager.act.AdvancedChatTorch;
import br.com.eterniaserver.modules.chatmanager.commands.*;
import br.com.eterniaserver.player.PlayerManager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ChatManager {

    public ChatManager(EterniaServer plugin, Messages messages, Strings strings, Vars vars, PlayerManager playerManager) {
        if (plugin.serverConfig.getBoolean("modules.chat")) {
            File chatFile = new File(plugin.getDataFolder(), "chat.yml");
            if (!chatFile.exists()) {
                plugin.saveResource("chat.yml", false);
            }
            plugin.chatConfig = new YamlConfiguration();
            try {
                plugin.chatConfig.load(chatFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            File fileGroups = new File(plugin.getDataFolder(), "groups.yml");
            if (!fileGroups.exists()) {
                plugin.saveResource("groups.yml", false);
            }
            plugin.groupConfig = new YamlConfiguration();
            try {
                plugin.groupConfig.load(fileGroups);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            File placeholdersFile = new File(plugin.getDataFolder(), "customplaceholders.yml");
            if (!placeholdersFile.exists()) {
                plugin.saveResource("customplaceholders.yml", false);
            }
            plugin.placeholderConfig = new YamlConfiguration();
            try {
                plugin.placeholderConfig.load(placeholdersFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            Objects.requireNonNull(plugin.getCommand("global")).setExecutor(new Global(messages, vars));
            Objects.requireNonNull(plugin.getCommand("local")).setExecutor(new Local(messages, vars));
            Objects.requireNonNull(plugin.getCommand("staff")).setExecutor(new Staff(messages, vars));
            Objects.requireNonNull(plugin.getCommand("broadcast")).setExecutor(new Broadcast(messages, strings));
            Objects.requireNonNull(plugin.getCommand("resp")).setExecutor(new Resp(plugin, messages, strings, vars));
            Objects.requireNonNull(plugin.getCommand("tell")).setExecutor(new Tell(plugin, messages, strings, vars));
            Objects.requireNonNull(plugin.getCommand("spy")).setExecutor(new Spy(messages, vars));
            Objects.requireNonNull(plugin.getCommand("mute")).setExecutor(new Mute(plugin, messages, vars, playerManager));
            Objects.requireNonNull(plugin.getCommand("tempmute")).setExecutor(new TempMute(plugin, messages, vars, playerManager));
            Objects.requireNonNull(plugin.getCommand("unmute")).setExecutor(new UnMute(plugin, messages, vars, playerManager));
            new AdvancedChatTorch(plugin, messages, vars);
            messages.ConsoleMessage("modules.enable", "%module%", "Chat");
        } else {
            messages.ConsoleMessage("modules.disable", "%module%", "Chat");
        }
    }

}
