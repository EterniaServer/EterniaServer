package br.com.eterniaserver.eterniaserver.modules.cash;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.CommandsCfg;
import br.com.eterniaserver.eterniaserver.api.interfaces.FileCfg;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;

import br.com.eterniaserver.eterniaserver.objects.CommandI18n;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


final class Configurations {

    static class CommandsLocales implements CommandsCfg {

        private final FileConfiguration inFileConfiguration;
        private final FileConfiguration outFileConfiguration;

        private final CommandI18n[] commandsLocalesArray = new CommandI18n[Enums.Commands.values().length];

        protected CommandsLocales() {
            this.inFileConfiguration = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFileConfiguration = new YamlConfiguration();

            commandsLocalesArray[Enums.Commands.CASH.ordinal()] = new CommandI18n(
                    Enums.Commands.CASH.name(),
                    "cash",
                    "",
                    " Abre a GUI da loja de Cash",
                    "eternia.cash"
            );
            commandsLocalesArray[Enums.Commands.CASH_HELP.ordinal()] = new CommandI18n(
                    Enums.Commands.CASH_HELP.name(),
                    "help|ajuda",
                    " <página>",
                    " Ajuda para o sistema de Cash",
                    "eternia.cash"
            );
            commandsLocalesArray[Enums.Commands.CASH_PAY.ordinal()] = new CommandI18n(
                    Enums.Commands.CASH_PAY.name(),
                    "pay|pagar",
                    " <jogador> <quantia>",
                    " Paga uma quantia de cash a um jogador",
                    "eternia.cash"
            );
            commandsLocalesArray[Enums.Commands.CASH_ACCEPT.ordinal()] = new CommandI18n(
                    Enums.Commands.CASH_ACCEPT.name(),
                    "accept|aceitar",
                    "",
                    " Aceita uma compra da loja de cash",
                    "eternia.cash"
            );
            commandsLocalesArray[Enums.Commands.CASH_DENY.ordinal()] = new CommandI18n(
                    Enums.Commands.CASH_DENY.name(),
                    "deny|negar",
                    "",
                    " Recusa uma compra da loja de cash",
                    "eternia.cash"
            );
            commandsLocalesArray[Enums.Commands.CASH_BALANCE.ordinal()] = new CommandI18n(
                    Enums.Commands.CASH_BALANCE.name(),
                    "balance|saldo",
                    " <jogador>",
                    " Mostra o saldo atual de cash de um jogador",
                    "eternia.cash"
            );
            commandsLocalesArray[Enums.Commands.CASH_GIVE.ordinal()] = new CommandI18n(
                    Enums.Commands.CASH_GIVE.name(),
                    "give|dar",
                    " <jogador> <quantia>",
                    " Dá uma quantia de cash a um jogador",
                    "eternia.cash.admin"
            );
            commandsLocalesArray[Enums.Commands.CASH_REMOVE.ordinal()] = new CommandI18n(
                    Enums.Commands.CASH_REMOVE.name(),
                    "remove|retirar",
                    " <jogador> <quantia>",
                    " Remove uma quantia de cash de um jogador",
                    "eternia.cash.admin"
            );


            syncToFile();
            saveConfiguration(true);
        }

        @Override
        public CommandI18n[] getCommandsLocalesArray() {
            return commandsLocalesArray;
        }

        @Override
        public FileConfiguration inFileConfiguration() {
            return inFileConfiguration;
        }

        @Override
        public FileConfiguration outFileConfiguration() {
            return outFileConfiguration;
        }

        @Override
        public String getFolderPath() {
            return Constants.CASH_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.CASH_COMMAND_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return null;
        }
    }

    static class Configs implements FileCfg {

        private record PreGUI(String title, ItemStack[] items, int subGUIs) {}

        private final EterniaServer plugin;

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        protected Configs(final EterniaServer plugin) {
            this.plugin = plugin;
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();

            final String[] strings = plugin.strings();

            strings[Strings.CASH_MENU_TITLE.ordinal()] = this.inFile.getString("menu.name.value", "<color:#aaaaaa>Cash<color:%s>");

            PreGUI menu = this.loadMenuFromFile();
            menu = menu == null ? this.loadDefaultMenu() : menu;
            PreGUI[] guis = this.loadGUIsFromFile(menu.subGUIs());
            guis = guis == null ? this.loadDefaultGUIs() : guis;

            this.outFile.set("menu.name.value", strings[Strings.CASH_MENU_TITLE.ordinal()]);
            this.outFile.set("menu.name.info-pt", "Não altere o final (<color:%s>)");
            this.outFile.set("menu.name.info-en", "Don't change the end (<color:%s>)");
            this.outFile.set("menu.size", menu.items().length);
            for (int i = 0; i < menu.items().length; i++) {
                final ItemStack itemStack = menu.items()[i];
                if (itemStack != null) {
                    final ItemMeta itemMeta = itemStack.getItemMeta();
                    final String guiName = itemMeta.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CASH_GUI_NAME), PersistentDataType.STRING);
                    final String itemLore = itemMeta.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CASH_ITEM_LORE), PersistentDataType.STRING);

