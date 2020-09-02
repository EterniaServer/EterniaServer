package br.com.eterniaserver.eterniaserver.dependencies.papi;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.generics.APICash;
import br.com.eterniaserver.eterniaserver.generics.APIPlayer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import java.util.UUID;

public class PlaceHolders extends PlaceholderExpansion {

    private final String version = this.getClass().getPackage().getImplementationVersion();

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
    public String onRequest(OfflinePlayer p, @Nonnull String identifier) {
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
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        switch (var4) {
            case 0:
                return APIPlayer.getFirstLogin(uuid);
            case 1:
                return APIPlayer.isAFKPlaceholder(playerName);
            case 2:
                return String.valueOf(APICash.getCash(uuid));
            case 3:
                return APIPlayer.getGlowColor(playerName);
            case 4:
                return APIPlayer.isGodPlaceholder(playerName);
            default:
                return null;
        }
    }

}
