package br.com.eterniaserver.eterniaserver;

public class Strings {

    private Strings() {
        throw new IllegalStateException("Utility class");
    }

    public static final String NAME = "name";
    public static final String LOC = "location";
    public static final String PNAME = "player_name";
    public static final String BALANCE = "balance";

    public static final String M_NO_MONEY = "server.no-money";
    public static final String M_LOAD_DATA = "server.load-data";
    public static final String M_NO_PERM = "server.no-perm";
    public static final String M_TELEP = "server.telep";

    public static final String M_NO_SPAWN = "teleport.spawn.no-exists";
    public static final String M_CRE_SPAWN = "teleport.spawn.created";
    public static final String M_TARGET_SPAWN = "teleport.spawn.tp-target";

    public static final String M_NO_SHOP = "teleport.shop.no-exists";
    public static final String M_CRE_SHOP = "teleport.shop.created";
    public static final String M_DONE_SHOP = "teleport.shop.done";

    public static final String M_NO_WARP = "teleport.warp.no-exists";
    public static final String M_DEL_WARP = "teleport.warp.deleted";
    public static final String M_CRE_WARP = "teleport.warp.created";
    public static final String M_WARP_DONE = "teleport.warp.done";
    public static final String M_WARP_LIST = "teleport.warp.list";

    public static final String M_NO_BACK = "teleport.back.no-tp";
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
    public static final String M_SPAWNER_INVFULL = "spawners.invfull";

}
