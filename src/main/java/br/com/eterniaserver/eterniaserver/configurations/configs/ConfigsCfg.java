package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.configurations.GenericCfg;
import br.com.eterniaserver.eterniaserver.enums.ConfigBooleans;
import br.com.eterniaserver.eterniaserver.enums.ConfigDoubles;
import br.com.eterniaserver.eterniaserver.enums.ConfigIntegers;
import br.com.eterniaserver.eterniaserver.enums.ConfigLists;
import br.com.eterniaserver.eterniaserver.enums.ConfigStrings;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class ConfigsCfg extends GenericCfg {

    public ConfigsCfg(String[] strings, Boolean[] booleans, Integer[] integers, Double[] doubles, List<List<?>> lists) {

        super(strings, booleans, integers, doubles, lists);

        FileConfiguration file = YamlConfiguration.loadConfiguration(new File(Constants.CONFIG_FILE_PATH));
        FileConfiguration outFile = new YamlConfiguration();

        setBoolean(ConfigBooleans.MODULE_BED, file, outFile, "module.bed", true);
        setBoolean(ConfigBooleans.MODULE_BLOCK, file, outFile, "module.block-reward", true);
        setBoolean(ConfigBooleans.MODULE_CASH, file, outFile, "module.cash", true);
        setBoolean(ConfigBooleans.MODULE_CHAT, file, outFile, "module.chat", true);
        setBoolean(ConfigBooleans.MODULE_CLEAR, file, outFile, "module.clear", true);
        setBoolean(ConfigBooleans.MODULE_COMMANDS, file, outFile, "module.commands", true);
        setBoolean(ConfigBooleans.MODULE_ECONOMY, file, outFile, "module.economy", true);
        setBoolean(ConfigBooleans.MODULE_ELEVATOR, file, outFile, "module.elevator", true);
        setBoolean(ConfigBooleans.MODULE_EXPERIENCE, file, outFile, "module.experience", true);
        setBoolean(ConfigBooleans.MODULE_HOMES, file, outFile, "module.home", true);
        setBoolean(ConfigBooleans.MODULE_KITS, file, outFile, "module.kits", true);
        setBoolean(ConfigBooleans.MODULE_SPAWNERS, file, outFile, "module.spawners", true);
        setBoolean(ConfigBooleans.MODULE_TELEPORTS, file, outFile, "module.teleports", true);
        setBoolean(ConfigBooleans.MODULE_REWARDS, file, outFile, "module.rewards", true);
        setBoolean(ConfigBooleans.MODULE_SCHEDULE, file, outFile, "module.schedule", true);
        setBoolean(ConfigBooleans.ASYNC_CHECK, file, outFile, "server.async-check", true);
        setBoolean(ConfigBooleans.AFK_KICK, file, outFile, "server.afk-kick", true);
        setBoolean(ConfigBooleans.INV_DROP, file, outFile, "spawners.drop-in-inventory", true);
        setBoolean(ConfigBooleans.PREVENT_ANVIL, file, outFile, "spawners.prevent-anvil", true);

        setString(ConfigStrings.TABLE_KITS, file, outFile, "sql.table-kits", "es_kits");
        setString(ConfigStrings.TABLE_PLAYER, file, outFile, "sql.table-player", "es_players");
        setString(ConfigStrings.TABLE_REWARD, file, outFile, "sql.table-rewards", "es_rewards");
        setString(ConfigStrings.TABLE_LOCATIONS, file, outFile, "sql.table-locations", "es_locations");
        setString(ConfigStrings.MONEY_SINGULAR, file, outFile, "money.singular", "Eternia");
        setString(ConfigStrings.MONEY_PLURAL, file, outFile, "money.plural", "Eternias");

        setInteger(ConfigIntegers.SPAWNERS_COLORS, file, outFile, "spawners.color", 14);
        setInteger(ConfigIntegers.PLUGIN_TICKS, file, outFile, "server.checks", 1);
        setInteger(ConfigIntegers.SCHEDULE_THREADS, file, outFile, "server.schedule-threads", 1);
        setInteger(ConfigIntegers.AFK_TIMER, file, outFile, "server.afk-timer", 900);
        setInteger(ConfigIntegers.COOLDOWN, file, outFile, "server.cooldown", 4);
        setInteger(ConfigIntegers.PVP_TIME, file, outFile, "server.pvp-time", 15);
        setInteger(ConfigIntegers.CLEAR_RANGE, file, outFile, "server.clear-range", 1);
        setInteger(ConfigIntegers.ELEVATOR_MAX, file, outFile, "elevator.max", 50);
        setInteger(ConfigIntegers.ELEVATOR_MIN, file, outFile, "elevator.min", 2);
        setInteger(ConfigIntegers.NIGHT_SPEED, file, outFile, "bed.speed", 100);

        setDouble(ConfigDoubles.START_MONEY, file, outFile, "money.start", 300.0);
        setDouble(ConfigDoubles.BACK_COST, file, outFile, "money.back", 1000.0);
        setDouble(ConfigDoubles.NICK_COST, file, outFile, "money.nick", 500000.0);
        setDouble(ConfigDoubles.DROP_CHANCE, file, outFile, "spawners.drop-chance", 1.0);

        setList(ConfigLists.BLACKLISTED_BALANCE_TOP, file, outFile, "money.blacklisted-baltop", "yurinogueira");
        setList(ConfigLists.BLACKLISTED_WORLDS_FLY, file, outFile, "server.blacklisted-fly-worlds", "world_evento");
        setList(ConfigLists.BLACKLISTED_WORLDS_SLEEP, file, outFile, "bed.blacklisted-worlds", "world_evento");
        setList(ConfigLists.BLACKLISTED_COMMANDS, file, outFile, "blocked-commands", "/op", "/deop", "/stop");
        setList(ConfigLists.ELEVATOR_MATERIALS, file, outFile, "elevator.block", "IRON_BLOCK");
        setList(ConfigLists.BLACKLISTED_WORLDS_SPAWNERS, file, outFile, "spawners.blacklisted-worlds", "world_evento");
        setList(ConfigLists.PROFILE_CUSTOM_MESSAGES, file, outFile, "profile.custom-messages");

        saveFile(outFile, Constants.CONFIG_FILE_PATH, Constants.DATA_LAYER_FOLDER_PATH);

    }

}
