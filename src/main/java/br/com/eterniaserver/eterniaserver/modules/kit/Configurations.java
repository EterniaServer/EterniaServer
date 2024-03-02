package br.com.eterniaserver.eterniaserver.modules.kit;

import br.com.eterniaserver.eternialib.chat.MessageMap;
import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eternialib.configuration.interfaces.CmdConfiguration;
import br.com.eterniaserver.eternialib.configuration.interfaces.MsgConfiguration;
import br.com.eterniaserver.eternialib.configuration.interfaces.ReloadableConfiguration;
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

    static class KitMessagesConfiguration implements MsgConfiguration<Messages> {

        private final FileConfiguration inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
        private final FileConfiguration outFile = new YamlConfiguration();

        private final MessageMap<Messages, String> messageMap;

        public KitMessagesConfiguration(MessageMap<Messages, String> messageMap) {
            this.messageMap = messageMap;
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
            return Constants.KIT_MESSAGES_FILE_PATH;
        }

        @Override
        public MessageMap<Messages, String> messages() {
            return messageMap;
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.GENERIC;
        }

        @Override
        public void executeConfig() {
            addMessage(Messages.KIT_LIST,
                    "Lista de kits#555555: #00aaaa{0}#555555.",
                    "lista de kits"
            );
            addMessage(Messages.KIT_NOT_FOUND,
                    "O kit #00aaaa{0}#AAAAAA não foi encontrado#555555.",
                    "kit"
            );
            addMessage(Messages.KIT_IN_RECHARGE,
                    "Você precisa esperar #00aaaa{0}s#AAAAAA para pegar o kit novamente#555555.",
                    "cooldown"
            );
        }

        @Override
        public void executeCritical() { }
    }

    static class KitCommandsConfiguration implements CmdConfiguration<Enums.Commands> {

        private final FileConfiguration inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
        private final FileConfiguration outFile = new YamlConfiguration();

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
            return Constants.KIT_COMMANDS_FILE_PATH;
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.BLOCKED;
        }

        @Override
        public void executeConfig() { }

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

    static class KitConfiguration implements ReloadableConfiguration {

        private static final String KIT_PREFIX = "kits.";

        private final FileConfiguration inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
        private final FileConfiguration outFile = new YamlConfiguration();

        private final EterniaServer plugin;
        private final Services.KitService kitService;

        public KitConfiguration(EterniaServer plugin, Services.KitService kitService) {
            this.plugin = plugin;
            this.kitService = kitService;
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
        public ConfigurationCategory category() {
            return ConfigurationCategory.GENERIC;
        }

        @Override
        public void executeConfig() {
            Map<String, Utils.CustomKit> kitList = kitService.kitList();
            String[] strings = plugin.strings();

            kitList.clear();
            kitList.put(
                    "pa",
                    new Utils.CustomKit(
                            300,
                            List.of("give %player_name% minecraft:golden_shovel 1"),
                            List.of("#555555[#34eb40E#3471ebS#555555]#AAAAAA Toma sua pá#555555!")
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
            strings[Strings.KIT_TABLE_NAME_TIME.ordinal()] = inFile.getString("database.table-name.time", "e_kit_time");

            outFile.set("permissions.kit-prefix", strings[Strings.PERM_KIT_PREFIX.ordinal()]);
            outFile.set("database.table-name.time", strings[Strings.KIT_TABLE_NAME_TIME.ordinal()]);

            if (tempKitList.isEmpty()) {
                tempKitList = new HashMap<>(kitList);
            }

            kitList.clear();
            kitList.putAll(tempKitList);

            tempKitList.forEach((k, v) -> {
                kitService.kitNames().add(k);
                outFile.set(KIT_PREFIX + k + ".delay", v.delay());
                outFile.set(KIT_PREFIX + k + ".command", v.commands());
                outFile.set(KIT_PREFIX + k + ".text", v.messages());
            });
        }

        @Override
        public void executeCritical() { }
    }


}
