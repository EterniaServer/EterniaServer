package br.com.eterniaserver.eterniaserver.modules.kit;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.Entity;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.Module;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitManager implements Module {

    private final EterniaServer plugin;

    private final Services.KitService kitService = new Services.KitService();

    public KitManager(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        Configurations.KitConfiguration configuration = new Configurations.KitConfiguration(plugin, kitService);
        Configurations.KitMessagesConfiguration kitMessagesConfiguration = new Configurations.KitMessagesConfiguration(plugin);
        Configurations.KitCommandsConfiguration kitCommandsConfiguration = new Configurations.KitCommandsConfiguration();

        EterniaLib.registerConfiguration("eterniaserver", "kit", configuration);
        EterniaLib.registerConfiguration("eterniaserver", "kit_messages", kitMessagesConfiguration);
        EterniaLib.registerConfiguration("eterniaserver", "kit_commands", kitCommandsConfiguration);

        configuration.executeConfig();
        kitMessagesConfiguration.executeConfig();
        kitCommandsConfiguration.executeCritical();

        configuration.saveConfiguration(true);
        kitMessagesConfiguration.saveConfiguration(true);
        kitCommandsConfiguration.saveConfiguration(true);

        loadCommandsLocale(kitCommandsConfiguration, Enums.Commands.class);

        try {
            Entity<Entities.KitTime> kitTimeEntity = new Entity<>(Entities.KitTime.class);

            EterniaLib.addTableName("%eternia_kit_time%", plugin.getString(Strings.KIT_TABLE_NAME_TIME));

            EterniaLib.getDatabase().register(Entities.KitTime.class, kitTimeEntity);
        }
        catch (Exception exception) {
            EterniaLib.registerLog("EE-118-Kit");
        }
    }

    @Override
    public void loadCommandsCompletions() {
        EterniaLib.getCmdManager().getCommandCompletions().registerCompletion("kits", kits -> {
            Player player = kits.getPlayer();
            List<String> kitsAvailable = new ArrayList<>();
            for (String kitName : kitService.kitNames()) {
                if (player.hasPermission(plugin.getString(Strings.PERM_KIT_PREFIX) + kitName)) {
                    kitsAvailable.add(kitName);
                }
            }
            return kitsAvailable;
        });
    }

    @Override
    public void loadConditions() {
    }

    @Override
    public void loadListeners() {
    }

    @Override
    public void loadSchedules() {
    }

    @Override
    public void loadCommands() {
        EterniaLib.getCmdManager().registerCommand(new Commands.Kit(plugin, kitService));
    }

}
