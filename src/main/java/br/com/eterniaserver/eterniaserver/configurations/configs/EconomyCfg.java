package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eternialib.core.enums.ConfigurationCategory;
import br.com.eterniaserver.eternialib.core.interfaces.ReloadableConfiguration;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configurations.GenericCfg;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;

public class EconomyCfg extends GenericCfg implements ReloadableConfiguration {

    private final EterniaServer plugin;

    private final Map<Material, Integer> limit;

    public EconomyCfg(final EterniaServer plugin,
                      final boolean[] booleans,
                      final double[] doubles,
                      final String[] strings,
                      final Map<Material, Integer> limit) {
        super(plugin, strings, booleans, null, doubles);
        this.plugin = plugin;
        this.limit = limit;
    }


    @Override
    public ConfigurationCategory category() {
        return ConfigurationCategory.GENERIC;
    }

    @Override
    public void executeConfig() {
        final FileConfiguration file = YamlConfiguration.loadConfiguration(new File(Constants.ECONOMY_FILE_PATH));
        final FileConfiguration outFile = new YamlConfiguration();

        setString(Strings.MONEY_SINGULAR, file, outFile, "general.singular", "Eternia");
        setString(Strings.MONEY_PLURAL, file, outFile, "general.plural", "Eternias");
        setString(Strings.SERVER_BALANCE_ACCOUNT, file, outFile, "general.balance-account", "EterniaServer");

        setBoolean(Booleans.CHEST_SHOP_SUPPORT, file, outFile, "supports.chest-shop", true);

        setDouble(Doubles.START_MONEY, file, outFile, "general.start", 15.0);
        setDouble(Doubles.BACK_COST, file, outFile, "command.back-cost", 500.0);
        setDouble(Doubles.NICK_COST, file, outFile, "command.nick-cost", 250000.0);

        setList(Lists.BLACKLISTED_BALANCE_TOP, file, outFile, "general.blacklisted-baltop", "NadaParaSeVerAqui");

        saveFile(outFile, Constants.ECONOMY_FILE_PATH);
    }

    @Override
    public void executeCritical() {

    }
}
