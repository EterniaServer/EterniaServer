package br.com.eterniaserver.eterniaserver.modules.item;

import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

final class Configurations {

    private Configurations() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class ItemConfiguration implements ReloadableConfiguration {

        private final EterniaServer plugin;

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        private final CommandLocale[] commandsLocalesArray;

        public ItemConfiguration(EterniaServer plugin) {
            this.plugin = plugin;
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();
            this.commandsLocalesArray = new CommandLocale[Enums.Commands.values().length];

            NamespacedKey[] namespacedKeys = plugin.namespacedKeys();

            namespacedKeys[ItemsKeys.TAG_RUN_IN_CONSOLE.ordinal()] = new NamespacedKey(
                    plugin, Constants.TAG_RUN_IN_CONSOLE
            );
            namespacedKeys[ItemsKeys.TAG_USAGES.ordinal()] = new NamespacedKey(
                    plugin, Constants.TAG_USAGES
            );
            namespacedKeys[ItemsKeys.TAG_RUN_COMMAND.ordinal()] = new NamespacedKey(
                    plugin, Constants.TAG_RUN_COMMAND
            );
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
            return Constants.ITEM_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.ITEM_CONFIG_FILE_PATH;
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
            addMessage(Messages.ITEM_CUSTOM_GIVE,
                    "Item customizado enviado com sucesso<color:#555555>."
            );
            addMessage(Messages.ITEM_CUSTOM_RECEIVED,
                    "Você recebeu um item customizado<color:#555555>."
            );
            addMessage(Messages.ITEM_CUSTOM_CANT_GIVE,
                    "Não foi possível enviar o item customizado<color:#555555>."
            );
            addMessage(Messages.ITEM_SET_CUSTOM,
                    "Modelo customizado definido para <color:#00aaaa>{0}<color:#555555>."
            );
            addMessage(Messages.ITEM_SET_LORE,
                    "Lore do item definida para <color:#00aaaa>{0}<color:#555555>."
            );
            addMessage(Messages.ITEM_SET_NAME,
                    "Nome do item definido para <color:#00aaaa>{0}<color:#555555>."
            );
            addMessage(Messages.ITEM_CLEAR_LORE,
                    "Lore do item limpa<color:#555555>."
            );
            addMessage(Messages.ITEM_CLEAR_NAME,
                    "Nome do item limpo<color:#555555>."
            );
            addMessage(Messages.ITEM_NOT_FOUND,
                    "Item não encontrado<color:#555555>."
            );
            addMessage(Messages.ITEM_ADD_LORE,
                    "Linha adicionada na lore do item<color:#555555>."
            );
        }

        @Override
        public void executeCritical() {
            addCommandLocale(Enums.Commands.ITEM, new CommandLocale(
                    "item",
                    "",
                    " Ajuda para o sistema de itens",
                    "eternia.item",
                    null
            ));
            addCommandLocale(Enums.Commands.ITEM_SEND_CUSTOM, new CommandLocale(
                    "sendcustom",
                    " <player> <usos> <console> <item> <linhas de comando>",
                    " Envia um item customizado para um jogador",
                    "eternia.item",
                    null
            ));
            addCommandLocale(Enums.Commands.ITEM_CUSTOM_MODEL, new CommandLocale(
                    "custommodel",
                    " <valor>",
                    " Define um valor para o modelo customizado do item",
                    "eternia.item",
                    null
            ));
            addCommandLocale(Enums.Commands.ITEM_CLEAR, new CommandLocale(
                    "clear",
                    "",
                    " Ajuda para o sistema de limpar item",
                    "eternia.item",
                    null
            ));
            addCommandLocale(Enums.Commands.ITEM_CLEAR_LORE, new CommandLocale(
                    "lore",
                    "",
                    " Limpa a lore do item",
                    "eternia.item",
                    null
            ));
            addCommandLocale(Enums.Commands.ITEM_CLEAR_NAME, new CommandLocale(
                    "name",
                    "",
                    " Limpa o nome do item",
                    "eternia.item",
                    null
            ));
            addCommandLocale(Enums.Commands.ITEM_ADD_LORE, new CommandLocale(
                    "addlore",
                    " <linha>",
                    " Adiciona uma linha na lore do item",
                    "eternia.item",
                    null
            ));
            addCommandLocale(Enums.Commands.ITEM_SET, new CommandLocale(
                    "set",
                    "",
                    " Ajuda para o sistema de set de item",
                    "eternia.item",
                    null
            ));
            addCommandLocale(Enums.Commands.ITEM_SET_LORE, new CommandLocale(
                    "lore",
                    " <linha>",
                    " Define a lore do item",
                    "eternia.item",
                    null
            ));
            addCommandLocale(Enums.Commands.ITEM_SET_NAME, new CommandLocale(
                    "name",
                    " <nome>",
                    " Define o nome do item",
                    "eternia.item",
                    null
            ));
        }
    }

}
