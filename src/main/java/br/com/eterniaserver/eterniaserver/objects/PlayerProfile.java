package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class PlayerProfile {

    private int cash = 0;
    private int xp = 0;
    private double money = 0D;

    private Set<String> homes = new HashSet<>();

    private String playerName;
    private String playerDisplayName;
    private String activeTitle;

    private Set<String> titles;

    private final long firstLogin;
    private long lastLogin;
    private long hours;

    private long onPvP;

    private int chatChannel = 0;
    private long muted = System.currentTimeMillis();

    public PlayerProfile(String playerName, String playerDisplayName, long firstLogin, long lastLogin, long hours) {
        this.playerName = playerName;
        this.playerDisplayName = playerDisplayName == null ? playerName : playerDisplayName;
        this.firstLogin = firstLogin;
        this.lastLogin = lastLogin;
        this.hours = hours;
    }

    public int getExp() { return this.xp; }
    public double getMoney() { return this.money; }
    public int getCash() { return cash;}

    public void setCash(int value) { this.cash = value; }
    public void setExp(int value) { this.xp = value; }
    public void setMoney(double value) { this.money = value; }

    public void setHomes(String homes) {
        if (homes != null) {
            this.homes.addAll(List.of(homes.split(":")));
        }
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

    public int getChatChannel() {
        return chatChannel;
    }

    public long getFirstLogin() {
        return firstLogin;
    }

    public long getMuted() {
        return muted;
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

}
