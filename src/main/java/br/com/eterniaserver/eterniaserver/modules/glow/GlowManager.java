package br.com.eterniaserver.eterniaserver.modules.glow;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.Module;

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
        EterniaLib.getCfgManager().registerConfiguration(
                "eterniaserver",
                "glow_messages",
                true,
                new Configurations.GlowMessageConfiguration(plugin)
        );
        EterniaLib.getCfgManager().registerConfiguration(
                "eterniaserver",
                "glow",
                true,
                new Configurations.GlowConfiguration(servicesGlow)
        );
        EterniaLib.getCfgManager().registerConfiguration(
                "eterniaserver",
                "glow_commands",
                true,
                new Configurations.GlowCommandsConfiguration()
        );
    }

    @Override
    public void loadCommandsCompletions() {
        EterniaLib.getCmdManager().getCommandCompletions().registerStaticCompletion(
                "es_colors",
                Stream.of(Enums.Color.values()).map(Enum::name).collect(Collectors.toList())
        );
    }

    @Override
    public void loadConditions() {}

    @Override
    public void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new Handlers(servicesGlow), plugin);
    }

    @Override
    public void loadSchedules() {}

    @Override
    public void loadCommands() {
        EterniaLib.getCmdManager().registerCommand(new Commands.Glow(plugin, servicesGlow));
    }

}
