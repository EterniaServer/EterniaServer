package br.com.eterniaserver.eterniaserver.modules.kit;

import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class Configurations {

    private Configurations() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class ItemConfiguration implements ReloadableConfiguration {

        private static final String KIT_PREFIX = "kits.";

        private final EterniaServer plugin;
        private final Services.KitService kitService;

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;
        private final CommandLocale[] commandsLocalesArray;

        public ItemConfiguration(EterniaServer plugin, Services.KitService kitService) {
            this.plugin = plugin;
            this.kitService = kitService;

            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();
            this.commandsLocalesArray = new CommandLocale[Enums.Commands.values().length];
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
            return Constants.KIT_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.KIT_CONFIG_FILE_PATH;
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
            addMessage(Messages.KIT_LIST,
                    "Lista de kits<color:#555555>: <color:#00aaaa>{0}<color:#555555>.",
                    "lista de kits"
            );
            addMessage(Messages.KIT_NOT_FOUND,
                    "O kit <color:#00aaaa>{0}<color:#AAAAAA> não foi encontrado<color:#555555>.",
                    "kit"
            );
            addMessage(Messages.KIT_IN_RECHARGE,
                    "Você precisa esperar <color:#00aaaa>{0}s<color:#AAAAAA> para pegar o kit novamente<color:#555555>.",
                    "cooldown"
            );

            Map<String, Utils.CustomKit> kitList = kitService.kitList();
            String[] strings = plugin.strings();

            kitList.clear();
            kitList.put(
                    "pa",
                    new Utils.CustomKit(
                            300,
                            List.of("give %player_name% minecraft:golden_shovel 1"),
                            List.of("<color:#555555>[<color:#34eb40>E<color:#3471eb>S<color:#555555>]<color:#AAAAAA> Toma sua pá<color:#555555>!")
                    )
            );

            Map<String, Utils.CustomKit> tempKitList = new HashMap<>();

            ConfigurationSection configurationSection = inFile.getConfigurationSection("kits");
            if (configurationSection != null) {
                for (String key : configurationSection.getKeys(false)) {
                    tempKitList.put(
                            key,
                            new Utils.CustomKit(
                                    inFile.getInt(KIT_PREFIX + key + ".delay"),
                                    inFile.getStringList(KIT_PREFIX + key + ".command"),
                                    inFile.getStringList(KIT_PREFIX + key + ".text")
                            )
                    );
                }
            }

            strings[Strings.PERM_KIT_PREFIX.ordinal()] = inFile.getString("permissions.kit-prefix", "eternia.kit.");
            strings[Strings.KIT_NAME_DISPLAY.ordinal()] = inFile.getString("messages.kit-name-display", "<color:#00aaaa>{0}<color:#AAAAAA>, ");
            strings[Strings.KIT_TABLE_NAME_TIME.ordinal()] = inFile.getString("database.table-name.time", "e_kit_time");

            outFile.set("permissions.kit-prefix", strings[Strings.PERM_KIT_PREFIX.ordinal()]);
            outFile.set("messages.kit-name-display", strings[Strings.KIT_NAME_DISPLAY.ordinal()]);
            outFile.set("database.table-name.time", strings[Strings.KIT_TABLE_NAME_TIME.ordinal()]);

            if (tempKitList.isEmpty()) {
                tempKitList = new HashMap<>(kitList);
            }

            kitList.clear();
            kitList.putAll(tempKitList);

            tempKitList.forEach((k, v) -> {
                kitService.kitNames().add(k);
                outFile.set(KIT_PREFIX + k + ".delay", v.getDelay());
                outFile.set(KIT_PREFIX + k + ".command", v.getCommands());
                outFile.set(KIT_PREFIX + k + ".text", v.getMessages());
            });
        }

        @Override
        public void executeCritical() {
            addCommandLocale(Enums.Commands.KIT, new CommandLocale(
                    "kit",
                    " <kit>",
                    " Use um kit",
                    "eternia.kit.user",
                    null
            ));
            addCommandLocale(Enums.Commands.KITS, new CommandLocale(
                    "kits",
                    "",
                    " Veja a lista de kits",
                    "eternia.kit.user",
                    null
            ));
        }
    }


}
