package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.acf.ConditionFailedException;
import br.com.eterniaserver.eternialib.CommandManager;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.Module;


public class ChatManager implements Module {

    private final EterniaServer plugin;
    private final Services.ChatService chatService;

    public ChatManager(final EterniaServer plugin) {
        this.plugin = plugin;
        this.plugin.setChatAPI(new Services.CraftChat(plugin));
        this.chatService = new Services.ChatService(plugin);
    }

    @Override
    public void loadConfigurations() {
        new Configurations.Configs(plugin, chatService);
    }

    @Override
    public void loadConditions() {
        CommandManager.getCommandConditions().addCondition(String.class, "channel", (c, exec, value) -> {
            if (value == null || !chatService.channels.contains(value)) {
                throw new ConditionFailedException("Você precisa informar um canal válido");
            }
        });
    }

    @Override
    public void loadCommandsCompletions() {

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

    }

    @Override
    public void reloadConfigurations() {

    }
}
