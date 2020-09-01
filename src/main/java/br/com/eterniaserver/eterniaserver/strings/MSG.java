package br.com.eterniaserver.eterniaserver.strings;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import org.bukkit.ChatColor;

public class MSG {

    private MSG() {
        throw new IllegalStateException("Utility class");
    }

    public static final String M_SERVER_PREFIX = getColor(EterniaServer.msgConfig.getString("server.prefix"));
    public static final String MSG_MODULE_ENABLE = putPrefix("modules.enable");
    public static final String MSG_MODULE_DISABLE = putPrefix("modules.disable");
    public static final String MSG_NO_MONEY = putPrefix("server.no-money");
    public static final String MSG_LOAD_DATA = putPrefix("server.load-data");
    public static final String MSG_NO_PERM = putPrefix("server.no-perm");
    public static final String MSG_IN_TELEPORT = putPrefix("server.telep");
    public static final String MSG_JOIN = putPrefix("server.join");
    public static final String MSG_LEAVE = putPrefix("server.leave");
    public static final String MSG_TIMING = putPrefix("server.timing");
    public static final String MSG_NETHER_TRAP = putPrefix("server.nether-trap");
    public static final String MSG_NO_NEGATIVE = putPrefix("server.neg");
    public static final String MSG_ERROR = putPrefix("server.chat-error");

    public static final String MSG_SPAWN_CREATED = putPrefix("teleport.spawn.created");
    public static final String MSG_SPAWN_NO_EXISTS = putPrefix("teleport.spawn.no-exists");
    public static final String MSG_SPAWN_TELEPORT_TARGET = putPrefix("teleport.spawn.tp-target");
    public static final String SPAWN_BY = putPrefix("teleport.spawn.tp-you");

    public static final String MSG_SHOP_CREATED = putPrefix("teleport.shop.created");
    public static final String MSG_SHOP_DONE = putPrefix("teleport.shop.done");
    public static final String MSG_SHOP_NO_EXISTS = putPrefix("teleport.shop.no-exists");

    public static final String MSG_WARP_NO_EXISTS = putPrefix("teleport.warp.no-exists");
    public static final String MSG_WARP_DELETED = putPrefix("teleport.warp.deleted");
    public static final String MSG_WARP_CREATED = putPrefix("teleport.warp.created");
    public static final String MSG_WARP_DONE = putPrefix("teleport.warp.done");
    public static final String MSG_WARP_LIST = putPrefix("teleport.warp.list");

    public static final String MSG_BACK_NO_TELEPORT = putPrefix("teleport.back.no-tp");
    public static final String MSG_BACK_COST = putPrefix("teleport.back.no-free");
    public static final String MSG_BACK_FREE = putPrefix("teleport.back.free");

    public static final String MSG_TELEPORT_ALL = putPrefix("teleport.tp.all");
    public static final String MSG_TELEPORT_MOVE = putPrefix("teleport.tp.move");
    public static final String MSG_TELEPORT_TIMING = putPrefix("teleport.tp.timing");
    public static final String MSG_TELEPORT_YOURSELF = putPrefix("teleport.tpa.yourself");
    public static final String MSG_TELEPORT_EXISTS = putPrefix("teleport.tpa.exists");
    public static final String MSG_TELEPORT_SENT = putPrefix("teleport.tpa.sent");
    public static final String MSG_TELEPORT_RECEIVED = putPrefix("teleport.tpa.received");
    public static final String MSG_TELEPORT_ACCEPT = putPrefix("teleport.tpa.accept");
    public static final String MSG_TELEPORT_DONE = putPrefix("teleport.tpa.done");
    public static final String MSG_TELEPORT_NO_REQUEST = putPrefix("teleport.tpa.no-request");
    public static final String MSG_TELEPORT_DENY = putPrefix("teleport.tpa.deny");
    public static final String MSG_TELEPORT_DENIED = putPrefix("teleport.tpa.denied");

    public static final String MSG_SPAWNER_GIVE = putPrefix("spawner.give.types");
    public static final String MSG_SPAWNER_SENT = putPrefix("spawner.give.sent");
    public static final String MSG_SPAWNER_RECEIVED = putPrefix("spawner.give.received");
    public static final String MSG_SPAWNER_INVFULL = putPrefix("spawners.others.inv-full");
    public static final String MSG_SPAWNER_BLOCKED = putPrefix("spawner.others.blocked");
    public static final String MSG_SPAWNER_SILK = putPrefix("spawner.others.need-silktouch");
    public static final String MSG_SPAWNER_FAILED = putPrefix("spawner.others.failed");
    public static final String MSG_SPAWNER_NAME = putPrefix("spawner.others.change-name");
    public static final String MSG_SPAWNER_LOG = putPrefix("spawner.log.change-name");

    public static final String MSG_LIGHTNING_SENT = putPrefix("generic.simp.sent-lightning");
    public static final String MSG_LIGHTNING_RECEIVED = putPrefix("generic.simp.received-lightning");

