package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.acf.ConditionFailedException;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.Entity;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.Module;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import java.util.List;
import java.util.logging.Level;

public class ChatManager implements Module {

    private final EterniaServer plugin;
    private final Services.Chat chatService;

    public ChatManager(EterniaServer plugin) {
        this.plugin = plugin;
        this.chatService = new Services.Chat(plugin);

        EterniaServer.setChatAPI(chatService);
    }

    @Override
    public void loadConfigurations() {
        Configurations.ChatConfiguration configuration = new Configurations.ChatConfiguration(plugin, chatService);

        EterniaLib.registerConfiguration("eterniaserver", "chat", configuration);

        configuration.executeConfig();
        configuration.executeCritical();
        configuration.saveConfiguration(true);

        loadCommandsLocale(configuration, Enums.Commands.class);

        try {
            Entity<Entities.ChatInfo> chatInfoEntity = new Entity<>(Entities.ChatInfo.class);

            EterniaLib.addTableName("%eternia_server_chat%", plugin.getString(Strings.CHAT_TABLE_NAME));

            EterniaLib.getDatabase().register(Entities.ChatInfo.class, chatInfoEntity);
        }
        catch (Exception exception) {
            EterniaLib.registerLog("EE-103-Revision");
            return;
        }

        List<Entities.ChatInfo> chatInfos = EterniaLib.getDatabase().listAll(Entities.ChatInfo.class);
        this.plugin.getLogger().log(Level.INFO, "Core module: {0} revisions loaded", chatInfos.size());
    }

    @Override
    public void loadCommandsCompletions() {

    }

    @Override
    public void loadConditions() {
        EterniaLib.getCmdManager().getCommandConditions().addCondition(String.class, "channel", (c, exec, value) -> {
            if (value == null || !chatService.channels.contains(value)) {
                throw new ConditionFailedException("Você precisa informar um canal válido");
            }
        });
    }

    @Override
    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Handlers(plugin, chatService), plugin);
    }

    @Override
    public void loadSchedules() {

    }

    @Override
    public void loadCommands() {
        EterniaLib.getCmdManager().registerCommand(new Commands.Generic(plugin, chatService));
        EterniaLib.getCmdManager().registerCommand(new Commands.Chat(plugin, chatService));
    }

}
