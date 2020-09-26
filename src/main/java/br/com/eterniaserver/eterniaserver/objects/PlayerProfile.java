package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eterniaserver.Configs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayerProfile {

    public String playerName;
    public String playerDisplayName;

    public List<String> homes;

    public final long firstLogin;
    public long lastLogin;
    public long hours;

    public double balance = 0.0;
    public int cash = 0;
    public int xp = 0;

    public long onPvP;

    public int chatChannel = 0;
    public boolean nickRequest = false;
    public String tempNick;
    public long muted = System.currentTimeMillis();

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
        if (onPvP == 0) return false;
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - onPvP) < Configs.instance.pvpTime;
    }

    public void setIsOnPvP() {
        this.onPvP = System.currentTimeMillis();
    }

    public List<String> getHomes() {
        return homes != null ? homes : new ArrayList<>();
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

}
