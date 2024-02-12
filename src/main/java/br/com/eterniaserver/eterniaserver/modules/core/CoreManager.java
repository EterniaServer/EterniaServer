package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.acf.ConditionFailedException;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.Entity;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.modules.Module;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.core.Commands.Afk;
import br.com.eterniaserver.eterniaserver.modules.core.Commands.EGameMode;
import br.com.eterniaserver.eterniaserver.modules.core.Commands.Generic;
import br.com.eterniaserver.eterniaserver.modules.core.Commands.GodMode;
import br.com.eterniaserver.eterniaserver.modules.core.Commands.Inventory;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.Revision;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;


public class CoreManager implements Module {

    private final Map<String, Utils.CommandData> customCommandMap = new HashMap<>();

    private final EterniaServer plugin;

    private Services.Afk afkServices;

    public CoreManager(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void loadConfigurations() {
        Configurations.MainConfiguration configuration = new Configurations.MainConfiguration(plugin, customCommandMap);

        EterniaLib.registerConfiguration("eterniaserver", "core", configuration);

        configuration.executeConfig();
        configuration.executeCritical();
        configuration.saveConfiguration(true);

        loadCommandsLocale(configuration, Enums.Commands.class);

        try {
            Entity<Revision> revisionEntity = new Entity<>(Revision.class);
            Entity<PlayerProfile> profileEntity = new Entity<>(PlayerProfile.class);

            EterniaLib.addTableName("%eternia_server_revision%", plugin.getString(Strings.REVISION_TABLE_NAME));
            EterniaLib.addTableName("%eternia_server_profile%", plugin.getString(Strings.PROFILE_TABLE_NAME));

            EterniaLib.getDatabase().register(Revision.class, revisionEntity);
            EterniaLib.getDatabase().register(PlayerProfile.class, profileEntity);
        }
        catch (Exception exception) {
            EterniaLib.registerLog("EE-103-Revision");
            return;
        }

        List<Revision> revisions = EterniaLib.getDatabase().listAll(Revision.class);
        this.plugin.getLogger().log(Level.INFO, "Core module: {0} revisions loaded", revisions.size());

        List<PlayerProfile> playerProfiles = EterniaLib.getDatabase().listAll(PlayerProfile.class);
        this.plugin.getLogger().log(Level.INFO, "Core module: {0} player profiles loaded", playerProfiles.size());

        this.afkServices = new Services.Afk(plugin);
        EterniaServer.setGuiAPI(new Services.GUI(plugin));

        if (plugin.getBoolean(Booleans.CUSTOM_COMMANDS)) {
            customCommandMap.forEach((commandName, commandObject) -> new Utils.CustomCommand(
                    plugin,
                    commandName,
                    commandObject.description(),
                    commandObject.aliases(),
                    commandObject.text(),
                    commandObject.commands(),
                    commandObject.console()
            ));
        }
    }

    @Override
    public void loadCommandsCompletions() {}

    @Override
    public void loadConditions() {
        EterniaLib.getCmdManager().getCommandConditions().addCondition(Integer.class, "limits", (c, exec, value) -> {
            if (value == null || c.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException("O valor mínimo precisa ser &3" + c.getConfigValue("min", 0));
            }
            if (c.getConfigValue("max", 3) < value) {
                throw new ConditionFailedException("O valor máximo precisa ser &3 " + c.getConfigValue("max", 2147483647));
            }
        });

        EterniaLib.getCmdManager().getCommandConditions().addCondition(Double.class, "limits", (c, exec, value) -> {
            if (value == null || c.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException("O valor mínimo precisa ser &3" + c.getConfigValue("min", 0));
            }
            if (c.getConfigValue("max", 3) < value) {
                throw new ConditionFailedException("O valor máximo precisa ser &3" + c.getConfigValue("max", 1));
            }
        });
    }

    @Override
    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Handlers(plugin, afkServices), plugin);
    }

    @Override
    public void loadSchedules() {
        new Schedules.MainTick(plugin, afkServices).runTaskTimer(plugin, 20L, plugin.getInteger(Integers.PLUGIN_TICKS));
    }

    @Override
    public void loadCommands() {
        EterniaLib.getCmdManager().registerCommand(new Generic(plugin));
        EterniaLib.getCmdManager().registerCommand(new EGameMode(plugin));
        EterniaLib.getCmdManager().registerCommand(new Afk(plugin));
        EterniaLib.getCmdManager().registerCommand(new GodMode(plugin));
        EterniaLib.getCmdManager().registerCommand(new Inventory(plugin));
    }

}
