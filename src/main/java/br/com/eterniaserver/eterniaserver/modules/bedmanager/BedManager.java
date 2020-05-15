package br.com.eterniaserver.eterniaserver.modules.bedmanager;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.configs.methods.Checks;
import br.com.eterniaserver.eterniaserver.events.OnBedEnter;
import br.com.eterniaserver.eterniaserver.events.OnBedLeave;
import br.com.eterniaserver.eterniaserver.modules.bedmanager.tasks.AccelerateWorld;

public class BedManager {

    public BedManager(EterniaServer plugin, Messages messages, Checks checks, Vars vars) {
        if (plugin.serverConfig.getBoolean("modules.bed")) {
            if (plugin.serverConfig.getBoolean("server.async-check")) {
                plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new AccelerateWorld(plugin, messages, vars), 0L, plugin.serverConfig.getInt("server.checks") * 40);
            } else {
                plugin.getServer().getScheduler().runTaskTimer(plugin, new AccelerateWorld(plugin, messages, vars), 0L, plugin.serverConfig.getInt("server.checks") * 40);
            }
            plugin.getServer().getPluginManager().registerEvents(new OnBedEnter(messages, checks, vars), plugin);
            plugin.getServer().getPluginManager().registerEvents(new OnBedLeave(messages, checks, vars), plugin);
            messages.sendConsole("modules.enable", "%module%", "Bed");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Bed");
        }
    }

}
