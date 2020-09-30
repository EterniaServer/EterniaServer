package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.core.PluginVars;
import br.com.eterniaserver.eterniaserver.objects.CashItem;
import br.com.eterniaserver.eterniaserver.objects.CustomPlaceholdersObjects;
import br.com.eterniaserver.eterniaserver.objects.CustomizableMessage;

import br.com.eterniaserver.eterniaserver.objects.KitObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Configs {

    private static final String DATA_LAYER_FOLDER_PATH = "plugins" + File.separator + "EterniaServer";
    private static final String MESSAGES_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "messages.yml";
    private static final String CONFIG_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "config.yml";
    private static final String BLOCKS_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "blocks.yml";
    private static final String CASHGUI_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "cashgui.yml";
    private static final String KITS_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "kits.yml";
    private static final String CHAT_FILE_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "chat.yml";

    private String[] messages;

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

    public final Map<String, Map<Double, List<String>>> blockRewardsDrop = new HashMap<>();
    public final Map<String, Map<Double, List<String>>> farmRewardsDrop = new HashMap<>();

    public final List<ItemStack> menuGui = new ArrayList<>();
    public final Map<Integer, String> guis = new HashMap<>();
    public final Map<String, Integer> guisInvert = new HashMap<>();
    public final Map<Integer, List<CashItem>> othersGui = new HashMap<>();

    public final Map<String, KitObject> kitList = new HashMap<>();

    public final boolean autoUpdateGroups;
    public final int localRange;
    public final String localFormat;
    public final String globalFormat;
    public final String staffFormat;

    public final Map<String, CustomPlaceholdersObjects> customPlaceholdersObjectsMap = new HashMap<>();

    protected Configs() {

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(CONFIG_FILE_PATH));
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

        this.mobSpawnerColor = PluginVars.colors.get(config.getInt("spawners.color", 13));
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

        outConfig.set("spawners.color", config.getInt("spawners.color", 13));
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

        loadMessages();

        // blocks.yml

        FileConfiguration blocks = YamlConfiguration.loadConfiguration(new File(BLOCKS_FILE_PATH));
        FileConfiguration outBlocks = new YamlConfiguration();

        Map<Double, List<String>> tempBlockzinMap = new HashMap<>();
        tempBlockzinMap.put(0.001, List.of("crates givekey %player_name% minerios"));
        tempBlockzinMap.put(0.00005, List.of("crates givekey %player_name% mineriosraros"));
        this.blockRewardsDrop.put("STONE", tempBlockzinMap);

        Map<String, Map<Double, List<String>>> tempBlock = new HashMap<>();

        ConfigurationSection configurationSection = blocks.getConfigurationSection("blocks");
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                Map<Double, List<String>> tempChanceMap = new HashMap<>();
                ConfigurationSection section = blocks.getConfigurationSection("blocks." + key);
                if (section != null) {
                    for (String chance : section.getKeys(false)){
                        tempChanceMap.put(Double.parseDouble(chance.replace(',', '.')), blocks.getStringList("blocks." + key + "." + chance));
                    }
                }
                tempBlock.put(key, tempChanceMap);
            }
        }


        if (tempBlock.isEmpty()) {
            tempBlock = new HashMap<>(this.blockRewardsDrop);
        }

        this.blockRewardsDrop.clear();
        tempBlock.forEach(this.blockRewardsDrop::put);

        this.blockRewardsDrop.forEach((k, v) -> v.forEach((l, b) -> outBlocks.set("blocks." + k + '.' + String.format("%.10f", l).replace('.', ','), b)));

        Map<Double, List<String>> tempFarmMap = new HashMap<>();
        tempFarmMap.put(0.001, List.of("crates givekey %player_name% farm"));
        tempFarmMap.put(0.00005, List.of("crates givekey %player_name% farmraros"));
        this.farmRewardsDrop.put("CARROT", tempFarmMap);

        Map<String, Map<Double, List<String>>> tempFarm = new HashMap<>();

        configurationSection = blocks.getConfigurationSection("farm");
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                Map<Double, List<String>> tempChanceMap = new HashMap<>();
                ConfigurationSection section = blocks.getConfigurationSection("farm." + key);
                if (section != null) {
                    for (String chance : section.getKeys(false)) {
                        tempChanceMap.put(Double.parseDouble(chance.replace(',', '.')), blocks.getStringList("farm." + key + "." + chance));
                    }
                }
                tempFarm.put(key, tempChanceMap);
            }
        }

        if (tempFarm.isEmpty()) {
            tempFarm = new HashMap<>(this.farmRewardsDrop);
        }

        this.farmRewardsDrop.clear();
        tempFarm.forEach(this.farmRewardsDrop::put);

        this.farmRewardsDrop.forEach((k, v) -> v.forEach((l, b) -> outBlocks.set("farm." + k + "." + String.format("%.10f", l).replace('.', ','), b)));
        outBlocks.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");

        // cashgui.yml

        FileConfiguration cashGui = YamlConfiguration.loadConfiguration(new File(CASHGUI_FILE_PATH));
        FileConfiguration outCash = new YamlConfiguration();

        for (int i = 0; i < 27; i++) {
            if (i == 10) {
                ItemStack itemStack = new ItemStack(Material.OAK_SIGN);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("$7Permissões$8.".replace('$', (char) 0x00A7));
                itemMeta.setLore(List.of( "$7Compre permissões".replace('$', (char) 0x00A7), "$7para lhe ajudar".replace('$', (char) 0x00A7), "$7nessa jornada$8.".replace('$', (char) 0x00A7)));
                itemStack.setItemMeta(itemMeta);
                guis.put(10, "Perm");
                guisInvert.put("Perm", 10);
                menuGui.add(itemStack);
                List<CashItem> tempList = new ArrayList<>();
                for (int j = 0; j < 36; j++) {
                    if (j == 10) {
                        ItemStack is = new ItemStack(Material.EXPERIENCE_BOTTLE);
                        ItemMeta iss = is.getItemMeta();
                        iss.setDisplayName("$725% Bônus de XP no McMMO!".replace('$', (char) 0x00A7));
                        iss.setLore(List.of("$aUpe mais rápido na sua especialidade! Não acumlativos!".replace('$', (char) 0x00A7),"$230 dias de duração!".replace('$', (char) 0x00A7), "", "$2Preço C$ 70".replace('$', (char) 0x00A7)));
                        is.setItemMeta(iss);
                        tempList.add(new CashItem(is, 70, List.of( "$8[$aE$9S$8] $7parabéns pela aquisição$8!".replace('$', (char) 0x00A7)), List.of("lp user %player_name% permission settemp mcmmo.perks.xp.customboost.all true 30d"), false));
                    } else {
                        tempList.add(new CashItem(getGlass(), 0, null, null, true));
                    }
                }
                this.othersGui.put(i, tempList);
            } else {
                this.menuGui.add(getGlass());
            }
        }

        List<ItemStack> menuGuiTemp = new ArrayList<>();
        Map<Integer, String> guisTemp = new HashMap<>();
        Map<String, Integer> guisTempInvert = new HashMap<>();
        Map<Integer, List<CashItem>> othersGuiTemp = new HashMap<>();

        for (int i = 0; i < cashGui.getInt("menu.size"); i++) {
            if (cashGui.contains("menu." + i)) {
                ItemStack itemStack = new ItemStack(Material.valueOf(cashGui.getString("menu." + i + ".material")));
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(cashGui.getString("menu." + i + ".name").replace('$', (char) 0x00A7));
                List<String> listando = cashGui.getStringList("menu." + i + ".lore");
                putColorOnList(listando);
                itemMeta.setLore(listando);
                itemStack.setItemMeta(itemMeta);
                menuGuiTemp.add(itemStack);
                String guiName = cashGui.getString("menu." + i + ".gui");
                guisTemp.put(i, guiName);
                guisTempInvert.put(guiName, i);
                List<CashItem> tempList = new ArrayList<>();
                for (int j = 0; j < cashGui.getInt("guis." + guiName + ".size"); j++) {
                    if (cashGui.contains("guis." + guiName + "." + j)) {
                        ItemStack guiItem = new ItemStack(Material.valueOf(cashGui.getString("guis." + guiName + "." + j + ".material")));
                        ItemMeta guiMeta = guiItem.getItemMeta();
                        guiMeta.setDisplayName(cashGui.getString("guis." + guiName + "." + j + ".name").replace('$', (char) 0x00A7));
                        List<String> listandoNovo = cashGui.getStringList("guis." + guiName + "." + j + ".lore");
                        putColorOnList(listandoNovo);
                        guiMeta.setLore(listandoNovo);
                        guiItem.setItemMeta(guiMeta);
                        List<String> commands = cashGui.getStringList("guis." + guiName + "." + j + ".commands");
                        List<String> messages = cashGui.getStringList("guis." + guiName + "." + j + ".messages");
                        putColorOnList(messages);
                        tempList.add(new CashItem(guiItem, cashGui.getInt("guis." + guiName + "." + j + ".cost"), messages, commands, false));
                    } else {
                        tempList.add(new CashItem(getGlass(), 0, null, null, true));
                    }
                }
                othersGuiTemp.put(i, tempList);
            } else {
                menuGuiTemp.add(getGlass());
            }
        }

        if (othersGuiTemp.isEmpty()) {
            menuGuiTemp = new ArrayList<>(this.menuGui);
            guisTemp = new HashMap<>(guis);
            guisTempInvert = new HashMap<>(guisInvert);
            othersGuiTemp = new HashMap<>(othersGui);
        }

        this.menuGui.clear();
        menuGui.addAll(menuGuiTemp);
        this.guis.clear();
        guisTemp.forEach(this.guis::put);
        this.guisInvert.clear();
        guisTempInvert.forEach(this.guisInvert::put);
        othersGui.clear();
        othersGuiTemp.forEach(this.othersGui::put);

        outCash.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");
        outCash.set("menu.size", menuGuiTemp.size());
        for (int i = 0; i < menuGuiTemp.size(); i++) {
            ItemStack itemStack = menuGuiTemp.get(i);
            if (!itemStack.isSimilar(getGlass())) {
                outCash.set("menu." + i + ".material", itemStack.getType().name());
                outCash.set("menu." + i + ".name", itemStack.getItemMeta().getDisplayName());
                List<String> listando = itemStack.getLore();
                outCash.set("menu." + i + ".lore", listando);
                String guiName = guisTemp.get(i);
                outCash.set("menu." + i + ".gui", guiName);
                List<CashItem> cashItems = othersGuiTemp.get(i);
                outCash.set("guis." + guiName + ".size", cashItems.size());
                for (int j = 0; j < cashItems.size(); j++) {
                    CashItem cashItem = cashItems.get(j);
                    if (!cashItem.isGlass()) {
                        outCash.set("guis." + guiName + "." + j + ".cost", cashItem.getCost());
                        outCash.set("guis." + guiName + "." + j + ".material", cashItem.getItemStack().getType().name());
                        outCash.set("guis." + guiName + "." + j + ".name", cashItem.getItemStack().getItemMeta().getDisplayName());
                        outCash.set("guis." + guiName + "." + j + ".commands", cashItem.getCommands());
                        List<String> lore = cashItem.getItemStack().getLore();
                        outCash.set("guis." + guiName + "." + j + ".lore", lore);
                        List<String> messages = cashItem.getMessages();
                        outCash.set("guis." + guiName + "." + j + ".messages", messages);
                    }
                }
            }
        }

        // kits.yml

        FileConfiguration kits = YamlConfiguration.loadConfiguration(new File(KITS_FILE_PATH));
        FileConfiguration outKit = new YamlConfiguration();

        this.kitList.put("pa", new KitObject(300, List.of("give %player_name% minecraft:golden_shovel 1"), List.of("$8[$aE$9S$8] $7Toma sua pá$8!".replace('$', (char) 0x00A7))));

        Map<String, KitObject> tempKitList = new HashMap<>();
        configurationSection = kits.getConfigurationSection("kits");
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                List<String> lista = kits.getStringList("kits." + key + ".text");
                putColorOnList(lista);
                tempKitList.put(key, new KitObject(kits.getInt("kits." + key + ".delay"), kits.getStringList("kits." + key + ".command"), lista));
            }
        }

        if (tempKitList.isEmpty()) {
            tempKitList = new HashMap<>(this.kitList);
        }

        this.kitList.clear();
        tempKitList.forEach(this.kitList::put);

        tempKitList.forEach((k, v) -> {
            outKit.set("kits." + k + ".delay", v.getDelay());
            outKit.set("kits." + k + ".command", v.getCommands());
            outKit.set("kits." + k + ".text", v.getMessages());
        });

        outKit.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");

        // chat.yml

        FileConfiguration chatConfig = YamlConfiguration.loadConfiguration(new File(CHAT_FILE_PATH));
        FileConfiguration outChat = new YamlConfiguration();

        this.localFormat = chatConfig.getString("format.local", "$8[$eL$8] %vault_suffix% $e%player_displayname%$8 ➤ $e%message%").replace('$', (char) 0x00A7);
        this.globalFormat = chatConfig.getString("format.global", "{canal}{clan}{sufix}{prefix}{player}{marry}{separator}").replace('$', (char) 0x00A7);
        this.staffFormat = chatConfig.getString("format.staff", "$8[$bS$8] %vault_prefix%%player_displayname%$8 ➤ $b%message%").replace('$', (char) 0x00A7);
        this.localRange = chatConfig.getInt("format.local-range", 64);
        this.autoUpdateGroups = chatConfig.getBoolean("format.auto-update-groups", false);

        this.customPlaceholdersObjectsMap.put("prefix", new CustomPlaceholdersObjects("eternia.chat.global", "%vault_prefix%", "", "", 3));
        this.customPlaceholdersObjectsMap.put("player", new CustomPlaceholdersObjects("eternia.chat.global", "%player_displayname% ", "&7Nome real&8: &3%player_name%&8.", "/profile %player_name%", 4));
        this.customPlaceholdersObjectsMap.put("separator", new CustomPlaceholdersObjects("eternia.chat.global", " &8➤ ", "", "", 6));
        this.customPlaceholdersObjectsMap.put("sufix", new CustomPlaceholdersObjects("eternia.chat.global", "%vault_suffix% ", "&7Clique para enviar uma mensagem&8.", "/msg %player_name% ", 2));
        this.customPlaceholdersObjectsMap.put("clan", new CustomPlaceholdersObjects("eternia.chat.global", "%simpleclans_tag_label%", "&7Clan&8: &3%simpleclans_clan_name%&8.", "", 1));
        this.customPlaceholdersObjectsMap.put("canal", new CustomPlaceholdersObjects("eternia.chat.global", "&8[&fG&8] ", "&7Clique para entrar no &fGlobal&8.", "/global ", 0));
        this.customPlaceholdersObjectsMap.put("marry", new CustomPlaceholdersObjects("eternia.chat.global", "%eterniamarriage_statusheart%", "&7Casado(a) com&8: &3%eterniamarriage_partner%&8.", "", 5));

        outChat.set("format.local", this.localFormat);
        outChat.set("format.global", this.globalFormat);
        outChat.set("format.staff", this.staffFormat);
        outChat.set("format.local-range", this.localRange);
        outChat.set("format.auto-update-groups", this.autoUpdateGroups);

        Map<String, CustomPlaceholdersObjects> tempCustomPlaceholdersMap = new HashMap<>();
        configurationSection = chatConfig.getConfigurationSection("placeholders");
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                tempCustomPlaceholdersMap.put(key, new CustomPlaceholdersObjects(chatConfig.getString("placeholders." + key + ".perm"), chatConfig.getString("placeholders." + key + ".value"), chatConfig.getString("placeholders." + key + ".hover-text"), chatConfig.getString("placeholders." + key + ".suggest-command"), chatConfig.getInt("placeholders." + key + ".priority")));
            }
        }

        if (tempCustomPlaceholdersMap.isEmpty()) {
            tempCustomPlaceholdersMap = new HashMap<>(customPlaceholdersObjectsMap);
        }

        this.customPlaceholdersObjectsMap.clear();
        tempCustomPlaceholdersMap.forEach(this.customPlaceholdersObjectsMap::put);

        tempCustomPlaceholdersMap.forEach((k, v) -> {
            outChat.set("placeholders." + k + ".perm", v.getPermission());
            outChat.set("placeholders." + k + ".value", v.getValue());
            outChat.set("placeholders." + k + ".hover-text", v.getHoverText());
            outChat.set("placeholders." + k + ".suggest-command", v.getSuggestCmd());
            outChat.set("placeholders." + k + ".priority", v.getPriority());
        });

        outChat.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");

        // SAVING

        try {
            outChat.save(CHAT_FILE_PATH);
            outConfig.save(CONFIG_FILE_PATH);
            outBlocks.save(BLOCKS_FILE_PATH);
            outCash.save(CASHGUI_FILE_PATH);
            outKit.save(KITS_FILE_PATH);
        } catch (IOException exception) {
            APIServer.logError("Impossível de criar arquivos em " + DATA_LAYER_FOLDER_PATH, 3);
        }

    }

    private void putColorOnList(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).replace('$', (char) 0x00A7));
        }
    }

    private ItemStack getGlass() {
        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("$7Loja de $aCash$8.".replace('$', (char) 0x00A7));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private void loadMessages() {
        Messages[] economiesID = Messages.values();
        messages = new String[Messages.values().length];

        Map<String, CustomizableMessage> defaults = new HashMap<>();

        this.addDefault(defaults, Messages.SERVER_NO_PERM, "Você não possui permissão para isso$8.", null);
        this.addDefault(defaults, Messages.SERVER_NO_PLAYER, "Esse jogador não existe$8.", null);
        this.addDefault(defaults, Messages.SERVER_IN_TELEPORT, "Você já está em teleporte$8.", null);
        this.addDefault(defaults, Messages.SERVER_MODULE_ENABLED, "Modulo de $3{0}$7 ativado e carregado$8.", "0: modulo");
        this.addDefault(defaults, Messages.SERVER_MODULE_DISABLED, "Module de $3{0}$7 desativado$8.", "0: modulo");
        this.addDefault(defaults, Messages.SERVER_DATA_LOADED, "Carregando $3{0} $7foram carregadas $3{1}$7 dados$8.", "0: modulo; 1: quantia de dados");
        this.addDefault(defaults, Messages.SERVER_NETHER_TRAP_TIMING, "Trap de portal identificada$8, $7você irá ser teleportado em $3{0}$8.", "0: tempo");
        this.addDefault(defaults, Messages.SERVER_REMOVED_ENTITIES, "Foram removidas $3{0}$7 entidades$8.", "0: quantia de entidades");
        this.addDefault(defaults, Messages.SERVER_LOGIN, "$3{1} $8[$a+$8]", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.SERVER_LOGOUT, "$3{1} $8[$c-$8]", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.SERVER_TIMING, "Você não pode usar isso pelos próximos $3{0} $7segundos$8.", "0: tempo restante");
        this.addDefault(defaults, Messages.SERVER_TPS, "TPS$8:$3 {0}$8.", "0: tps");
        this.addDefault(defaults, Messages.SERVER_MOTD_1, "$l#333832◢◤◢◤◢◤ $l#a7d95b✙ $l#69de3bE$l#46e33dt$l#3ee66ee$l#46e89fr$l#50ebc4n$l#5bebedi$l#61b4f0a$l#677ff2S$l#7469f5e$l#825ff7r$l#ab5cfav$l#c962fce$l#f36affr $l#ff5cc9S$l#ff579a$l#ff4b6fu$l#ff454er$l#ff4b41v$l#ff583bi$l#ff8250v$l#ffa959a$l#ffcc4al $l#ffe77c✙ $l#333832◥◣◥◣◥◣", null);
        this.addDefault(defaults, Messages.SERVER_MOTD_2, "$r#71b32aVanilla #b5ac2ecom #b8813eextras#74bdbb!!!", null);
        this.addDefault(defaults, Messages.ECO_PAY, "Você pagou $3{0} $7para $3{2}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.ECO_PAY_RECEIVED, "Você recebeu $3{0} $7de $3{2}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.ECO_NO_MONEY, "Você não possui todo esse dinheiro$8.", null);
        this.addDefault(defaults, Messages.ECO_AUTO_PAY, "Você não pode pagar a si mesmo$8.", null);
        this.addDefault(defaults, Messages.ECO_SET_FROM, "Você definiu para $3{0} $7o saldo de $3{2}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.ECO_SETED, "O seu saldo foi definido para $3{0} $7por $3{2}$8.", "0: quantia de money; 1: nome de quem alterou; 2: apelido de quem alterou");
        this.addDefault(defaults, Messages.ECO_REMOVE_FROM, "Você removeu $3{0} $7do saldo de $3{2}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.ECO_REMOVED, "Foi retirado $3{0} $7do seu saldo por $3{2}$8.", "0: quantia de money; 1: nome de quem removeu; 2: apelido de quem alterou");
        this.addDefault(defaults, Messages.ECO_GIVE_FROM, "Você deu $3{0} $7de saldo de $3{2}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.ECO_GIVED, "Foi recebeu $3{0} $7de saldo por $3{2}$8.", "0: quantia de money; 1: nome de quem deu; 2: apelido de quem alterou");
        this.addDefault(defaults, Messages.ECO_BALANCE, "Você possui $3{0}$8.", "0: quantia de money");
        this.addDefault(defaults, Messages.ECO_BALANCE_OTHER,  "O $3{2} $7possui $3{0}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.ECO_BALTOP_TITLE, "Top money$8:", null);
        this.addDefault(defaults, Messages.ECO_BALTOP_LIST, "$3{0} $8- $3{2} $8- $7Saldo$8: $3{3}", "0: posição do jogador; 1: nome do jogador; 2: apelido do jogador; 3: saldo do jogador");
        this.addDefault(defaults, Messages.CASH_BALANCE, "Você possui $3{0} $7de cash$8.", "0: quantia de cash");
        this.addDefault(defaults, Messages.CASH_BALANCE_OTHER, "$3{2} $7possui $3{0} $7de cash$8.", "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.CASH_NOTHING_TO_BUY, "Você não possui nada para comprar$8.", null);
        this.addDefault(defaults, Messages.CASH_BOUGHT, "Compra confirmada com sucesso$8.", null);
        this.addDefault(defaults, Messages.CASH_CANCELED, "Compra cancelada com sucesso$8.", null);
        this.addDefault(defaults, Messages.CASH_RECEVEID, "Você recebeu $3{0}$7 de cash por $3{2}$8.", "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.CASH_SENT, "Você enviou $3{0}$7 de cash para $3{2}$8.", "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.CASH_LOST, "Você perdeu $3{0}$7 de cash por $3{2}$8.", "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.CASH_REMOVED, "Você removeu $3{0}$7 de cash de $3{2}$8.", "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.CASH_COST, "Isso irá custar $3{0}$7 de cash$8.", "0: quantia de cash");
        this.addDefault(defaults, Messages.CASH_CHOOSE, "Use $6/cash accept $7ou $6/cash deny$7 para aceitar ou negar a compra$8.", null);
        this.addDefault(defaults, Messages.CASH_NO_HAS, "Você não possui $3{0}$7 de cash$8.", "0: quantia de cash");
        this.addDefault(defaults, Messages.CASH_ALREADY_BUYING, "Você já possui uma compra em andamento$8.", null);
        this.addDefault(defaults, Messages.AFK_BROADCAST_KICK, "$3{1} $7estava AFK e foi kickado$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.AFK_KICKED, "Você foi kickado por estar AFK$8.", null);
        this.addDefault(defaults, Messages.AFK_AUTO_ENTER, "$3{1}$7 ficou ausente agora está AFK$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.AFK_LEAVE, "$3{1} $7saiu do modo AFK$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.AFK_ENTER, "$3{1} $7entrou no modo AFK$8.", null);
        this.addDefault(defaults, Messages.GLOW_ENABLED, "Glow ativado$8.", null);
        this.addDefault(defaults, Messages.GLOW_DISABLED, "Glow desativado$8.", null);
        this.addDefault(defaults, Messages.GLOW_COLOR_CHANGED, "Cor do glow alterada para $3{0}$8.", "0: cor do glow");
        this.addDefault(defaults, Messages.EXP_SET_FROM, "Você definiu para $3{0} $7o saldo de exp de $3{2}$8.", "0: quantia de exp; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.EXP_SETED, "O seu saldo de exp foi definido para $3{0} $7por $3{2}$8.", "0: quantia de exp; 1: nome de quem alterou; 2: apelido de quem alterou");
        this.addDefault(defaults, Messages.EXP_REMOVE_FROM, "Você removeu $3{0} $7do saldo de exp de $3{2}$8.", "0: quantia de exp; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.EXP_REMOVED, "Foi retirado $3{0} $7do seu saldo de exp por $3{2}$8.", "0: quantia de exp; 1: nome de quem removeu; 2: apelido de quem alterou");
        this.addDefault(defaults, Messages.EXP_GIVE_FROM, "Você deu $3{0} $7de saldo de exp de $3{2}$8.", "0: quantia de exp; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.EXP_GIVED, "Foi recebeu $3{0} $7de saldo de exp por $3{2}$8.", "0: quantia de exp; 1: nome de quem deu; 2: apelido de quem alterou");
        this.addDefault(defaults, Messages.EXP_BALANCE, "Você possui $3{0}$7 de exp$8.", "0: quantia de exp");
        this.addDefault(defaults, Messages.EXP_BOTTLED, "Tome sua garrafinha$8.", null);
        this.addDefault(defaults, Messages.EXP_INSUFFICIENT, "Você não possui tudo isso de exp$8.", null);
        this.addDefault(defaults, Messages.EXP_WITHDRAW, "Você sacou $3{0}$7 níveis$8.", "0: quantia de nível");
        this.addDefault(defaults, Messages.EXP_DEPOSIT, "Você depositou $3{0}$7 níveis$8.", "0: quantia de nível");
        this.addDefault(defaults, Messages.GAMEMODE_SETED, "Seu modo de jogo foi definido para {0}$8.", "0: modo de jogo");
        this.addDefault(defaults, Messages.GAMEMODE_SET_FROM, "O modo de jogo de $3{2}$7 foi definido para {0}$8.", "0: modo de jogo; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.WARP_TELEPORTED, "Você foi teleportado até o $3{0}$8.", "0: nome da warp");
        this.addDefault(defaults, Messages.WARP_CREATED, "A warp $3{0}$7 foi criada com sucesso$8.", "0: nome da warp");
        this.addDefault(defaults, Messages.WARP_DELETED, "A warp $3{0}$7 foi deletada$8.", "0: nome da warp");
        this.addDefault(defaults, Messages.WARP_NOT_FOUND, "A warp $3{0}$7 não existe$8.", "0: nome da warp");
        this.addDefault(defaults, Messages.WARP_LIST, "Warps$8: $3{0}$8.", "0: lista de warps");
        this.addDefault(defaults, Messages.WARP_SPAWN_TELEPORTED, "Você foi teleportado até o $3Spawn$8.", null);
        this.addDefault(defaults, Messages.WARP_SPAWN_TELEPORT_TARGET, "Você teleportou $3{1}$7 até o $3Spawn$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.WARP_SPAWN_TELEPORTED_BY, "Você foi teleportado até o $3Spawn$7 por $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.WARP_SPAWN_CREATED, "$3Spawn $7definido com sucesso$8.", null);
        this.addDefault(defaults, Messages.WARP_SPAWN_NOT_FOUND, "O $3Spawn $7não foi definido ainda$8.", null);
        this.addDefault(defaults, Messages.WARP_SHOP_CREATED, "$3Loja $7definida com sucesso$8.", null);
        this.addDefault(defaults, Messages.WARP_SHOP_DELETED, "$3Loja $7deletada com sucesso$8.", null);
        this.addDefault(defaults, Messages.WARP_SHOP_TELEPORTED, "Você foi teleportado até a $3Loja$8.", null);
        this.addDefault(defaults, Messages.WARP_SHOP_PLAYER_TELEPORTED, "Você foi teleportado até a loja de$3{0}$8.", "0: nome da loja");
        this.addDefault(defaults, Messages.WARP_SHOP_NOT_FOUND, "$3{1}$7 não possui loja$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.WARP_SHOP_CENTRAL_NOT_FOUND, "$3Loja $7não encontrada$8.", null);
        this.addDefault(defaults, Messages.TELEPORT_ALL_PLAYERS, "$3{1}$7 teleportou todos jogadores até si$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.TELEPORT_NOT_REQUESTED, "Nenhum pedido de teleporte solicitado$8.", null);
        this.addDefault(defaults, Messages.TELEPORT_TARGET_ACCEPT, "$3{1}$7 aceitou o seu pedido de tpa$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.TELEPORT_GOING_TO_PLAYER, "Você foi teleportado até $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.TELEPORT_ACCEPT, "Você aceitou o pedido de tpa de $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.TELEPORT_TARGET_OFFLINE, "O usuário que fez o pedido de tpa estáo offline$8.", null);
        this.addDefault(defaults, Messages.TELEPORT_TARGET_DENIED, "$3{1}$7 negou o seu pedido de tpa$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.TELEPORT_DENIED, "Você negou o pedido de tpa de $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.TELEPORT_CANT_YOURSELF, "Você não pode enviar um pedido para si mesmo$8.", null);
        this.addDefault(defaults, Messages.TELEPORT_ALREADY_REQUESTED, "$3{1}$7 já possui um pedido de tpa$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.TELEPORT_SENT, "Você enviou um pedido de tpa para $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.TELEPORT_RECEIVED, "Você recebeu um pedido de tpa de $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.TELEPORT_MOVED, "Você se moveu e por isso o teleporte foi cancelado$8.", null);
        this.addDefault(defaults, Messages.TELEPORT_TIMING, "Você irá ser teleportado em $3{0}$7 segundos$8.", "0: tempo para ser teleportado");
        this.addDefault(defaults, Messages.TELEPORT_BACK_NOT_DIED, "Você ainda não morreu para poder usar esse comando$8.", null);
        this.addDefault(defaults, Messages.TELEPORT_BACK_WITH_COST, "Você foi teleportado até o local de sua morte por $3{0}$8.", "0: custo do back");
        this.addDefault(defaults, Messages.TELEPORT_BACK_WITHOUT_COST, "Você foi teleportado até o local de sua morte de graça$8.", null);
        this.addDefault(defaults, Messages.REWARD_INVALID_KEY, "$3{0}$7 não é uma chave válida$8.", "0: chave");
        this.addDefault(defaults, Messages.REWARD_CREATED, "Reward criado com sucesso chave $3{0}$8.", "0: chave");
        this.addDefault(defaults, Messages.REWARD_NOT_FOUND, "Não foi encontrado nenhum reward com o nome de $3{0}$8.", "0: reward");
        this.addDefault(defaults, Messages.CHAT_NICK_CHANGED, "O seu nick foi alterado com sucesso para $3{0}$8.", "0: apelido do jogador");
        this.addDefault(defaults, Messages.CHAT_NICK_CHANGED_BY, "$3{2}$7alterou seu nick para $3{0}$8.", "0: novo apelido; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, Messages.CHAT_NICK_CHANGED_FROM, "Você alterou o apelido de $3{2}$7 para $3{0}", "0: novo apelido; 1: nome do jogador 2: apelido do jogador");
        this.addDefault(defaults, Messages.CHAT_NICK_CLEAR, "Seu apelido foi removido$8.", null);
        this.addDefault(defaults, Messages.CHAT_NICK_CLEAR_BY, "Seu apelido foi removido por $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.CHAT_NICK_CLEAR_FROM, "Você removeu o apelido de $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.CHAT_NICK_CHANGE_REQUEST, "Seu apelido será definido como $3{0}$7 por $3{1}$8.", "0: apelido do jogador; 1: custo");
        this.addDefault(defaults, Messages.CHAT_NICK_USE, "Para aceitar seu novo apelido use $6/nick accept $7ou $6/nick deny$8.", null);
        this.addDefault(defaults, Messages.CHAT_NICK_NOT_REQUESTED, "Você não tem nenhum pedido de mudança de apelido$8.", null);
        this.addDefault(defaults, Messages.CHAT_NICK_DENIED, "Você cancelou a mudança de apelido$8.", null);
        this.addDefault(defaults, Messages.CHAT_BROADCAST, "$7{0}", "0: mensagem");
        this.addDefault(defaults, Messages.CHAT_IGNORE, "Você agora está ignorando o $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.CHAT_UNIGNORE, "Você agora não está ignorando o $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.CHAT_SPY_ENABLED, "Chat spy ativado$8.", null);
        this.addDefault(defaults, Messages.CHAT_SPY_DISABLED, "Chat spy desativado$8.", null);
        this.addDefault(defaults, Messages.CHAT_ARE_IGNORED, "Este usuário está ignorando você$8.", null);
        this.addDefault(defaults, Messages.CHAT_NO_ONE_TO_RESP, "Você não possui ninguém para responder$8.", null);
        this.addDefault(defaults, Messages.CHAT_CHANNEL_CHANGED, "Você foi para o canal $3{0}$8.", "0: nome do canal");
        this.addDefault(defaults, Messages.CHAT_TELL_LOCKED, "Você travou a conversa privada com $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.CHAT_TELL_UNLOCKED, "Você destravou a conversa privada com $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.CHAT_ARE_MUTED, "Você está mutado pelos próximos $3{0}$8.", "0: tempo restante");
        this.addDefault(defaults, Messages.CHAT_NO_ONE_NEAR, "Não há ninguém para ti ouvir$8.", null);
        this.addDefault(defaults, Messages.CHAT_TELL_TO, "$8[$6P$8]$6 {1} -> {3} ➤ {0}", "0: mensagem; 1: nome do enviante; 2: apelido do enviante; 3: nome do recebinte; 4: apelido do recebinte");
        this.addDefault(defaults, Messages.CHAT_TELL_FROM, "$8[$6P$8]$6 {1} <- {3} ➤ {0}", "0: mensagem; 1: nome do recebinte; 2: apelido do recebinte; 3: nome do enviante; 4: apelido do enviante");
        this.addDefault(defaults, Messages.CHAT_CHANNELS_MUTED, "Todos os canais estão silenciados$8.", null);
        this.addDefault(defaults, Messages.CHAT_CHANNELS_ENABLED, "$3{1}$7 mutou todos os canais$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.CHAT_CHANNELS_DISABLED, "$3{1}$7 desmutou todos os canais$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.CHAT_BROADCAST_TEMP_MUTE, "$3{1}$7 foi mutado em $3{2}$7 por $3{4}$7 motivo$8:$3{5}", "0: nome do mutado; 1: apelido do mutado; 2: tempo mutado; 3: nome de quem mutou; 4: apelido de quem mutou; 5: motivo");
        this.addDefault(defaults, Messages.CHAT_BROADCAST_UNMUTE, "$3{1}$7 foi desmutado por $3{3}$8.", "0: nome do mutado; 1: apelido do mutado; 2: nome de quem mutou; 3: apelido de quem mutou");
        this.addDefault(defaults, Messages.CHAT_BROADCAST_MUTE, "$3{1}$7 foi mutado permanentemente por $3{3}$7 motivo$8:$3{4}", "0: nome do mutado; 1: apelido do mutado; 2: nome de quem mutou; 3: apelido de quem mutou; 4: motivo");
        this.addDefault(defaults, Messages.SPAWNER_INV_FULL, "O inventário do jogador está cheio$8.", null);
        this.addDefault(defaults, Messages.SPAWNER_RECEIVED, "Você recebeu $3{3}$7 spawner de $3{0}$7 por $3{2}$8.", "0: mob; 1: nome do jogador; 2: apelido do jogador; 3: quantia");
        this.addDefault(defaults, Messages.SPAWNER_SENT, "Você enviou $3{3} spawner de $3{0}$7 para $3{2}$8.", "0: mob; 1: nome do jogador; 2: apelido do jogador; 3: quantia");
        this.addDefault(defaults, Messages.SPAWNER_SEND_TYPES, "Os tipos de spawners válidos são$8: $3{0}$8.", "0: tipos");
        this.addDefault(defaults, Messages.SPAWNER_CANT_CHANGE_NAME, "Você não pode renomear spawners$8.", null);
        this.addDefault(defaults, Messages.SPAWNER_WORLD_BLOCKED, "Você não pode quebrar spawners nesse mundo$8.", null);
        this.addDefault(defaults, Messages.SPAWNER_DROP_FAILED, "Não foi dessa vez$8.", null);
        this.addDefault(defaults, Messages.SPAWNER_SILK_REQUESTED, "Você precisa de uma picareta com toque suave para isso$8.", null);
        this.addDefault(defaults, Messages.HOME_NOT_FOUND, "A home $3{0}$7 não existe$8.", "0: nome da home");
        this.addDefault(defaults, Messages.HOME_DELETED, "Você deletou a home $3{0}$8.", "0: nome da home");
        this.addDefault(defaults, Messages.HOME_LIMIT_REACHED, "Você alcançou o limite de homes$8, $7tome um bússola$8.", null);
        this.addDefault(defaults, Messages.HOME_CREATED, "Home definida com sucesso$8.", null);
        this.addDefault(defaults, Messages.HOME_STRING_LIMIT, "O nome $3{0}$7 pasou do limite de $310 $7caracteres permitidos$8.", "0: nome da home");
        this.addDefault(defaults, Messages.HOME_GOING, "Você foi até a sua home $3{0}$8.", "0: nome da home");
        this.addDefault(defaults, Messages.HOME_LIST, "Suas homes$8: $3{0}$8.", "0: lista de homes");
        this.addDefault(defaults, Messages.ITEM_NOT_FOUND, "Nenhum item foi encontrado em sua mão$8.", null);
        this.addDefault(defaults, Messages.ITEM_NBT_ADDKEY, "Foi adicionado a chave $3{0}$7 com valor $3{1}$7 ao item$8.", "0: chave; 1: valor");
        this.addDefault(defaults, Messages.ITEM_CLEAR_LORE, "A lore do item foi removida$8.", null);
        this.addDefault(defaults, Messages.ITEM_CLEAR_NAME, "O nome do item foi removido$8.", null);
        this.addDefault(defaults, Messages.ITEM_ADD_LORE, "Foi adicionada a linha $3{0}$7 a lore$8.", "0: nova linha de lore");
        this.addDefault(defaults, Messages.ITEM_SET_LORE, "A lore foi limpa e definida como$3 {0}$8.", "0: lore");
        this.addDefault(defaults, Messages.ITEM_SET_NAME, "O nome do item foi definido como $3{0}$8.", "0: nome do item");
        this.addDefault(defaults, Messages.ITEM_HELMET, "Você equipou seu caçapete$8.", null);
        this.addDefault(defaults, Messages.SPEED_SET, "Sua velocidade foi definida para $3{0}$8.", "0: nova velocidade");
        this.addDefault(defaults, Messages.SPEED_LIMIT, "Você precisa escolher um valor entre $31 $7e $310$8.", null);
        this.addDefault(defaults, Messages.PROFILE_TITLE, "$8[]====[$7Perfil$8]====[]", null);
        this.addDefault(defaults, Messages.PROFILE_REGISTER_DATA, "$7Registro$8: $3{0}", "0: data de registro");
        this.addDefault(defaults, Messages.PROFILE_LAST_LOGIN, "$7Ultimo login$8: $3{0}", "0: ultimo login");
        this.addDefault(defaults, Messages.PROFILE_ACCOUNT_HOURS, "$7Horas de jogo$8: $3{0}", "0: horas registradas");
        this.addDefault(defaults, Messages.GODMODE_ENABLED, "Você ativou o god mode$8.", null);
        this.addDefault(defaults, Messages.GODMODE_DISABLED, "Você desativou o god mode$8.", null);
        this.addDefault(defaults, Messages.SUICIDE_BROADCAST, "$3{1} disse$8: $3{2}$7 e logo após se matou$8.", "0: nome do jogador; 1: apelido do jogador; 2: mensagem");
        this.addDefault(defaults, Messages.LIGHTNING_CURSOR, "THOR$8! (pausa dramática) $7FILHO DE ODIM$8!", null);
        this.addDefault(defaults, Messages.LIGHTNING_TARGET, "Você castigou $3{1}$7 com as forças divinas$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.LIGHTNING_RECEIVED, "Você foi punido por $3{1}$7 os deuses não estão ao seu favor$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.ITEM_CONDENSER, "Compactando blocos$8.", null);
        this.addDefault(defaults, Messages.FEED_YOURSELF, "Você se saciou$8.", null);
        this.addDefault(defaults, Messages.FEED_TARGET, "Você saciou o $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.FEED_RECEIVED, "Você foi saciado por $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.FEED_YOURSELF, "Você foi saciado por $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.STATS_MEM, "Memória$8: $3{0}MB$8/$3{1}MB$8.", "0: memória usada; 1: memória total");
        this.addDefault(defaults, Messages.STATS_HOURS, "Tempo online$8: $7Dias$8: $3{0} $7horas$8: $3{1} $7minutos$8:{2} $7segundos$8:$3{3}$8.", "0: dias; 1: horas; 2: minutos; 3: segundos");
        this.addDefault(defaults, Messages.FLY_ENABLED, "Modo voar ativado$8.", null);
        this.addDefault(defaults, Messages.FLY_DISABLED, "Modo voar desativado$8.", null);
        this.addDefault(defaults, Messages.FLY_ENABLED_FROM, "Modo voar ativado para $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.FLY_ENABLED_BY, "Modo voar ativado por $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.FLY_DISABLED_FROM, "Modo voar desativado para $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.FLY_DISABLED_BY, "Modo voar desativado por $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.FLY_ARE_PVP, "Você está em combate$8, $7aguarde $3{0}$7 fora de combate para poder voar$8.", "0: tempo em combate");
        this.addDefault(defaults, Messages.FLY_TARGET_ARE_PVP, "O alvo está em combate pelos próximos $3{0} $7segundos$8, $7logo não pode voar$8.", "0: tempo em combate");
        this.addDefault(defaults, Messages.FLY_ENTER_PVP, "Você entrou em combate e seu voar foi desativado$8.", null);
        this.addDefault(defaults, Messages.KIT_NOT_FOUND, "Nenhum kit com o nome de $3{0}$7 foi encontrado$8.", "0: nome do kit");
        this.addDefault(defaults, Messages.KIT_LIST, "Kits$8: $3{0}$8.", "0: lista de kits");
        this.addDefault(defaults, Messages.NIGHT_PLAYER_SLEEPING, "$3{1}$7 está dormindo$8, $7durma também para passar a noite mais rápido$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(defaults, Messages.NIGHT_SKIPPING, "A noite está passando em $3{0}$8.", "0: nome do mundo");
        this.addDefault(defaults, Messages.NIGHT_SKIPPED, "A noite passou em $3{0}$8.", "0: nome do mundo");

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(MESSAGES_FILE_PATH));

        for (Messages messagesEnum : economiesID) {
            CustomizableMessage messageData = defaults.get(messagesEnum.name());

            final String path = getPath(messagesEnum);

            if (messageData == null) {
                messageData = new CustomizableMessage(messagesEnum, this.serverPrefix +"Mensagem faltando para $3" + messagesEnum.name() + "$8.", null);
                APIServer.logError("Entrada para a mensagem " + messagesEnum.name(), 2);
            }

            this.messages[messagesEnum.ordinal()] = config.getString(path + messagesEnum.name() + ".text", messageData.text);
            config.set(path + messagesEnum.name() + ".text", this.messages[messagesEnum.ordinal()]);

            this.messages[messagesEnum.ordinal()] = this.messages[messagesEnum.ordinal()].replace('$', (char) 0x00A7);

            if (messageData.getNotes() != null) {
                messageData.setNotes(config.getString(path + messagesEnum.name() + ".notes", messageData.getNotes()));
                config.set(path + messagesEnum.name() + ".notes", messageData.getNotes());
            }

        }

        // SAVE

        try {
            config.save(MESSAGES_FILE_PATH);
        } catch (IOException exception) {
            APIServer.logError("Impossível de criar arquivos em " + DATA_LAYER_FOLDER_PATH, 3);
        }

        defaults.clear();
        System.gc();
    }

    private void addDefault(Map<String, CustomizableMessage> defaults, Messages id, String text, String notes) {
        CustomizableMessage message = new CustomizableMessage(id, text, notes);
        defaults.put(id.name(), message);
    }

    public void sendMessage(CommandSender player, Messages messagesId, String... args) {
        sendMessage(player, messagesId, true, args);
    }

    public void sendMessage(CommandSender player, Messages messagesId, boolean prefix, String... args) {
        player.sendMessage(getMessage(messagesId, prefix, args));
    }

    public String getMessage(Messages messagesId, boolean prefix, String... args) {
        String message = messages[messagesId.ordinal()];

        for (int i = 0; i < args.length; i++) {
            String param = args[i];
            message = message.replace("{" + i + "}", param);
        }

        if (prefix) {
            return this.serverPrefix + message;
        }

        return message;
    }

    private String getPath(Messages messagesEnum) {
        switch (messagesEnum.name().split("_")[0].hashCode()) {
            case -1852497085:
                return "server.";
            case -577575125:
                return "teleport.";
            case 64710:
                return "afk.";
            case 68465:
                return "eco.";
            case 69117:
                return "exp.";
            case 2061107:
                return "cash.";
            case 2067288:
                return "chat.";
            case 2223327:
                return "home.";
            case 2257683:
                return "item.";
            case 2656904:
                return "warp.";
            default:
                return "generic.";
        }
    }

}
