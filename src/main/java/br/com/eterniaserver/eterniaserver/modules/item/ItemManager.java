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
        EterniaLib.getCfgManager().registerConfiguration(
                "eterniaserver",
                "item",
                true,
                new Configurations.ItemMessagesConfiguration(plugin)
        );
        EterniaLib.getCfgManager().registerConfiguration(
                "eterniaserver",
                "item_commands",
                true,
                new Configurations.ItemCommandsConfiguration()
        );
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
