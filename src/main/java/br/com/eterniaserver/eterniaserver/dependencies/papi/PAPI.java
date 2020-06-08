package br.com.eterniaserver.eterniaserver.dependencies.papi;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;

import me.clip.placeholderapi.PlaceholderAPI;

public class PAPI {
    public PAPI(EterniaServer plugin, Messages messages, PlaceHolders placeHolders) {
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            messages.sendConsole("server.no-papi");
            plugin.hasPlaceholderAPI = false;
        } else {
            PlaceholderAPI.registerPlaceholderHook("eterniaserver", placeHolders);
        }
    }
}
