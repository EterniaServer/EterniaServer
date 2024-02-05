package br.com.eterniaserver.eterniaserver.modules.teleport;

import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

final class Configurations {

    private Configurations() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class TeleportConfiguration implements ReloadableConfiguration {

        private final EterniaServer plugin;

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;
        private final CommandLocale[] commandsLocalesArray;

        public TeleportConfiguration(EterniaServer plugin) {
            this.plugin = plugin;

            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();
            this.commandsLocalesArray = new CommandLocale[Enums.Commands.values().length];

            NamespacedKey[] namespaceKeys = plugin.namespacedKeys();

            namespaceKeys[ItemsKeys.TAG_WORLD.ordinal()] = new NamespacedKey(plugin, Constants.TAG_WORLD);
            namespaceKeys[ItemsKeys.TAG_COORD_X.ordinal()] = new NamespacedKey(plugin, Constants.TAG_COORD_X);
            namespaceKeys[ItemsKeys.TAG_COORD_Y.ordinal()] = new NamespacedKey(plugin, Constants.TAG_COORD_Y);
            namespaceKeys[ItemsKeys.TAG_COORD_Z.ordinal()] = new NamespacedKey(plugin, Constants.TAG_COORD_Z);
            namespaceKeys[ItemsKeys.TAG_COORD_YAW.ordinal()] = new NamespacedKey(plugin, Constants.TAG_COORD_YAW);
            namespaceKeys[ItemsKeys.TAG_COORD_PITCH.ordinal()] = new NamespacedKey(plugin, Constants.TAG_COORD_PITCH);
            namespaceKeys[ItemsKeys.TAG_LOC_NAME.ordinal()] = new NamespacedKey(plugin, Constants.TAG_LOC_NAME);
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
            return Constants.TELEPORT_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.TELEPORT_CONFIG_FILE_PATH;
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
            addMessage(Messages.TELEPORT_ON_GOING,
                    "Teleportando para <color:#00aaaa>{0}",
                    "local"
            );
            addMessage(Messages.HOME_DELETED,
                    "Home <color:#00aaaa>{0}<color:#aaaaaa> deletada com sucesso<color:#555555>.",
                    "home"
            );
            addMessage(Messages.HOME_NOT_FOUND,
                    "Home <color:#00aaaa>{0}<color:#aaaaaa> não encontrada<color:#555555>.",
                    "home"
            );
            addMessage(Messages.HOME_LIST,
                    "Lista de homes<color:#555555>: <color:#00aaaa>{0}<color:#555555>.",
                    "lista de homes"
            );
            addMessage(Messages.HOME_STRING_LIMIT,
                    "O nome da home não pode ter mais de <color:#00aaaa>{0}<color:#aaaaaa> caracteres<color:#555555>.",
                    "limite de caracteres"
            );
            addMessage(Messages.HOME_CREATED,
                    "Home <color:#00aaaa>{0}<color:#aaaaaa> criada com sucesso<color:#555555>.",
                    "home"
            );
            addMessage(Messages.HOME_NO_PERM_TO_COMPASS,
                    "Você não tem permissão para criar homes extras com bússolas<color:#555555>."
            );
            addMessage(Messages.HOME_ITEM_NAME,
                    "<color:#555555>[<color:#00aaaa>{0}<color:#555555>]",
                    "home"
            );
            addMessage(Messages.HOME_LIMIT_REACHED,
                    "Você atingiu o limite de homes<color:#555555>."
            );
            addMessage(Messages.TPA_REQUESTED_TO,
                    "Solicitação de teleporte para <color:#00aaaa>{1}<color:#aaaaaa> enviada<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.TPA_REQUESTED_FROM,
                    "Solicitação de teleporte de <color:#00aaaa>{1}<color:#aaaaaa> recebida<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.TPA_HERE_REQUESTED_TO,
                    "Solicitação de teleporte de <color:#00aaaa>{1}<color:#aaaaaa> até você enviada<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.TPA_HERE_REQUESTED_FROM,
                    "Solicitação de teleporte para <color:#00aaaa>{1}<color:#aaaaaa> recebida<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );

            String[] strings = plugin.strings();
            int[] integers = plugin.integers();

            strings[Strings.TELEPORT_TABLE_NAME_HOME.ordinal()] = inFile.getString("database.table_name_home", "e_home_location");
            strings[Strings.PERM_TELEPORT_TIME_BYPASS.ordinal()] = inFile.getString("permissions.teleport_time_bypass", "eternia.teleport.time.bypass");
            strings[Strings.PERM_HOME_OTHER.ordinal()] = inFile.getString("permissions.home_others", "eternia.home.others");
            strings[Strings.PERM_HOME_COMPASS.ordinal()] = inFile.getString("permissions.home_compass", "eternia.home.compass");
            strings[Strings.PERM_SETHOME_LIMIT_PREFIX.ordinal()] = inFile.getString("permissions.sethome_limit_prefix", "eternia.sethome.limit.");

            integers[Integers.TELEPORT_TIMER.ordinal()] = inFile.getInt("teleport_timer", 5);

            outFile.set("database.table_name_home", strings[Strings.TELEPORT_TABLE_NAME_HOME.ordinal()]);
            outFile.set("permissions.teleport_time_bypass", strings[Strings.PERM_TELEPORT_TIME_BYPASS.ordinal()]);
            outFile.set("permissions.home_others", strings[Strings.PERM_HOME_OTHER.ordinal()]);
            outFile.set("permissions.home_compass", strings[Strings.PERM_HOME_COMPASS.ordinal()]);
            outFile.set("permissions.sethome_limit_prefix", strings[Strings.PERM_SETHOME_LIMIT_PREFIX.ordinal()]);

            outFile.set("teleport_timer", integers[Integers.TELEPORT_TIMER.ordinal()]);
        }

        @Override
        public void executeCritical() {
            addCommandLocale(Enums.Commands.HOME, new CommandLocale(
                    "home",
                    " <nome>",
                    " Teleporta para uma home",
                    "eternia.home.user",
                    null
            ));
            addCommandLocale(Enums.Commands.HOMES, new CommandLocale(
                    "homes",
                    null,
                    " Veja a lista de suas homes",
                    "eternia.home.user",
                    null
            ));
            addCommandLocale(Enums.Commands.SETHOME, new CommandLocale(
                    "sethome",
                    " <nome>",
                    " Define uma home",
                    "eternia.sethome.user",
                    null
            ));
            addCommandLocale(Enums.Commands.DELHOME, new CommandLocale(
                    "delhome",
                    " <nome>",
                    " Deleta uma home",
                    "eternia.delhome.user",
                    null
            ));
            addCommandLocale(Enums.Commands.TPA, new CommandLocale(
                    "tpa",
                    " <jogador>",
                    " Solicita um teleporte até um jogador",
                    "eternia.tpa",
                    null
            ));
            addCommandLocale(Enums.Commands.TPAHERE, new CommandLocale(
                    "tpahere",
                    " <jogador>",
                    " Solicita que um jogador se teleporte até você",
                    "eternia.tpahere",
                    null
            ));
            addCommandLocale(Enums.Commands.TPALL, new CommandLocale(
                    "tpall",
                    null,
                    " Teleporta todos os jogadores para você",
                    "eternia.tpall",
                    null
            ));
        }
    }

}
