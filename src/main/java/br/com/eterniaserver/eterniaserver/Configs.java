package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.enums.MessagesEnum;
import br.com.eterniaserver.eterniaserver.generics.APIServer;
import br.com.eterniaserver.eterniaserver.generics.PluginVars;
import br.com.eterniaserver.eterniaserver.objects.CustomizableMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configs {

    public static Configs instance;

    protected Configs() {

        loadConfig();
        loadMessages();

        instance = this;

    }

    private String[] messages;

    public boolean moduleBed;
    public boolean moduleBlock;
    public boolean moduleCash;
    public boolean moduleChat;
    public boolean moduleClear;
    public boolean moduleCommands;
    public boolean moduleEconomy;
    public boolean moduleElevator;
    public boolean moduleExperience;
    public boolean moduleHomes;
    public boolean moduleKits;
    public boolean moduleSpawners;
    public boolean moduleTeleports;
    public boolean moduleRewards;
    public boolean moduleSchedule;

    public String afkPlaceholder;
    public String godPlaceholder;

    public String tableKits;
    public String tablePlayer;
    public String tableRewards;
    public String tableLocations;

    public boolean asyncCheck;
    public int pluginTicks;
    public int scheduleThreads;
    public int afkTimer;
    public boolean afkKick;
    public int cooldown;
    public int pvpTime;
    public int clearRange;
    public String serverPrefix;

    public List<Material> elevatorMaterials = new ArrayList<>();
    public int elevatorMax;
    public int elevatorMin;

    public double startMoney;
    public double backCost;
    public double nickCost;
    public String singularName;
    public String pluralName;
    public List<String> blacklistedBaltop = new ArrayList<>();

    public int nightSpeed;
    public List<String> blacklistedWorldsBed = new ArrayList<>();

    public List<String> blockedCommands = new ArrayList<>();

    public ChatColor mobSpawnerColor;
    public boolean invDrop;
    public double dropChance;
    public boolean preventAnvil;
    public List<String> blacklistedWorldsSpawners = new ArrayList<>();

    private final String dataLayerFolderPath = "plugins" + File.separator + "EterniaServer";
    private final String messagesFilePath = dataLayerFolderPath + File.separator + "messages.yml";
    private final String configFilePath = dataLayerFolderPath + File.separator + "config.yml";

    private void loadConfig() {

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(configFilePath));
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

        this.afkPlaceholder = config.getString("placeholders.afk", "&9 AFK");
        this.godPlaceholder = config.getString("placeholders.godmode", "&9 GOD");

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
        this.serverPrefix = config.getString("server.prefix", "$8[$aE$9S$8]$7 ");

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

        List<String> defaultMaterialBlocksList = new ArrayList<>();
        for (Material config_siege_block : this.elevatorMaterials) {
            defaultMaterialBlocksList.add(config_siege_block.name());
        }

        List<String> tempBlockMaterials = config.getStringList("elevator.block");
        List<String> tempBlockBaltop = config.getStringList("money.blacklisted-baltop");
        List<String> tempBlockWorld = config.getStringList("bed.blacklisted-worlds");
        List<String> tempBlockedCommands = config.getStringList("blocked-commands");
        List<String> tempBlockWorldSpawners = config.getStringList("spawners.blacklisted-worlds");

        if (tempBlockMaterials.size() == 0) {
            tempBlockMaterials = defaultMaterialBlocksList;
        }

        if (tempBlockBaltop.size() == 0) {
            tempBlockBaltop = blacklistedBaltop;
        }

        if (tempBlockWorld.size() == 0) {
            tempBlockWorld = blacklistedWorldsBed;
        }

        if (tempBlockedCommands.size() == 0) {
            tempBlockedCommands = blockedCommands;
        }

        if (tempBlockWorldSpawners.size() == 0) {
            tempBlockWorldSpawners = blacklistedWorldsSpawners;
        }

        this.elevatorMaterials = new ArrayList<>();
        for (String material : tempBlockMaterials) {
            Material blockMaterial = Material.getMaterial(material);
            if (blockMaterial == null) {
                APIServer.logError("Configuração de elevadores material " + material + " não encontrado", 2);
            } else {
                this.elevatorMaterials.add(blockMaterial);
            }
        }
        this.blacklistedBaltop = new ArrayList<>();
        this.blacklistedBaltop.addAll(tempBlockBaltop);
        this.blacklistedWorldsBed = new ArrayList<>();
        this.blacklistedWorldsBed.addAll(tempBlockWorld);
        this.blockedCommands = new ArrayList<>();
        this.blockedCommands.addAll(tempBlockedCommands);
        this.blacklistedWorldsSpawners = new ArrayList<>();
        this.blacklistedWorldsSpawners.addAll(tempBlockWorldSpawners);

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

        outConfig.set("placeholders.afk", this.afkPlaceholder);
        outConfig.set("placeholders.godmode", this.afkPlaceholder);

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

        this.serverPrefix = serverPrefix.replace('$', (char) 0x00A7);
        this.afkPlaceholder = afkPlaceholder.replace('$', (char) 0x00A7);
        this.godPlaceholder = godPlaceholder.replace('$', (char) 0x00A7);

        try {
            outConfig.save(configFilePath);
        } catch (IOException exception) {
            APIServer.logError("Impossível de criar arquivos em " + dataLayerFolderPath, 3);
        }

    }

    private void loadMessages() {
        MessagesEnum[] economiesID = MessagesEnum.values();
        messages = new String[MessagesEnum.values().length];

        Map<String, CustomizableMessage> defaults = new HashMap<>();

        this.addDefault(defaults, MessagesEnum.ServerNoPerm, "Você não possui permissão para isso$8.", null);
        this.addDefault(defaults, MessagesEnum.ServerNoPlayer, "Esse jogador não existe$8.", null);
        this.addDefault(defaults, MessagesEnum.EcoPay, "Você pagou $3{0} $7para $3{2}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, MessagesEnum.EcoPayReceived, "Você recebeu $3{0} $7de $3{2}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, MessagesEnum.EcoNoMoney, "Você não possui todo esse dinheiro$8.", null);
        this.addDefault(defaults, MessagesEnum.EcoAutoPay, "Você não pode pagar a si mesmo$8.", null);
        this.addDefault(defaults, MessagesEnum.EcoSetFrom, "Você definiu para $3{0} $7o saldo de $3{2}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, MessagesEnum.EcoSeted, "O seu saldo foi definido para $3{0} $7por $3{2}$8.", "0: quantia de money; 1: nome de quem alterou; 2: apelido de quem alterou");
        this.addDefault(defaults, MessagesEnum.EcoRemoveFrom, "Você removeu $3{0} $7do saldo de $3{2}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, MessagesEnum.EcoRemoved, "Foi retirado $3{0} $7do seu saldo por $3{2}$8.", "0: quantia de money; 1: nome de quem removeu; 2: apelido de quem alterou");
        this.addDefault(defaults, MessagesEnum.EcoGiveFrom, "Você deu $3{0} $7de saldo de $3{2}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, MessagesEnum.EcoGived, "Foi recebeu $3{0} $7de saldo por $3{2}$8.", "0: quantia de money; 1: nome de quem deu; 2: apelido de quem alterou");
        this.addDefault(defaults, MessagesEnum.EcoBalance, "Você possui $3{0}$8.", "0: quantia de money");
        this.addDefault(defaults, MessagesEnum.EcoBalanceOther,  "O $3{2} $7possui $3{0}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, MessagesEnum.EcoBaltopTitle, "Top money$8:", null);
        this.addDefault(defaults, MessagesEnum.EcoBaltopList, "$3{0} $8- $3{2} $8- $7Saldo$8: $3{3}", "0: posição do jogador; 1: nome do jogador; 2: apelido do jogador; 3: saldo do jogador");
        this.addDefault(defaults, MessagesEnum.CashBalance, "Você possui $3{0} $7de cash$8.", "0: quantia de cash");
        this.addDefault(defaults, MessagesEnum.CashBalanceOther, "$3{2} $7possui $3{0} $7de cash$8.", "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, MessagesEnum.CashNothingToBuy, "Você não possui nada para comprar$8.", null);
        this.addDefault(defaults, MessagesEnum.CashBought, "Compra confirmada com sucesso$8.", null);
        this.addDefault(defaults, MessagesEnum.CashCanceled, "Compra cancelada com sucesso$8.", null);
        this.addDefault(defaults, MessagesEnum.CashReceveid, "Você recebeu $3{0}$7 de cash por $3{2}$8.", "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, MessagesEnum.CashSent, "Você enviou $3{0}$7 de cash para $3{2}$8.", "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, MessagesEnum.CashLost, "Você perdeu $3{0}$7 de cash por $3{2}$8.", "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(defaults, MessagesEnum.CashRemoved, "Você removeu $3{0}$7 de cash de $3{2}$8.", "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador");

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(messagesFilePath));

        for (MessagesEnum messagesEnum : economiesID) {
            CustomizableMessage messageData = defaults.get(messagesEnum.name());

            String path;

            if (messagesEnum.name().contains("Eco")) {
                path = "eco.";
            } else if (messagesEnum.name().contains("Server")) {
                path = "server.";
            } else if (messagesEnum.name().contains("Cash")) {
                path = "cash.";
            } else {
                path = "generic.";
            }

            if (messageData == null) {
                messageData = new CustomizableMessage(messagesEnum, this.serverPrefix +"Mensagem faltando para $3" + messagesEnum.name() + "$8.", null);
                APIServer.logError("Entrada para a mensagem " + messagesEnum.name(), 2);
            }

            messages[messagesEnum.ordinal()] = config.getString(path + messagesEnum.name() + ".text", messageData.text);
            config.set(path + messagesEnum.name() + ".text", messages[messagesEnum.ordinal()]);

            messages[messagesEnum.ordinal()] = messages[messagesEnum.ordinal()].replace('$', (char) 0x00A7);

            if (messageData.notes != null) {
                messageData.notes = config.getString(path + messagesEnum.name() + ".notes", messageData.notes);
                config.set(path + messagesEnum.name() + ".notes", messageData.notes);
            }

        }

        try {
            config.save(messagesFilePath);
        } catch (IOException exception) {
            APIServer.logError("Impossível de criar arquivos em " + dataLayerFolderPath, 3);
        }

        defaults.clear();
        System.gc();
    }

    private void addDefault(Map<String, CustomizableMessage> defaults, MessagesEnum id, String text, String notes) {
        CustomizableMessage message = new CustomizableMessage(id, text, notes);
        defaults.put(id.name(), message);
    }

    public void sendMessage(CommandSender player, MessagesEnum messagesId, String... args) {
        sendMessage(player, messagesId, true, args);
    }

    public void sendMessage(CommandSender player, MessagesEnum messagesId, boolean prefix, String... args) {
        player.sendMessage(getMessage(messagesId, prefix, args));
    }

    public String getMessage(MessagesEnum messagesId, boolean prefix, String... args) {
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

}
