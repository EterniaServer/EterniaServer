package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class PlayerProfile {

    private String playerName;
    private String playerDisplayName;
    private String activeTitle;

    private Set<String> homes;
    private Set<String> titles;

    private final long firstLogin;
    private long lastLogin;
    private long hours;

    private int cash = 0;
    private int xp = 0;

    private long onPvP;

    private int chatChannel = 0;
    private long muted = System.currentTimeMillis();

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

    public String getActiveTitle() {
        return activeTitle;
    }

    public void setActiveTitle(String activeTitle) {
        this.activeTitle = activeTitle;
    }

    public Set<String> getTitles() {
        return titles;
    }

    public boolean isOnPvP() {
        if (onPvP == 0) return false;
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - onPvP) < EterniaServer.getUserAPI().getPvPTime();
    }

    public void setIsOnPvP() {
        this.onPvP = System.currentTimeMillis();
    }

    public Set<String> getHomes() {
        if (homes == null) {
            homes = new HashSet<>();
        }
        return homes;
    }

    public int getOnPvP() {
        if (onPvP == 0) return 0;
        return (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - onPvP);
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

    public int getCash() {
        return cash;
    }

    public int getChatChannel() {
        return chatChannel;
    }

    public int getXp() {
        return xp;
    }

    public long getFirstLogin() {
        return firstLogin;
    }

    public long getMuted() {
        return muted;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public void setChatChannel(int chatChannel) {
        this.chatChannel = chatChannel;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setMuted(long muted) {
        this.muted = muted;
    }

    public void setPlayerDisplayName(String playerDisplayName) {
        this.playerDisplayName = playerDisplayName;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

}
