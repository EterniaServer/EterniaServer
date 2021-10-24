package br.com.eterniaserver.eterniaserver.modules.placeholderapi;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.OfflinePlayer;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;


final class Services {

    static class Placeholders {

        private final EterniaServer plugin;
        private final String version;

        private final DateFormat dateFormat;

        public Placeholders(final EterniaServer plugin) {
            this.plugin = plugin;
            this.version = plugin.getClass().getPackage().getImplementationVersion();
            this.dateFormat = new SimpleDateFormat(plugin.getString(Strings.DATA_FORMAT));
        }

        public void register() {
            if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                new Placeholders.PlaceHoldersEnabled().register();
            }
            else {
                plugin.getLogger().log(Level.WARNING, "PlaceHolderAPI not found");
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
            public String onRequest(OfflinePlayer offlinePlayer, @NotNull String identifier) {
                if (offlinePlayer == null) return "";

                final PlayerProfile playerProfile = plugin.userManager().get(offlinePlayer.getUniqueId());

                if (playerProfile == null) return "";

                return getPlaceHolder(getIdentifier(identifier), playerProfile);
            }

            private int getIdentifier(String identifier) {
                return switch (identifier.hashCode()) {
                    case -690213213 -> 0; // "register"
                    case -500526734 -> 1; // "balance_top"
                    case 96486 -> 2; // "afk"
                    case 3046195 -> 3; // "cash"
                    case 3175821 -> 4; // "glow"
                    case 197143583 -> 5; // "godmode"
                    default -> 12;
                };
            }

            private String getPlaceHolder(int var4, PlayerProfile playerProfile) {
                return switch (var4) {
                    case 0 -> getFirstLoginPlaceholder(playerProfile.getFirstLogin());
                    case 1 -> getBalanceTopPlaceholder(playerProfile.isBalanceTop());
                    case 2 -> getAFKPlaceholder(playerProfile.getAfk());
                    case 3 -> getCashPlaceholder(playerProfile.getCash());
                    case 4 -> playerProfile.getColor();
                    case 5 -> getGodModePlaceholder(playerProfile.getGod());
                    default -> plugin.getString(Strings.INVALID_PLACEHOLDER);
                };
            }

            private String getFirstLoginPlaceholder(long firstLogin) {
                return dateFormat.format(new Date(firstLogin));
            }

            private String getBalanceTopPlaceholder(boolean isBalanceTop) {
                return isBalanceTop ? plugin.getString(Strings.BALANCE_TOP_TAG) : "";
            }

            public String getAFKPlaceholder(boolean isAfk) {
                return isAfk ? plugin.getString(Strings.AFK_PLACEHOLDER) : "";
            }

            public String getGodModePlaceholder(boolean isGod) {
                return isGod ? plugin.getString(Strings.GOD_PLACEHOLDER) : "";
            }

            public String getCashPlaceholder(int cash) {
                return String.valueOf(cash);
            }
        }
    }
}
