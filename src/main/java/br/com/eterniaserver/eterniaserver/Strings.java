package br.com.eterniaserver.eterniaserver;

public class Strings {

    private Strings() {
        throw new IllegalStateException("Utility class");
    }

    public static final String NAME = "name";
    public static final String LOC = "location";
    public static final String PNAME = "player_name";
    public static final String BALANCE = "balance";
    public static final String CODE = "code";
    public static final String CODE_GROUP = "lalalala";
    public static final String TIME = "time";
    public static final String COOLDOWN = "cooldown";
    public static final String XP = "xp";
    public static final String HOMES = "homes";

    public static final String M_NO_MONEY = "server.no-money";
    public static final String M_LOAD_DATA = "server.load-data";
    public static final String M_NO_PERM = "server.no-perm";
    public static final String M_TELEP = "server.telep";
    public static final String M_JOIN = "server.join";
    public static final String M_LEAVE = "server.leave";
    public static final String M_TIMING = "server.timing";

    public static final String M_SPAWN_NO = "teleport.spawn.no-exists";
    public static final String M_SPAWN_CREATE = "teleport.spawn.created";
    public static final String M_SPAWN_TARGET = "teleport.spawn.tp-target";

    public static final String M_SHOP_NO = "teleport.shop.no-exists";
    public static final String M_SHOP_CREATE = "teleport.shop.created";
    public static final String M_SHOP_DONE = "teleport.shop.done";

    public static final String M_WARP_NO = "teleport.warp.no-exists";
    public static final String M_WARP_DELETE = "teleport.warp.deleted";
    public static final String M_WARP_CREATE = "teleport.warp.created";
    public static final String M_WARP_DONE = "teleport.warp.done";
    public static final String M_WARP_LIST = "teleport.warp.list";

    public static final String M_BACK_NO = "teleport.back.no-tp";
    public static final String M_BACK = "teleport.back.no-free";
    public static final String M_BACK_FREE = "teleport.back.free";

    public static final String M_TPALL = "teleport.tp.all";
    public static final String M_TPA_YOU = "teleport.tpa.yourself";
    public static final String M_TPA_EXISTS = "teleport.tpa.exists";
    public static final String M_TPA_SENT = "teleport.tpa.sent";
    public static final String M_TPA_RECEIVED = "teleport.tpa.received";
    public static final String M_TPA_ACCEPT = "teleport.tpa.accept";
    public static final String M_TPA_DONE = "teleport.tpa.done";
    public static final String M_TPA_NO_REQUEST = "teleport.tpa.no-request";
    public static final String M_TPA_DENY = "teleport.tpa.deny";
    public static final String M_TPA_DENIED = "teleport.tpa.denied";

    public static final String M_SPAWNER_GIVE = "spawner.give.types";
    public static final String M_SPAWNER_SENT = "spawner.give.sent";
    public static final String M_SPAWNER_RECEIVED = "spawner.give.received";
    public static final String M_SPAWNER_INVFULL = "spawners.others.inv-full";
    public static final String M_SPAWNER_BLOCKED = "spawner.others.blocked";
    public static final String M_SPAWNER_SILK = "spawner.others.need-silktouch";
    public static final String M_SPAWNER_FAILED = "spawner.others.failed";
    public static final String M_SPAWNER_NAME = "spawner.others.change-name";
    public static final String M_SPAWNER_LOG = "spawner.log.change-name";

    public static final String M_LIGHTNING_SENT = "generic.simp.sent-lightning";
    public static final String M_LIGHTNING_RECEIVED = "generic.simp.received-lightning";

    public static final String M_PROFILE_REGISTER = "generic.profile.register";

    public static final String M_SUICIDE = "generic.simp.suicide";
    public static final String M_WEATHER = "generic.simp.weather-changed";
    public static final String M_SPEED = "generic.others.invalid";
    public static final String M_CONDENSER = "generic.others.condenser";

    public static final String M_REWARD_INVALID = "reward.invalid";
    public static final String M_REWARD_NO = "reward.no-exists";
    public static final String M_REWARD_CREATE = "reward.created";

    public static final String M_MEM = "replaces.mem";
    public static final String M_MEM_ONLINE = "replaces.online";
    public static final String M_TPS = "replaces.tps";

    public static final String M_AFK_ENABLE = "generic.afk.enabled";
    public static final String M_AFK_DISABLE = "generic.afk.disabled";
    public static final String M_FLY_ENABLE = "generic.others.fly-enabled";
    public static final String M_FLY_DISABLE = "generic.others.fly-disabled";
    public static final String M_GOD_ENABLE = "generic.others.god-enabled";
    public static final String M_GOD_DISABLE = "generic.others.god-disabled";
    public static final String M_RELOAD_S = "generic.others.reload-start";
    public static final String M_RELOAD_F = "generic.others.reload-finish";
    public static final String M_ITEM_RENAME = "generic.items.rename";
    public static final String M_ITEM_NO = "generic.items.no-item";
    public static final String M_ITEM_HELMET = "generic.items.helmet";

    public static final String M_BED_S = "bed.player-s";
    public static final String M_BED_SKIP = "bed.skip-night";
    public static final String M_BED_NIGHT_SKIP = "bed.night-skipping";

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

    public static final String M_KIT_NO_EXISTS = "kit.no-exists";
    public static final String M_KIT_LIST = "kit.list";

    public static final String M_CASH = "cash.use";
    public static final String M_CASH_COST = "cash.cost";
    public static final String M_CASH_USE = "cash.use";
    public static final String M_CASH_NO = "cash.no-cash";
    public static final String M_CASH_ALREADY = "cash.already";

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
    public static final String M_ECO_OTHER = "eternia.money.other";
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
