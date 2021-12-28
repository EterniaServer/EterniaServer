package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.CommandsCfg;
import br.com.eterniaserver.eterniaserver.api.interfaces.FileCfg;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.objects.CommandI18n;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.Random;

final class Configurations {

    static class CommandsLocales implements CommandsCfg {

        private final FileConfiguration inFileConfiguration;
        private final FileConfiguration outFileConfiguration;

        private final CommandI18n[] commandsLocalesArray = new CommandI18n[Enums.Commands.values().length];

        protected CommandsLocales() {
            this.inFileConfiguration = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFileConfiguration = new YamlConfiguration();

            commandsLocalesArray[Enums.Commands.GAMEMODE.ordinal()] = new CommandI18n(
                    Enums.Commands.GAMEMODE.name(),
                    "gamemode|gm",
                    " <página>",
                    " Ajuda para o sistema de Gamemode",
                    "eternia.gamemode"
            );
            commandsLocalesArray[Enums.Commands.GAMEMODE_SURVIVAL.ordinal()] = new CommandI18n(
                    Enums.Commands.GAMEMODE_SURVIVAL.name(),
                    "survival|s|0",
                    " (opcional) <jogador>",
                    " Define o modo de jogo seu, ou de outro jogador, para Sobrevivência",
                    "eternia.gamemode"
            );
            commandsLocalesArray[Enums.Commands.GAMEMODE_CREATIVE.ordinal()] = new CommandI18n(
                    Enums.Commands.GAMEMODE_CREATIVE.name(),
                    "creative|c|1",
                    " (opcional) <jogador>",
                    " Define o modo de jogo seu, ou de outro jogador, para Criativo",
                    "eternia.gamemode"
            );
            commandsLocalesArray[Enums.Commands.GAMEMODE_ADVENTURE.ordinal()] = new CommandI18n(
                    Enums.Commands.GAMEMODE_ADVENTURE.name(),
                    "adventure|a|2",
                    " (opcional) <jogador>",
                    " Define o modo de jogo seu, ou de outro jogador, para Aventura",
                    "eternia.gamemode"
            );
            commandsLocalesArray[Enums.Commands.GAMEMODE_SPECTATOR.ordinal()] = new CommandI18n(
                    Enums.Commands.GAMEMODE_SPECTATOR.name(),
                    "spectator|spec|3",
                    " (opcional) <jogador>",
                    " Define o modo de jogo seu, ou de outro jogador, para Espectador",
                    "eternia.gamemode"
            );
            commandsLocalesArray[Enums.Commands.AFK.ordinal()] = new CommandI18n(
                    Enums.Commands.AFK.name(),
                    "afk",
                    "",
                    " Fique ausente",
                    "eternia.afk"
            );
            commandsLocalesArray[Enums.Commands.GODMODE.ordinal()] = new CommandI18n(
                    Enums.Commands.GODMODE.name(),
                    "god",
                    " (opcional) <jogador>",
                    " Ative ou desative seu God Mode, ou o de outro jogador",
                    "eternia.god"
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
            return Constants.DATA_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.CORE_COMMAND_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return null;
        }
    }

    static class Configs implements FileCfg {

