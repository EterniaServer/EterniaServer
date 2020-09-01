package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class APIPlayer {

    private APIPlayer() {
        throw new IllegalStateException("Utility class");
    }

    public static String getFirstLogin(UUID uuid) {
        return Vars.playerProfile.containsKey(uuid) ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(Vars.playerProfile.get(uuid).firstLogin)) : "Sem registro";
    }

    public static String isAFK(String playerName) {
        return Vars.afk.contains(playerName) ? EterniaServer.serverConfig.getString("placeholders.afk") : "";
    }

    public static String getGlowColor(String playerName) {
        return Vars.glowingColor.getOrDefault(playerName, "");
    }

    public static boolean isGod(String playerName) {
        return Vars.god.contains(playerName);
    }

}
