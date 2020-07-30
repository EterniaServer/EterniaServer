package br.com.eterniaserver.eterniaserver;

public class Strings {

    private Strings() {
        throw new IllegalStateException("Utility class");
    }

    public static final String NAME = "name";
    public static final String LOCATION = "location";
    public static final String PLAYER_NAME = "player_name";
    public static final String PLAYER_DISPLAY = "player_display";
    public static final String BALANCE = "balance";
    public static final String CODE = "code";
    public static final String CODE_GROUP = "lalalala";
    public static final String TIME = "time";
    public static final String COOLDOWN = "cooldown";
    public static final String XP = "xp";
    public static final String HOMES = "homes";
    public static final String CLEAR = "clear";

    public static final String MSG_NO_MONEY = "server.no-money";
    public static final String MSG_LOAD_DATA = "server.load-data";
    public static final String MSG_NO_PERM = "server.no-perm";
    public static final String MSG_IN_TELEPORT = "server.telep";
    public static final String MSG_JOIN = "server.join";
    public static final String MSG_LEAVE = "server.leave";
    public static final String MSG_TIMING = "server.timing";
    public static final String MSG_NETHER_TRAP = "server.nether-trap";
    public static final String MSG_NO_NEGATIVE = "server.neg";
    public static final String MSG_ERROR = "server.chat-error";

    public static final String MSG_SPAWN_CREATED = "teleport.spawn.created";
    public static final String MSG_SPAWN_NO_EXISTS = "teleport.spawn.no-exists";
    public static final String MSG_SPAWN_TELEPORT_TARGET = "teleport.spawn.tp-target";

    public static final String MSG_SHOP_CREATED = "teleport.shop.created";
    public static final String MSG_SHOP_DONE = "teleport.shop.done";
    public static final String MSG_SHOP_NO_EXISTS = "teleport.shop.no-exists";

    public static final String MSG_WARP_NO_EXISTS = "teleport.warp.no-exists";
    public static final String MSG_WARP_DELETED = "teleport.warp.deleted";
    public static final String MSG_WARP_CREATED = "teleport.warp.created";
    public static final String MSG_WARP_DONE = "teleport.warp.done";
    public static final String MSG_WARP_LIST = "teleport.warp.list";

    public static final String MSG_BACK_NO_TELEPORT = "teleport.back.no-tp";
    public static final String MSG_BACK_COST = "teleport.back.no-free";
    public static final String MSG_BACK_FREE = "teleport.back.free";

    public static final String MSG_TELEPORT_ALL = "teleport.tp.all";
    public static final String MSG_TELEPORT_MOVE = "teleport.tp.move";
    public static final String MSG_TELEPORT_TIMING = "teleport.tp.timing";
    public static final String MSG_TELEPORT_YOURSELF = "teleport.tpa.yourself";
    public static final String MSG_TELEPORT_EXISTS = "teleport.tpa.exists";
    public static final String MSG_TELEPORT_SENT = "teleport.tpa.sent";
    public static final String MSG_TELEPORT_RECEIVED = "teleport.tpa.received";
    public static final String MSG_TELEPORT_ACCEPT = "teleport.tpa.accept";
    public static final String MSG_TELEPORT_DONE = "teleport.tpa.done";
    public static final String MSG_TELEPORT_NO_REQUEST = "teleport.tpa.no-request";
    public static final String MSG_TELEPORT_DENY = "teleport.tpa.deny";
    public static final String MSG_TELEPORT_DENIED = "teleport.tpa.denied";

    public static final String MSG_SPAWNER_GIVE = "spawner.give.types";
    public static final String MSG_SPAWNER_SENT = "spawner.give.sent";
    public static final String MSG_SPAWNER_RECEIVED = "spawner.give.received";
    public static final String MSG_SPAWNER_INVFULL = "spawners.others.inv-full";
    public static final String MSG_SPAWNER_BLOCKED = "spawner.others.blocked";
    public static final String MSG_SPAWNER_SILK = "spawner.others.need-silktouch";
    public static final String MSG_SPAWNER_FAILED = "spawner.others.failed";
    public static final String MSG_SPAWNER_NAME = "spawner.others.change-name";
    public static final String MSG_SPAWNER_LOG = "spawner.log.change-name";

    public static final String MSG_LIGHTNING_SENT = "generic.simp.sent-lightning";
    public static final String MSG_LIGHTNING_RECEIVED = "generic.simp.received-lightning";

    public static final String MSG_PROFILE_REGISTER = "generic.profile.register";

    public static final String MSG_SUICIDE = "generic.simp.suicide";
    public static final String MSG_WEATHER = "generic.simp.weather-changed";
    public static final String MSG_SPEED = "generic.others.invalid";
    public static final String MSG_CONDENSER = "generic.others.condenser";

    public static final String MSG_REWARD_INVALID = "reward.invalid";
    public static final String MSG_REWARD_NO = "reward.no-exists";
    public static final String MSG_REWARD_CREATE = "reward.created";

    public static final String MSG_MEM = "replaces.mem";
    public static final String MSG_MEM_ONLINE = "replaces.online";
    public static final String MSG_TPS = "replaces.tps";

    public static final String MSG_AFK_ENABLE = "generic.afk.enabled";
    public static final String MSG_AFK_DISABLE = "generic.afk.disabled";
    public static final String MSG_AFK_KICKED = "generic.afk.kicked";
    public static final String MSG_AFK_BROAD = "generic.afk.broadcast-kicked";