        private final EterniaServer plugin;

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        protected Configs(final EterniaServer plugin) {
            this.plugin = plugin;
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();

            this.loadNamespacedKeys();

            final Random random = new Random();

            final boolean[] booleans = plugin.booleans();
            final int[] integers = plugin.integers();
            final String[] strings = plugin.strings();
            final List<List<String>> stringLists = plugin.stringLists();

            // Booleans
            booleans[Booleans.MODULE_SPAWNERS.ordinal()] = inFile.getBoolean("modules.spawners", true);
            booleans[Booleans.MODULE_EXPERIENCE.ordinal()] = inFile.getBoolean("modules.experience", true);
            booleans[Booleans.MODULE_ELEVATOR.ordinal()] = inFile.getBoolean("modules.elevator", true);
            booleans[Booleans.MODULE_REWARDS.ordinal()] = inFile.getBoolean("modules.rewards", true);
            booleans[Booleans.MODULE_GLOW.ordinal()] = inFile.getBoolean("modules.glow", true);
            booleans[Booleans.MODULE_PAPI.ordinal()] = inFile.getBoolean("modules.papi", true);
            booleans[Booleans.MODULE_CASH.ordinal()] = inFile.getBoolean("modules.cash", true);
            booleans[Booleans.AFK_KICK.ordinal()] = inFile.getBoolean("afk.kick-if-no-perm", true);
            // Integers
            integers[Integers.PLUGIN_TICKS.ordinal()] = inFile.getInt("critical-configs.plugin-ticks", 20);
            integers[Integers.AFK_TIMER.ordinal()] = inFile.getInt("afk.limit-time", 900);
            integers[Integers.COOLDOWN.ordinal()] = inFile.getInt("critical-configs.teleport-ticks", 80);
            // Strings
            strings[Strings.DATA_FORMAT.ordinal()] = inFile.getString("format.data-time", "dd/MM/yyyy HH:mm");
            strings[Strings.MINI_MESSAGES_SERVER_SERVER_LIST.ordinal()] = inFile.getString("mini-messages.motd", "            <color:#69CEDB>⛏ <gradient:#111111:#112222>❱---❰</gradient> <gradient:#6FE657:#6892F2>EterniaServer</gradient> <gradient:#112222:#111111>❱---❰</gradient> <color:#69CEDB>⛏\n                     <gradient:#926CEB:#6892F2>MOUNTAIN UPDATE</gradient>");
            strings[Strings.PERM_AFK.ordinal()] = inFile.getString("afk.perm-to-stay-afk", "eternia.afk");
            strings[Strings.PERM_TIMING_BYPASS.ordinal()] = inFile.getString("teleport.timing-bypass", "eternia.timing.bypass");
            strings[Strings.GUI_SECRET.ordinal()] = inFile.getString("secret.value", String.format("#%06x", random.nextInt(0xffffff + 1)));
            // Lists
            final List<String> list = inFile.getStringList("critical-configs.blocked-commands");
            stringLists.set(Lists.BLACKLISTED_COMMANDS.ordinal(), list.isEmpty() ? List.of("/op", "/deop", "/stop") : list);

            // Booleans
            outFile.set("modules.spawners", booleans[Booleans.MODULE_SPAWNERS.ordinal()]);
            outFile.set("modules.experience", booleans[Booleans.MODULE_EXPERIENCE.ordinal()]);
            outFile.set("modules.elevator", booleans[Booleans.MODULE_ELEVATOR.ordinal()]);
            outFile.set("modules.rewards", booleans[Booleans.MODULE_REWARDS.ordinal()]);
            outFile.set("modules.glow", booleans[Booleans.MODULE_GLOW.ordinal()]);
            outFile.set("modules.papi", booleans[Booleans.MODULE_PAPI.ordinal()]);
            outFile.set("modules.cash", booleans[Booleans.MODULE_CASH.ordinal()]);
            outFile.set("afk.kick-if-no-perm", booleans[Booleans.AFK_KICK.ordinal()]);
            // Integers
            outFile.set("critical-configs.plugin-ticks", integers[Integers.PLUGIN_TICKS.ordinal()]);
            outFile.set("afk.limit-time", integers[Integers.AFK_TIMER.ordinal()]);
            outFile.set("critical-configs.teleport-ticks", integers[Integers.COOLDOWN.ordinal()]);
            // Strings
            outFile.set("format.data-time", strings[Strings.DATA_FORMAT.ordinal()]);
            outFile.set("mini-messages.motd", strings[Strings.MINI_MESSAGES_SERVER_SERVER_LIST.ordinal()]);
            outFile.set("afk.perm-to-stay-afk", strings[Strings.PERM_AFK.ordinal()]);
            outFile.set("teleport.timing-bypass", strings[Strings.PERM_TIMING_BYPASS.ordinal()]);
            outFile.set("secret.value", strings[Strings.GUI_SECRET.ordinal()]);
            outFile.set("secret.info-pt", "Não exponha esse código hex");
            outFile.set("secret.info-en", "Don't expose this hex code");
            // Lists
            outFile.set("critical-configs.blocked-commands", plugin.stringLists.get(Lists.BLACKLISTED_COMMANDS.ordinal()));

            saveConfiguration(true);
        }

