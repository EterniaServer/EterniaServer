package br.com.eterniaserver.eterniaserver.configs;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Strings {

    private Strings() {
        throw new IllegalStateException("Utility class");
    }

    public static void reloadMessages(FileConfiguration msg) {
        M_SERVER_PREFIX = getColor(msg.getString("server.prefix"));
        MSG_HELP_FORMAT = getColor(msg.getString("server.help-format"));
        MSG_MODULE_ENABLE = putPrefix(msg, "modules.enable");
        MSG_MODULE_DISABLE = putPrefix(msg, "modules.disable");
        MSG_NO_MONEY = putPrefix(msg, "server.no-money");
        MSG_LOAD_DATA = putPrefix(msg, "server.load-data");
        MSG_NO_PERM = putPrefix(msg, "server.no-perm");
        MSG_IN_TELEPORT = putPrefix(msg, "server.telep");
        MSG_JOIN = putPrefix(msg, "server.join");
        MSG_LEAVE = putPrefix(msg, "server.leave");
        MSG_TIMING = putPrefix(msg, "server.timing");
        MSG_NETHER_TRAP = putPrefix(msg, "server.nether-trap");
        MSG_NO_NEGATIVE = putPrefix(msg, "server.neg");
        MSG_ERROR = putPrefix(msg, "server.chat-error");
        MSG_SPAWN_CREATED = putPrefix(msg, "teleport.spawn.created");
        MSG_SPAWN_NO_EXISTS = putPrefix(msg, "teleport.spawn.no-exists");
        MSG_SPAWN_TELEPORT_TARGET = putPrefix(msg, "teleport.spawn.tp-target");
        MSG_SHOP_CREATED = putPrefix(msg, "teleport.shop.created");
        MSG_SHOP_DONE = putPrefix(msg, "teleport.shop.done");
        MSG_SHOP_NO_EXISTS = putPrefix(msg, "teleport.shop.no-exists");
        MSG_WARP_NO_EXISTS = putPrefix(msg, "teleport.warp.no-exists");
        MSG_WARP_DELETED = putPrefix(msg, "teleport.warp.deleted");
        MSG_WARP_CREATED = putPrefix(msg, "teleport.warp.created");
        MSG_WARP_DONE = putPrefix(msg, "teleport.warp.done");
        MSG_WARP_LIST = putPrefix(msg, "teleport.warp.list");
        MSG_BACK_NO_TELEPORT = putPrefix(msg, "teleport.back.no-tp");
        MSG_BACK_COST = putPrefix(msg, "teleport.back.no-free");
        MSG_BACK_FREE = putPrefix(msg, "teleport.back.free");
        MSG_TELEPORT_ALL = putPrefix(msg, "teleport.tp.all");
        MSG_TELEPORT_MOVE = putPrefix(msg, "teleport.tp.move");
        MSG_TELEPORT_TIMING = putPrefix(msg, "teleport.tp.timing");
        MSG_TELEPORT_YOURSELF = putPrefix(msg, "teleport.tpa.yourself");
        MSG_TELEPORT_EXISTS = putPrefix(msg, "teleport.tpa.exists");
        MSG_TELEPORT_SENT = putPrefix(msg, "teleport.tpa.sent");
        MSG_TELEPORT_RECEIVED = putPrefix(msg, "teleport.tpa.received");
        MSG_TELEPORT_ACCEPT = putPrefix(msg, "teleport.tpa.accept");
        MSG_TELEPORT_DONE = putPrefix(msg, "teleport.tpa.done");
        MSG_TELEPORT_NO_REQUEST = putPrefix(msg, "teleport.tpa.no-request");
        MSG_TELEPORT_DENY = putPrefix(msg, "teleport.tpa.deny");
        MSG_TELEPORT_DENIED = putPrefix(msg, "teleport.tpa.denied");
        MSG_SPAWNER_GIVE = putPrefix(msg, "spawner.give.types");
        MSG_SPAWNER_SENT = putPrefix(msg, "spawner.give.sent");
        MSG_SPAWNER_RECEIVED = putPrefix(msg, "spawner.give.received");
        MSG_SPAWNER_INVFULL = putPrefix(msg, "spawners.others.inv-full");
        MSG_SPAWNER_BLOCKED = putPrefix(msg, "spawner.others.blocked");
        MSG_SPAWNER_SILK = putPrefix(msg, "spawner.others.need-silktouch");
        MSG_SPAWNER_FAILED = putPrefix(msg, "spawner.others.failed");
        MSG_SPAWNER_NAME = putPrefix(msg, "spawner.others.change-name");
        MSG_SPAWNER_LOG = putPrefix(msg, "spawner.log.change-name");
        MSG_LIGHTNING_SENT = putPrefix(msg, "generic.simp.sent-lightning");
        MSG_LIGHTNING_RECEIVED = putPrefix(msg, "generic.simp.received-lightning");
        MSG_PROFILE_REGISTER = putPrefix(msg, "generic.profile.register");
        MSG_PROFILE_LAST = putPrefix(msg, "generic.profile.last");
        MSG_PROFILE_HOURS = putPrefix(msg, "generic.profile.hours");
        MSG_SUICIDE = putPrefix(msg, "generic.simp.suicide");
        MSG_WEATHER = putPrefix(msg, "generic.simp.weather-changed");
        MSG_SPEED = putPrefix(msg, "generic.others.invalid");
        MSG_CONDENSER = putPrefix(msg, "generic.others.condenser");
        MSG_REWARD_INVALID = putPrefix(msg, "reward.invalid");
        MSG_REWARD_NO = putPrefix(msg, "reward.no-exists");
        MSG_REWARD_CREATE = putPrefix(msg, "reward.created");
        MSG_MEM = putPrefix(msg, "replaces.mem");
        MSG_MEM_ONLINE = putPrefix(msg, "replaces.online");
        MSG_TPS = putPrefix(msg, "replaces.tps");
        MSG_AFK_ENABLE = putPrefix(msg, "generic.afk.enabled");
        MSG_AFK_DISABLE = putPrefix(msg, "generic.afk.disabled");
        MSG_AFK_KICKED = putPrefix(msg, "generic.afk.kicked");
        MSG_AFK_BROAD = putPrefix(msg, "generic.afk.broadcast-kicked");
        MSG_FLY_ENABLE = putPrefix(msg, "generic.others.fly-enabled");
        MSG_FLY_DISABLE = putPrefix(msg, "generic.others.fly-disabled");
        MSG_GOD_ENABLE = putPrefix(msg, "generic.others.god-enabled");
        MSG_GOD_DISABLE = putPrefix(msg, "generic.others.god-disabled");
        MSG_RELOAD_START = putPrefix(msg, "generic.others.reload-start");
        MSG_RELOAD_FINISH = putPrefix(msg, "generic.others.reload-finish");
        MSG_ITEM_RENAME = putPrefix(msg, "generic.items.rename");
        MSG_ITEM_NO = putPrefix(msg, "generic.items.no-item");
        MSG_ITEM_HELMET = putPrefix(msg, "generic.items.helmet");
        MSG_PLAYER_SKIP = putPrefix(msg, "bed.player-s");
        MSG_SKIP_NIGHT = putPrefix(msg, "bed.skip-night");
        MSG_SKIPPING = putPrefix(msg, "bed.night-skipping");
        M_HOME_DONE = putPrefix(msg, "home.done");
        M_HOME_DELETED = putPrefix(msg, "home.deleted");
        M_HOME_NO = putPrefix(msg, "home.no-exists");
        M_HOME_LIST = putPrefix(msg, "home.list");
        M_HOME_CREATED = putPrefix(msg, "home.created");
        M_HOME_LIMIT = putPrefix(msg, "home.limit");
        M_HOME_EXCEEDED = putPrefix(msg, "home.exceeded");
        M_CHAT_MUTED = putPrefix(msg, "chat.muted");
        M_CHATMUTED = putPrefix(msg, "chat.chatmuted");
        M_CHAT_D = putPrefix(msg, "chat.cm-d");
        M_CHAT_E = putPrefix(msg, "chat.cm-e");
        M_CHAT_MUTEBROAD = putPrefix(msg, "chat.mutebroad");
        M_CHAT_UNMUTEBROAD = putPrefix(msg, "chat.unmutebroad");
        M_CHAT_MUTET = putPrefix(msg, "chat.mutetbroad");
        M_CHAT_NOONE = putPrefix(msg, "chat.noone");
        M_CHAT_TO = getColor(msg.getString("chat.tell.to-player"));
        M_CHAT_FROM = getColor(msg.getString("chat.tell.from-player"));
        M_CHAT_R_NO = putPrefix(msg, "chat.tell.no-one");
        MSG_CHAT_LOCKED = putPrefix(msg, "chat.tell.locked");
        MSG_CHAT_UNLOCKED = putPrefix(msg, "chat.tell.unlocked");
        M_CHAT_NO_CHANGE = putPrefix(msg, "chat.no-change");
        M_CHAT_NICK_DENY = putPrefix(msg, "chat.nick-deny");
        M_CHAT_NEWNICK = putPrefix(msg, "chat.newnick");
        M_CHAT_REMOVE_NICK = putPrefix(msg, "chat.remove-nick");
        M_CHAT_NICK_MONEY = putPrefix(msg, "chat.nick-money");
        M_CHAT_NICK_MONEY_2 = putPrefix(msg, "chat.nick-money-2");
        M_CHAT_SPY_D = putPrefix(msg, "chat.spyd");
        M_CHAT_SPY_E = putPrefix(msg, "chat.spye");
        M_CHAT_ADVICE = putPrefix(msg, "chat.global-advice");
        M_CHAT_C = putPrefix(msg, "chat.channelc");
        M_CHAT_IGNORED = putPrefix(msg, "chat.ignored");
        M_CHAT_IGNORE = putPrefix(msg, "chat.ignore-sucess");
        M_CHAT_DENY = putPrefix(msg, "chat.ignore-remove");
        M_KIT_NO_EXISTS = putPrefix(msg, "kit.no-exists");
        M_KIT_LIST = putPrefix(msg, "kit.list");
        M_CASH = putPrefix(msg, "cash.use");
        M_CASH_COST = putPrefix(msg, "cash.cost");
        M_CASH_NO = putPrefix(msg, "cash.no-cash");
        M_CASH_ALREADY = putPrefix(msg, "cash.already");
        M_CASH_BALANCE_OTHER = putPrefix(msg, "cash.balance-other");
        M_CASH_NO_PLAYER = putPrefix(msg, "cash.no-player");
        M_CASH_BALANCE = putPrefix(msg, "cash.balance");
        M_CASH_SUCESS = putPrefix(msg, "cash.sucess");
        M_CASH_NO_BUY = putPrefix(msg, "cash.no-buy");
        M_CASH_CANCEL = putPrefix(msg, "cash.canc");
        M_CASH_RECEIVED = putPrefix(msg, "cash.receive");
        M_CASH_SEND = putPrefix(msg, "cash.send");
        M_CASH_REMOVED = putPrefix(msg, "cash.removed");
        M_CASH_REMOVE = putPrefix(msg, "cash.remove");
        M_GLOW_ENABLED = putPrefix(msg, "glow.brilho");
        M_GLOW_DISABLED = putPrefix(msg, "glow.desbrilho");
        M_GLOW_COLOR = putPrefix(msg, "glow.color");
        M_GM_CHANGED = putPrefix(msg, "generic.gm.changed");
        M_GM_TARGET = putPrefix(msg, "generic.gm.changed-target");
        M_GM_USE = putPrefix(msg, "generic.gm.use");
        M_XP_CHECK = putPrefix(msg, "experience.check");
        M_XP_BOTTLE = putPrefix(msg, "experience.bottleexp");
        M_XP_INSUFFICIENT = putPrefix(msg, "experience.insufficient");
        M_XP_WITHDRAW = putPrefix(msg, "experience.withdraw");
        M_XP_DEPOSIT = putPrefix(msg, "experience.deposit");
        MSG_XP_HELP_TITLE = putPrefix(msg, "experience.help.title");
        MSG_XP_HELP_SET = getColor(msg.getString("experience.help.set"));
        MSG_XP_HELP_TAKE = getColor(msg.getString("experience.help.take"));
        MSG_XP_HELP_GIVE = getColor(msg.getString("experience.help.give"));
        MSG_XP_HELP_CHECK = getColor(msg.getString("experience.help.check"));
        MSG_XP_HELP_BOTTLE = getColor(msg.getString("experience.help.bottle"));
        MSG_XP_HELP_DEPOSIT = getColor(msg.getString("experience.help.deposit"));
        MSG_XP_HELP_WITHDRAW = getColor(msg.getString("experience.help.withdraw"));
        M_XP_SET = putPrefix(msg,"experience.xp-set");
        M_XP_RSET = putPrefix(msg, "experience.xp-rset");
        M_XP_REMOVE = putPrefix(msg, "experience.xp-remove");
        M_XP_RREMOVE = putPrefix(msg, "experience.xp-rremove");
        M_XP_GIVE = putPrefix(msg, "experience.xp-give");
        M_XP_RECEIVE = putPrefix(msg, "experience.xp-receive");
        M_ECO_MONEY = putPrefix(msg, "eco.money");
        M_ECO_OTHER = putPrefix(msg, "eco.money-other");
        M_ECO_PAY_NO = putPrefix(msg, "eco.pay-nomoney");
        M_ECO_AUTO = putPrefix(msg, "eco.auto-pay");
        M_ECO_PAY = putPrefix(msg, "eco.pay");
        M_ECO_PAY_ME = putPrefix(msg, "eco.pay-me");
        M_ECO_BALLIST = putPrefix(msg, "eco.ballist");
        M_ECO_SET = putPrefix(msg,"eco.eco-set");
        M_ECO_RSET = putPrefix(msg, "eco.eco-rset");
        M_ECO_REMOVE = putPrefix(msg, "eco.eco-remove");
        M_ECO_RREMOVE = putPrefix(msg, "eco.eco-rremove");
        M_ECO_GIVE = putPrefix(msg, "eco.eco-give");
        M_ECO_RECEIVE = putPrefix(msg, "eco.eco-receive");
        MSG_ECO_HELP_TITLE = putPrefix(msg, "eco.help.title");
        MSG_ECO_HELP_SET = getColor(msg.getString("eco.help.set"));
        MSG_ECO_HELP_TAKE = getColor(msg.getString("eco.help.take"));
        MSG_ECO_HELP_GIVE = getColor(msg.getString("eco.help.give"));
        MSG_ECO_HELP_MONEY = getColor(msg.getString("eco.help.money"));
        MSG_ECO_HELP_MONEY_ADMIN = getColor(msg.getString("eco.help.money-admin"));
        MSG_ECO_HELP_PAY = getColor(msg.getString("eco.help.pay"));
        MSG_ECO_HELP_BALTOP = getColor(msg.getString("eco.help.baltop"));
        MSG_FEEDED = putPrefix(msg, "generic.others.feeded");
        MSG_FEEDED_TARGET = putPrefix(msg, "generic.others.feeded-target");
    }

    public static String M_SERVER_PREFIX;
    public static String MSG_HELP_FORMAT;
    public static String MSG_MODULE_ENABLE;
    public static String MSG_MODULE_DISABLE;
    public static String MSG_NO_MONEY;
    public static String MSG_LOAD_DATA;
    public static String MSG_NO_PERM;
    public static String MSG_IN_TELEPORT;
    public static String MSG_JOIN;
    public static String MSG_LEAVE;
    public static String MSG_TIMING;
    public static String MSG_NETHER_TRAP;
    public static String MSG_NO_NEGATIVE;
    public static String MSG_ERROR;

    public static String MSG_SPAWN_CREATED;
    public static String MSG_SPAWN_NO_EXISTS;
    public static String MSG_SPAWN_TELEPORT_TARGET;

    public static String MSG_SHOP_CREATED;
    public static String MSG_SHOP_DONE;
    public static String MSG_SHOP_NO_EXISTS;

    public static String MSG_WARP_NO_EXISTS;
    public static String MSG_WARP_DELETED;
    public static String MSG_WARP_CREATED;
    public static String MSG_WARP_DONE;
    public static String MSG_WARP_LIST;

    public static String MSG_BACK_NO_TELEPORT;
    public static String MSG_BACK_COST;
    public static String MSG_BACK_FREE;

    public static String MSG_TELEPORT_ALL;
    public static String MSG_TELEPORT_MOVE;
    public static String MSG_TELEPORT_TIMING;
    public static String MSG_TELEPORT_YOURSELF;
    public static String MSG_TELEPORT_EXISTS;
    public static String MSG_TELEPORT_SENT;
    public static String MSG_TELEPORT_RECEIVED;
    public static String MSG_TELEPORT_ACCEPT;
    public static String MSG_TELEPORT_DONE;
    public static String MSG_TELEPORT_NO_REQUEST;
    public static String MSG_TELEPORT_DENY;
    public static String MSG_TELEPORT_DENIED;

    public static String MSG_SPAWNER_GIVE;
    public static String MSG_SPAWNER_SENT;
    public static String MSG_SPAWNER_RECEIVED;
    public static String MSG_SPAWNER_INVFULL;
    public static String MSG_SPAWNER_BLOCKED;
    public static String MSG_SPAWNER_SILK;
    public static String MSG_SPAWNER_FAILED;
    public static String MSG_SPAWNER_NAME;
    public static String MSG_SPAWNER_LOG;

    public static String MSG_LIGHTNING_SENT;
    public static String MSG_LIGHTNING_RECEIVED;

    public static String MSG_PROFILE_REGISTER;
    public static String MSG_PROFILE_LAST;
    public static String MSG_PROFILE_HOURS;

    public static String MSG_SUICIDE;
    public static String MSG_WEATHER;
    public static String MSG_SPEED;
    public static String MSG_CONDENSER;

    public static String MSG_REWARD_INVALID;
    public static String MSG_REWARD_NO;
    public static String MSG_REWARD_CREATE;

    public static String MSG_MEM;
    public static String MSG_MEM_ONLINE;
    public static String MSG_TPS;

    public static String MSG_AFK_ENABLE;
    public static String MSG_AFK_DISABLE;
    public static String MSG_AFK_KICKED;
    public static String MSG_AFK_BROAD;

    public static String MSG_FLY_ENABLE;
    public static String MSG_FLY_DISABLE;
    public static String MSG_GOD_ENABLE;
    public static String MSG_GOD_DISABLE;
    public static String MSG_RELOAD_START;
    public static String MSG_RELOAD_FINISH;
    public static String MSG_ITEM_RENAME;
    public static String MSG_ITEM_NO;
    public static String MSG_ITEM_HELMET;

    public static String MSG_PLAYER_SKIP;
    public static String MSG_SKIP_NIGHT;
    public static String MSG_SKIPPING;

    public static String MSG_FEEDED;
    public static String MSG_FEEDED_TARGET;

    public static String M_HOME_DONE;
    public static String M_HOME_DELETED;
    public static String M_HOME_NO;
    public static String M_HOME_LIST;
    public static String M_HOME_CREATED;
    public static String M_HOME_LIMIT;
    public static String M_HOME_EXCEEDED;

    public static String M_CHAT_MUTED;
    public static String M_CHATMUTED;
    public static String M_CHAT_D;
    public static String M_CHAT_E;
    public static String M_CHAT_MUTEBROAD;
    public static String M_CHAT_UNMUTEBROAD;
    public static String M_CHAT_MUTET;
    public static String M_CHAT_NOONE;
    public static String M_CHAT_TO;
    public static String M_CHAT_FROM;
    public static String M_CHAT_R_NO;
    public static String MSG_CHAT_LOCKED;
    public static String MSG_CHAT_UNLOCKED;
    public static String M_CHAT_NO_CHANGE;
    public static String M_CHAT_NICK_DENY;
    public static String M_CHAT_NEWNICK;
    public static String M_CHAT_REMOVE_NICK;
    public static String M_CHAT_NICK_MONEY;
    public static String M_CHAT_NICK_MONEY_2;
    public static String M_CHAT_SPY_D;
    public static String M_CHAT_SPY_E;
    public static String M_CHAT_ADVICE;
    public static String M_CHAT_C;
    public static String M_CHAT_IGNORED;
    public static String M_CHAT_IGNORE;
    public static String M_CHAT_DENY;

    public static String M_KIT_NO_EXISTS;
    public static String M_KIT_LIST;

    public static String M_CASH;
    public static String M_CASH_COST;
    public static String M_CASH_NO;
    public static String M_CASH_ALREADY;
    public static String M_CASH_BALANCE_OTHER;
    public static String M_CASH_NO_PLAYER;
    public static String M_CASH_BALANCE;
    public static String M_CASH_SUCESS;
    public static String M_CASH_NO_BUY;
    public static String M_CASH_CANCEL;
    public static String M_CASH_RECEIVED;
    public static String M_CASH_SEND;
    public static String M_CASH_REMOVED;
    public static String M_CASH_REMOVE;

    public static String M_GLOW_ENABLED;
    public static String M_GLOW_DISABLED;
    public static String M_GLOW_COLOR;

    public static String M_GM_CHANGED;
    public static String M_GM_TARGET;
    public static String M_GM_USE;

    public static String M_XP_CHECK;
    public static String M_XP_BOTTLE;
    public static String M_XP_INSUFFICIENT;
    public static String M_XP_WITHDRAW;
    public static String M_XP_DEPOSIT;
    public static String MSG_XP_HELP_TITLE;
    public static String MSG_XP_HELP_SET;
    public static String MSG_XP_HELP_TAKE;
    public static String MSG_XP_HELP_GIVE;
    public static String MSG_XP_HELP_CHECK;
    public static String MSG_XP_HELP_BOTTLE;
    public static String MSG_XP_HELP_DEPOSIT;
    public static String MSG_XP_HELP_WITHDRAW;
    public static String M_XP_SET;
    public static String M_XP_RSET;
    public static String M_XP_REMOVE;
    public static String M_XP_RREMOVE;
    public static String M_XP_GIVE;
    public static String M_XP_RECEIVE;

    public static String M_ECO_MONEY;
    public static String M_ECO_OTHER;
    public static String M_ECO_PAY_NO;
    public static String M_ECO_AUTO;
    public static String M_ECO_PAY;
    public static String M_ECO_PAY_ME;
    public static String M_ECO_BALLIST;
    public static String M_ECO_SET;
    public static String M_ECO_RSET;
    public static String M_ECO_REMOVE;
    public static String M_ECO_RREMOVE;
    public static String M_ECO_GIVE;
    public static String M_ECO_RECEIVE;
    public static String MSG_ECO_HELP_TITLE;
    public static String MSG_ECO_HELP_SET;
    public static String MSG_ECO_HELP_TAKE;
    public static String MSG_ECO_HELP_GIVE;
    public static String MSG_ECO_HELP_MONEY;
    public static String MSG_ECO_HELP_MONEY_ADMIN;
    public static String MSG_ECO_HELP_PAY;
    public static String MSG_ECO_HELP_BALTOP;

    private static String putPrefix(FileConfiguration msg, String path) {
        String message = msg.getString(path);
        if (message == null) message = "&7Erro&8, &7texto &3" + path + "&7não encontrado&8.";
        return M_SERVER_PREFIX + getColor(message);
    }

    public static String getColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
