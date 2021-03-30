package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class PluginSchedule extends BukkitRunnable {

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
                runDay(plugin.getScheduleMap().get("sunday"));
                break;
            case Calendar.MONDAY:
                runDay(plugin.getScheduleMap().get("monday"));
                break;
            case Calendar.TUESDAY:
                runDay(plugin.getScheduleMap().get("tuesday"));
                break;
            case Calendar.WEDNESDAY:
                runDay(plugin.getScheduleMap().get("wednesday"));
                break;
            case Calendar.THURSDAY:
                runDay(plugin.getScheduleMap().get("thursday"));
                break;
            case Calendar.FRIDAY:
                runDay(plugin.getScheduleMap().get("friday"));
                break;
            case Calendar.SATURDAY:
                runDay(plugin.getScheduleMap().get("saturday"));
                break;
            default:
                plugin.logError("Talvez você não seja da terra", 3);
        }
    }

    private void runDay(Map<Integer, List<String>> map) {
        map.forEach((key, lista) -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (String command : lista) {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
            }
        }, key * 60 * 20L));
    }

}
