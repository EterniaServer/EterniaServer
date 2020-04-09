package br.com.eterniaserver.modules;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.events.OnBedEnter;
import br.com.eterniaserver.configs.CVar;
import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.events.OnBedLeave;
import br.com.eterniaserver.modules.bedmanager.BedTimer;

public class BedManager {

    public BedManager(EterniaServer plugin) {
        if (CVar.getBool("modules.bed")) {
            if (CVar.getBool("server.async-check")) {
                plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new BedTimer(plugin), 0L, CVar.getInt("server.checks") * 20);
            } else {
                plugin.getServer().getScheduler().runTaskTimer(plugin, new BedTimer(plugin), 0L, CVar.getInt("server.checks") * 20);
            }
            plugin.getServer().getPluginManager().registerEvents(new OnBedEnter(plugin), plugin);
            plugin.getServer().getPluginManager().registerEvents(new OnBedLeave(plugin), plugin);
            new ConsoleMessage("modules.enable", "Bed");
        } else {
            new ConsoleMessage("modules.disable", "Bed");
        }
    }

}
