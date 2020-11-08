package br.com.eterniaserver.eterniaserver.configurations.locales;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.configurations.GenericCfg;
import br.com.eterniaserver.eterniaserver.enums.ConfigStrings;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConstantsCfg extends GenericCfg {

    public ConstantsCfg(String[] strings) {

        super(strings, null, null, null, null);

        FileConfiguration file = YamlConfiguration.loadConfiguration(new File(Constants.CONSTANTS_FILE_PATH));
        FileConfiguration outFile = new YamlConfiguration();

        setString(ConfigStrings.SERVER_PREFIX, file, outFile, "server.prefix", "$8[$aE$9S$8]$7 ");
        setString(ConfigStrings.BALANCE_TOP_TAG, file, outFile, "server.baltop", "$8[$2Magnata$8]");
        setString(ConfigStrings.BOTTLE_EXP_NAME, file, outFile, "server.bottle-exp", "$8[$eGarrafa de EXP$8]");
        setString(ConfigStrings.NOT_SUPPORTED, file, outFile, "server.no-supported", "$7Sem suporte");
        setString(ConfigStrings.CLEAR_STRING, file, outFile, "server.clear-str", "clear");
        setString(ConfigStrings.NO_REGISTER, file, outFile, "server.no-registered", "Sem registro");
        setString(ConfigStrings.DATA_FORMAT, file, outFile, "server.data-format", "dd/MM/yyyy HH:mm");
        setString(ConfigStrings.MONEY_LANGUAGE, file, outFile, "server.money.language", "pt");
        setString(ConfigStrings.MONEY_COUNTRY, file, outFile, "server.money.country", "BR");
        setString(ConfigStrings.PERM_CHAT_COLOR, file, outFile, "perm.chat-color", "eternia.chat.color");
        setString(ConfigStrings.PERM_CHAT_MENTION, file, outFile, "perm.chat-mention", "eternia.chat.mention");
        setString(ConfigStrings.PERM_CHAT_ITEM, file, outFile, "perm.chat-item", "eternia.chat.item");
        setString(ConfigStrings.PERM_BASE_COMMAND, file, outFile, "perm.base-command", "eternia.");
        setString(ConfigStrings.PERM_CHAT_COLOR_NICK, file, outFile, "perm.chat-color-nick", "eternia.chat.color.nick");
        setString(ConfigStrings.PERM_CHAT_STAFF, file, outFile, "perm.chat-staff", "eternia.chat.staff");
        setString(ConfigStrings.PERM_SPY, file, outFile, "perm.spy", "eternia.spy");
        setString(ConfigStrings.PERM_AFK, file, outFile, "perm.afk", "eternia.afk");
        setString(ConfigStrings.PERM_NO_KICK_BY_AFK, file, outFile, "perm.no-kick-by-afk", "eternia.nokickbyafksorrymates");
        setString(ConfigStrings.PERM_TIMING_BYPASS, file, outFile, "perm.timing-bypass", "eternia.timing.bypass");
        setString(ConfigStrings.PERM_SIGN_COLOR, file, outFile, "perm.sign-color", "eternia.sign.color");
        setString(ConfigStrings.PERM_SPAWNERS_BREAK, file, outFile, "perm.sign-color", "eternia.spawners.break");
        setString(ConfigStrings.PERM_SPAWNERS_NO_SILK, file, outFile, "perm.spawners-no-silk", "eternia.spawners.nosilk");
        setString(ConfigStrings.PERM_SPAWNERS_CHANGE, file, outFile, "perm.spawners-change", "eternia.change-spawner");
        setString(ConfigStrings.PERM_FLY_BYPASS, file, outFile, "perm.fly-bypass", "eternia.fly.bypass");
        setString(ConfigStrings.PERM_ELEVATOR, file, outFile, "perm.elevator", "eternia.elevator");
        setString(ConfigStrings.PERM_MUTE_BYPASS, file, outFile, "perm.mute-bypass", "eternia.mute.bypass");
        setString(ConfigStrings.PERM_BACK_FREE, file, outFile, "perm.back-free", "eternia.backfree");
        setString(ConfigStrings.PERM_WARP_PREFIX, file, outFile, "perm.warp-prefix", "eternia.warp.");
        setString(ConfigStrings.PERM_WARP_SHOP, file, outFile, "perm.warp-shop", "eternia.warp.shop");
        setString(ConfigStrings.PERM_SPAWN_OTHER, file, outFile, "perm.spawn-other", "eternia.spawn.other");
        setString(ConfigStrings.PERM_NICK_OTHER, file, outFile, "perm.nickname-other", "eternia.nickname.others");
        setString(ConfigStrings.PERM_KIT_PREFIX, file, outFile, "perm.kit-prefix", "eternia.kit.");
        setString(ConfigStrings.PERM_EC_OTHER, file, outFile, "perm.ec-other", "eternia.enderchest.other");
        setString(ConfigStrings.PERM_HOME_OTHER, file, outFile, "perm.home-other", "eternia.home.other");
        setString(ConfigStrings.PERM_SETHOME_LIMIT_PREFIX, file, outFile, "perm.sethome-limit-prefix", "eternia.sethome.");
        setString(ConfigStrings.PERM_FEED_OTHER, file, outFile, "perm.feed-other", "eternia.feed.other");
        setString(ConfigStrings.PERM_FLY_OTHER, file, outFile, "perm.fly-other", "eternia.fly.others");
        setString(ConfigStrings.PERM_MONEY_OTHER, file, outFile, "perm.money-other", "eternia.money.admin");
        setString(ConfigStrings.AFK_PLACEHOLDER, file, outFile, "placeholders.afk", "$9 AFK");
        setString(ConfigStrings.GOD_PLACEHOLDER, file, outFile, "placeholders.godmode", "$9 GOD");
        setString(ConfigStrings.SHOW_ITEM_PLACEHOLDER, file, outFile, "placeholders.show-item", "[item]");
        setString(ConfigStrings.MENTION_PLACEHOLDER, file, outFile, "placeholders.mention", "@");
        setString(ConfigStrings.CONS_ADVENTURE, file, outFile, "generics.gm.adventure", "aventura");
        setString(ConfigStrings.CONS_CREATIVE, file, outFile, "generics.gm.creative", "criativo");
        setString(ConfigStrings.CONS_SPECTATOR, file, outFile, "generics.gm.spectator", "espectador");
        setString(ConfigStrings.CONS_SURVIVAL, file, outFile, "generics.gm.survival", "sobrevivência");
        setString(ConfigStrings.CONS_BLACK, file, outFile, "chat.cn.black", "Preto");
        setString(ConfigStrings.CONS_DARK_BLUE, file, outFile, "chat.cn.darkblue", "Azul Escuro");
        setString(ConfigStrings.CONS_DARK_GREEN, file, outFile, "chat.cn.darkgreen", "Verde Escuro");
        setString(ConfigStrings.CONS_DARK_AQUA, file, outFile, "chat.cn.darkaqua", "Ciano");
        setString(ConfigStrings.CONS_DARK_RED, file, outFile, "chat.cn.darkred", "Vermelho Escuro");
        setString(ConfigStrings.CONS_DARK_PURPLE, file, outFile, "chat.cn.darkpurple", "Roxo");
        setString(ConfigStrings.CONS_GOLD, file, outFile, "chat.cn.gold", "Laranja");
        setString(ConfigStrings.CONS_GRAY, file, outFile, "chat.cn.gray", "Cinza");
        setString(ConfigStrings.CONS_DARK_GRAY, file, outFile, "chat.cn.darkgray", "Cinza Escuro");
        setString(ConfigStrings.CONS_BLUE, file, outFile, "chat.cn.blue", "Azul");
        setString(ConfigStrings.CONS_GREEN, file, outFile, "chat.cn.green", "Verde");
        setString(ConfigStrings.CONS_AQUA, file, outFile, "chat.cn.aqua", "Azul Claro");
        setString(ConfigStrings.CONS_RED, file, outFile, "chat.cn.red", "Vermelho");
        setString(ConfigStrings.CONS_LIGHT_PURPLE, file, outFile, "chat.cn.lightpurple", "Rosa");
        setString(ConfigStrings.CONS_YELLOW, file, outFile, "chat.cn.yellow", "Amarelo");
        setString(ConfigStrings.CONS_WHITE, file, outFile, "chat.cn.white", "Branco");
        setString(ConfigStrings.CONS_LOCAL, file, outFile, "chat.local", "Local");
        setString(ConfigStrings.CONS_GLOBAL, file, outFile, "chat.global", "Global");
        setString(ConfigStrings.CONS_STAFF, file, outFile, "chat.staff", "Staff");
        setString(ConfigStrings.CONS_SHOW_ITEM, file, outFile, "chat.show-item.format", "$3x{0} {1}$f");
        setString(ConfigStrings.CONS_MENTION_TITLE, file, outFile, "chat.mention-title.format", "{1}");
        setString(ConfigStrings.CONS_MENTION_SUBTITLE, file, outFile, "chat.mention-subtitle.format", "$7mencionou você$8!");
        setString(ConfigStrings.CONS_SPY, file, outFile, "spy.tell.format", "&8[&7SPY-&6P&8] {1} -> {3}: {4}");
        setString(ConfigStrings.CONS_SPY_LOCAL, file, outFile, "spy.local.format", "&8[&7SPY&8-&eL&8] {1}: {2}");

        outFile.set("chat.show-item.notes", "0: quantia de items; 1: item");
        outFile.set("chat.mention-title.notes", "0: nome do jogador; 1: apelido do jogador");
        outFile.set("chat.mention-subtitle.notes", "0: nome do jogador; 1: apelido do jogador");
        outFile.set("spy.tell.notes", "0: nome do jogador; 1: apelido do jogador; 2: nome do alvo; 3: apelido do alvo; 4: mensagem");
        outFile.set("spy.local.notes", "0: nome do jogador; 1: apelido do jogador; 2: mensagem");

        saveFile(outFile, Constants.CONSTANTS_FILE_PATH, Constants.DATA_LAYER_FOLDER_PATH);

    }

}
