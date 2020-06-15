package br.com.eterniaserver.eterniaserver;

public class Strings {

    private Strings() {
        throw new IllegalStateException("Utility class");
    }

    // Placeholders
    public static final String TARGET_NAME = "%target_name%";
    public static final String PLAYER_NAME = "%player_name%";
    public static final String MONEY = "%money%";
    public static final String TIME = "%time%";
    public static final String WARP_NAME = "%warp_name%";
    public static final String GAMEMODE = "%gamemode%";
    public static final String MESSAGE = "%message%";

    // Messages
    public static final String MODULES_CHAT = "modules.chat";
    public static final String SERVER_CHECKS = "server.checks";
    public static final String SERVER_COOLDOWN = "server.cooldown";
    public static final String SERVER_TELEP = "server.telep";
    public static final String HOME_NO_EXIST = "home.no-exists";
    public static final String WARP_DONE = "teleport.warp.done";
    public static final String BLOCKS_GET = "blocks.";
    public static final String KITS_GET = "kits.";
    public static final String SERVER_NO_PERM = "server.no-perm";
    public static final String WARP_NO_EXIST = "teleport.warp.no-exists";
    public static final String EXP_INSUFFICIENT = "experience.insufficient";
    public static final String NO_ITEMS = "generic.items.no-item";
    public static final String GM_CHANGED = "generic.gm.changed";
    public static final String GM_CHANGED_TARGET = "generic.gm.changed-target";

    // Tables
    public static final String TABLE_MUTED = "sql.table-muted";
    public static final String TABLE_REWARDS = "sql.table-rewards";
    public static final String TABLE_HOME = "sql.table-home";
    public static final String TABLE_HOMES = "sql.table-homes";
    public static final String TABLE_KITS = "sql.table-kits";
    public static final String TABLE_WARP = "sql.table-warp";
    public static final String TABLE_SHOP = "sql.table-shop";

}
