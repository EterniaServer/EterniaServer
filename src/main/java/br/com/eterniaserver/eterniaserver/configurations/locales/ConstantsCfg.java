package br.com.eterniaserver.eterniaserver.configurations.locales;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConstantsCfg {

    public final String serverPrefix;
    public final String baltopTag;
    public final String bottleExpName;
    public final String noSupport;
    public final String clearStr;
    public final String noRegister;
    public final String dataFormat;
    public final String moneyLanguage;
    public final String moneyCountry;

    public final String permChatColor;
    public final String permChatMention;
    public final String permChatItem;
    public final String permBaseCommand;
    public final String permChatColorNick;
    public final String permChatStaff;
    public final String permSpy;
    public final String permAfk;
    public final String permNoKickAfk;
    public final String permTimingBypass;
    public final String permSignColor;
    public final String permSpawnersBreak;
    public final String permSpawnersNoSilk;
    public final String permSpawnersChange;
    public final String permFlyBypass;
    public final String permElevator;
    public final String permMuteBypass;
    public final String permBackFree;
    public final String permWarpPrefix;
    public final String permWarpShop;
    public final String permSpawnOther;
    public final String permNicknameOther;
    public final String permKitPrefix;
    public final String permEcOther;
    public final String permHomeOther;
    public final String permSethomeLimitPrefix;
    public final String permFeedOther;
    public final String permFlyOther;
    public final String permMoneyOther;

    public final String afkPlaceholder;
    public final String godPlaceholder;
    public final String showItemPlaceholder;
    public final String mentionPlaceholder;

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
    public final String chShowItemFormat;
    public final String chMentionTitle;
    public final String chMentionSubtitle;

    public final String spyTell;
    public final String spyLocal;

    public ConstantsCfg() {

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Constants.CONSTANTS_FILE_PATH));
        FileConfiguration outConfig = new YamlConfiguration();

        this.serverPrefix = config.getString("server.prefix", "$8[$aE$9S$8]$7 ").replace('$', (char) 0x00A7);
        this.baltopTag = config.getString("server.baltop", "$8[$2Magnata$8]").replace('$', (char) 0x00A7);
        this.bottleExpName = config.getString("server.bottle-exp", "$8[$eGarrafa de EXP$8]").replace('$', (char) 0x00A7);
        this.noSupport = config.getString("server.no-supported", "$7Sem suporte").replace('$', (char) 0x00A7);
        this.clearStr = config.getString("server.clear-str", "clear");
        this.noRegister = config.getString("server.no-registered", "Sem registro");
        this.dataFormat = config.getString("server.data-format", "dd/MM/yyyy HH:mm");
        this.moneyLanguage = config.getString("server.money.language", "pt");
        this.moneyCountry = config.getString("server.money.country", "BR");

        this.permChatColor = config.getString("perm.chat-color", "eternia.chat.color");
        this.permChatMention = config.getString("perm.chat-mention", "eternia.chat.mention");
        this.permChatItem = config.getString("perm.chat-item", "eternia.chat.item");
        this.permBaseCommand = config.getString("perm.base-command", "eternia.");
        this.permChatColorNick = config.getString("perm.chat-color-nick", "eternia.chat.color.nick");
        this.permChatStaff = config.getString("perm.chat-staff", "eternia.chat.staff");
        this.permSpy = config.getString("perm.spy", "eternia.spy");
        this.permAfk = config.getString("perm.afk", "eternia.afk");
        this.permNoKickAfk = config.getString("perm.no-kick-by-afk", "eternia.nokickbyafksorrymates");
        this.permTimingBypass = config.getString("perm.timing-bypass", "eternia.timing.bypass");
        this.permSignColor = config.getString("perm.sign-color", "eternia.sign.color");
        this.permSpawnersBreak = config.getString("perm.spawners-break", "eternia.spawners.break");
        this.permSpawnersNoSilk = config.getString("perm.spawners-no-silk", "eternia.spawners.nosilk");
        this.permSpawnersChange = config.getString("perm.spawners-change", "eternia.change-spawner");
        this.permFlyBypass = config.getString("perm.fly-bypass", "eternia.fly.bypass");
        this.permElevator = config.getString("perm.elevator", "eternia.elevator");
        this.permMuteBypass = config.getString("perm.mute-bypass", "eternia.mute.bypass");
        this.permBackFree = config.getString("perm.back-free", "eternia.backfree");
        this.permWarpPrefix = config.getString("perm.warp-prefix", "eternia.warp.");
        this.permWarpShop = config.getString("perm.warp-shop", "eternia.warp.shop");
        this.permSpawnOther = config.getString("perm.spawn-other", "eternia.spawn.other");
        this.permNicknameOther = config.getString("perm.nickname-other", "eternia.nickname.others");
        this.permKitPrefix = config.getString("perm.kit-prefix", "eternia.kit.");
        this.permEcOther = config.getString("perm.ec-other", "eternia.enderchest.other");
        this.permHomeOther = config.getString("perm.home-other", "eternia.home.other");
        this.permSethomeLimitPrefix = config.getString("perm.sethome-limit-prefix", "eternia.sethome.");
        this.permFeedOther = config.getString("perm.feed-other", "eternia.feed.other");
        this.permFlyOther = config.getString("perm.fly-other", "eternia.fly.others");
        this.permMoneyOther = config.getString("perm.money-other", "eternia.money.admin");

        this.afkPlaceholder = config.getString("placeholders.afk", "$9 AFK").replace('$', (char) 0x00A7);
        this.godPlaceholder = config.getString("placeholders.godmode", "$9 GOD").replace('$', (char) 0x00A7);
        this.showItemPlaceholder = config.getString("placeholders.show-item", "[item]");
        this.mentionPlaceholder = config.getString("placeholders.mention", "@");

        this.gmAdventure = config.getString("generics.gm.adventure", "aventura");
        this.gmCreative = config.getString("generics.gm.creative", "criativo");
        this.gmSpectator = config.getString("generics.gm.spectator", "espectador");
        this.gmSurvival = config.getString("generics.gm.survival", "sobrevivência");

        this.cnBlack = config.getString("chat.cn.black", "Preto");
        this.cnDarkBlue = config.getString("chat.cn.darkblue", "Azul Escuro");
        this.cnDarkGreen = config.getString("chat.cn.darkgreen", "Verde Escuro");
        this.cnDarkAqua = config.getString("chat.cn.darkaqua", "Ciano");
        this.cnDarkRed = config.getString("chat.cn.darkred", "Vermelho Escuro");
        this.cnDarkPurple = config.getString("chat.cn.darkpurple", "Roxo");
        this.cnGold = config.getString("chat.cn.gold", "Laranja");
        this.cnGray = config.getString("chat.cn.gray", "Cinza");
        this.cnDarkGray = config.getString("chat.cn.darkgray", "Cinza Escuro");
        this.cnBlue = config.getString("chat.cn.blue", "Azul");
        this.cnGreen = config.getString("chat.cn.green", "Verde");
        this.cnAqua = config.getString("chat.cn.aqua", "Azul Claro");
        this.cnRed = config.getString("chat.cn.red", "Vermelho");
        this.cnLightPurple = config.getString("chat.cn.lightpurple", "Rosa");
        this.cnYellow = config.getString("chat.cn.yellow", "Amarelo");
        this.cnWhite = config.getString("chat.cn.white", "Branco");
        this.chLocal = config.getString("chat.local", "Local");
        this.chGlobal = config.getString("chat.global", "Global");
        this.chStaff = config.getString("chat.staff", "Staff");
        this.chShowItemFormat = config.getString("chat.show-item.format", "$3x{0} {1}$f").replace('$', (char) 0x00A7);
        this.chMentionTitle = config.getString("chat.mention-title.format", "{1}").replace('$', (char) 0x00A7);
        this.chMentionSubtitle = config.getString("chat.mention-subtitle.format", "$7mencionou você$8!").replace('$', (char) 0x00A7);

        this.spyTell = config.getString("spy.tell.format", "&8[&7SPY-&6P&8] {1} -> {3}: {4}");
        this.spyLocal = config.getString("spy.local.format", "&8[&7SPY&8-&eL&8] {1}: {2}");

        outConfig.set("server.prefix", this.serverPrefix);
        outConfig.set("server.baltop", this.baltopTag);
        outConfig.set("server.bottle-exp", this.bottleExpName);
        outConfig.set("server.no-supported", this.noSupport);
        outConfig.set("server.clear-str", this.clearStr);
        outConfig.set("server.no-registered", this.noRegister);
        outConfig.set("server.data-format", this.dataFormat);
        outConfig.set("server.money.language", this.moneyLanguage);
        outConfig.set("server.money.country", this.moneyCountry);

        outConfig.set("perm.chat-color", this.permChatColor);
        outConfig.set("perm.chat-mention", this.permChatMention);
        outConfig.set("perm.chat-item", this.permChatItem);
        outConfig.set("perm.base-command", this.permBaseCommand);
        outConfig.set("perm.chat-color-nick", this.permChatColorNick);
        outConfig.set("perm.chat-staff", this.permChatStaff);
        outConfig.set("perm.spy", this.permSpy);
        outConfig.set("perm.afk", this.permAfk);
        outConfig.set("perm.no-kick-by-afk", this.permNoKickAfk);
        outConfig.set("perm.timing-bypass", this.permTimingBypass);
        outConfig.set("perm.sign-color", this.permSignColor);
        outConfig.set("perm.spawners-break", this.permSpawnersBreak);
        outConfig.set("perm.spawners-no-silk", this.permSpawnersNoSilk);
        outConfig.set("perm.spawners-change", this.permSpawnersChange);
        outConfig.set("perm.fly-bypass", this.permFlyBypass);
        outConfig.set("perm.elevator", this.permElevator);
        outConfig.set("perm.mute-bypass", this.permMuteBypass);
        outConfig.set("perm.back-free", this.permBackFree);
        outConfig.set("perm.warp-prefix", this.permWarpPrefix);
        outConfig.set("perm.warp-shop", this.permWarpShop);
        outConfig.set("perm.spawn-other", this.permSpawnOther);
        outConfig.set("perm.nickname-other", this.permNicknameOther);
        outConfig.set("perm.kit-prefix", this.permKitPrefix);
        outConfig.set("perm.ec-other", this.permEcOther);
        outConfig.set("perm.home-other", this.permHomeOther);
        outConfig.set("perm.sethome-limit-prefix", this.permSethomeLimitPrefix);
        outConfig.set("perm.feed-other", this.permFeedOther);
        outConfig.set("perm.fly-other", this.permFlyOther);
        outConfig.set("perm.money-other", this.permMoneyOther);

        outConfig.set("placeholders.afk", this.afkPlaceholder);
        outConfig.set("placeholders.godmode", this.godPlaceholder);
        outConfig.set("placeholders.show-item", this.showItemPlaceholder);
        outConfig.set("placeholders.mention", this.mentionPlaceholder);

        outConfig.set("generics.gm.adventure", this.gmAdventure);
        outConfig.set("generics.gm.creative", this.gmCreative);
        outConfig.set("generics.gm.spectator", this.gmSpectator);
        outConfig.set("generics.gm.survival", this.gmSurvival);

        outConfig.set("chat.cn.black", this.cnBlack);
        outConfig.set("chat.cn.darkblue", this.cnDarkBlue);
        outConfig.set("chat.cn.darkgreen", this.cnDarkGreen);
        outConfig.set("chat.cn.darkaqua", this.cnDarkAqua);
        outConfig.set("chat.cn.darkred", this.cnDarkRed);
        outConfig.set("chat.cn.darkpurple", this.cnDarkPurple);
        outConfig.set("chat.cn.gold", this.cnGold);
        outConfig.set("chat.cn.gray", this.cnGray);
        outConfig.set("chat.cn.darkgray", this.cnDarkGray);
        outConfig.set("chat.cn.blue", this.cnBlue);
        outConfig.set("chat.cn.green", this.cnGreen);
        outConfig.set("chat.cn.aqua", this.cnAqua);
        outConfig.set("chat.cn.red", this.cnRed);
        outConfig.set("chat.cn.lightpurple", this.cnLightPurple);
        outConfig.set("chat.cn.yellow", this.cnYellow);
        outConfig.set("chat.cn.white", this.cnWhite);

        outConfig.set("chat.local", this.chLocal);
        outConfig.set("chat.global", this.chGlobal);
        outConfig.set("chat.staff", this.chStaff);
        outConfig.set("chat.show-item.format", this.chShowItemFormat);
        outConfig.set("chat.show-item.notes", "0: quantia de items; 1: item");
        outConfig.set("chat.mention-title.format", this.chMentionTitle);
        outConfig.set("chat.mention-title.notes", "0: nome do jogador; 1: apelido do jogador");
        outConfig.set("chat.mention-subtitle.format", this.chMentionSubtitle);
        outConfig.set("chat.mention-subtitle.notes", "0: nome do jogador; 1: apelido do jogador");

        outConfig.set("spy.tell.format", this.spyTell);
        outConfig.set("spy.tell.notes", "0: nome do jogador; 1: apelido do jogador; 2: nome do alvo; 3: apelido do alvo; 4: mensagem");
        outConfig.set("spy.local.format", this.spyLocal);
        outConfig.set("spy.local.notes", "0: nome do jogador; 1: apelido do jogador; 2: mensagem");

        try {
            outConfig.save(Constants.CONSTANTS_FILE_PATH);
        } catch (IOException exception) {
            APIServer.logError("Impossível de criar arquivos em " + Constants.DATA_LAYER_FOLDER_PATH, 3);
        }

    }

}
