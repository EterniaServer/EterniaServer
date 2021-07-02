package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eternialib.core.enums.ConfigurationCategory;
import br.com.eterniaserver.eternialib.core.interfaces.ReloadableConfiguration;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configurations.GenericCfg;
import br.com.eterniaserver.eterniaserver.enums.*;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class EconomyCfg extends GenericCfg implements ReloadableConfiguration {

    private final int[] chestShopBuyRoof;
    private final int[] chestShopSellRoof;

    public EconomyCfg(final EterniaServer plugin,
                      final boolean[] booleans,
                      final double[] doubles,
                      final String[] strings,
                      final int[] integers,
                      final int[] chestShopBuyRoof,
                      final int[] chestShopSellRoof) {
        super(plugin, strings, booleans, integers, doubles);
        this.chestShopBuyRoof = chestShopBuyRoof;
        this.chestShopSellRoof = chestShopSellRoof;
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

        setInteger(Integers.CS_AMOUNT_REMOVED, file, outFile, "supports.cs-amount-removed", 500);
        setInteger(Integers.CS_CLEAR_SCHEDULE_TIME, file, outFile, "supports.cs-clear-schedule-time", 12);
        setInteger(Integers.CS_CLEAR_TICK, file, outFile, "supports.cs-clear-tick", 40);

        setDouble(Doubles.START_MONEY, file, outFile, "general.start", 15.0);
        setDouble(Doubles.BACK_COST, file, outFile, "command.back-cost", 500.0);
        setDouble(Doubles.NICK_COST, file, outFile, "command.nick-cost", 250000.0);

        setList(Lists.BLACKLISTED_BALANCE_TOP, file, outFile, "general.blacklisted-baltop", "NadaParaSeVerAqui");

        for (final Material material : Material.values()) {
            chestShopBuyRoof[material.ordinal()] = file.getInt("chest-shop." + material.name() + ".buy", 23040);
            chestShopSellRoof[material.ordinal()] = file.getInt("chest-shop." + material.name() + ".sell", 230400);

            outFile.set("chest-shop." + material.name() + ".buy", chestShopBuyRoof[material.ordinal()]);
            outFile.set("chest-shop." + material.name() + ".sell", chestShopSellRoof[material.ordinal()]);
        }

        saveFile(outFile, Constants.ECONOMY_FILE_PATH);
    }

    @Override
    public void executeCritical() {

    }
}
