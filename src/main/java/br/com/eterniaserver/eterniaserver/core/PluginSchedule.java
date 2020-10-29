package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.Bukkit;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PluginSchedule extends TimerTask {

    private final Calendar calendar;
    private final EterniaServer plugin;

    public PluginSchedule(EterniaServer plugin) {
        this.plugin = plugin;
        calendar = Calendar.getInstance();
    }

    @Override
    public void run() {
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                runDay(EterniaServer.schedule.scheduleMap.get("sunday"));
                break;
            case Calendar.MONDAY:
                runDay(EterniaServer.schedule.scheduleMap.get("monday"));
                break;
            case Calendar.TUESDAY:
                runDay(EterniaServer.schedule.scheduleMap.get("tuesday"));
                break;
            case Calendar.WEDNESDAY:
                runDay(EterniaServer.schedule.scheduleMap.get("wednesday"));
                break;
            case Calendar.THURSDAY:
                runDay(EterniaServer.schedule.scheduleMap.get("thursday"));
                break;
            case Calendar.FRIDAY:
                runDay(EterniaServer.schedule.scheduleMap.get("friday"));
                break;
            case Calendar.SATURDAY:
                runDay(EterniaServer.schedule.scheduleMap.get("saturday"));
                break;
            default:
                APIServer.logError("Talvez você não seja da terra", 3);
        }
    }

    private void runDay(Map<Integer, List<String>> map) {
        map.forEach((key, lista) -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (String command : lista) {
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command));
            }
        }, TimeUnit.SECONDS.toMillis(key)));
    }

}
