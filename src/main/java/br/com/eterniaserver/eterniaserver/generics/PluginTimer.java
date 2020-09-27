package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import org.bukkit.Bukkit;

import java.util.Calendar;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PluginTimer extends TimerTask {

    private final Calendar calendar;
    private final EterniaServer plugin;

    public PluginTimer(EterniaServer plugin) {
        this.plugin = plugin;
        calendar = Calendar.getInstance();
    }

    @Override
    public void run() {
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                runDay("schedule.days.sunday");
                break;
            case Calendar.MONDAY:
                runDay("schedule.days.monday");
                break;
            case Calendar.TUESDAY:
                runDay("schedule.days.tuesday");
                break;
            case Calendar.WEDNESDAY:
                runDay("schedule.days.wednesday");
                break;
            case Calendar.THURSDAY:
                runDay("schedule.days.thursday");
                break;
            case Calendar.FRIDAY:
                runDay("schedule.days.friday");
                break;
            case Calendar.SATURDAY:
                runDay("schedule.days.saturday");
                break;
            default:
                APIServer.logError("Talvez você não seja da terra", 3);
        }
    }

    private void runDay(String day) {
        for (String key : EterniaServer.scheduleConfig.getConfigurationSection(day).getKeys(false)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for (String command : EterniaServer.scheduleConfig.getStringList(day + "." + key)) {
                    Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command));
                }
            }, TimeUnit.SECONDS.toMillis(Integer.parseInt(key)));
        }
    }

}
