package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.enums.Messages;
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

    private static final String dataLayerFolderPath = "plugins" + File.separator + "EterniaServer";
    private static final String messagesFilePath = dataLayerFolderPath + File.separator + "messages.yml";
    private static final String configFilePath = dataLayerFolderPath + File.separator + "config.yml";

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

    protected Configs() {

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

        this.afkPlaceholder = config.getString("placeholders.afk", "$9 AFK").replace('$', (char) 0x00A7);
        this.godPlaceholder = config.getString("placeholders.godmode", "$9 GOD").replace('$', (char) 0x00A7);

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

        List<String> defaultMaterialBlocksList = new ArrayList<>();
        for (Material material : this.elevatorMaterials) {
            defaultMaterialBlocksList.add(material.name());
        }

        List<String> tempBlockMaterials = config.getStringList("elevator.block");
        List<String> tempBlockBaltop = config.getStringList("money.blacklisted-baltop");
        List<String> tempBlockWorld = config.getStringList("bed.blacklisted-worlds");
        List<String> tempBlockedCommands = config.getStringList("blocked-commands");
        List<String> tempBlockWorldSpawners = config.getStringList("spawners.blacklisted-worlds");

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

        try {
            outConfig.save(configFilePath);
        } catch (IOException exception) {
            APIServer.logError("Impossível de criar arquivos em " + dataLayerFolderPath, 3);
        }
        loadMessages();

    }

    private void loadMessages() {
        Messages[] economiesID = Messages.values();
        messages = new String[Messages.values().length];

        Map<String, CustomizableMessage> defaults = new HashMap<>();

        this.addDefault(defaults, Messages.SERVER_NO_PERM, "Você não possui permissão para isso$8.", null);
        this.addDefault(defaults, Messages.SERVER_NO_PLAYER, "Esse jogador não existe$8.", null);
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

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(messagesFilePath));

        for (Messages messages : economiesID) {
            CustomizableMessage messageData = defaults.get(messages.name());

            String path;

            if (messages.name().contains("ECO")) {
                path = "eco.";
            } else if (messages.name().contains("SERVER")) {
                path = "server.";
            } else if (messages.name().contains("CASH")) {
                path = "cash.";
            } else if (messages.name().contains("AFK")) {
                path = "afk.";
            } else if (messages.name().contains("EXP")) {
                path = "exp.";
            } else {
                path = "generic.";
            }

            if (messageData == null) {
                messageData = new CustomizableMessage(messages, this.serverPrefix +"Mensagem faltando para $3" + messages.name() + "$8.", null);
                APIServer.logError("Entrada para a mensagem " + messages.name(), 2);
            }

            this.messages[messages.ordinal()] = config.getString(path + messages.name() + ".text", messageData.text);
            config.set(path + messages.name() + ".text", this.messages[messages.ordinal()]);

            this.messages[messages.ordinal()] = this.messages[messages.ordinal()].replace('$', (char) 0x00A7);

            if (messageData.getNotes() != null) {
                messageData.setNotes(config.getString(path + messages.name() + ".notes", messageData.getNotes()));
                config.set(path + messages.name() + ".notes", messageData.getNotes());
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

}
