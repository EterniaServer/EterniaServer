package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Lists;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class EconomySaveCfg {

    private final EterniaServer plugin;

    private final int[] chestShopBuyRoof;
    private final int[] chestShopSellRoof;

    public EconomySaveCfg(final EterniaServer plugin,
                          final int[] chestShopBuyRoof,
                          final int[] chestShopSellRoof) {
        this.plugin = plugin;
        this.chestShopBuyRoof = chestShopBuyRoof;
        this.chestShopSellRoof = chestShopSellRoof;
    }

    public void saveToFile(int[] chestShopSellRoofNew, int[] chestShopBuyRoofNew) {
        final FileConfiguration outFile = new YamlConfiguration();

        outFile.set("general.singular", plugin.getString(Strings.MONEY_SINGULAR));
        outFile.set("general.plural", plugin.getString(Strings.MONEY_PLURAL));
        outFile.set("general.balance-account", plugin.getString(Strings.SERVER_BALANCE_ACCOUNT));

        outFile.set("supports.chest-shop", plugin.getBoolean(Booleans.CHEST_SHOP_SUPPORT));

        outFile.set("supports.cs-amount-removed", plugin.getInteger(Integers.CS_AMOUNT_REMOVED));
        outFile.set("supports.cs-clear-schedule-time", plugin.getInteger(Integers.CS_CLEAR_SCHEDULE_TIME));
        outFile.set("supports.cs-clear-tick", plugin.getInteger(Integers.CS_CLEAR_TICK));

        outFile.set("general.start", plugin.getDouble(Doubles.START_MONEY));
        outFile.set("command.back-cost", plugin.getDouble(Doubles.BACK_COST));
        outFile.set("command.nick-cost", plugin.getDouble(Doubles.NICK_COST));

        outFile.set("general.blacklisted-baltop", plugin.getStringList(Lists.BLACKLISTED_BALANCE_TOP));

        for (final Material material : Material.values()) {
            chestShopBuyRoof[material.ordinal()] = chestShopBuyRoofNew[material.ordinal()];
            chestShopSellRoof[material.ordinal()] = chestShopSellRoofNew[material.ordinal()];

            outFile.set("chest-shop." + material.name() + ".buy", chestShopBuyRoof[material.ordinal()]);
            outFile.set("chest-shop." + material.name() + ".sell", chestShopSellRoof[material.ordinal()]);
        }

        try {
            outFile.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");
            outFile.save(Constants.ECONOMY_FILE_PATH);
        } catch (IOException exception) {
            plugin.logError("Impossível de criar arquivos em " + Constants.DATA_LAYER_FOLDER_PATH, 3);
        }
    }

}