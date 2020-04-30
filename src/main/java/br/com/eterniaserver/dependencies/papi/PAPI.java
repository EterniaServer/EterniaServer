package br.com.eterniaserver.dependencies.papi;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;

import br.com.eterniaserver.configs.Vars;
import me.clip.placeholderapi.PlaceholderAPI;


public class PAPI {
    public PAPI(EterniaServer plugin, Messages messages, Vars vars) {
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            messages.ConsoleMessage("server.no-papi");
            plugin.hasPlaceholderAPI = false;
        } else {
            PlaceholderAPI.registerPlaceholderHook("eterniaserver", new PlaceHolders(plugin, vars));
        }
    }
}
