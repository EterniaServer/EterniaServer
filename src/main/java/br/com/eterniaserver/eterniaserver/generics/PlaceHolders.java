package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniamarriage.generic.APIMarry;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaceHolders extends PlaceholderExpansion {

    private final boolean hasMarry;
    private final SimpleDateFormat sdf;
    private final String version = this.getClass().getPackage().getImplementationVersion();

    public PlaceHolders(SimpleDateFormat sdf) {
        this.sdf = sdf;
        hasMarry = Bukkit.getServer().getPluginManager().getPlugin("EterniaMarriage") != null;
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
            case -792929080:
                return 6;
            case -883341420:
                return 7;
            case -1404969206:
                return 8;
            case -1570088016:
                return 9;
            default:
                return 12;
        }
    }

    private String getPlaceHolder(final byte var4, final String playerName) {
        String placeholder = null;
        switch (var4) {
            case 1:
                if (Vars.playerLogin.containsKey(playerName)) placeholder = sdf.format(new Date(Vars.playerLogin.get(playerName)));
                else placeholder = "Sem registro";
                break;
            case 2:
                if (Vars.afk.contains(playerName)) placeholder = EterniaServer.serverConfig.getString("placeholders.afk");
                else placeholder = "";
                break;
            case 3:
                placeholder = String.valueOf(Vars.cash.getOrDefault(playerName, 0));
                break;
            case 4:
                placeholder = Vars.glowingColor.getOrDefault(playerName, "");
                break;
            case 5:
                if (Vars.god.contains(playerName)) placeholder = EterniaServer.serverConfig.getString("placeholders.godmode");
                else placeholder = "";
                break;
            default:
                break;
        }
        if (hasMarry) {
            switch (var4) {
                case 6:
                    if (APIMarry.isMarried(playerName)) placeholder = APIMarry.getPartner(playerName);
                    else placeholder = "Ninguém";
                    break;
                case 7:
                    if (APIMarry.isMarried(playerName)) placeholder = "&3❤";
                    else placeholder = "&7❤";
                    break;
                case 8:
                    if (APIMarry.isMarried(playerName)) placeholder = "&3❤";
                    else placeholder = "";
                    break;
                case 9:
                    if (APIMarry.isMarried(playerName)) placeholder = String.valueOf(APIMarry.getMarriedBankName(playerName));
                    else placeholder = "0";
                    break;
                default:
                    break;
            }
        }
        return placeholder;
    }

}
