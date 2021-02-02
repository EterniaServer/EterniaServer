package br.com.eterniaserver.eterniaserver.configurations.locales;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.configurations.GenericCfg;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConstantsCfg extends GenericCfg {

    public ConstantsCfg(String[] strings) {

        super(strings, null, null, null, null);

        FileConfiguration file = YamlConfiguration.loadConfiguration(new File(Constants.CONSTANTS_FILE_PATH));
        FileConfiguration outFile = new YamlConfiguration();

        setString(Strings.SERVER_PREFIX, file, outFile, "server.prefix", "$8[$aE$9S$8]$7 ");
        setString(Strings.BALANCE_TOP_TAG, file, outFile, "server.baltop", "$8[$2Magnata$8]");
        setString(Strings.BOTTLE_EXP_NAME, file, outFile, "server.bottle-exp", "$8[$eGarrafa de EXP$8]");
        setString(Strings.NOT_SUPPORTED, file, outFile, "server.no-supported", "$7Sem suporte");
        setString(Strings.CLEAR_STRING, file, outFile, "server.clear-str", "clear");
        setString(Strings.NO_REGISTER, file, outFile, "server.no-registered", "Sem registro");
        setString(Strings.DATA_FORMAT, file, outFile, "server.data-format", "dd/MM/yyyy HH:mm");
        setString(Strings.MONEY_LANGUAGE, file, outFile, "server.money.language", "pt");
        setString(Strings.MONEY_COUNTRY, file, outFile, "server.money.country", "BR");
        setString(Strings.PERM_CHAT_COLOR, file, outFile, "perm.chat-color", "eternia.chat.color");
        setString(Strings.PERM_CHAT_MENTION, file, outFile, "perm.chat-mention", "eternia.chat.mention");
        setString(Strings.PERM_CHAT_ITEM, file, outFile, "perm.chat-item", "eternia.chat.item");
        setString(Strings.PERM_BASE_COMMAND, file, outFile, "perm.base-command", "eternia.");
        setString(Strings.PERM_CHAT_COLOR_NICK, file, outFile, "perm.chat-color-nick", "eternia.chat.color.nick");
        setString(Strings.PERM_SPY, file, outFile, "perm.spy", "eternia.spy");
        setString(Strings.PERM_AFK, file, outFile, "perm.afk", "eternia.afk");
        setString(Strings.PERM_NO_KICK_BY_AFK, file, outFile, "perm.no-kick-by-afk", "eternia.nokickbyafksorrymates");
        setString(Strings.PERM_TIMING_BYPASS, file, outFile, "perm.timing-bypass", "eternia.timing.bypass");
        setString(Strings.PERM_SIGN_COLOR, file, outFile, "perm.sign-color", "eternia.sign.color");
        setString(Strings.PERM_SPAWNERS_BREAK, file, outFile, "perm.spawners-break", "eternia.spawners.break");
        setString(Strings.PERM_SPAWNERS_NO_SILK, file, outFile, "perm.spawners-no-silk", "eternia.spawners.nosilk");
        setString(Strings.PERM_SPAWNERS_CHANGE, file, outFile, "perm.spawners-change", "eternia.change-spawner");
        setString(Strings.PERM_FLY_BYPASS, file, outFile, "perm.fly-bypass", "eternia.fly.bypass");
        setString(Strings.PERM_ELEVATOR, file, outFile, "perm.elevator", "eternia.elevator");
        setString(Strings.PERM_MUTE_BYPASS, file, outFile, "perm.mute-bypass", "eternia.mute.bypass");
        setString(Strings.PERM_BACK_FREE, file, outFile, "perm.back-free", "eternia.backfree");
        setString(Strings.PERM_WARP_PREFIX, file, outFile, "perm.warp-prefix", "eternia.warp.");
        setString(Strings.PERM_WARP_SHOP, file, outFile, "perm.warp-shop", "eternia.warp.shop");
        setString(Strings.PERM_SPAWN_OTHER, file, outFile, "perm.spawn-other", "eternia.spawn.other");
        setString(Strings.PERM_NICK_OTHER, file, outFile, "perm.nickname-other", "eternia.nickname.others");
        setString(Strings.PERM_KIT_PREFIX, file, outFile, "perm.kit-prefix", "eternia.kit.");
        setString(Strings.PERM_EC_OTHER, file, outFile, "perm.ec-other", "eternia.enderchest.other");
        setString(Strings.PERM_HOME_OTHER, file, outFile, "perm.home-other", "eternia.home.other");
        setString(Strings.PERM_SETHOME_LIMIT_PREFIX, file, outFile, "perm.sethome-limit-prefix", "eternia.sethome.");
        setString(Strings.PERM_FEED_OTHER, file, outFile, "perm.feed-other", "eternia.feed.other");
        setString(Strings.PERM_FLY_OTHER, file, outFile, "perm.fly-other", "eternia.fly.others");
        setString(Strings.PERM_MONEY_OTHER, file, outFile, "perm.money-other", "eternia.money.admin");
        setString(Strings.PERM_CHAT_BYPASS_PROTECTION, file, outFile, "perm.chat-bypass-protection", "eternia.chat.bypass");
        setString(Strings.PERM_HOME_COMPASS, file, outFile, "perm.sethome-compass", "eternia.sethome.compass");
        setString(Strings.AFK_PLACEHOLDER, file, outFile, "placeholders.afk", "$9 AFK");
        setString(Strings.GOD_PLACEHOLDER, file, outFile, "placeholders.godmode", "$9 GOD");
        setString(Strings.SHOW_ITEM_PLACEHOLDER, file, outFile, "placeholders.show-item", "[item]");
        setString(Strings.CONS_ADVENTURE, file, outFile, "generics.gm.adventure", "aventura");
        setString(Strings.CONS_CREATIVE, file, outFile, "generics.gm.creative", "criativo");
        setString(Strings.CONS_SPECTATOR, file, outFile, "generics.gm.spectator", "espectador");
        setString(Strings.CONS_SURVIVAL, file, outFile, "generics.gm.survival", "sobrevivência");
        setString(Strings.CONS_BLACK, file, outFile, "chat.cn.black", "Preto");
        setString(Strings.CONS_DARK_BLUE, file, outFile, "chat.cn.darkblue", "Azul Escuro");
        setString(Strings.CONS_DARK_GREEN, file, outFile, "chat.cn.darkgreen", "Verde Escuro");
        setString(Strings.CONS_DARK_AQUA, file, outFile, "chat.cn.darkaqua", "Ciano");
        setString(Strings.CONS_DARK_RED, file, outFile, "chat.cn.darkred", "Vermelho Escuro");
        setString(Strings.CONS_DARK_PURPLE, file, outFile, "chat.cn.darkpurple", "Roxo");
        setString(Strings.CONS_GOLD, file, outFile, "chat.cn.gold", "Laranja");
        setString(Strings.CONS_GRAY, file, outFile, "chat.cn.gray", "Cinza");
        setString(Strings.CONS_DARK_GRAY, file, outFile, "chat.cn.darkgray", "Cinza Escuro");
        setString(Strings.CONS_BLUE, file, outFile, "chat.cn.blue", "Azul");
        setString(Strings.CONS_GREEN, file, outFile, "chat.cn.green", "Verde");
        setString(Strings.CONS_AQUA, file, outFile, "chat.cn.aqua", "Azul Claro");
        setString(Strings.CONS_RED, file, outFile, "chat.cn.red", "Vermelho");
        setString(Strings.CONS_LIGHT_PURPLE, file, outFile, "chat.cn.lightpurple", "Rosa");
        setString(Strings.CONS_YELLOW, file, outFile, "chat.cn.yellow", "Amarelo");
        setString(Strings.CONS_WHITE, file, outFile, "chat.cn.white", "Branco");
        setString(Strings.CONS_LOCAL, file, outFile, "chat.local", "Local");
        setString(Strings.CONS_GLOBAL, file, outFile, "chat.global", "Global");
        setString(Strings.CONS_STAFF, file, outFile, "chat.staff", "Staff");
        setString(Strings.CONS_SHOW_ITEM, file, outFile, "chat.show-item.format", "$3x{0} {1}$f");
        setString(Strings.CONS_MENTION_TITLE, file, outFile, "chat.mention-title.format", "{1}");
        setString(Strings.CONS_MENTION_SUBTITLE, file, outFile, "chat.mention-subtitle.format", "$7mencionou você$8!");
        setString(Strings.CONS_SPY, file, outFile, "spy.tell.format", "&8[&7SPY-&6P&8] {1} -> {3}: {4}");
        setString(Strings.CONS_SPY_LOCAL, file, outFile, "spy.local.format", "&8[&7SPY&8-&aC&8] {1}: {2}");

        outFile.set("chat.show-item.notes", "0: quantia de items; 1: item");
        outFile.set("chat.mention-title.notes", "0: nome do jogador; 1: apelido do jogador");
        outFile.set("chat.mention-subtitle.notes", "0: nome do jogador; 1: apelido do jogador");
        outFile.set("spy.tell.notes", "0: nome do jogador; 1: apelido do jogador; 2: nome do alvo; 3: apelido do alvo; 4: mensagem");
        outFile.set("spy.local.notes", "0: nome do jogador; 1: apelido do jogador; 2: mensagem");

        saveFile(outFile, Constants.CONSTANTS_FILE_PATH);

    }

}
