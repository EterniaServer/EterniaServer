package br.com.eterniaserver.eterniaserver.utils;

import java.util.concurrent.TimeUnit;

public enum TimeEnum {

    HASCOOLDOWN(System.currentTimeMillis());

    private final long systemTime;

    TimeEnum(long systemTime) {
        this.systemTime = systemTime;
    }

    public String getTimeLeft(long cooldown) {
        return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(cooldown - systemTime));
    }

    public String getTimeLeft(long cooldown, long cd) {
        return String.valueOf(cooldown - TimeUnit.MILLISECONDS.toSeconds(systemTime - cd));
    }

    public boolean hasCooldown(long cooldown, int timeNeeded) {
        return TimeUnit.MILLISECONDS.toSeconds(systemTime - cooldown) >= timeNeeded;
    }

    public boolean stayMuted(long cooldown) {
        return cooldown - systemTime > 0;
    }

}
