package br.com.eterniaserver.eterniaserver.configurations;

import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.Constants;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleCfg {

    public final int scheduleHour;
    public final int scheduleMinute;
    public final int scheduleSecond;
    public final int scheduleDelay;

    public final Map<String, Map<Integer, List<String>>> scheduleMap = new HashMap<>();

    public ScheduleCfg() {

        FileConfiguration scheduleConfig = YamlConfiguration.loadConfiguration(new File(Constants.SCHEDULE_FILE_PATH));
        FileConfiguration outSchedule = new YamlConfiguration();

        this.scheduleHour = scheduleConfig.getInt("schedule.hour", 10);
        this.scheduleMinute = scheduleConfig.getInt("schedule.minute", 0);
        this.scheduleSecond = scheduleConfig.getInt("schedule.second", 0);
        this.scheduleDelay = scheduleConfig.getInt("schedule.delay", 1);

        Map<Integer, List<String>> tempMapForAll = new HashMap<>();
        tempMapForAll.put(0, new ArrayList<>());
        tempMapForAll.put(1, new ArrayList<>());
        tempMapForAll.put(2, new ArrayList<>());
        tempMapForAll.put(3, new ArrayList<>());

        this.scheduleMap.put("sunday", new HashMap<>(tempMapForAll));
        this.scheduleMap.put("monday", new HashMap<>(tempMapForAll));
        this.scheduleMap.put("tuesday", new HashMap<>(tempMapForAll));
        this.scheduleMap.put("wednesday", new HashMap<>(tempMapForAll));
        this.scheduleMap.put("thursday", new HashMap<>(tempMapForAll));
        this.scheduleMap.put("friday", new HashMap<>(tempMapForAll));
        this.scheduleMap.put("saturday", new HashMap<>(tempMapForAll));

        Map<String, Map<Integer, List<String>>> tempScheduleMap = new HashMap<>();
        ConfigurationSection configurationSection = scheduleConfig.getConfigurationSection("schedule.days");
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                Map<Integer, List<String>> tempTimesKeys = new HashMap<>();
                ConfigurationSection section = scheduleConfig.getConfigurationSection("schedule.days." + key);
                if (section != null) {
                    for (String times : section.getKeys(false)) {
                        tempTimesKeys.put(Integer.parseInt(times), scheduleConfig.getStringList("schedule.days." + key + "." + times));
                    }
                }
                tempScheduleMap.put(key, tempTimesKeys);
            }
        }

        if (tempScheduleMap.isEmpty()) {
            tempScheduleMap = new HashMap<>(this.scheduleMap);
        }

        this.scheduleMap.clear();
        tempScheduleMap.forEach(this.scheduleMap::put);

        tempScheduleMap.forEach((k, v) -> v.forEach((l, b) -> outSchedule.set("schedule.days." + k + "." + l, b)));
        outSchedule.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");

        try {
            outSchedule.save(Constants.SCHEDULE_FILE_PATH);
        } catch (IOException exception) {
            APIServer.logError("Impossível de criar arquivos em " + Constants.DATA_LAYER_FOLDER_PATH, 3);
        }

    }

}
