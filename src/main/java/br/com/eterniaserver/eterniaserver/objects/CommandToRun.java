package br.com.eterniaserver.eterniaserver.objects;

public class CommandToRun {

    private final Runnable runnable;
    private final long time;

    public CommandToRun(Runnable runnable, long time) {
        this.runnable = runnable;
        this.time = time;
    }

    public Runnable getRunnable() {
        return runnable;
    }
    
    public long getTime() {
        return time;
    }
    
}
