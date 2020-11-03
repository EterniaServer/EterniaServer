package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.configurations.configs.BlocksCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.CashCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.ChatCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.CommandsCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.ConfigsCfg;
import br.com.eterniaserver.eterniaserver.configurations.locales.ConstantsCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.KitsCfg;
import br.com.eterniaserver.eterniaserver.configurations.locales.CommandsLocaleCfg;
import br.com.eterniaserver.eterniaserver.configurations.locales.MsgCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.RewardsCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.ScheduleCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.TableCfg;
import br.com.eterniaserver.eterniaserver.configurations.dependencies.Placeholders;
import br.com.eterniaserver.eterniaserver.configurations.dependencies.VaultHook;
import br.com.eterniaserver.eterniaserver.enums.ConfigBooleans;
import br.com.eterniaserver.eterniaserver.enums.ConfigDoubles;
import br.com.eterniaserver.eterniaserver.enums.ConfigIntegers;
import br.com.eterniaserver.eterniaserver.enums.ConfigLists;
import br.com.eterniaserver.eterniaserver.enums.ConfigStrings;
import br.com.eterniaserver.eterniaserver.events.BlockHandler;
import br.com.eterniaserver.eterniaserver.events.EntityHandler;
import br.com.eterniaserver.eterniaserver.events.PlayerHandler;
import br.com.eterniaserver.eterniaserver.events.ServerHandler;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EterniaServer extends JavaPlugin {

    public static final MsgCfg msg = new MsgCfg();
    public static final CommandsLocaleCfg cmdsLocale = new CommandsLocaleCfg();
    public static final BlocksCfg block = new BlocksCfg();
    public static final CashCfg cash = new CashCfg();
    public static final KitsCfg kits = new KitsCfg();
    public static final ChatCfg chat = new ChatCfg();
    public static final ScheduleCfg schedule =  new ScheduleCfg();
    public static final CommandsCfg commands = new CommandsCfg();
    public static final RewardsCfg rewards = new RewardsCfg();

    private static final String[] strings = new String[ConfigStrings.values().length];
    private static final Boolean[] booleans = new Boolean[ConfigBooleans.values().length];
    private static final Integer[] integers = new Integer[ConfigIntegers.values().length];
    private static final Double[] doubles = new Double[ConfigDoubles.values().length];
    private static final List<List<?>> lists = Arrays.asList(new ArrayList<?>[ConfigLists.values().length]);

    @Override
    public void onEnable() {

        new ConstantsCfg(strings);
        new ConfigsCfg(strings, booleans, integers, doubles, lists);

        new TableCfg();
        new Placeholders().register();
        new Managers(this);
        new VaultHook(this);

        this.getServer().getPluginManager().registerEvents(new BlockHandler(), this);
        this.getServer().getPluginManager().registerEvents(new EntityHandler(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerHandler(this), this);
        this.getServer().getPluginManager().registerEvents(new ServerHandler(), this);

    }

    public static String getString(ConfigStrings stringName) {
        return strings[stringName.ordinal()];
    }

    public static boolean getBoolean(ConfigBooleans booleanName) {
        return booleans[booleanName.ordinal()];
    }

    public static int getInteger(ConfigIntegers integerName) {
        return integers[integerName.ordinal()];
    }

    public static double getDouble(ConfigDoubles doubleName) {
        return doubles[doubleName.ordinal()];
    }

    public static List<?> getList(ConfigLists listName) {
        return lists.get(listName.ordinal());
    }

}
