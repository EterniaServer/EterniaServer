package br.com.eterniaserver.eterniaserver.modules.item;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.Module;

public class ItemManager implements Module {

    private final EterniaServer plugin;

    public ItemManager(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        Configurations.ItemMessagesConfiguration configuration = new Configurations.ItemMessagesConfiguration(plugin);
        Configurations.ItemCommandsConfiguration itemCommandsConfiguration = new Configurations.ItemCommandsConfiguration();

        EterniaLib.getCfgManager().registerConfiguration("eterniaserver", "item_messages", true, configuration);
        EterniaLib.getCfgManager().registerConfiguration("eterniaserver", "item_commands", true, itemCommandsConfiguration);
    }

    @Override
    public void loadCommandsCompletions() {
    }

    @Override
    public void loadConditions() {
    }

    @Override
    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Handlers(plugin), plugin);
    }

    @Override
    public void loadSchedules() {
    }

    @Override
    public void loadCommands() {
        EterniaLib.getCmdManager().registerCommand(new Commands.Item(plugin));
    }
}
