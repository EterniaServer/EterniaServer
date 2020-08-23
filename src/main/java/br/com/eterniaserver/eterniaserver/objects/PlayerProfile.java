package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import java.util.concurrent.TimeUnit;

public class PlayerProfile {

    private String playerName;
    public String playerDisplayName;

    public final long firstLogin;
    public long lastLogin;
    public long hours;

    public long isOnPvP;

    public int chatChannel = 0;
    public boolean nickRequest = false;
    public String tempNick;

    public PlayerProfile(String playerName, long firstLogin, long lastLogin, long hours) {
        this.playerName = playerName;
        this.firstLogin = firstLogin;
        this.lastLogin = lastLogin;
        this.hours = hours;
    }

    public long updateTimePlayed() {
        this.hours = hours + (System.currentTimeMillis() - lastLogin);
        this.lastLogin = System.currentTimeMillis();
        return hours;
    }

    public boolean isOnPvP() {
        if (isOnPvP == 0) return false;
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - isOnPvP) < EterniaServer.serverConfig.getInt("server.pvp-time");
    }

    public int getIsOnPvP() {
        if (isOnPvP == 0) return 0;
        return (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - isOnPvP);
    }

    public void setIsOnPvP() {
        this.isOnPvP = System.currentTimeMillis();
    }

    public String getPlayerDisplayName() {
        return playerDisplayName != null ? playerDisplayName : playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

}
