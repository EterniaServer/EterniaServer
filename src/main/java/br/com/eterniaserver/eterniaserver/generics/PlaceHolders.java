package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaceHolders extends PlaceholderExpansion {

    private final SimpleDateFormat sdf;
    private final String version = this.getClass().getPackage().getImplementationVersion();

    public PlaceHolders(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    @Nonnull
    public String getAuthor() {
        return "yurinogueira";
    }

    @Override
    @Nonnull
    public String getIdentifier() {
        return "eterniaserver";
    }

    @Override
    @Nonnull
    public String getVersion() {
        return this.version;
    }

    @Override
    @Nonnull
    public String onRequest(OfflinePlayer p, String identifier) {
        return p != null ? getPlaceHolder(getIdentifier(identifier), p.getName()) : "";
    }

    private byte getIdentifier(final String identifier) {
        switch(identifier.hashCode()) {
            case -690213213:
                return 0;
            case 96486:
                return 1;
            case 3046195:
                return 2;
            case 3175821:
                return 3;
            case 197143583:
                return 4;
            default:
                return 12;
        }
    }

    private String getPlaceHolder(final byte var4, final String playerName) {
        switch (var4) {
            case 0:
                return Vars.playerLogin.containsKey(playerName) ? sdf.format(new Date(Vars.playerLogin.get(playerName))) : "Sem registro";
            case 1:
                return Vars.afk.contains(playerName) ? EterniaServer.serverConfig.getString("placeholders.afk") : "";
            case 2:
                return String.valueOf(Vars.cash.getOrDefault(playerName, 0));
            case 3:
                return Vars.glowingColor.getOrDefault(playerName, "");
            case 4:
                return Vars.god.contains(playerName) ? EterniaServer.serverConfig.getString("placeholders.godmode") : "";
            default:
                return null;
        }
    }

}
