package br.com.eterniaserver.eterniaserver.modules.glow;

import br.com.eterniaserver.eternialib.CommandManager;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.Module;

import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GlowManager implements Module {

    private final EterniaServer plugin;

    private final Services.Glow servicesGlow;

    public GlowManager(final EterniaServer plugin) {
        this.plugin = plugin;
        this.servicesGlow = new Services.Glow(plugin);
    }

    @Override
    public void loadConfigurations() {
        new Configurations.Configs(servicesGlow);
        new Configurations.Locales(plugin);

        loadCommandsLocales(new Configurations.CommandsLocales(), Enums.Commands.class);
    }

    @Override
    public void loadCommandsCompletions() {
        CommandManager.getCommandCompletions().registerStaticCompletion(
                "es_colors",
                Stream.of(Enums.Color.values()).map(Enum::name).collect(Collectors.toList())
        );
    }

    @Override
    public void loadListeners() {
        plugin.getLogger().log(Level.INFO, "Glow module: no listeners");
    }

    @Override
    public void loadSchedules() {
        plugin.getLogger().log(Level.INFO, "Glow module: no schedules");
    }

    @Override
    public void loadCommands() {
        CommandManager.registerCommand(new Commands.Glow(plugin, servicesGlow));
    }

    @Override
    public void reloadConfigurations() {
        new Configurations.Configs(servicesGlow);
        new Configurations.Locales(plugin);
    }

}