    public static final String MSG_FLY_ENABLE = "generic.others.fly-enabled";
    public static final String MSG_FLY_DISABLE = "generic.others.fly-disabled";
    public static final String MSG_GOD_ENABLE = "generic.others.god-enabled";
    public static final String MSG_GOD_DISABLE = "generic.others.god-disabled";
    public static final String MSG_RELOAD_START = "generic.others.reload-start";
    public static final String MSG_RELOAD_FINISH = "generic.others.reload-finish";
    public static final String MSG_ITEM_RENAME = "generic.items.rename";
    public static final String MSG_ITEM_NO = "generic.items.no-item";
    public static final String MSG_ITEM_HELMET = "generic.items.helmet";

    public static final String MSG_PLAYER_SKIP = "bed.player-s";
    public static final String MSG_SKIP_NIGHT = "bed.skip-night";
    public static final String MSG_SKIPPING = "bed.night-skipping";

    public static final String M_HOME_DONE = "home.done";
    public static final String M_HOME_DELETED = "home.deleted";
    public static final String M_HOME_NO = "home.no-exists";
    public static final String M_HOME_LIST = "home.list";
    public static final String M_HOME_CREATED = "home.created";
    public static final String M_HOME_LIMIT = "home.limit";
    public static final String M_HOME_EXCEEDED = "home.exceeded";

    public static final String M_CHAT_MUTED = "chat.muted";
    public static final String M_CHATMUTED = "chat.chatmuted";
    public static final String M_CHAT_D = "chat.cm-d";
    public static final String M_CHAT_E = "chat.cm-e";
    public static final String M_CHAT_MUTEBROAD = "chat.mutebroad";
    public static final String M_CHAT_UNMUTEBROAD = "chat.unmutebroad";
    public static final String M_CHAT_MUTET = "chat.mutetbroad";
    public static final String M_CHAT_NOONE = "chat.noone";
    public static final String M_CHAT_TO = "chat.toplayer";
    public static final String M_CHAT_FROM = "chat.fromplayer";
    public static final String M_CHAT_R_NO = "chat.rnaote";
    public static final String M_CHAT_NO_CHANGE = "chat.no-change";
    public static final String M_CHAT_NICK_DENY = "chat.nick-deny";
    public static final String M_CHAT_NEWNICK = "chat.newnick";
    public static final String M_CHAT_REMOVE_NICK = "chat.remove-nick";
    public static final String M_CHAT_NICK_MONEY = "chat.nick-money";
    public static final String M_CHAT_NICK_MONEY_2 = "chat.nick-money-2";
    public static final String M_CHAT_SPY_D = "chat.spyd";
    public static final String M_CHAT_SPY_E = "chat.spye";
    public static final String M_CHAT_ADVICE = "chat.global-advice";
    public static final String M_CHAT_C = "chat.channelc";
    public static final String M_CHAT_IGNORED = "chat.ignored";
    public static final String M_CHAT_IGNORE = "chat.ignore-sucess";
    public static final String M_CHAT_DENY = "chat.ignore-remove";

    public static final String M_KIT_NO_EXISTS = "kit.no-exists";
    public static final String M_KIT_LIST = "kit.list";

    public static final String M_CASH = "cash.use";
    public static final String M_CASH_COST = "cash.cost";
    public static final String M_CASH_NO = "cash.no-cash";
    public static final String M_CASH_ALREADY = "cash.already";
    public static final String M_CASH_BALANCE_OTHER = "cash.balance-other";
    public static final String M_CASH_NO_PLAYER = "cash.no-player";
    public static final String M_CASH_BALANCE = "cash.balance";
    public static final String M_CASH_SUCESS = "cash.sucess";
    public static final String M_CASH_NO_BUY = "cash.no-buy";
    public static final String M_CASH_CANCEL = "cash.canc";
    public static final String M_CASH_RECEIVED = "cash.receive";
    public static final String M_CASH_SEND = "cash.send";
    public static final String M_CASH_REMOVED = "cash.removed";
    public static final String M_CASH_REMOVE = "cash.remove";

    public static final String M_GLOW_ENABLED = "glow.brilho";
    public static final String M_GLOW_DISABLED = "glow.desbrilho";
    public static final String M_GLOW_COLOR = "glow.color";

    public static final String M_GM_CHANGED = "generic.gm.changed";
    public static final String M_GM_TARGET = "generic.gm.changed-target";
    public static final String M_GM_USE = "generic.gm.use";

    public static final String M_XP_CHECK = "experience.check";
    public static final String M_XP_BOTTLE = "experience.bottleexp";
    public static final String M_XP_INSUFFICIENT = "experience.insufficient";
    public static final String M_XP_WITHDRAW = "experience.withdraw";
    public static final String M_XP_DEPOSIT = "experience.deposit";

    public static final String M_ECO_MONEY = "eco.money";
    public static final String M_ECO_OTHER = "eco.money-other";
    public static final String M_ECO_PAY_NO = "eco.pay-nomoney";
    public static final String M_ECO_AUTO = "eco.auto-pay";
    public static final String M_ECO_PAY = "eco.pay";
    public static final String M_ECO_PAY_ME = "eco.pay-me";
    public static final String M_ECO_BALLIST = "eco.ballist";
    public static final String M_ECO_SET = "eco.eco-set";
    public static final String M_ECO_RSET = "eco.eco-rset";
    public static final String M_ECO_REMOVE = "eco.eco-remove";
    public static final String M_ECO_RREMOVE = "eco.eco-rremove";
    public static final String M_ECO_GIVE = "eco.eco-give";
    public static final String M_ECO_RECEIVE = "eco.eco-receive";

}
