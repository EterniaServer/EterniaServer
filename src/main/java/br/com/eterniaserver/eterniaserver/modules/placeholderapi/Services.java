package br.com.eterniaserver.eterniaserver.modules.placeholderapi;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import br.com.eterniaserver.eterniaserver.modules.cash.Entities.CashBalance;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.OfflinePlayer;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;


final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class Placeholders {

        private final EterniaServer plugin;
        private final DatabaseInterface database;
        private final String version;

        private final DateFormat dateFormat;

        public Placeholders(EterniaServer plugin) {
            this.plugin = plugin;
            this.database = EterniaLib.getDatabase();
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
                if (offlinePlayer == null) {
                    return "";
                }

                int identifierId = getIdentifier(identifier);
                UUID playerUuid = offlinePlayer.getUniqueId();

                return switch (identifierId) {
                    case 0,1,2,4,5 -> getFromPlayerProfile(identifierId, playerUuid);
                    case 3 -> getFromCashBalance(identifierId, playerUuid);
                    default -> plugin.getString(Strings.INVALID_PLACEHOLDER);
                };
            }

            private int getIdentifier(String identifier) {
                return switch (identifier.hashCode()) {
                    case -690213213 -> 0; // register
                    case -500526734 -> 1; // balance_top
                    case 96486 -> 2; // afk
                    case 3046195 -> 3; // cash
                    case 3175821 -> 4; // glow
                    case 197143583 -> 5; // godmode
                    default -> 12;
                };
            }

            private String getFromCashBalance(int var4, UUID uuid) {
                CashBalance cashBalance = database.get(CashBalance.class, uuid);
                if (cashBalance == null) {
                    return "";
                }

                if (var4 == 3) {
                    return getCashPlaceholder(cashBalance.getBalance());
                }

                return plugin.getString(Strings.INVALID_PLACEHOLDER);
            }

            private String getFromPlayerProfile(int var4, UUID uuid) {
                PlayerProfile playerProfile = database.get(PlayerProfile.class, uuid);
                if (playerProfile == null) {
                    return "";
                }

                return switch (var4) {
                    case 0 -> getFirstLoginPlaceholder(playerProfile.getFirstJoin().getTime());
                    case 1 -> getBalanceTopPlaceholder(playerProfile.isBalanceTop());
                    case 2 -> getAFKPlaceholder(playerProfile.isAfk());
                    case 4 -> playerProfile.getColor();
                    case 5 -> getGodModePlaceholder(playerProfile.isGod());
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
