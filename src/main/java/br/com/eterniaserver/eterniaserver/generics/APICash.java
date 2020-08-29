package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import java.util.UUID;

public class APICash {

    private APICash() {
        throw new IllegalStateException("Utility class");
    }

    public static int getCash(UUID uuid) {
        if (Vars.playerProfile.containsKey(uuid)) {
            return Vars.playerProfile.get(uuid).cash;
        } else {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);
            EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_PLAYER, "(uuid, player_name, time, last, hours, balance, cash)",
                    "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + EterniaServer.serverConfig.getDouble("money.start") + "', '" + 0 +"')"));
            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.balance = EterniaServer.serverConfig.getDouble("money.start");
            Vars.playerProfile.put(uuid, playerProfile);
            return 0;
        }
    }

    public static boolean hasCash(UUID uuid, int amount) {
        return getCash(uuid) >= amount;
    }

    public static void setCash(UUID uuid, int amount) {
        if (Vars.playerProfile.containsKey(uuid)) {
            Vars.playerProfile.get(uuid).cash = amount;
            EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_PLAYER, Constants.CASH_STR, amount, Constants.UUID_STR, uuid.toString()));
        } else {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);
            EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_PLAYER, "(uuid, player_name, time, last, hours, balance, cash)",
                    "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + EterniaServer.serverConfig.getDouble("money.start") + "', '" + 0 +"')"));
            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.balance = EterniaServer.serverConfig.getDouble("money.start");
            Vars.playerProfile.put(uuid, playerProfile);
            setCash(uuid, amount);
        }
    }

    public static void addCash(UUID uuid, int amount) {
        setCash(uuid, getCash(uuid) + amount);
    }

    public static void removeCash(UUID uuid, int amount) {
        setCash(uuid, getCash(uuid) - amount);
    }

}
