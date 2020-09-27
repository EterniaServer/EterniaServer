package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import org.bukkit.ChatColor;

public class PluginMSGs {

    private PluginMSGs() {
        throw new IllegalStateException("Utility class");
    }

    public static final String MSG_MODULE_ENABLE = putPrefix("modules.enable");
    public static final String MSG_MODULE_DISABLE = putPrefix("modules.disable");
    public static final String MSG_NO_MONEY = putPrefix("server.no-money");
    public static final String MSG_LOAD_DATA = putPrefix("server.load-data");
    public static final String MSG_JOIN = putPrefix("server.join");
    public static final String MSG_LEAVE = putPrefix("server.leave");
    public static final String MSG_TIMING = putPrefix("server.timing");
    public static final String MSG_NETHER_TRAP = putPrefix("server.nether-trap");
    public static final String MSG_ERROR = putPrefix("server.chat-error");
    public static final String REMOVED_ENTITIES = putPrefix("server.entities-removeds");

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

    public static final String MSG_MEM = putPrefix("replaces.mem");
    public static final String MSG_MEM_ONLINE = putPrefix("replaces.online");
    public static final String MSG_TPS = putPrefix("replaces.tps");

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

    public static final String ITEM_LORE_CLEAR = putPrefix("item.lore.clear");
    public static final String ITEM_LORE_ADD = putPrefix("item.lore.add");
    public static final String ITEM_LORE_SET = putPrefix("item.lore.set");
    public static final String ITEM_NAME_CLEAR = putPrefix("item.name.clear");
    public static final String ITEM_NAME_SET = putPrefix("item.name.set");
    public static final String ITEM_ADDKEY = putPrefix("item.addkey");
    public static final String ITEM_NO = putPrefix("item.no");
    public static final String ITEM_HELMET = putPrefix("item.helmet");

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

    public static final String M_KIT_NO_EXISTS = putPrefix("kit.no-exists");
    public static final String M_KIT_LIST = putPrefix("kit.list");

    private static String putPrefix(String path) {
        String message = EterniaServer.msgConfig.getString(path);
        if (message == null) message = "&7Erro&8, &7texto &3" + path + "&7não encontrado&8.";
        return EterniaServer.configs.serverPrefix + getColor(message);
    }

    public static String getColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
