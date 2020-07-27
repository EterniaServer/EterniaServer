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
            return getPlaceHolder(getIdentifier(identifier), p.getName());
        }
    }

    private byte getIdentifier(final String identifier) {
        switch(identifier.hashCode()) {
            case -690213213:
                return 1;
            case 96486:
                return 2;
            case 3046195:
                return 3;
            case 3175821:
                return 4;
            case 197143583:
                return 5;
            default:
                return 12;
        }
    }

    private String getPlaceHolder(final byte var4, final String playerName) {
        switch (var4) {
            case 1:
                if (Vars.playerLogin.containsKey(playerName)) return sdf.format(new Date(Vars.playerLogin.get(playerName)));
                else return "Sem registro";
            case 2:
                if (Vars.afk.contains(playerName)) return EterniaServer.serverConfig.getString("placeholders.afk");
                else return "";
            case 3:
                return String.valueOf(Vars.cash.getOrDefault(playerName, 0));
            case 4:
                return Vars.glowingColor.getOrDefault(playerName, "");
            case 5:
                if (Vars.god.contains(playerName))return EterniaServer.serverConfig.getString("placeholders.godmode");
                else return "";
            default:
                return null;
        }
    }

}
