package br.com.eterniaserver.eterniaserver.configurations.locales;

import br.com.eterniaserver.eternialib.core.enums.ConfigurationCategory;
import br.com.eterniaserver.eternialib.core.interfaces.ReloadableConfiguration;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configurations.GenericCfg;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConstantsCfg extends GenericCfg implements ReloadableConfiguration {

    private final EterniaServer plugin;
    private final NamespacedKey[] namespaceKeys;

    public ConstantsCfg(final EterniaServer plugin, final String[] strings, final NamespacedKey[] namespaceKeys) {
        super(plugin, strings, null, null, null);
        this.plugin = plugin;
        this.namespaceKeys = namespaceKeys;
    }

    @Override
    public ConfigurationCategory category() {
        return ConfigurationCategory.GENERIC;
    }

    @Override
    public void executeConfig() {
        final FileConfiguration file = YamlConfiguration.loadConfiguration(new File(Constants.CONSTANTS_FILE_PATH));
        final FileConfiguration outFile = new YamlConfiguration();

        setString(Strings.SERVER_PREFIX, file, outFile, "server.prefix", "$8[$aE$9S$8]$7 ");
        setString(Strings.BOTTLE_EXP_NAME, file, outFile, "server.bottle-exp", "$8[$eGarrafa de EXP$8]");
        setString(Strings.SPAWNERS_FORMAT, file, outFile, "server.spawner-format", "$8[$e%spawner_name% $7Spawner$8]");
        setString(Strings.NOT_SUPPORTED, file, outFile, "server.no-supported", "$7Sem suporte");
        setString(Strings.CLEAR_STRING, file, outFile, "server.clear-str", "clear");
        setString(Strings.NO_REGISTER, file, outFile, "server.no-registered", "Sem registro");
        setString(Strings.MONEY_LANGUAGE, file, outFile, "server.money.language", "pt");
        setString(Strings.MONEY_COUNTRY, file, outFile, "server.money.country", "BR");
        setString(Strings.PERM_CHAT_COLOR, file, outFile, "perm.chat-color", "eternia.chat.color");
        setString(Strings.PERM_CHAT_MENTION, file, outFile, "perm.chat-mention", "eternia.chat.mention");
        setString(Strings.PERM_CHAT_ITEM, file, outFile, "perm.chat-item", "eternia.chat.item");
        setString(Strings.PERM_BASE_COMMAND, file, outFile, "perm.base-command", "eternia.");
        setString(Strings.PERM_CHAT_COLOR_NICK, file, outFile, "perm.chat-color-nick", "eternia.chat.color.nick");
        setString(Strings.PERM_SPY, file, outFile, "perm.spy", "eternia.spy");
        setString(Strings.PERM_NO_KICK_BY_AFK, file, outFile, "perm.no-kick-by-afk", "eternia.nokickbyafksorrymates");
        setString(Strings.PERM_TIMING_BYPASS, file, outFile, "perm.timing-bypass", "eternia.timing.bypass");
        setString(Strings.PERM_SIGN_COLOR, file, outFile, "perm.sign-color", "eternia.sign.color");
        setString(Strings.PERM_FLY_BYPASS, file, outFile, "perm.fly-bypass", "eternia.fly.bypass");
        setString(Strings.PERM_MUTE_BYPASS, file, outFile, "perm.mute-bypass", "eternia.mute.bypass");
        setString(Strings.PERM_BACK_FREE, file, outFile, "perm.back-free", "eternia.backfree");
        setString(Strings.PERM_WARP_PREFIX, file, outFile, "perm.warp-prefix", "eternia.warp.");
        setString(Strings.PERM_WARP_SHOP, file, outFile, "perm.warp-shop", "eternia.warp.shop");
        setString(Strings.PERM_SPAWN_OTHER, file, outFile, "perm.spawn-other", "eternia.spawn.other");
        setString(Strings.PERM_NICK_OTHER, file, outFile, "perm.nickname-other", "eternia.nickname.others");
        setString(Strings.PERM_KIT_PREFIX, file, outFile, "perm.kit-prefix", "eternia.kit.");
        setString(Strings.PERM_HOME_OTHER, file, outFile, "perm.home-other", "eternia.home.other");
        setString(Strings.PERM_SETHOME_LIMIT_PREFIX, file, outFile, "perm.sethome-limit-prefix", "eternia.sethome.");
        setString(Strings.PERM_FEED_OTHER, file, outFile, "perm.feed-other", "eternia.feed.other");
        setString(Strings.PERM_FLY_OTHER, file, outFile, "perm.fly-other", "eternia.fly.others");
        setString(Strings.PERM_MONEY_OTHER, file, outFile, "perm.money-other", "eternia.money.admin");
        setString(Strings.PERM_CHAT_BYPASS_PROTECTION, file, outFile, "perm.chat-bypass-protection", "eternia.chat.bypass");
        setString(Strings.PERM_HOME_COMPASS, file, outFile, "perm.sethome-compass", "eternia.sethome.compass");
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

        namespaceKeys[ItemsKeys.TAG_RUN_COMMAND.ordinal()] = new NamespacedKey(plugin, Constants.TAG_RUN_COMMAND);
        namespaceKeys[ItemsKeys.TAG_RUN_IN_CONSOLE.ordinal()] = new NamespacedKey(plugin, Constants.TAG_RUN_IN_CONSOLE);
        namespaceKeys[ItemsKeys.TAG_USAGES.ordinal()] = new NamespacedKey(plugin, Constants.TAG_USAGES);
        namespaceKeys[ItemsKeys.TAG_WORLD.ordinal()] = new NamespacedKey(plugin, Constants.TAG_WORLD);
        namespaceKeys[ItemsKeys.TAG_COORD_X.ordinal()] = new NamespacedKey(plugin, Constants.TAG_COORD_X);
        namespaceKeys[ItemsKeys.TAG_COORD_Y.ordinal()] = new NamespacedKey(plugin, Constants.TAG_COORD_Y);
        namespaceKeys[ItemsKeys.TAG_COORD_Z.ordinal()] = new NamespacedKey(plugin, Constants.TAG_COORD_Z);
        namespaceKeys[ItemsKeys.TAG_COORD_YAW.ordinal()] = new NamespacedKey(plugin, Constants.TAG_COORD_YAW);
        namespaceKeys[ItemsKeys.TAG_COORD_PITCH.ordinal()] = new NamespacedKey(plugin, Constants.TAG_COORD_PITCH);
        namespaceKeys[ItemsKeys.TAG_LOC_NAME.ordinal()] = new NamespacedKey(plugin, Constants.TAG_LOC_NAME);
        namespaceKeys[ItemsKeys.CHEST_BUY_AMOUNT.ordinal()] = new NamespacedKey(plugin, "ChestBuy_A");
        namespaceKeys[ItemsKeys.CHEST_SELL_AMOUNT.ordinal()] = new NamespacedKey(plugin, "ChestSell_A");
        namespaceKeys[ItemsKeys.CHEST_NAME.ordinal()] = new NamespacedKey(plugin, "ChestShop_Name");
    }

    @Override
    public void executeCritical() {

    }

}
