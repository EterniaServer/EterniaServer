package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.acf.ConditionFailedException;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.Entity;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.modules.Module;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.chat.Configurations.ChatConfiguration;
import br.com.eterniaserver.eterniaserver.modules.chat.Configurations.ChatCommand;
import br.com.eterniaserver.eterniaserver.modules.chat.Configurations.ChatMessages;
import br.com.eterniaserver.eterniaserver.modules.chat.Configurations.ChatChannels;
import br.com.eterniaserver.eterniaserver.modules.chat.Entities.ChatInfo;

import github.scarsz.discordsrv.DiscordSRV;

import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class ChatManager implements Module {

    private final EterniaServer plugin;
    private final Services.CraftChat craftChatService;

    public ChatManager(EterniaServer plugin) {
        this.plugin = plugin;
        this.craftChatService = new Services.CraftChat(plugin);

        EterniaServer.setChatAPI(craftChatService);
    }

    @Override
    public void loadConfigurations() {
        EterniaLib.getCfgManager().registerConfiguration(
                "eterniaserver",
                "chat_messages",
                true,
                new ChatMessages(plugin)
        );
        EterniaLib.getCfgManager().registerConfiguration(
                "eterniaserver",
                "chat_commands",
                true,
                new ChatCommand()
        );
        EterniaLib.getCfgManager().registerConfiguration(
                "eterniaserver",
                "chat_channels",
                true,
                new ChatChannels(craftChatService)
        );
        EterniaLib.getCfgManager().registerConfiguration(
                "eterniaserver",
                "chat",
                true,
                new ChatConfiguration(plugin, craftChatService)
        );

        try {
            Entity<ChatInfo> chatInfoEntity = new Entity<>(ChatInfo.class);
            EterniaLib.getDatabase().addTableName("%eternia_server_chat%", plugin.getString(Strings.CHAT_TABLE_NAME));
            EterniaLib.getDatabase().register(ChatInfo.class, chatInfoEntity);
        }
        catch (Exception exception) {
            //todo EterniaLib.registerLog("EE-103-Revision");
            return;
        }

        List<ChatInfo> chatInfos = EterniaLib.getDatabase().listAll(ChatInfo.class);
        this.plugin.getLogger().log(Level.INFO, "Core module: {0} revisions loaded", chatInfos.size());
    }

    @Override
    public void loadCommandsCompletions() {
        EterniaLib.getCmdManager().getCommandCompletions().registerCompletion("channels", c -> craftChatService.channels);
    }

    @Override
    public void loadConditions() {
        EterniaLib.getCmdManager().getCommandConditions().addCondition(String.class, "channel", (c, exec, value) -> {
            if (value == null || !craftChatService.channels.contains(value)) {
                throw new ConditionFailedException("Você precisa informar um canal válido");
            }
        });
    }

    @Override
    public void loadListeners() {
        Plugin discordSrv = plugin.getServer().getPluginManager().getPlugin("DiscordSRV");
        Optional<Plugin> discordSrvOptional = Optional.ofNullable(discordSrv);

        if (discordSrvOptional.isPresent() && plugin.getBoolean(Booleans.DISCORD_SRV)) {
            DiscordSRV.api.subscribe(new Handlers.DiscordSRVHandler());
        }

        plugin.getServer().getPluginManager().registerEvents(new Handlers(plugin, craftChatService), plugin);
    }

    @Override
    public void loadSchedules() { }

    @Override
    public void loadCommands() {
        EterniaLib.getCmdManager().registerCommand(new Commands.Mute(plugin, craftChatService));
        EterniaLib.getCmdManager().registerCommand(new Commands.Generic(craftChatService));
        EterniaLib.getCmdManager().registerCommand(new Commands.Chat(plugin, craftChatService));
    }

}
