package br.com.eterniaserver.eterniaserver.configurations.dependencies;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.User;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Placeholders {

    private final EterniaServer plugin;
    private final String version;

    public Placeholders(final EterniaServer plugin) {
        this.plugin = plugin;
        this.version = plugin.getClass().getPackage().getImplementationVersion();

    }

    public void register() {
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceHoldersEnabled().register();
        }
    }

    private class PlaceHoldersEnabled extends PlaceholderExpansion {

        @Override
        public boolean persist(){
            return true;
        }

        @Override
        public boolean canRegister() {
            return true;
        }

        @Override
        public @NotNull String getAuthor() {
            return "yurinogueira";
        }

        @Override
        public @NotNull String getIdentifier() {
            return "eterniaserver";
        }

        @Override
        public @NotNull String getVersion() {
            return version;
        }

        @Override
        public String onRequest(OfflinePlayer p, @NotNull String identifier) {
            return p != null ? getPlaceHolder(getIdentifier(identifier), p.getPlayer()) : "";
        }

        private byte getIdentifier(String identifier) {
            switch(identifier.hashCode()) {
                case -690213213: // "register"
                    return 0;
                case -500526734: // "balance_top"
                    return 1;
                case 96486:  // "afk"
                    return 2;
                case 3046195: // "cash"
                    return 3;
                case 3175821: // "glow"
                    return 4;
                case 197143583: // "godmode"
                    return 5;
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
                    return getBalanceTopPlaceholder(user);
                case 2:
                    return user.getAfkPlaceholder();
                case 3:
                    return String.valueOf(EterniaServer.getCashAPI().getCash(user.getUUID()));
                case 4:
                    return user.getGlowColor();
                case 5:
                    return user.getGodeModePlaceholder();
                default:
                    return plugin.getString(Strings.NOT_SUPPORTED);
            }
        }

        private String getBalanceTopPlaceholder(User user) {
            return user.isBalanceTop() ? plugin.getString(Strings.BALANCE_TOP_TAG) : "";
        }

    }

}
