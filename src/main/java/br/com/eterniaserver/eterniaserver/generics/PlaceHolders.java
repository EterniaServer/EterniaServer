package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaceHolders extends PlaceholderExpansion {

    private final SimpleDateFormat sdf;
    private final String version = this.getClass().getPackage().getImplementationVersion();

    public PlaceHolders(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public String getAuthor() {
        return "yurinogueira";
    }

    public String getIdentifier() {
        return "eterniaserver";
    }

    public String getVersion() {
        return this.version;
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (p == null) {
            return "";
        } else {
            final String playerName = p.getName();
            byte var4;
            switch(identifier.hashCode()) {
                case -690213213:
                    var4 = 1;
                    break;
                case 96486:
                    var4 = 2;
                    break;
                case 3046195:
                    var4 = 3;
                    break;
                case 3175821:
                    var4 = 4;
                    break;
                case 197143583:
                    var4 = 5;
                    break;
                default:
                    var4 = 12;
            }
            switch (var4) {
                case 1:
                    if (Vars.playerLogin.containsKey(playerName)) {
                        return sdf.format(new Date(Vars.playerLogin.get(playerName)));
                    } else {
                        return "Sem registro";
                    }
                case 2:
                    if (Vars.afk.contains(playerName)) return EterniaServer.serverConfig.getString("placeholders.afk");
                    else return "";
                case 3:
                    return String.valueOf(Vars.cash.getOrDefault(playerName, 0));
                case 4:
                    return Vars.glowingColor.getOrDefault(playerName, "");
                case 5:
                    if (Vars.god.contains(playerName)) return EterniaServer.serverConfig.getString("placeholders.godmode");
                    else return "";
                default:
                    return null;
            }
        }
    }

}
