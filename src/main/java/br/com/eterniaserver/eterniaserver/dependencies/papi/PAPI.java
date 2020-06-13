package br.com.eterniaserver.eterniaserver.dependencies.papi;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import me.clip.placeholderapi.PlaceholderAPI;

public class PAPI {
    public PAPI(EterniaServer plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            plugin.getEFiles().sendConsole("server.no-papi");
            plugin.hasPlaceholderAPI = false;
        } else {
            PlaceholderAPI.registerPlaceholderHook("eterniaserver", plugin.getPlaceHolders());
        }
    }
}
