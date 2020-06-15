package br.com.eterniaserver.eterniaserver;

import java.text.SimpleDateFormat;

public class Strings {

    private Strings() {
        throw new IllegalStateException("Utility class");
    }

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    // Placeholders
    public static final String TARGET_NAME = "%target_name%";
    public static final String PLAYER_NAME = "%player_name%";
    public static final String TIME = "%time%";
    public static final String WARP_NAME = "%warp_name%";

    // Messages
    public static final String MODULES_CHAT = "modules.chat";
    public static final String SERVER_CHECKS = "server.checks";
    public static final String SERVER_COOLDOWN = "server.cooldown";
    public static final String SERVER_TELEP = "server.telep";
    public static final String HOME_NO_EXIST = "home.no-exists";
    public static final String WARP_DONE = "teleport.warp.done";
    public static final String KITS_GET = "kits.";
    public static final String SERVER_NO_PERM = "server.no-perm";
    public static final String WARP_NO_EXIST = "teleport.warp.no-exists";

    // Tables
    public static final String TABLE_HOME = "sql.table-home";
    public static final String TABLE_HOMES = "sql.table-homes";
    public static final String TABLE_KITS = "sql.table-kits";
    public static final String TABLE_WARP = "sql.table-warp";
    public static final String TABLE_SHOP = "sql.table-shop";

}
