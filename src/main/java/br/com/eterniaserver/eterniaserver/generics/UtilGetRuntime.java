package br.com.eterniaserver.eterniaserver.generics;

import java.lang.management.ManagementFactory;

public class UtilGetRuntime {

    public long freemem;
    public long totalmem;
    public int seconds;
    public int minutes;
    public int hours;

    public void recalculateRuntime() {
        Runtime runtime = Runtime.getRuntime();
        long milliseconds = ManagementFactory.getRuntimeMXBean().getUptime();
        totalmem = runtime.totalMemory() / 1048576;
        freemem = totalmem - (runtime.freeMemory() / 1048576);
        seconds = (int) (milliseconds / 1000) % 60;
        minutes = (int) ((milliseconds / (1000*60)) % 60);
        hours   = (int) ((milliseconds / (1000*60*60)) % 24);
    }


}