                    this.outFile.set("menu." + i + ".material", itemStack.getType().name());
                    this.outFile.set("menu." + i + ".name", guiName);
                    this.outFile.set("menu." + i + ".lore", Arrays.asList(itemLore.split(";")));
                }
            }

            for (PreGUI preGUI : guis) {
                if (preGUI != null) {
                    String name = preGUI.title();
                    String entry = name.split(" ")[0].substring(15);
                    ItemStack[] itemStacks = preGUI.items();
                    int size = preGUI.items().length;

                    this.outFile.set("guis." + entry + ".size", size);
                    this.outFile.set("guis." + entry + ".title", name);

                    for (int i = 0; i < size; i++) {
                        final ItemStack itemStack = itemStacks[i];
                        if (itemStack != null) {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            String guiName = itemMeta.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CASH_GUI_NAME), PersistentDataType.STRING);
                            String itemLore = itemMeta.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CASH_ITEM_LORE), PersistentDataType.STRING);
                            String commands = itemMeta.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CASH_ITEM_COMMANDS), PersistentDataType.STRING);
                            Integer cost = itemMeta.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CASH_ITEM_COST), PersistentDataType.INTEGER);
                            String messages = itemMeta.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CASH_ITEM_MESSAGE), PersistentDataType.STRING);

                            this.outFile.set("guis." + entry + "." + i + ".cost", cost);
                            this.outFile.set("guis." + entry + "." + i + ".material", itemStack.getType().name());
                            this.outFile.set("guis." + entry + "." + i + ".name", guiName);
                            this.outFile.set("guis." + entry + "." + i + ".commands", Arrays.asList(commands.split(";")));
                            this.outFile.set("guis." + entry + "." + i + ".lore", Arrays.asList(itemLore.split(";")));
                            this.outFile.set("guis." + entry + "." + i + ".messages", Arrays.asList(messages.split(";")));
                        }
                    }
                }
            }

            for (int i = 0; i < menu.items().length; i++) {
                if (menu.items()[i] == null) {
                    menu.items()[i] = getGlass();
                }
            }
            EterniaServer.getGuiAPI().createGUI(menu.title(), menu.items());
            for (PreGUI preGUI : guis) {
                if (preGUI != null) {
                    for (int i = 0; i < preGUI.items().length; i++) {
                        if (preGUI.items()[i] == null) {
                            preGUI.items()[i] = getGlass();
                        }
                    }
                    EterniaServer.getGuiAPI().createGUI(preGUI.title(), preGUI.items());
                }
            }

            saveConfiguration(true);
        }

        private PreGUI loadMenuFromFile() {
            final int menuSize = this.inFile.getInt("menu.size", 0);
            final ItemStack[] itemStacks = new ItemStack[menuSize];

            if (menuSize == 0) {
                return null;
            }

            int subGUIs = 0;
            for (int i = 0; i < menuSize; i++) {
                if (this.inFile.contains("menu." + i)) {
                    ItemStack itemStack = new ItemStack(Material.valueOf(this.inFile.getString("menu." + i + ".material")));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    String guiName = this.inFile.getString("menu." + i + ".name", "");
                    List<String> itemLore = this.inFile.getStringList("menu." + i + ".lore");

                    itemMeta.displayName(this.plugin.parseColor(guiName));
                    itemMeta.lore(itemLore.stream().map(this.plugin::parseColor).collect(Collectors.toList()));

                    itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CASH_ITEM_LORE), PersistentDataType.STRING, String.join(";", itemLore));
                    itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CASH_GUI_NAME), PersistentDataType.STRING, guiName);

                    itemStack.setItemMeta(itemMeta);
                    itemStacks[i] = itemStack;
                    ++subGUIs;
                }
            }

            return subGUIs == 0 ? null : new PreGUI(plugin.getString(Strings.CASH_MENU_TITLE), itemStacks, subGUIs);
        }

        private PreGUI[] loadGUIsFromFile(int guiAmount) {
            final PreGUI[] preGUIS = new PreGUI[guiAmount];
            ConfigurationSection section = this.inFile.getConfigurationSection("guis");

            if (section == null) {
                return null;
            }

            int guis = 0;
            for (String guiName : section.getKeys(false)) {
                final int guiSize = this.inFile.getInt("guis." + guiName + ".size", 0);
                final String guiTitle = this.inFile.getString("guis." + guiName + ".title", "");
                final ItemStack[] itemStacks = new ItemStack[guiSize];

                if (guiSize == 0) {
                    return null;
                }

                for (int i = 0; i < guiSize; i++) {
                    if (this.inFile.contains("guis." + guiName + "." + i)) {
                        ItemStack itemStack = new ItemStack(Material.valueOf(this.inFile.getString("guis." + guiName + "." + i + ".material")));
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        List<String> itemLore = this.inFile.getStringList("guis." + guiName + "." + i + ".lore");
                        String itemName = this.inFile.getString("guis." + guiName + "." + i + ".name", "");

                        itemMeta.displayName(this.plugin.parseColor(itemName));
                        itemMeta.lore(itemLore.stream().map(this.plugin::parseColor).collect(Collectors.toList()));

                        itemMeta.getPersistentDataContainer().set(
                            plugin.getKey(ItemsKeys.CASH_GUI_NAME),
                            PersistentDataType.STRING,
                            itemName
                        );
                        itemMeta.getPersistentDataContainer().set(
                            plugin.getKey(ItemsKeys.CASH_ITEM_LORE),
                            PersistentDataType.STRING,
                            String.join(
                                ";",
                                itemLore
                            )
                        );
                        itemMeta.getPersistentDataContainer().set(
                            plugin.getKey(ItemsKeys.CASH_ITEM_COST),
                            PersistentDataType.INTEGER,
                            this.inFile.getInt("guis." + guiName + "." + i + ".cost", 0)
                        );
                        itemMeta.getPersistentDataContainer().set(
                            plugin.getKey(ItemsKeys.CASH_ITEM_COMMANDS),
                            PersistentDataType.STRING,
                            String.join(
                                ";",
                                this.inFile.getStringList("guis." + guiName + "." + i + ".commands")
                            )
                        );
                        itemMeta.getPersistentDataContainer().set(
                            plugin.getKey(ItemsKeys.CASH_ITEM_MESSAGE),
                            PersistentDataType.STRING,
                            String.join(
                                ";",
                                this.inFile.getStringList("guis." + guiName + "." + i + ".messages")
                            )
                        );

                        itemStack.setItemMeta(itemMeta);
                        itemStacks[i] = itemStack;
                    }
                }

                preGUIS[guis++] = new PreGUI(guiTitle, itemStacks, 0);
            }

            return preGUIS;
        }

        private PreGUI loadDefaultMenu() {
            final ItemStack[] itemStacks = new ItemStack[27];
            final ItemStack itemStack = new ItemStack(Material.OAK_SIGN);
            final ItemMeta itemMeta = itemStack.getItemMeta();
            final List<String> itemLore = List.of(
                "<color:#aaaaaa>Compre permissões",
                "<color:#aaaaaa>Para lhe ajudar",
                "<color:#aaaaaa>nessa jornada<color:#555555>."
            );

            itemMeta.displayName(this.plugin.parseColor("<color:#aaaaaa>Permissões"));
            itemMeta.lore(itemLore.stream().map(this.plugin::parseColor).collect(Collectors.toList()));
            itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CASH_ITEM_LORE), PersistentDataType.STRING, String.join(";", itemLore));
            itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CASH_GUI_NAME), PersistentDataType.STRING, "<color:#aaaaaa>Permissões");

            itemStack.setItemMeta(itemMeta);
            itemStacks[10] = itemStack;

            return new PreGUI(plugin.getString(Strings.CASH_MENU_TITLE), itemStacks, 1);
        }

        private PreGUI[] loadDefaultGUIs() {
            final PreGUI[] preGUIS = new PreGUI[1];
            final ItemStack[] itemStacks = new ItemStack[27];
            final ItemStack itemStack = new ItemStack(Material.EXPERIENCE_BOTTLE);
            final ItemMeta itemMeta = itemStack.getItemMeta();
            final List<String> itemLore = List.of(
                "<color:#aaaaaa>Upe mais rápido em",
                "<color:#aaaaaa>sua especialidade<color:#555555>!",
                "<color:#aaaaaa>Não acumulativo<color:#555555>.",
                "",
                "<color:#aaaaaa>Duração<color:#555555>: <color:#00aaaa>30 dias",
                "<color:#aaaaaa>Preço<color:#555555>: <color:#00aaaa>70 C$"
            );

            String guiName = "<color:#00aaaa>25% <color:#aaaaaa>Bônus de XP no McMMO<color:#555555>!";
            itemMeta.displayName(this.plugin.parseColor(guiName));
            itemMeta.lore(itemLore.stream().map(this.plugin::parseColor).collect(Collectors.toList()));

            itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CASH_GUI_NAME), PersistentDataType.STRING, guiName);
            itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CASH_ITEM_LORE), PersistentDataType.STRING, String.join(";", itemLore));
            itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CASH_ITEM_COST), PersistentDataType.INTEGER, 70);
            itemMeta.getPersistentDataContainer().set(
                plugin.getKey(ItemsKeys.CASH_ITEM_COMMANDS), PersistentDataType.STRING, String.join(
                    ";",
                    List.of(
                        "lp user %player_name% permission settemp mcmmo.perks.xp.customboost.all true 30d",
                        "broadcast <color:#aaaaaa>O jogador <color:#00aaaa>%player_name% <color:aaaaaa>comprou um boost de XP<color:555555>."
                    )
                )
            );
            itemMeta.getPersistentDataContainer().set(
                plugin.getKey(ItemsKeys.CASH_ITEM_MESSAGE), PersistentDataType.STRING, String.join(
                    ";",
                    List.of(
                        "lp user %player_name% permission settemp mcmmo.perks.xp.customboost.all true 30d",
                        "broadcast <color:#aaaaaa>O jogador <color:#00aaaa>%player_name% <color:aaaaaa>comprou um boost de XP<color:555555>."
                    )
                )
            );

            itemStack.setItemMeta(itemMeta);
            itemStacks[10] = itemStack;

            preGUIS[0] = new PreGUI("<color:#aaaaaa>Permissões", itemStacks, 0);

            return preGUIS;
        }

        private ItemStack getGlass() {
            final ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            final ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CASH_ITEM_LORE), PersistentDataType.STRING, "a");
            itemMeta.displayName(this.plugin.parseColor("<color:#aaaaaa>Loja de <color:#00aaaa>C.A.S.H.<color:#555555>!"));
            itemStack.setItemMeta(itemMeta);

            return itemStack;
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
            return Constants.CASH_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.CASH_CONFIG_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return null;
        }

    }


    static class Locales implements FileCfg {

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        private final String[] messages;

        protected Locales(final EterniaServer plugin) {
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();

            this.messages = plugin.messages();

            addMessage(Messages.CASH_BALANCE,
                    "Você possui <color:#00aaaa>{0} C$<color:#555555>.",
                    "0: quantia de cash"
            );
            addMessage(Messages.CASH_BALANCE_OTHER,
                    "<color:#00aaaa>{2}<color:#aaaaaa> possui <color:#00aaaa>{0} C$<color:#555555>.",
                    "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador"
            );
            addMessage(Messages.CASH_NOTHING_TO_BUY,
                    "Você não está comprando nada<color:#555555>.",
                    ""
            );
            addMessage(Messages.CASH_BOUGHT,
                    "<Compra confirmada com sucesso<color:#555555>.",
                    ""
            );
            addMessage(Messages.CASH_CANCELED,
                    "Compra cancelada com sucesso<color:#555555>.",
                    ""
            );
            addMessage(Messages.CASH_RECEVEID,
                    "Você recebeu <color:#00aaaa>{0} C$ <color:#aaaaaa>por <color:#00aaaa>{2}<color:#555555>.",
                    "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador"
            );
            addMessage(Messages.CASH_SENT,
                    "Você enviou <color:#00aaaa>{0} C$ <color:#aaaaaa>para <color:#00aaaa>{2}<color:#555555>.",
                    "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador"
            );
            addMessage(Messages.CASH_LOST,
                    "Você perdeu <color:#00aaaa>{0} C$ <color:#aaaaaa>por <color:#00aaaa>{2}<color:#555555>.",
                    "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador"
            );
            addMessage(Messages.CASH_REMOVED,
                    "Você removeu <color:#00aaaa>{0} C$ <color:#aaaaaa>de <color:#00aaaa>{2}<color:#555555>.",
                    "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador"
            );
            addMessage(Messages.CASH_COST,
                    "Isso irá custar <color:#00aaaa>{0} C$<color:#555555>.",
                    "0: quantia de cash"
            );
            addMessage(Messages.CASH_CHOOSE,
                    "Use <color:#ffaa00>/cash accept <color:#aaaaaa>ou <color:#ffaa00>/cash deny<color:#aaaaaa> para aceitar ou negar a compra<color:#555555>.",
                    ""
            );
            addMessage(Messages.CASH_NO_HAS,
                    "Você não possui <color:#00aaaa>{0} C$<color:#555555>.",
                    "0: quantia de cash"
            );
            addMessage(Messages.CASH_ALREADY_BUYING,
                    "Você já possui uma compra em andamento<color:#555555>.",
                    ""
            );

            saveConfiguration(true);
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
            return Constants.CASH_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.CASH_MESSAGE_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return messages;
        }

    }

}
