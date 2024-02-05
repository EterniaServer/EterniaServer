package br.com.eterniaserver.eterniaserver.modules.teleport;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.Entity;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.Module;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import org.bukkit.entity.Player;

public class TeleportManager implements Module {

    private final Services.HomeService homeService;
    private final EterniaServer plugin;

    public TeleportManager(EterniaServer plugin) {
        this.plugin = plugin;
        this.homeService = new Services.HomeService(plugin);
    }

    @Override
    public void loadConfigurations() {
        Configurations.TeleportConfiguration configuration = new Configurations.TeleportConfiguration(plugin);

        EterniaLib.registerConfiguration("eterniaserver", "teleport", configuration);

        configuration.executeConfig();
        configuration.executeCritical();
        configuration.saveConfiguration(true);

        loadCommandsLocale(configuration, Enums.Commands.class);

        try {
            Entity<Entities.HomeLocation> homeLocationEntity = new Entity<>(Entities.HomeLocation.class);

            EterniaLib.addTableName("%eternia_home%", plugin.getString(Strings.TELEPORT_TABLE_NAME_HOME));

            EterniaLib.getDatabase().register(Entities.HomeLocation.class, homeLocationEntity);
        }
        catch (Exception exception) {
            EterniaLib.registerLog("EE-123-Teleport");
        }

        homeService.setHomes(EterniaLib.getDatabase().listAll(Entities.HomeLocation.class));
    }

    @Override
    public void loadCommandsCompletions() {
        EterniaLib.getCmdManager().getCommandCompletions().registerCompletion("homes", homes -> {
            Player player = homes.getPlayer();
            return homeService.getHomeNames(player.getUniqueId());
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
        EterniaLib.getCmdManager().registerCommand(new Commands.Tpa(plugin));
        EterniaLib.getCmdManager().registerCommand(new Commands.Home(plugin, homeService));
    }
}
