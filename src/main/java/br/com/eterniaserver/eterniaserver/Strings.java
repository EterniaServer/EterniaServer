package br.com.eterniaserver.eterniaserver;

public class Strings {

    private Strings() {
        throw new IllegalStateException("Utility class");
    }

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

    // SQL
    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS ";
    public static final String INSERT_INTO = "INSERT INTO ";
    public static final String SELECT_FROM = "SELECT * FROM ";
    public static final String UPDATE = "UPDATE ";
    public static final String WHERE_PLAYER_NAME = " WHERE player_name='";
    public static final String WHERE_NAME = " WHERE name='";

    // String
    public static final String PLAYER_NAME_STR = "player_name";

}
