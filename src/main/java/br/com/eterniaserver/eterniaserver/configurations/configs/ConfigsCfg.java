package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.configurations.GenericCfg;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class ConfigsCfg extends GenericCfg {

    public ConfigsCfg(String[] strings, Boolean[] booleans, Integer[] integers, Double[] doubles, List<List<String>> lists, List<Material> elevatorMaterials) {

        super(strings, booleans, integers, doubles, lists);

        FileConfiguration file = YamlConfiguration.loadConfiguration(new File(Constants.CONFIG_FILE_PATH));
        FileConfiguration outFile = new YamlConfiguration();

        setBoolean(Booleans.MODULE_BED, file, outFile, "module.bed", true);
        setBoolean(Booleans.MODULE_BLOCK, file, outFile, "module.block-reward", true);
        setBoolean(Booleans.MODULE_CASH, file, outFile, "module.cash", true);
        setBoolean(Booleans.MODULE_CHAT, file, outFile, "module.chat", true);
        setBoolean(Booleans.MODULE_ENTITY, file, outFile, "module.entity", true);
        setBoolean(Booleans.MODULE_COMMANDS, file, outFile, "module.commands", true);
        setBoolean(Booleans.MODULE_ECONOMY, file, outFile, "module.economy", true);
        setBoolean(Booleans.MODULE_ELEVATOR, file, outFile, "module.elevator", true);
        setBoolean(Booleans.MODULE_EXPERIENCE, file, outFile, "module.experience", true);
        setBoolean(Booleans.MODULE_HOMES, file, outFile, "module.home", true);
        setBoolean(Booleans.MODULE_KITS, file, outFile, "module.kits", true);
        setBoolean(Booleans.MODULE_SPAWNERS, file, outFile, "module.spawners", true);
        setBoolean(Booleans.MODULE_TELEPORTS, file, outFile, "module.teleports", true);
        setBoolean(Booleans.MODULE_REWARDS, file, outFile, "module.rewards", true);
        setBoolean(Booleans.MODULE_SCHEDULE, file, outFile, "module.schedule", true);
        setBoolean(Booleans.ASYNC_CHECK, file, outFile, "server.async-check", true);
        setBoolean(Booleans.AFK_KICK, file, outFile, "server.afk-kick", true);
        setBoolean(Booleans.INV_DROP, file, outFile, "spawners.drop-in-inventory", true);
        setBoolean(Booleans.PREVENT_ANVIL, file, outFile, "spawners.prevent-anvil", true);

        setString(Strings.TABLE_KITS, file, outFile, "sql.table-kits", "es_kits");
        setString(Strings.TABLE_PLAYER, file, outFile, "sql.table-player", "es_players");
        setString(Strings.TABLE_REWARD, file, outFile, "sql.table-rewards", "es_rewards");
        setString(Strings.TABLE_LOCATIONS, file, outFile, "sql.table-locations", "es_locations");
        setString(Strings.MONEY_SINGULAR, file, outFile, "money.singular", "Eternia");
        setString(Strings.MONEY_PLURAL, file, outFile, "money.plural", "Eternias");
        setString(Strings.SPAWNERS_COLORS, file, outFile, "spawners.color", "§e");

        setInteger(Integers.PLUGIN_TICKS, file, outFile, "server.checks", 1);
        setInteger(Integers.AFK_TIMER, file, outFile, "server.afk-timer", 900);
        setInteger(Integers.COOLDOWN, file, outFile, "server.cooldown", 4);
        setInteger(Integers.PVP_TIME, file, outFile, "server.pvp-time", 15);
        setInteger(Integers.ELEVATOR_MAX, file, outFile, "elevator.max", 50);
        setInteger(Integers.ELEVATOR_MIN, file, outFile, "elevator.min", 2);
        setInteger(Integers.NIGHT_SPEED, file, outFile, "bed.speed", 100);
        setInteger(Integers.COMMAND_CONFIRM_TIME, file, outFile, "command-confirm.time", 60);

        setDouble(Doubles.START_MONEY, file, outFile, "money.start", 300.0);
        setDouble(Doubles.BACK_COST, file, outFile, "money.back", 1000.0);
        setDouble(Doubles.NICK_COST, file, outFile, "money.nick", 500000.0);
        setDouble(Doubles.DROP_CHANCE, file, outFile, "spawners.drop-chance", 1.0);

        setList(Lists.BLACKLISTED_BALANCE_TOP, file, outFile, "money.blacklisted-baltop", "yurinogueira");
        setList(Lists.BLACKLISTED_WORLDS_FLY, file, outFile, "server.blacklisted-fly-worlds", "world_evento");
        setList(Lists.BLACKLISTED_WORLDS_SLEEP, file, outFile, "bed.blacklisted-worlds", "world_evento");
        setList(Lists.BLACKLISTED_COMMANDS, file, outFile, "blocked-commands", "/op", "/deop", "/stop");
        setList(Lists.BLACKLISTED_WORLDS_SPAWNERS, file, outFile, "spawners.blacklisted-worlds", "world_evento");
        setList(Lists.PROFILE_CUSTOM_MESSAGES, file, outFile, "profile.custom-messages");

        setMaterials(elevatorMaterials, file, outFile, "IRON_BLOCK");

        saveFile(outFile, Constants.CONFIG_FILE_PATH, Constants.DATA_LAYER_FOLDER_PATH);

    }

}
