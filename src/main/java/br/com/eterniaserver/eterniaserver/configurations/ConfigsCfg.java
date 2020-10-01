package br.com.eterniaserver.eterniaserver.configurations;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.core.PluginVars;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigsCfg {

    public final boolean moduleBed;
    public final boolean moduleBlock;
    public final boolean moduleCash;
    public final boolean moduleChat;
    public final boolean moduleClear;
    public final boolean moduleCommands;
    public final boolean moduleEconomy;
    public final boolean moduleElevator;
    public final boolean moduleExperience;
    public final boolean moduleHomes;
    public final boolean moduleKits;
    public final boolean moduleSpawners;
    public final boolean moduleTeleports;
    public final boolean moduleRewards;
    public final boolean moduleSchedule;

    public final String afkPlaceholder;
    public final String godPlaceholder;

    public final String tableKits;
    public final String tablePlayer;
    public final String tableRewards;
    public final String tableLocations;

    public final boolean asyncCheck;
    public final int pluginTicks;
    public final int scheduleThreads;
    public final int afkTimer;
    public final boolean afkKick;
    public final int cooldown;
    public final int pvpTime;
    public final int clearRange;
    public final String serverPrefix;

    public final List<Material> elevatorMaterials = new ArrayList<>();
    public final int elevatorMax;
    public final int elevatorMin;

    public final double startMoney;
    public final double backCost;
    public final double nickCost;
    public final String singularName;
    public final String pluralName;
    public final List<String> blacklistedBaltop = new ArrayList<>();

    public final int nightSpeed;
    public final List<String> blacklistedWorldsBed = new ArrayList<>();

    public final List<String> blockedCommands = new ArrayList<>();

    public final ChatColor mobSpawnerColor;
    public final boolean invDrop;
    public final double dropChance;
    public final boolean preventAnvil;
    public final List<String> blacklistedWorldsSpawners = new ArrayList<>();

    public final List<String> profileCustomMessages = new ArrayList<>();

    public final String baltopTag;

    public final String gmSpectator;
    public final String gmSurvival;
    public final String gmCreative;
    public final String gmAdventure;

    public final String cnBlack;
    public final String cnDarkBlue;
    public final String cnDarkGreen;
    public final String cnDarkAqua;
    public final String cnDarkRed;
    public final String cnDarkPurple;
    public final String cnGold;
    public final String cnGray;
    public final String cnDarkGray;
    public final String cnBlue;
    public final String cnGreen;
    public final String cnAqua;
    public final String cnRed;
    public final String cnLightPurple;
    public final String cnYellow;
    public final String cnWhite;

    public final String chLocal;
    public final String chGlobal;
    public final String chStaff;

    public ConfigsCfg() {

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Constants.CONFIG_FILE_PATH));
        FileConfiguration outConfig = new YamlConfiguration();

        this.moduleBed = config.getBoolean("module.bed", true);
        this.moduleBlock = config.getBoolean("module.block-reward", true);
        this.moduleCash = config.getBoolean("module.cash", true);
        this.moduleChat = config.getBoolean("module.chat", true);
        this.moduleClear = config.getBoolean("module.clear", true);
        this.moduleCommands = config.getBoolean("module.commands", true);
        this.moduleEconomy = config.getBoolean("module.economy", true);
        this.moduleElevator = config.getBoolean("module.elevator", true);
        this.moduleExperience = config.getBoolean("module.experience", true);
        this.moduleHomes = config.getBoolean("module.home", true);
        this.moduleKits = config.getBoolean("module.kits", true);
        this.moduleSpawners = config.getBoolean("module.spawners", true);
        this.moduleTeleports = config.getBoolean("module.teleports", true);
        this.moduleRewards = config.getBoolean("module.rewards", true);
        this.moduleSchedule = config.getBoolean("module.schedule", true);

        this.tableKits = config.getString("sql.table-kits", "es_kits");
        this.tablePlayer = config.getString("sql.table-player", "es_players");
        this.tableRewards = config.getString("sql.table-rewards", "es_rewards");
        this.tableLocations = config.getString("sql.table-locations", "es_locations");

        this.asyncCheck = config.getBoolean("server.async-check", true);
        this.pluginTicks = config.getInt("server.checks", 1);
        this.scheduleThreads = config.getInt("server.schedule-threads", 1);
        this.afkTimer = config.getInt("server.afk-timer", 900);
        this.afkKick = config.getBoolean("server.afk-kick", true);
        this.cooldown = config.getInt("server.cooldown", 4);
        this.pvpTime = config.getInt("server.pvp-time", 15);
        this.clearRange = config.getInt("server.clear-range", 1);
        this.serverPrefix = config.getString("server.prefix", "$8[$aE$9S$8]$7 ").replace('$', (char) 0x00A7);

        this.elevatorMaterials.add(Material.IRON_BLOCK);
        this.elevatorMax = config.getInt("elevator.max", 50);
        this.elevatorMin = config.getInt("elevator.min", 2);

        this.startMoney = config.getDouble("money.start", 300.0);
        this.backCost = config.getDouble("money.back", 1000.0);
        this.nickCost = config.getDouble("money.nick", 500000.0);
        this.singularName = config.getString("money.singular", "Eternia");
        this.pluralName = config.getString("money.plural", "Eternias");
        this.blacklistedBaltop.add("yurinogueira");

        this.nightSpeed = config.getInt("bed.speed", 100);
        this.blacklistedWorldsBed.add("world_evento");

        this.blockedCommands.add("/op");
        this.blockedCommands.add("/deop");
        this.blockedCommands.add("/stop");

        this.mobSpawnerColor = PluginVars.colors.get(config.getInt(Constants.SPAWNERS_COLOR, 13));
        this.invDrop = config.getBoolean("spawners.drop-in-inventory", true);
        this.dropChance = config.getDouble("spawners.drop-chance", 1.0);
        this.preventAnvil = config.getBoolean("spawners.prevent-anvil", true);
        this.blacklistedWorldsSpawners.add("world_evento");

        this.baltopTag = config.getString("strings.baltop", "$8[$2Magnata$8]").replace('$', (char) 0x00A7);

        this.afkPlaceholder = config.getString("strings.afk", "$9 AFK").replace('$', (char) 0x00A7);
        this.godPlaceholder = config.getString("strings.godmode", "$9 GOD").replace('$', (char) 0x00A7);

        this.gmAdventure = config.getString("strings.gm.adventure", "aventura");
        this.gmCreative = config.getString("strings.gm.creative", "criativo");
        this.gmSpectator = config.getString("strings.gm.spectator", "espectador");
        this.gmSurvival = config.getString("strings.gm.survival", "sobrevivência");

        this.cnBlack = config.getString("strings.cn.black", "Preto");
        this.cnDarkBlue = config.getString("strings.cn.darkblue", "Azul Escuro");
        this.cnDarkGreen = config.getString("strings.cn.darkgreen", "Verde Escuro");
        this.cnDarkAqua = config.getString("strings.cn.darkaqua", "Ciano");
        this.cnDarkRed = config.getString("strings.cn.darkred", "Vermelho Escuro");
        this.cnDarkPurple = config.getString("strings.cn.darkpurple", "Roxo");
        this.cnGold = config.getString("strings.cn.gold", "Laranja");
        this.cnGray = config.getString("strings.cn.gray", "Cinza");
        this.cnDarkGray = config.getString("strings.cn.darkgray", "Cinza Escuro");
        this.cnBlue = config.getString("strings.cn.blue", "Azul");
        this.cnGreen = config.getString("strings.cn.green", "Verde");
        this.cnAqua = config.getString("strings.cn.aqua", "Azul Claro");
        this.cnRed = config.getString("strings.cn.red", "Vermelho");
        this.cnLightPurple = config.getString("strings.cn.lightpurple", "Rosa");
        this.cnYellow = config.getString("strings.cn.yellow", "Amarelo");
        this.cnWhite = config.getString("strings.cn.white", "Branco");

        this.chLocal = config.getString("strings.ch.local", "Local");
        this.chGlobal = config.getString("strings.ch.global", "Global");
        this.chStaff = config.getString("strings.ch.staff", "Staff");


        List<String> defaultMaterialBlocksList = new ArrayList<>();
        for (Material material : this.elevatorMaterials) {
            defaultMaterialBlocksList.add(material.name());
        }

        List<String> tempBlockMaterials = config.getStringList("elevator.block");
        List<String> tempBlockBaltop = config.getStringList("money.blacklisted-baltop");
        List<String> tempBlockWorld = config.getStringList("bed.blacklisted-worlds");
        List<String> tempBlockedCommands = config.getStringList("blocked-commands");
        List<String> tempBlockWorldSpawners = config.getStringList("spawners.blacklisted-worlds");
        List<String> tempCustomProfileMessages = config.getStringList("profile.custom-messages");

        if (tempCustomProfileMessages.isEmpty()) {
            tempCustomProfileMessages = new ArrayList<>(profileCustomMessages);
        }

        if (tempBlockMaterials.isEmpty()) {
            tempBlockMaterials = defaultMaterialBlocksList;
        }

        if (tempBlockBaltop.isEmpty()) {
            tempBlockBaltop = new ArrayList<>(blacklistedBaltop);
        }

        if (tempBlockWorld.isEmpty()) {
            tempBlockWorld = new ArrayList<>(blacklistedWorldsBed);
        }

        if (tempBlockedCommands.isEmpty()) {
            tempBlockedCommands = new ArrayList<>(blockedCommands);
        }

        if (tempBlockWorldSpawners.isEmpty()) {
            tempBlockWorldSpawners = new ArrayList<>(blacklistedWorldsSpawners);
        }

        this.elevatorMaterials.clear();
        for (String material : tempBlockMaterials) {
            Material blockMaterial = Material.getMaterial(material);
            if (blockMaterial == null) {
                APIServer.logError("Configuração de elevadores material " + material + " não encontrado", 2);
            } else {
                this.elevatorMaterials.add(blockMaterial);
            }
        }
        this.blacklistedBaltop.clear();
        this.blacklistedBaltop.addAll(tempBlockBaltop);
        this.blacklistedWorldsBed.clear();
        this.blacklistedWorldsBed.addAll(tempBlockWorld);
        this.blockedCommands.clear();
        this.blockedCommands.addAll(tempBlockedCommands);
        this.blacklistedWorldsSpawners.clear();
        this.blacklistedWorldsSpawners.addAll(tempBlockWorldSpawners);
        this.profileCustomMessages.clear();
        this.profileCustomMessages.addAll(tempCustomProfileMessages);

        outConfig.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");

        outConfig.set("module.bed", this.moduleBed);
        outConfig.set("module.block-reward", this.moduleBlock);
        outConfig.set("module.cash", this.moduleCash);
        outConfig.set("module.chat", this.moduleChat);
        outConfig.set("module.clear", this.moduleClear);
        outConfig.set("module.commands", this.moduleCommands);
        outConfig.set("module.economy", this.moduleEconomy);
        outConfig.set("module.elevator", this.moduleElevator);
        outConfig.set("module.experience", this.moduleExperience);
        outConfig.set("module.home", this.moduleHomes);
        outConfig.set("module.kits", this.moduleKits);
        outConfig.set("module.spawners", this.moduleSpawners);
        outConfig.set("module.teleports", this.moduleTeleports);
        outConfig.set("module.rewards", this.moduleRewards);
        outConfig.set("module.schedule", this.moduleSchedule);

        outConfig.set("sql.table-kits", this.tableKits);
        outConfig.set("sql.table-player", this.tablePlayer);
        outConfig.set("sql.table-rewards", this.tableRewards);
        outConfig.set("sql.table-locations", this.tableLocations);

        outConfig.set("server.async-check", this.asyncCheck);
        outConfig.set("server.checks", this.pluginTicks);
        outConfig.set("server.schedule-threads", this.scheduleThreads);
        outConfig.set("server.afk-timer", this.afkTimer);
        outConfig.set("server.afk-kick", this.afkKick);
        outConfig.set("server.cooldown", this.cooldown);
        outConfig.set("server.pvp-time", this.pvpTime);
        outConfig.set("server.clear-range", this.clearRange);
        outConfig.set("server.prefix", this.serverPrefix);

        outConfig.set("elevator.block", tempBlockMaterials);
        outConfig.set("elevator.max", this.elevatorMax);
        outConfig.set("elevator.min", this.elevatorMin);

        outConfig.set("money.start", this.startMoney);
        outConfig.set("money.back", this.backCost);
        outConfig.set("money.nick", this.nickCost);
        outConfig.set("money.singular", this.singularName);
        outConfig.set("money.plural", this.pluralName);
        outConfig.set("money.blacklisted-baltop", tempBlockBaltop);

        outConfig.set("bed.speed", this.nightSpeed);
        outConfig.set("bed.blacklisted-worlds", tempBlockWorld);

        outConfig.set("blocked-commands", tempBlockedCommands);

        outConfig.set(Constants.SPAWNERS_COLOR, config.getInt(Constants.SPAWNERS_COLOR, 13));
        outConfig.set("spawners.drop-in-inventory", this.invDrop);
        outConfig.set("spawners.drop-chance", this.dropChance);
        outConfig.set("spawners.prevent-anvil", this.preventAnvil);
        outConfig.set("spawners.blacklisted-worlds", tempBlockWorldSpawners);

        outConfig.set("profile.custom-messages", tempCustomProfileMessages);

        outConfig.set("strings.baltop", this.baltopTag);

        outConfig.set("strings.afk", this.afkPlaceholder);
        outConfig.set("strings.godmode", this.afkPlaceholder);

        outConfig.set("strings.gm.adventure", this.gmAdventure);
        outConfig.set("strings.gm.creative", this.gmCreative);
        outConfig.set("strings.gm.spectator", this.gmSpectator);
        outConfig.set("strings.gm.survival", this.gmSurvival);

        outConfig.set("strings.cn.black", this.cnBlack);
        outConfig.set("strings.cn.darkblue", this.cnDarkBlue);
        outConfig.set("strings.cn.darkgreen", this.cnDarkGreen);
        outConfig.set("strings.cn.darkaqua", this.cnDarkAqua);
        outConfig.set("strings.cn.darkred", this.cnDarkRed);
        outConfig.set("strings.cn.darkpurple", this.cnDarkPurple);
        outConfig.set("strings.cn.gold", this.cnGold);
        outConfig.set("strings.cn.gray", this.cnGray);
        outConfig.set("strings.cn.darkgray", this.cnDarkGray);
        outConfig.set("strings.cn.blue", this.cnBlue);
        outConfig.set("strings.cn.green", this.cnGreen);
        outConfig.set("strings.cn.aqua", this.cnAqua);
        outConfig.set("strings.cn.red", this.cnRed);
        outConfig.set("strings.cn.lightpurple", this.cnLightPurple);
        outConfig.set("strings.cn.yellow", this.cnYellow);
        outConfig.set("strings.cn.white", this.cnWhite);

        outConfig.set("strings.ch.local", this.chLocal);
        outConfig.set("strings.ch.global", this.chGlobal);
        outConfig.set("strings.ch.staff", this.chStaff);

        try {
            outConfig.save(Constants.CONFIG_FILE_PATH);
        } catch (IOException exception) {
            APIServer.logError("Impossível de criar arquivos em " + Constants.DATA_LAYER_FOLDER_PATH, 3);
        }

    }


}
