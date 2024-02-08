package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.*;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.Random;

final class Configurations {

    private Configurations() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class MainConfiguration implements ReloadableConfiguration {

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        private final CommandLocale[] commandsLocalesArray;

        private final boolean[] booleans;
        private final int[] integers;
        private final String[] strings;
        private final String[] messages;

        private final List<List<String>> stringLists;

        protected MainConfiguration(EterniaServer plugin) {
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();
            this.commandsLocalesArray = new CommandLocale[Enums.Commands.values().length];

            this.booleans = plugin.booleans();
            this.integers = plugin.integers();
            this.strings = plugin.strings();
            this.messages = plugin.messages();
            this.stringLists = plugin.stringLists();

            NamespacedKey[] namespacedKeys = plugin.namespacedKeys();

            namespacedKeys[ItemsKeys.TAG_FUNCTION.ordinal()] = new NamespacedKey(
                    plugin, Constants.TAG_FUNCTION
            );
            namespacedKeys[ItemsKeys.TAG_INT_VALUE.ordinal()] = new NamespacedKey(
                    plugin, Constants.TAG_INT_VALUE
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
            return Constants.DATA_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.CORE_CONFIG_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return messages;
        }

        @Override
        public CommandLocale[] commandsLocale() {
            return commandsLocalesArray;
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.BLOCKED;
        }

        @Override
        public void executeConfig() {
            addMessage(Messages.SERVER_NO_PLAYER,
                    "Somente jogadores podem utilizar esse comando<color:#555555>."
            );
            addMessage(Messages.GAMEMODE_SETED,
                    "Seu modo de jogo foi definido para <color:#00aaaa>{0}<color:#555555>.",
                    "modo de jogo"
            );
            addMessage(Messages.GAMEMODE_SET_FROM,
                    "O modo de jogo de <color:#00aaaa>{2}<color:#aaaaaa> foi definido para <color:#00aaaa>{0}<color:#555555>.",
                    "modo de jogo",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.GAMEMODE_NOT_BY_CONSOLE,
                    "Você precisa informar o nome de um jogador online<color:#555555>."
            );
            addMessage(Messages.AFK_AUTO_ENTER,
                    "<color:#00aaaa>{1} <color:#aaaaaa>ficou ausente e agora está AFK<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.AFK_ENTER,
                    "<color:#00aaaa>{1} <color:#aaaaaa>está AFK<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.AFK_LEAVE,
                    "<color:#00aaaa>{1} <color:#aaaaaa>não está mais AFK<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.AFK_BROADCAST_KICK,
                    "<color:#00aaaa>{1} <color:#aaaaaa>ficou ausente e foi kickado<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.AFK_KICKED,
                    "<color:#aaaaaa>Você foi kickado por estar ausente<color:#555555>."
            );
            addMessage(Messages.GODMODE_ENABLED,
                    "Você ativou o God Mode<color:#555555>."
            );
            addMessage(Messages.GODMODE_DISABLED,
                    "Vocẽ desativou o God Mode<color:#555555>."
            );
            addMessage(Messages.GODMODE_DISABLED_TO,
                    "Você desativou o God Mode de <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.GODMODE_DISABLED_BY,
                    "God Mode desativado por <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.GODMODE_ENABLED_TO,
                    "Você ativou o God Mode de <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.GODMODE_ENABLED_BY,
                    "God Mode ativado por <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.SERVER_NO_PERM,
                    "Você não possui permissão para isso<color:#555555>."
            );
            addMessage(Messages.ITEM_NOT_FOUND,
                    "Nenhum item foi encontrado em sua mão<color:#555555>."
            );
            addMessage(Messages.ITEM_HELMET,
                    "Você equipou seu caçapete<color:#555555>."
            );
            addMessage(Messages.STATS_MEM,
                    "Memória<color:#555555>: <color:#00aaaa>{0}MB<color:#555555>/<color:#00aaaa>{1}MB<color:#555555>.",
                    "memória usada",
                    "memória total"
            );
            addMessage(Messages.STATS_HOURS,
                    "Tempo online<color:#555555>: <color:#aaaaaa>Dias<color:#555555>: <color:#00aaaa>{0} <color:#aaaaaa>horas<color:#555555>: <color:#00aaaa>{1} <color:#aaaaaa>minutos<color:#555555>: <color:#00aaaa>{2} <color:#aaaaaa>segundos<color:#555555>: <color:#00aaaa>{3}<color:#555555>.",
                    "dias",
                    "horas",
                    "minutos",
                    "segundos"
            );
            addMessage(Messages.ITEM_CONDENSER,
                    "Compactando blocos."
            );
            addMessage(Messages.PROFILE_TITLE,
                    "<color:#555555>[]====[<color:#aaaaaa>Perfil<color:#555555>]====[]"
            );
            addMessage(Messages.PROFILE_REGISTER_DATA,
                    "<color:#aaaaaa>Registro<color:#555555>: <color:#00aaaa>{0}<color:#555555>."
            );
            addMessage(Messages.PROFILE_LAST_LOGIN,
                    "<color:#aaaaaa>Ultimo login<color:#555555>: <color:#00aaaa>{0}<color:#555555>."
            );
            addMessage(Messages.PROFILE_ACCOUNT_PLAYED_TIME,
                    "<color:#aaaaaa>Tempo jogado<color:#555555>: "
            );
            addMessage(Messages.SUICIDE_BROADCAST,
                    "<color:#00aaaa>{1} <color:#aaaaaa>disse<color:#555555>: <color:#00aaaa>{2} <color:#aaaaaa>e logo após se matou<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador",
                    "mensagem"
            );
            addMessage(Messages.SPEED_INVALID,
                    "Você deve informar um valor entre 1 e 10<color:#555555>."
            );
            addMessage(Messages.SPEED_SETED,
                    "Você alterou a velocidade de movimento para <color:#00aaaa>{0}<color:#555555>.",
                    "velocidade de movimento"
            );
            addMessage(Messages.FEED_SETED_TO,
                    "Você alimentou <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.FEED_SETED_BY,
                    "Você foi alimentado por <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.FEED_SETED,
                    "Você alimentou-se<color:#555555>."
            );
            addMessage(Messages.THOR_SETED_TO,
                    "Você chamou o Thor para punir <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.THOR_SETED_BY,
                    "Você foi punido por <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.LEFT_PVP,
                    "Você saiu do PvP<color:#555555>."
            );
            addMessage(Messages.ENTERED_PVP,
                    "Você entrou em PvP<color:#555555>."
            );
            addMessage(Messages.FLY_DISABLED_ENTERED_PVP,
                    "Vôo desativado por estar em PvP<color:#555555>."
            );
            addMessage(Messages.FLY_DISABLED_TO,
                    "Você desativou o vôo de <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.FLY_DISABLED_BY,
                    "Vôo desativado por <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.FLY_ENABLED_TO,
                    "Você ativou o vôo de <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.FLY_ENABLED_BY,
                    "Vôo ativado por <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.FLY_PVP_BLOCKED,
                    "Você não pode ativar o vôo em PvP<color:#555555>."
            );
            addMessage(Messages.FLY_WORLD_BLOCKED,
                    "Você não pode ativar o vôo nesse mundo<color:#555555>."
            );
            addMessage(Messages.FLY_DISABLED,
                    "Você desativou o vôo<color:#555555>."
            );
            addMessage(Messages.FLY_ENABLED,
                    "Você ativou o vôo<color:#555555>."
            );
            addMessage(Messages.SERVER_TPS,
                    "TPS<color:#555555>: <color:#00aaaa>{0}<color:#555555>.",
                    "valor do TPS"
            );
            addMessage(Messages.SERVER_LOGIN,
                    "<color:#00aaaa>{1} <color:#555555>[<color:#55FF55>+<color:#555555>]",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.SERVER_LOGOUT,
                    "<color:#00aaaa>{1} <color:#555555>[<color:#FF5555>-<color:#555555>]",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.ALREADY_IN_TIMING,
                    "Você já está em um processo de teleporte<color:#555555>."
            );
            addMessage(Messages.ALREADY_IN_CONFIRMATION,
                    "Você já está em um processo de confirmação<color:#555555>."
            );

            // Booleans
            booleans[Booleans.MODULE_SPAWNERS.ordinal()] = inFile.getBoolean("modules.spawners", true);
            booleans[Booleans.MODULE_EXPERIENCE.ordinal()] = inFile.getBoolean("modules.experience", true);
            booleans[Booleans.MODULE_ELEVATOR.ordinal()] = inFile.getBoolean("modules.elevator", true);
            booleans[Booleans.MODULE_REWARDS.ordinal()] = inFile.getBoolean("modules.rewards", true);
            booleans[Booleans.MODULE_GLOW.ordinal()] = inFile.getBoolean("modules.glow", true);
            booleans[Booleans.MODULE_PAPI.ordinal()] = inFile.getBoolean("modules.papi", true);
            booleans[Booleans.MODULE_CASH.ordinal()] = inFile.getBoolean("modules.cash", true);
            booleans[Booleans.MODULE_ENTITY.ordinal()] = inFile.getBoolean("modules.entity", true);
            booleans[Booleans.MODULE_ECONOMY.ordinal()] = inFile.getBoolean("modules.economy", true);
            booleans[Booleans.MODULE_BED.ordinal()] = inFile.getBoolean("modules.bed", true);
            booleans[Booleans.MODULE_ITEM.ordinal()] = inFile.getBoolean("modules.item", true);
            booleans[Booleans.MODULE_KIT.ordinal()] = inFile.getBoolean("modules.kit", true);
            booleans[Booleans.MODULE_TELEPORT.ordinal()] = inFile.getBoolean("modules.teleport", true);
            booleans[Booleans.MODULE_CHAT.ordinal()] = inFile.getBoolean("modules.chat", true);
            booleans[Booleans.AFK_KICK.ordinal()] = inFile.getBoolean("afk.kick-if-no-perm", true);
            booleans[Booleans.HAS_ECONOMY_PLUGIN.ordinal()] = inFile.getBoolean("critical-configs.has-economy-plugin", true);
            // Integers
            integers[Integers.PLUGIN_TICKS.ordinal()] = inFile.getInt("critical-configs.plugin-ticks", 20);
            integers[Integers.AFK_TIMER.ordinal()] = inFile.getInt("afk.limit-time", 900);
            integers[Integers.COOLDOWN.ordinal()] = inFile.getInt("critical-configs.teleport-ticks", 80);
            integers[Integers.PVP_TIME.ordinal()] = inFile.getInt("critical-configs.pvp-time", 10);
            // Strings
            strings[Strings.DATA_FORMAT.ordinal()] = inFile.getString("format.data-time", "dd/MM/yyyy HH:mm");
            strings[Strings.MINI_MESSAGES_SERVER_SERVER_LIST.ordinal()] = inFile.getString("mini-messages.motd", "            <color:#69CEDB>⛏ <gradient:#111111:#112222>❱---❰</gradient> <gradient:#6FE657:#6892F2>EterniaServer</gradient> <gradient:#112222:#111111>❱---❰</gradient> <color:#69CEDB>⛏\n                     <gradient:#926CEB:#6892F2>MOUNTAIN UPDATE</gradient>");
            strings[Strings.PERM_AFK.ordinal()] = inFile.getString("afk.perm-to-stay-afk", "eternia.afk");
            strings[Strings.SERVER_PREFIX.ordinal()] = inFile.getString("mini-messages.prefix", "<color:#555555>[<color:#34eb40>E<color:#3471eb>S<color:#555555>]<color:#AAAAAA> ");
            strings[Strings.PERM_TIMING_BYPASS.ordinal()] = inFile.getString("teleport.timing-bypass", "eternia.timing.bypass");
            strings[Strings.GUI_SECRET.ordinal()] = inFile.getString("secret.value", String.format("#%06x", new Random().nextInt(0xffffff + 1)));
            strings[Strings.PERM_EC_OTHER.ordinal()] = inFile.getString("permissions.ec-other", "eternia.enderchest.other");
            strings[Strings.REVISION_TABLE_NAME.ordinal()] = inFile.getString("table-name.revision", "e_revision");
            strings[Strings.PROFILE_TABLE_NAME.ordinal()] = inFile.getString("table-name.player-profile", "e_player_profile");
            strings[Strings.CONS_ADVENTURE.ordinal()] = inFile.getString("const.gm.adventure", "aventura");
            strings[Strings.CONS_CREATIVE.ordinal()] = inFile.getString("const.gm.creative", "criativo");
            strings[Strings.CONS_SPECTATOR.ordinal()] = inFile.getString("const.gm.spectator", "espectador");
            strings[Strings.CONS_SURVIVAL.ordinal()] = inFile.getString("const.gm.survival", "sobrevivência");
            strings[Strings.PROFILE_PLAYED_TIME.ordinal()] = inFile.getString("mini-messages.profile.played-time", "<color:#aaaaaa>Dias<color:#555555>: <color:#00aaaa>%02d <color:#aaaaaa>Horas<color:#555555>: <color:#00aaaa>%02d <color:#aaaaaa>Minutos<color:#555555>: <color:#00aaaa>%02d<color:#555555>.");
            strings[Strings.PERM_FLY_OTHER.ordinal()] = inFile.getString("permissions.fly.other", "eternia.fly.other");
            strings[Strings.PERM_FLY_BYPASS.ordinal()] = inFile.getString("permissions.fly.bypass", "eternia.fly.bypass");
            strings[Strings.JOIN_NAMES.ordinal()] = inFile.getString("messages.join-names-display", "<color:#00aaaa>{0}<color:#AAAAAA>, ");

            // Lists
            List<String> blackedCommands = inFile.getStringList("critical-configs.blocked-commands");
            stringLists.set(Lists.BLACKLISTED_COMMANDS.ordinal(), blackedCommands.isEmpty() ? List.of("/op", "/deop", "/stop") : blackedCommands);
            List<String> profileMessages = inFile.getStringList("mini-messages.profile.custom-messages");
            stringLists.set(Lists.PROFILE_CUSTOM_MESSAGES.ordinal(), profileMessages.isEmpty() ? List.of("<color:#aaaaaa>Saldo em C.A.S.H.<color:#555555>: <color:#00aaaa>%eterniaserver_cash%<color:#555555>.") : profileMessages);
            List<String> flyBlockedWorlds = inFile.getStringList("critical-configs.blocked-worlds-fly");
            stringLists.set(Lists.BLACKLISTED_WORLDS_FLY.ordinal(), flyBlockedWorlds.isEmpty() ? List.of("world_nether", "world_the_end") : flyBlockedWorlds);

            // Booleans
            outFile.set("modules.spawners", booleans[Booleans.MODULE_SPAWNERS.ordinal()]);
            outFile.set("modules.experience", booleans[Booleans.MODULE_EXPERIENCE.ordinal()]);
            outFile.set("modules.elevator", booleans[Booleans.MODULE_ELEVATOR.ordinal()]);
            outFile.set("modules.rewards", booleans[Booleans.MODULE_REWARDS.ordinal()]);
            outFile.set("modules.glow", booleans[Booleans.MODULE_GLOW.ordinal()]);
            outFile.set("modules.papi", booleans[Booleans.MODULE_PAPI.ordinal()]);
            outFile.set("modules.cash", booleans[Booleans.MODULE_CASH.ordinal()]);
            outFile.set("modules.entity", booleans[Booleans.MODULE_ENTITY.ordinal()]);
            outFile.set("modules.economy", booleans[Booleans.MODULE_ECONOMY.ordinal()]);
            outFile.set("modules.bed", booleans[Booleans.MODULE_BED.ordinal()]);
            outFile.set("modules.item", booleans[Booleans.MODULE_ITEM.ordinal()]);
            outFile.set("modules.kit", booleans[Booleans.MODULE_KIT.ordinal()]);
            outFile.set("modules.teleport", booleans[Booleans.MODULE_TELEPORT.ordinal()]);
            outFile.set("modules.chat", booleans[Booleans.MODULE_CHAT.ordinal()]);
            outFile.set("afk.kick-if-no-perm", booleans[Booleans.AFK_KICK.ordinal()]);
            outFile.set("critical-configs.has-economy-plugin", booleans[Booleans.HAS_ECONOMY_PLUGIN.ordinal()]);
            // Integers
            outFile.set("critical-configs.plugin-ticks", integers[Integers.PLUGIN_TICKS.ordinal()]);
            outFile.set("afk.limit-time", integers[Integers.AFK_TIMER.ordinal()]);
            outFile.set("critical-configs.teleport-ticks", integers[Integers.COOLDOWN.ordinal()]);
            outFile.set("critical-configs.pvp-time", integers[Integers.PVP_TIME.ordinal()]);
            // Strings
            outFile.set("format.data-time", strings[Strings.DATA_FORMAT.ordinal()]);
            outFile.set("mini-messages.motd", strings[Strings.MINI_MESSAGES_SERVER_SERVER_LIST.ordinal()]);
            outFile.set("afk.perm-to-stay-afk", strings[Strings.PERM_AFK.ordinal()]);
            outFile.set("teleport.timing-bypass", strings[Strings.PERM_TIMING_BYPASS.ordinal()]);
            outFile.set("secret.value", strings[Strings.GUI_SECRET.ordinal()]);
            outFile.set("secret.info-pt", "Não exponha esse código hex");
            outFile.set("secret.info-en", "Don't expose this hex code");
            outFile.set("permissions.ec-other", strings[Strings.PERM_EC_OTHER.ordinal()]);
            outFile.set("table-name.revision", strings[Strings.REVISION_TABLE_NAME.ordinal()]);
            outFile.set("table-name.player-profile", strings[Strings.PROFILE_TABLE_NAME.ordinal()]);
            outFile.set("mini-messages.prefix", strings[Strings.SERVER_PREFIX.ordinal()]);
            outFile.set("mini-messages.profile.played-time", strings[Strings.PROFILE_PLAYED_TIME.ordinal()]);
            outFile.set("permissions.fly.other", strings[Strings.PERM_FLY_OTHER.ordinal()]);
            outFile.set("permissions.fly.bypass", strings[Strings.PERM_FLY_BYPASS.ordinal()]);
            outFile.set("messages.join-names-display", strings[Strings.JOIN_NAMES.ordinal()]);
            // Lists
            outFile.set("critical-configs.blocked-commands", stringLists.get(Lists.BLACKLISTED_COMMANDS.ordinal()));
            outFile.set("mini-messages.profile.custom-messages", stringLists.get(Lists.PROFILE_CUSTOM_MESSAGES.ordinal()));
            outFile.set("critical-configs.blocked-worlds-fly", stringLists.get(Lists.BLACKLISTED_WORLDS_FLY.ordinal()));
        }

        @Override
        public void executeCritical() {
            addCommandLocale(Enums.Commands.BROADCAST, new CommandLocale(
                    "broadcast",
                    " <prefix> <mensagem>",
                    " Envia uma mensagem global ao servidor",
                    "eternia.broadcast",
                    null
            ));
            addCommandLocale(Enums.Commands.GAMEMODE, new CommandLocale(
                    "gamemode|gm",
                    " <página>",
                    " Ajuda para o sistema de Gamemode",
                    "eternia.gamemode",
                    null
            ));
            addCommandLocale(Enums.Commands.GAMEMODE_SURVIVAL, new CommandLocale(
                    "survival|s|0",
                    " <página>",
                    " Define o modo de jogo seu, ou de outro jogador para Sobrevivência",
                    "eternia.gamemode",
                    null
            ));
            addCommandLocale(Enums.Commands.GAMEMODE_CREATIVE, new CommandLocale(
                    "creative|c|1",
                    " <página>",
                    " Define o modo de jogo seu, ou de outro jogador para Criativo",
                    "eternia.gamemode",
                    null
            ));
            addCommandLocale(Enums.Commands.GAMEMODE_ADVENTURE, new CommandLocale(
                    "adventure|a|2",
                    " <página>",
                    " Define o modo de jogo seu, ou de outro jogador para Aventura",
                    "eternia.gamemode",
                    null
            ));
            addCommandLocale(Enums.Commands.GAMEMODE_SPECTATOR, new CommandLocale(
                    "spectator|sp|3",
                    " <página>",
                    " Define o modo de jogo seu, ou de outro jogador para Espectador",
                    "eternia.gamemode",
                    null
            ));
            addCommandLocale(Enums.Commands.AFK, new CommandLocale(
                    "afk",
                    null,
                    " Entra no modo AFK",
                    "eternia.afk",
                    null
            ));
            addCommandLocale(Enums.Commands.GODMODE, new CommandLocale(
                    "god",
                    null,
                    " Entra em GodMode",
                    "eternia.god",
                    null
            ));
            addCommandLocale(Enums.Commands.HAT, new CommandLocale(
                    "hat|capacete",
                    null,
                    " Coloca o item na sua mão na sua cabeça",
                    "eternia.hat",
                    null
            ));
            addCommandLocale(Enums.Commands.WORKBENCH, new CommandLocale(
                    "workbench|craftingtable|crafting",
                    null,
                    " Abre uma mesa de trabalho",
                    "eternia.workbench",
                    null
            ));
            addCommandLocale(Enums.Commands.ENDERCHEST, new CommandLocale(
                    "enderchest|ender|chest",
                    null,
                    " Abre um baú de ender",
                    "eternia.enderchest",
                    null
            ));
            addCommandLocale(Enums.Commands.OPENINV, new CommandLocale(
                    "openinv|inv",
                    " <jogador>",
                    " Abre o inventário de outro jogador",
                    "eternia.openinv",
                    null
            ));
            addCommandLocale(Enums.Commands.MEM, new CommandLocale(
                    "mem",
                    null,
                    " Veja informações sobre o servidor",
                    "eternia.mem",
                    null
            ));
            addCommandLocale(Enums.Commands.MEM_ALL, new CommandLocale(
                    "memall",
                    null,
                    " Mostre globalmente as informações sobre o servidor",
                    "eternia.mem.all",
                    null
            ));
            addCommandLocale(Enums.Commands.CONDENSER, new CommandLocale(
                    "blocks|condenser|compactar",
                    null,
                    " Comprima seus minérios em blocos para liberar espaço",
                    "eternia.blocks",
                    null
            ));
            addCommandLocale(Enums.Commands.PROFILE, new CommandLocale(
                    "profile",
                    " <jogador>",
                    " Veja o seu perfil ou o de outro jogador",
                    "eternia.profile",
                    null
            ));
            addCommandLocale(Enums.Commands.SUICIDE, new CommandLocale(
                    "suicide|suicidio",
                    " <mensagem>",
                    " Envie uma mensagem e se mate",
                    "eternia.suicide",
                    null
            ));
            addCommandLocale(Enums.Commands.SPEED, new CommandLocale(
                    "speed",
                    " <velocidade>",
                    " Altere a sua velocidade",
                    "eternia.speed",
                    null
            ));
            addCommandLocale(Enums.Commands.FEED, new CommandLocale(
                    "feed",
                    " <jogador>",
                    " Sacie-se ou sacie outra pessoa",
                    "eternia.feed",
                    null
            ));
            addCommandLocale(Enums.Commands.THOR, new CommandLocale(
                    "thor",
                    " <jogador>",
                    " Chame o Thor para punir um jogador",
                    "eternia.thor",
                    null
            ));
            addCommandLocale(Enums.Commands.FLY, new CommandLocale(
                    "fly|voar",
                    " <jogador>",
                    " VOA BRABULETA!!!",
                    "eternia.fly",
                    null
            ));
        }
    }

}
