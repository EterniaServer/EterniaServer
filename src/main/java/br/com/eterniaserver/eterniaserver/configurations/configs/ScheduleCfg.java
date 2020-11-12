package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eterniaserver.configurations.GenericCfg;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleCfg extends GenericCfg {

    public ScheduleCfg(Map<String, Map<Integer, List<String>>> scheduleMap, Integer[] integers) {
        super(null, null, integers, null, null);

        scheduleMap.clear();

        FileConfiguration scheduleConfig = YamlConfiguration.loadConfiguration(new File(Constants.SCHEDULE_FILE_PATH));
        FileConfiguration outSchedule = new YamlConfiguration();

        setInteger(Integers.SCHEDULE_HOUR, scheduleConfig, outSchedule, "schedule.hour", 10);
        setInteger(Integers.SCHEDULE_MINUTE, scheduleConfig, outSchedule, "schedule.minute", 10);
        setInteger(Integers.SCHEDULE_SECONDS, scheduleConfig, outSchedule, "schedule.second", 10);
        setInteger(Integers.SCHEDULE_DELAY, scheduleConfig, outSchedule, "schedule.delay", 1);

        Map<Integer, List<String>> tempMapForAll = new HashMap<>();
        tempMapForAll.put(0, new ArrayList<>());
        tempMapForAll.put(1, new ArrayList<>());
        tempMapForAll.put(2, new ArrayList<>());
        tempMapForAll.put(3, new ArrayList<>());

        scheduleMap.put("sunday", new HashMap<>(tempMapForAll));
        scheduleMap.put("monday", new HashMap<>(tempMapForAll));
        scheduleMap.put("tuesday", new HashMap<>(tempMapForAll));
        scheduleMap.put("wednesday", new HashMap<>(tempMapForAll));
        scheduleMap.put("thursday", new HashMap<>(tempMapForAll));
        scheduleMap.put("friday", new HashMap<>(tempMapForAll));
        scheduleMap.put("saturday", new HashMap<>(tempMapForAll));

        Map<String, Map<Integer, List<String>>> tempScheduleMap = new HashMap<>();
        ConfigurationSection configurationSection = scheduleConfig.getConfigurationSection("schedule.days");
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                Map<Integer, List<String>> tempTimesKeys = new HashMap<>();
                ConfigurationSection section = scheduleConfig.getConfigurationSection("schedule.days" + "." + key);
                if (section != null) {
                    for (String times : section.getKeys(false)) {
                        tempTimesKeys.put(Integer.parseInt(times), scheduleConfig.getStringList("schedule.days" + "." + key + "." + times));
                    }
                }
                tempScheduleMap.put(key, tempTimesKeys);
            }
        }

        if (tempScheduleMap.isEmpty()) {
            tempScheduleMap = new HashMap<>(scheduleMap);
        }

        scheduleMap.clear();
        tempScheduleMap.forEach(scheduleMap::put);
        tempScheduleMap.forEach((k, v) -> v.forEach((l, b) -> outSchedule.set("schedule.days" + "." + k + "." + l, b)));

        saveFile(outSchedule, Constants.SCHEDULE_FILE_PATH, Constants.DATA_LAYER_FOLDER_PATH);

    }

}
