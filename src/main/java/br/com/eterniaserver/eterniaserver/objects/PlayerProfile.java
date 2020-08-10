package br.com.eterniaserver.eterniaserver.objects;

public class PlayerProfile {

    private String playerName;
    private String playerDisplayName;
    private final long firstLogin;
    private long lastLogin;
    private int hours;

    public PlayerProfile(String playerName, long firstLogin, long lastLogin, int hours) {
        this.playerName = playerName;
        this.firstLogin = firstLogin;
        this.lastLogin = lastLogin;
        this.hours = hours;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getHours() {
        return hours;
    }

    public long getFirstLogin() {
        return firstLogin;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public String getPlayerDisplayName() {
        return playerDisplayName;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setPlayerDisplayName(String playerDisplayName) {
        this.playerDisplayName = playerDisplayName;
    }
}
