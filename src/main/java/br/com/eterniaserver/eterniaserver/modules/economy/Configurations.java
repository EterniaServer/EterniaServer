package br.com.eterniaserver.eterniaserver.modules.economy;

import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

final class Configurations {

    private Configurations() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class EconomyConfiguration implements ReloadableConfiguration {

        private final EterniaServer plugin;

        private final CommandLocale[] commandsLocalesArray;

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        public EconomyConfiguration(EterniaServer plugin) {
            this.commandsLocalesArray = new CommandLocale[Enums.Commands.values().length];
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();
            this.plugin = plugin;
        }

        @Override
        public FileConfiguration inFileConfiguration() {
            return inFile;
        }

        @Override
        public FileConfiguration outFileConfiguration() {
            return outFile;
        }

        @Override
        public String getFolderPath() {
            return Constants.ECONOMY_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.ECONOMY_CONFIG_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return plugin.messages();
        }

        @Override
        public CommandLocale[] commandsLocale() {
            return commandsLocalesArray;
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.GENERIC;
        }

        @Override
        public void executeConfig() {
            String[] strings = plugin.strings();
            int[] integers = plugin.integers();
            boolean[] booleans = plugin.booleans();
            double[] doubles = plugin.doubles();

            integers[Integers.ECONOMY_COIN_DIGITS.ordinal()] = inFile.getInt("eco.digits", 2);

            booleans[Booleans.ECONOMY_HAS_BANK.ordinal()] = inFile.getBoolean("bank.enable", true);

            doubles[Doubles.ECO_START_MONEY.ordinal()] = inFile.getDouble("eco.start-money", 100D);

            strings[Strings.ECO_NAME.ordinal()] = inFile.getString("label.name", "EterniaEco");
            strings[Strings.ECO_LANGUAGE.ordinal()] = inFile.getString("eco.language", "pt");
            strings[Strings.ECO_COUNTRY.ordinal()] = inFile.getString("eco.country", "BR");
            strings[Strings.ECO_COIN_NAME.ordinal()] = inFile.getString("label.coin.s-name", "EterniaCoin");
            strings[Strings.ECO_COIN_PLURAL_NAME.ordinal()] = inFile.getString("label.coin.p-name", "EterniaCoins");
            strings[Strings.ECO_TABLE_NAME_BALANCE.ordinal()] = inFile.getString("critical-config.table-name.balance", "e_eco_balance");
            strings[Strings.ECO_TABLE_NAME_BANK.ordinal()] = inFile.getString("critical-config.table-name.bank", "e_eco_bank");
            strings[Strings.ECO_TABLE_NAME_BANK_MEMBER.ordinal()] = inFile.getString("critical-config.table-name.bank_member", "e_eco_bank_member");

            outFile.set("eco.digits", integers[Integers.ECONOMY_COIN_DIGITS.ordinal()]);

            outFile.set("bank.enable", booleans[Booleans.ECONOMY_HAS_BANK.ordinal()]);

            outFile.set("eco.start-money", doubles[Doubles.ECO_START_MONEY.ordinal()]);

            outFile.set("label.name", strings[Strings.ECO_NAME.ordinal()]);
            outFile.set("eco.language", strings[Strings.ECO_LANGUAGE.ordinal()]);
            outFile.set("eco.country", strings[Strings.ECO_COUNTRY.ordinal()]);
            outFile.set("label.coin.s-name", strings[Strings.ECO_COIN_NAME.ordinal()]);
            outFile.set("label.coin.p-name", strings[Strings.ECO_COIN_PLURAL_NAME.ordinal()]);
            outFile.set("critical-config.table-name.balance", strings[Strings.ECO_TABLE_NAME_BALANCE.ordinal()]);
            outFile.set("critical-config.table-name.bank", strings[Strings.ECO_TABLE_NAME_BANK.ordinal()]);
            outFile.set("critical-config.table-name.bank_member", strings[Strings.ECO_TABLE_NAME_BANK_MEMBER.ordinal()]);

        }

        @Override
        public void executeCritical() {

        }
    }

}
