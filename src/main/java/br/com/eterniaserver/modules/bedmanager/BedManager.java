package br.com.eterniaserver.modules.bedmanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.methods.Checks;
import br.com.eterniaserver.events.OnBedEnter;
import br.com.eterniaserver.events.OnBedLeave;
import br.com.eterniaserver.modules.bedmanager.tasks.AccelerateWorld;

public class BedManager {

    public BedManager(EterniaServer plugin, Messages messages, Checks checks, Vars vars) {
        if (plugin.serverConfig.getBoolean("modules.bed")) {
            if (plugin.serverConfig.getBoolean("server.async-check")) {
                plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new AccelerateWorld(plugin, messages, vars), 0L, plugin.serverConfig.getInt("server.checks") * 20);
            } else {
                plugin.getServer().getScheduler().runTaskTimer(plugin, new AccelerateWorld(plugin, messages, vars), 0L, plugin.serverConfig.getInt("server.checks") * 20);
            }
            plugin.getServer().getPluginManager().registerEvents(new OnBedEnter(messages, checks, vars), plugin);
            plugin.getServer().getPluginManager().registerEvents(new OnBedLeave(messages, checks, vars), plugin);
            messages.ConsoleMessage("modules.enable", "%module%", "Bed");
        } else {
            messages.ConsoleMessage("modules.disable", "%module%", "Bed");
        }
    }

}