    public static final String MSG_PROFILE_TITLE = getColor(EterniaServer.msgConfig.getString("generic.profile.title"));
    public static final String MSG_PROFILE_REGISTER = getColor(EterniaServer.msgConfig.getString("generic.profile.register"));
    public static final String MSG_PROFILE_LAST = getColor(EterniaServer.msgConfig.getString("generic.profile.last"));
    public static final String MSG_PROFILE_HOURS = getColor(EterniaServer.msgConfig.getString("generic.profile.hours"));

    public static final String MSG_SUICIDE = putPrefix("generic.simp.suicide");
    public static final String MSG_WEATHER = putPrefix("generic.simp.weather-changed");
    public static final String MSG_SPEED = putPrefix("generic.others.invalid");
    public static final String MSG_CONDENSER = putPrefix("generic.others.condenser");

    public static final String MSG_REWARD_INVALID = putPrefix("reward.invalid");
    public static final String MSG_REWARD_NO = putPrefix("reward.no-exists");
    public static final String MSG_REWARD_CREATE = putPrefix("reward.created");

    public static final String MSG_MEM = putPrefix("replaces.mem");
    public static final String MSG_MEM_ONLINE = putPrefix("replaces.online");
    public static final String MSG_TPS = putPrefix("replaces.tps");

    public static final String MSG_AFK_ENABLE = putPrefix("generic.afk.enabled");
    public static final String MSG_AFK_DISABLE = putPrefix("generic.afk.disabled");
    public static final String MSG_AFK_KICKED = putPrefix("generic.afk.kicked");
    public static final String MSG_AFK_BROAD = putPrefix("generic.afk.broadcast-kicked");

    public static final String FLY_PVP_DISABLED = putPrefix("generic.fly.getpvp");
    public static final String FLY_IN_PVP = putPrefix("generic.fly.pvp");
    public static final String FLY_TARGET_IN_PVP = putPrefix("generic.fly.target-pvp");
    public static final String FLY_ENABLED = putPrefix("generic.fly.enabled");
    public static final String FLY_ENABLED_BY = putPrefix("generic.fly.enabled-by");
    public static final String FLY_ENABLED_FOR = putPrefix("generic.fly-enabled-for");
    public static final String FLY_DISABLED = putPrefix("generic.fly.disabled");
    public static final String FLY_DISABLED_BY = putPrefix("generic.fly.disabled-by");
    public static final String FLY_DISABLED_FOR = putPrefix("generic.fly.disabled-for");

    public static final String MSG_GOD_ENABLE = putPrefix("generic.others.god-enabled");
    public static final String MSG_GOD_DISABLE = putPrefix("generic.others.god-disabled");
    public static final String MSG_RELOAD_START = putPrefix("generic.others.reload-start");
    public static final String MSG_RELOAD_FINISH = putPrefix("generic.others.reload-finish");

    public static final String ITEM_LORE_CLEAR = putPrefix("item.lore.clear");
    public static final String ITEM_LORE_ADD = putPrefix("item.lore.add");
    public static final String ITEM_LORE_SET = putPrefix("item.lore.set");
    public static final String ITEM_NAME_CLEAR = putPrefix("item.name.clear");
    public static final String ITEM_NAME_SET = putPrefix("item.name.set");
    public static final String ITEM_ADDKEY = putPrefix("item.addkey");
    public static final String ITEM_NO = putPrefix("item.no");
    public static final String ITEM_HELMET = putPrefix("item.helmet");

    public static final String ECO_MONEY = putPrefix("eco.money");
    public static final String ECO_OTHER = putPrefix("eco.money-other");
    public static final String ECO_PAY_NO = putPrefix("eco.pay-nomoney");
    public static final String ECO_AUTO = putPrefix("eco.auto-pay");
    public static final String ECO_PAY = putPrefix("eco.pay");
    public static final String ECO_PAY_ME = putPrefix("eco.pay-me");
    public static final String ECO_BALLIST = putPrefix("eco.ballist");
    public static final String ECO_SET = putPrefix("eco.eco-set");
    public static final String ECO_RSET = putPrefix("eco.eco-rset");
    public static final String ECO_REMOVE = putPrefix("eco.eco-remove");
    public static final String ECO_RREMOVE = putPrefix("eco.eco-rremove");
    public static final String ECO_GIVE = putPrefix("eco.eco-give");
    public static final String ECO_RECEIVE = putPrefix("eco.eco-receive");

    public static final String MSG_PLAYER_SKIP = putPrefix("bed.player-s");
    public static final String MSG_SKIP_NIGHT = putPrefix("bed.skip-night");
    public static final String MSG_SKIPPING = putPrefix("bed.night-skipping");

    public static final String MSG_FEEDED = putPrefix("generic.others.feeded");
    public static final String MSG_FEEDED_TARGET = putPrefix("generic.others.feeded-target");

    public static final String M_HOME_DONE = putPrefix("home.done");
    public static final String M_HOME_DELETED = putPrefix("home.deleted");
    public static final String M_HOME_NO = putPrefix("home.no-exists");
    public static final String M_HOME_LIST = putPrefix("home.list");
    public static final String M_HOME_CREATED = putPrefix("home.created");
    public static final String M_HOME_LIMIT = putPrefix("home.limit");
    public static final String M_HOME_EXCEEDED = putPrefix("home.exceeded");

