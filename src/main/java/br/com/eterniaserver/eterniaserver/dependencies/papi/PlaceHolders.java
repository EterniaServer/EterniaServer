package br.com.eterniaserver.eterniaserver.dependencies.papi;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Vars;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.entity.Player;


public class PlaceHolders extends PlaceholderExpansion {

    private final String VERSION = this.getClass().getPackage().getImplementationVersion();
    private final EterniaServer plugin;
    private final Vars vars;

    public PlaceHolders(EterniaServer plugin, Vars vars) {
        this.plugin = plugin;
        this.vars = vars;
    }

    public String getAuthor() {
        return "yurinogueira";
    }

    public String getIdentifier() {
        return "eterniaserver";
    }

    public String getVersion() {
        return this.VERSION;
    }

    public String onPlaceholderRequest(Player p, String identifier) {
        if (p == null) {
            return "";
        } else {
            byte var4 = -1;
            switch(identifier.hashCode()) {
                case -690213213:
                    var4 = 1;
                    break;
                case 96486:
                    var4 = 2;
                    break;
                case 197143583:
                    var4 = 3;
            }
            switch (var4) {
                case 1:
                    return vars.player_login.getOrDefault(p.getName(), "Sem registro");
                case 2:
                    if (vars.afk.contains(p.getName())) {
                        return plugin.serverConfig.getString("placeholders.afk");
                    } else {
                        return "";
                    }
                case 3:
                    if (vars.god.contains(p.getName())) {
                        return plugin.serverConfig.getString("placeholders.godmode");
                    } else {
                        return "";
                    }
                default:
                    return null;
            }
        }
    }

}
