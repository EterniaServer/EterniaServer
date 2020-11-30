package br.com.eterniaserver.eterniaserver.configurations.dependencies;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.CashRelated;
import br.com.eterniaserver.eterniaserver.core.User;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class Placeholders extends PlaceholderExpansion {

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
        return p != null ? getPlaceHolder(getIdentifier(identifier), p.getPlayer()) : "";
    }

    private byte getIdentifier(String identifier) {
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

    private String getPlaceHolder(byte var4, Player player) {
        User user = new User(player);
        switch (var4) {
            case 0:
                return user.getFirstLoginPlaceholder();
            case 1:
                return user.getAfkPlaceholder();
            case 2:
                return String.valueOf(CashRelated.getCash(user.getUUID()));
            case 3:
                return user.getGlowColor();
            case 4:
                return user.getGodeModePlaceholder();
            default:
                return EterniaServer.getString(Strings.NOT_SUPPORTED);
        }
    }

}