    public static final String M_CHAT_MUTED = putPrefix("chat.muted");
    public static final String M_CHATMUTED = putPrefix("chat.chatmuted");
    public static final String M_CHAT_D = putPrefix("chat.cm-d");
    public static final String M_CHAT_E = putPrefix("chat.cm-e");
    public static final String M_CHAT_MUTEBROAD = putPrefix("chat.mutebroad");
    public static final String M_CHAT_UNMUTEBROAD = putPrefix("chat.unmutebroad");
    public static final String M_CHAT_MUTET = putPrefix("chat.mutetbroad");
    public static final String M_CHAT_NOONE = putPrefix("chat.noone");
    public static final String M_CHAT_TO = getColor(EterniaServer.msgConfig.getString("chat.tell.to-player"));
    public static final String M_CHAT_FROM = getColor(EterniaServer.msgConfig.getString("chat.tell.from-player"));
    public static final String M_CHAT_R_NO = putPrefix("chat.tell.no-one");
    public static final String MSG_CHAT_LOCKED = putPrefix("chat.tell.locked");
    public static final String MSG_CHAT_UNLOCKED = putPrefix("chat.tell.unlocked");
    public static final String M_CHAT_NO_CHANGE = putPrefix("chat.no-change");
    public static final String M_CHAT_NICK_DENY = putPrefix("chat.nick-deny");
    public static final String M_CHAT_NEWNICK = putPrefix("chat.newnick");
    public static final String M_CHAT_REMOVE_NICK = putPrefix("chat.remove-nick");
    public static final String M_CHAT_NICK_MONEY = putPrefix("chat.nick-money");
    public static final String M_CHAT_NICK_MONEY_2 = putPrefix("chat.nick-money-2");
    public static final String M_CHAT_SPY_D = putPrefix("chat.spyd");
    public static final String M_CHAT_SPY_E = putPrefix("chat.spye");
    public static final String M_CHAT_ADVICE = putPrefix("chat.global-advice");
    public static final String M_CHAT_C = putPrefix("chat.channelc");
    public static final String M_CHAT_IGNORED = putPrefix("chat.ignored");
    public static final String M_CHAT_IGNORE = putPrefix("chat.ignore-sucess");
    public static final String M_CHAT_DENY = putPrefix("chat.ignore-remove");

    public static final String M_KIT_NO_EXISTS = putPrefix("kit.no-exists");
    public static final String M_KIT_LIST = putPrefix("kit.list");

    public static final String M_CASH = putPrefix("cash.use");
    public static final String M_CASH_COST = putPrefix("cash.cost");
    public static final String M_CASH_NO = putPrefix("cash.no-cash");
    public static final String M_CASH_ALREADY = putPrefix("cash.already");
    public static final String M_CASH_BALANCE_OTHER = putPrefix("cash.balance-other");
    public static final String M_CASH_NO_PLAYER = putPrefix("cash.no-player");
    public static final String M_CASH_BALANCE = putPrefix("cash.balance");
    public static final String M_CASH_SUCESS = putPrefix("cash.sucess");
    public static final String M_CASH_NO_BUY = putPrefix("cash.no-buy");
    public static final String M_CASH_CANCEL = putPrefix("cash.canc");
    public static final String M_CASH_RECEIVED = putPrefix("cash.receive");
    public static final String M_CASH_SEND = putPrefix("cash.send");
    public static final String M_CASH_REMOVED = putPrefix("cash.removed");
    public static final String M_CASH_REMOVE = putPrefix("cash.remove");

    public static final String M_GLOW_ENABLED = putPrefix("glow.brilho");
    public static final String M_GLOW_DISABLED = putPrefix("glow.desbrilho");
    public static final String M_GLOW_COLOR = putPrefix("glow.color");

    public static final String M_GM_CHANGED = putPrefix("generic.gm.changed");
    public static final String M_GM_TARGET = putPrefix("generic.gm.changed-target");
    public static final String M_GM_USE = putPrefix("generic.gm.use");

    public static final String M_XP_CHECK = putPrefix("experience.check");
    public static final String M_XP_BOTTLE = putPrefix("experience.bottleexp");
    public static final String M_XP_INSUFFICIENT = putPrefix("experience.insufficient");
    public static final String M_XP_WITHDRAW = putPrefix("experience.withdraw");
    public static final String M_XP_DEPOSIT = putPrefix("experience.deposit");

    public static final String M_XP_SET = putPrefix("experience.xp-set");
    public static final String M_XP_RSET = putPrefix("experience.xp-rset");
    public static final String M_XP_REMOVE = putPrefix("experience.xp-remove");
    public static final String M_XP_RREMOVE = putPrefix("experience.xp-rremove");
    public static final String M_XP_GIVE = putPrefix("experience.xp-give");
    public static final String M_XP_RECEIVE = putPrefix("experience.xp-receive");

    private static String putPrefix(String path) {
        String message = EterniaServer.msgConfig.getString(path);
        if (message == null) message = "&7Erro&8, &7texto &3" + path + "&7não encontrado&8.";
        return M_SERVER_PREFIX + getColor(message);
    }

    public static String getColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