        private void loadNamespacedKeys() {
            final NamespacedKey[] namespacedKeys = plugin.namespacedKeys();

            namespacedKeys[ItemsKeys.TAG_FUNCTION.ordinal()] = new NamespacedKey(plugin, "eternia_function");
            namespacedKeys[ItemsKeys.TAG_INT_VALUE.ordinal()] = new NamespacedKey(plugin, "eternia_int_value");
            namespacedKeys[ItemsKeys.CASH_GUI_NAME.ordinal()] = new NamespacedKey(plugin, "cash_gui_name");
            namespacedKeys[ItemsKeys.CASH_ITEM_COST.ordinal()] = new NamespacedKey(plugin, "cash_item_cost");
            namespacedKeys[ItemsKeys.CASH_ITEM_MESSAGE.ordinal()] = new NamespacedKey(plugin, "cash_item_message");
            namespacedKeys[ItemsKeys.CASH_ITEM_COMMANDS.ordinal()] = new NamespacedKey(plugin, "cash_item_commands");
            namespacedKeys[ItemsKeys.CASH_ITEM_LORE.ordinal()] = new NamespacedKey(plugin, "cash_item_lore");
            namespacedKeys[ItemsKeys.CASH_ITEM_LORE.ordinal()] = new NamespacedKey(plugin, "cash_item_lore");
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
            return Constants.DATA_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.CORE_CONFIG_FILE_PATH;
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

            addMessage(Messages.SERVER_NO_PLAYER,
                    "Somente jogadores podem utilizar esse comando<color:#555555>.",
                    ""
            );
            addMessage(Messages.GAMEMODE_SETED,
                    "Seu modo de jogo foi definido para <color:#00aaaa>{0}<color:#555555>.",
                    "0: modo de jogo"
            );
            addMessage(Messages.GAMEMODE_SET_FROM,
                    "O modo de jogo de <color:#00aaaa>{2}<color:#aaaaaa> foi definido para <color:#00aaaa>{0}<color:#555555>.",
                    "0: modo de jogo; 1: nome do jogador; 2: apelido do jogador"
            );
            addMessage(Messages.GAMEMODE_NOT_BY_CONSOLE,
                    "Você precisa informar o nome de um jogador online<color:#555555>.",
                    ""
            );
            addMessage(Messages.AFK_AUTO_ENTER,
                    "<color:#00aaaa>{1} <color:#aaaaaa>ficou ausente e agora está AFK<color:#555555>.",
                    "0: nome do jogador; 1: apelido do jogador"
            );
            addMessage(Messages.AFK_ENTER,
                    "<color:#00aaaa>{1} <color:#aaaaaa>está AFK<color:#555555>.",
                    "0: nome do jogador; 1: apelido do jogador"
            );
            addMessage(Messages.AFK_LEAVE,
                    "<color:#00aaaa>{1} <color:#aaaaaa>não está mais AFK<color:#555555>.",
                    "0: nome do jogador; 1: apelido do jogador"
            );
            addMessage(Messages.AFK_BROADCAST_KICK,
                    "<color:#00aaaa>{1} <color:#aaaaaa>ficou ausente e foi kickado<color:#555555>.",
                    "0: nome do jogador; 1: apelido do jogador"
            );
            addMessage(Messages.AFK_KICKED,
                    "<color:#aaaaaa>Você foi kickado por estar ausente<color:#555555>.",
                    ""
            );
            addMessage(Messages.TELEPORT_TIMING,
                    "Você irá ser teleportado em <color:#00aaaa>{1} segundos<color:#555555>.",
                    "0: tempo restante até ser teleportado (em segundos)"
            );
            addMessage(Messages.GODMODE_ENABLED,
                    "Você ativou o God Mode<color:#555555>.",
                    ""
            );
            addMessage(Messages.GODMODE_DISABLED,
                    "Vocẽ desativou o God Mode<color:#555555>.",
                    ""
            );
            addMessage(Messages.GODMODE_DISABLED_TO,
                    "Você desativou o God Mode de <color:#00aaaa>{1}<color:#555555>.",
                    "0: nome do jogador; 1: apelido do jogador"
            );
            addMessage(Messages.GODMODE_DISABLED_BY,
                    "God Mode desativado por <color:#00aaaa>{1}<color:#555555>.",
                    "0: nome do jogador; 1: apelido do jogador"
            );
            addMessage(Messages.GODMODE_ENABLED_TO,
                    "Você ativou o God Mode de <color:#00aaaa>{1}<color:#555555>.",
                    "0: nome do jogador; 1: apelido do jogador"
            );
            addMessage(Messages.GODMODE_ENABLED_BY,
                    "God Mode ativado por <color:#00aaaa>{1}<color:#555555>.",
                    "0: nome do jogador; 1: apelido do jogador"
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
            return Constants.SPAWNER_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.SPAWNER_MESSAGE_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return messages;
        }

    }

}
