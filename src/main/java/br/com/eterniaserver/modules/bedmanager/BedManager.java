package br.com.eterniaserver.modules.bedmanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.events.OnBedEnter;
import br.com.eterniaserver.events.OnBedLeave;
import br.com.eterniaserver.modules.bedmanager.tasks.AccelerateWorld;

public class BedManager {

    public BedManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.bed")) {
            if (EterniaServer.configs.getBoolean("server.async-check")) {
                plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new AccelerateWorld(plugin), 0L, EterniaServer.configs.getInt("server.checks") * 20);
            } else {
                plugin.getServer().getScheduler().runTaskTimer(plugin, new AccelerateWorld(plugin), 0L, EterniaServer.configs.getInt("server.checks") * 20);
            }
            plugin.getServer().getPluginManager().registerEvents(new OnBedEnter(), plugin);
            plugin.getServer().getPluginManager().registerEvents(new OnBedLeave(), plugin);
            Messages.ConsoleMessage("modules.enable", "%module%", "Bed");
        } else {
            Messages.ConsoleMessage("modules.disable", "%module%", "Bed");
        }
    }

}
