package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.chat.Services.CraftChat;
import br.com.eterniaserver.eterniaserver.modules.chat.Utils.ChannelObject;
import br.com.eterniaserver.eterniaserver.modules.chat.Utils.CustomPlaceholder;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

final class Configurations {

    private Configurations() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class ChatMessages implements ReloadableConfiguration {

        private final EterniaServer plugin;

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        public ChatMessages(EterniaServer plugin) {
            this.plugin = plugin;
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();
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
            return Constants.CHAT_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.CHAT_MESSAGES_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return plugin.messages();
        }

        @Override
        public CommandLocale[] commandsLocale() {
            return new CommandLocale[0];
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.GENERIC;
        }

        @Override
        public void executeConfig() {
            addMessage(Messages.CHAT_TELL,
                    "<color:#555555>[<color:#DF9719>TELL<color:#555555>] <color:#00aaaa>{2} <color:#DF9719>-> <color:#00aaaa>{4}<color:#555555>: <color:#DF9719>{0}",
                    "mensagem",
                    "nome do remetente",
                    "apelido do remetente",
                    "nome do destinatário",
                    "apelido do destinatário"
            );
            addMessage(Messages.CHAT_SPY_TELL,
                    "<color:#555555>[<color:#505050>TELL-SPY<color:#555555>] <color:#00aaaa>{2} <color:#505050>-> <color:#00aaaa>{4}<color:#555555>: <color:#505050>{0}",
                    "mensagem",
                    "nome do remetente",
                    "apelido do remetente",
                    "nome do destinatário",
                    "apelido do destinatário"
            );
            addMessage(Messages.CHAT_TELL_NO_PLAYER,
                    "O jogador que você estava conversando se desconectou<color:#555555>."
            );
            addMessage(Messages.CHAT_SPY_LOCAL,
                    "<color:#555555>[<color:#F7F764>LOCAL-SPY<color:#555555>] <color:#00aaaa>{2}<color:#555555>: <color:#F7F764>{0}",
                    "mensagem",
                    "nome do remetente",
                    "apelido do remetente"
            );
            addMessage(Messages.CHAT_NO_ONE_NEAR,
                    "Não há ninguém por perto para ouvir você<color:#555555>."
            );
            addMessage(Messages.CHAT_CHANNEL_CHANGED,
                    "Você entrou no canal <color:#00aaaa>{0}<color:#555555>.",
                    "nome do canal"
            );
            addMessage(Messages.CHAT_SPY_DISABLED,
                    "Você desativou o modo espião<color:#555555>."
            );
            addMessage(Messages.CHAT_SPY_ENABLED,
                    "Você ativou o modo espião<color:#555555>."
            );
            addMessage(Messages.CHAT_ARE_MUTED,
                    "Você está silenciado por <color:#00aaaa>{0}<color:#aaaaaa> segundos<color:#555555>.",
                    "segundos silenciado"
            );
            addMessage(Messages.CHAT_NO_ONE_TO_RESP,
                    "Não há ninguém para responder<color:#555555>."
            );
            addMessage(Messages.CHAT_TELL_UNLOCKED,
                    "Você parou de conversar com <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.CHAT_TELL_LOCKED,
                    "Você está conversando com <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.CHAT_TELL_YOURSELF,
                    "Você não pode conversar com você mesmo<color:#555555>."
            );
            addMessage(Messages.NICKNAME_REMOVED,
                    "Você removeu o seu apelido<color:#555555>."
            );
            addMessage(Messages.NICKNAME_UPDATED,
                    "Você definiu o seu apelido para <color:#00aaaa>{0}<color:#555555>.",
                    "novo apelido"
            );
            addMessage(Messages.NICKNAME_REMOVED_BY,
                    "O jogador <color:#00aaaa>{1}<color:#aaaaaa> removeu o seu apelido<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.NICKNAME_REMOVED_FOR,
                    "Você removeu o apelido de <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.NICKNAME_UPDATED_BY,
                    "O jogador <color:#00aaaa>{2}<color:#aaaaaa> definiu o seu apelido para <color:#00aaaa>{0}<color:#555555>.",
                    "nome do jogador",
                    "novo apelido"
            );
            addMessage(Messages.NICKNAME_UPDATED_FOR,
                    "Você definiu o apelido de <color:#00aaaa>{2}<color:#aaaaaa> para <color:#00aaaa>{0}<color:#555555>.",
                    "nome do jogador",
                    "novo apelido"
            );
            addMessage(Messages.CHAT_CHANNELS_MUTED,
                    "Todos os canais de chat estão silenciados<color:#555555>."
            );
            addMessage(Messages.CHAT_CHANNELS_DISABLED,
                    "Todos os canais foram mutados por <color:#00aaaa>{1}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.CHAT_CHANNELS_MUTED_TEMP,
                    "Todos os canais foram mutados por <color:#00aaaa>{0} minuto(s)<color:#aaaaaa> pelo <color:#00aaaa>{2}<color:#555555>.",
                    "tempo em segundos",
                    "nome do jogador",
                    "apelido do jogador"
            );
            addMessage(Messages.CHAT_CHANNELS_ENABLED,
                    "Todos os canais foram desmutados<color:#555555>."
            );
            addMessage(Messages.CHAT_BROADCAST_MUTE,
                    "O jogador <color:#00aaaa>{1}<color:#aaaaaa> foi mutado permanentemente por <color:#00aaaa>{3}<color:#555555>, <color:#aaaaaa>mensagem<color:#555555>: <color:#00aaaa>{4}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador",
                    "nome da pessoa que mutou",
                    "apelido da pessoa que mutou",
                    "mensagem"
            );
            addMessage(Messages.CHAT_BROADCAST_UNMUTE,
                    "O jogador <color:#00aaaa>{1}<color:#aaaaaa> foi desmutado<color:#aaaaaa> por <color:#00aaaa>{3}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador",
                    "nome da pessoa que desmutou",
                    "apelido da pessoa que desmutou"
            );
            addMessage(Messages.CHAT_BROADCAST_TEMP_MUTE,
                    "O jogador <color:#00aaaa>{1}<color:#aaaaaa> foi mutado por <color:#00aaaa>{4} minutos<color:#aaaaaa> por <color:#00aaaa>{3}<color:#555555>, <color:#aaaaaa>mensagem<color:#555555>: <color:#00aaaa>{5}<color:#555555>.",
                    "nome do jogador",
                    "apelido do jogador",
                    "nome da pessoa que mutou",
                    "apelido da pessoa que mutou",
                    "tempo em segundos",
                    "mensagem"
            );
        }

        @Override
        public void executeCritical() { }

    }

    static class ChatCommand implements ReloadableConfiguration {

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        private final CommandLocale[] commandsLocalesArray;

        public ChatCommand() {
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
            return Constants.CHAT_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.CHAT_COMMANDS_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return new String[0];
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
        public void executeConfig() { }

        @Override
        public void executeCritical() {
            addCommandLocale(Enums.Commands.CHAT, new CommandLocale(
                    "chat|c",
                    null,
                    " Abre a ajuda sobre o chat",
                    "eternia.chat",
                    null
            ));
            addCommandLocale(Enums.Commands.CHAT_CLEAR, new CommandLocale(
                    "clear",
                    null,
                    " Limpa o chat",
                    "eternia.chat.clear",
                    "clearchat"
            ));
            addCommandLocale(Enums.Commands.CHANNEL, new CommandLocale(
                    "channel|ch",
                    "<channel>",
                    " Entra em um canal de chat",
                    "eternia.chat.channel",
                    "channel|ch"
            ));
            addCommandLocale(Enums.Commands.CHAT_SPY, new CommandLocale(
                    "spy",
                    null,
                    " Ativa ou desativa o modo espião",
                    "eternia.chat.spy",
                    "spy"
            ));
            addCommandLocale(Enums.Commands.CHAT_REPLY, new CommandLocale(
                    "reply",
                    "<mensagem>",
                    " Responde a última mensagem recebida",
                    "eternia.chat.reply",
                    "reply|r"
            ));
            addCommandLocale(Enums.Commands.CHAT_TELL, new CommandLocale(
                    "tell|msg",
                    "<jogador> <mensagem>",
                    " Envia uma mensagem privada para um jogador",
                    "eternia.chat.tell",
                    "tell|msg"
            ));
            addCommandLocale(Enums.Commands.NICKNAME, new CommandLocale(
                    "nickname|nick",
                    "<jogador> <apelido>",
                    " Define um apelido para um jogador",
                    "eternia.chat.nickname",
                    "nickname|nick"
            ));
            addCommandLocale(Enums.Commands.MUTE, new CommandLocale(
                    "mute",
                    null,
                    " Abre a ajuda sobre o mute",
                    "eternia.chat.mute",
                    null
            ));
            addCommandLocale(Enums.Commands.MUTE_CHANNELS, new CommandLocale(
                    "channels",
                    " <minutos>",
                    " Mute todos os canais permanentemente ou por algum segundos tempo",
                    "eternia.chat.mutechannels",
                    null
            ));
            addCommandLocale(Enums.Commands.MUTE_PERMA, new CommandLocale(
                    "perma",
                    " <jogador> <mensagem>",
                    " Mute um jogador permanentemente",
                    "eternia.chat.muteperma",
                    null
            ));
            addCommandLocale(Enums.Commands.MUTE_UNDO, new CommandLocale(
                    "undo",
                    " <jogador>",
                    " Desmuta um jogador",
                    "eternia.chat.muteundo",
                    null
            ));
            addCommandLocale(Enums.Commands.MUTE_TEMP, new CommandLocale(
                    "temp",
                    " <jogador> <minutos> <mensagens>",
                    " Mute um jogador por algum tempo",
                    "eternia.chat.mutetemp",
                    null
            ));
        }

    }

    static class ChatChannels implements ReloadableConfiguration {

        private final EterniaServer plugin;

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        private final CraftChat craftChatService;

        public ChatChannels(EterniaServer plugin, CraftChat craftChatService) {
            this.plugin = plugin;
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();
            this.craftChatService = craftChatService;
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
            return Constants.CHAT_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.CHAT_CHANNELS_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return plugin.messages();
        }

        @Override
        public CommandLocale[] commandsLocale() {
            return new CommandLocale[0];
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.GENERIC;
        }

        @Override
        public void executeConfig() {
            craftChatService.channelObjectsMap.clear();
            craftChatService.customPlaceholdersObjectsMap.clear();
            craftChatService.channels.clear();

            Map<Integer, ChannelObject> tempChannelMap = new HashMap<>();
            tempChannelMap.put("global".hashCode(), new ChannelObject(
                    "{global}{clan}{suffix}{prefix}{player}{marry}{separator}",
                    "global",
                    "eternia.chat.global",
                    "#ffffff",
                    false,
                    0
            ));
            tempChannelMap.put("local".hashCode(), new ChannelObject(
                    "{local}{prefix}{player}{separator}",
                    "local",
                    "eternia.chat.local",
                    "#ffff55",
                    true,
                    100
            ));

            Set<String> lista = null;
            ConfigurationSection configurationSection = inFile.getConfigurationSection("channels");
            if (configurationSection != null) {
                lista = configurationSection.getKeys(false);
                for (String channel : lista) {
                    ChannelObject channelObject = new ChannelObject(
                            inFile.getString("channels." + channel + ".format", "{player}"),
                            channel,
                            inFile.getString("channels." + channel + ".perm", "eternia.chat.default"),
                            inFile.getString("channels." + channel + ".color", "#ffffff"),
                            inFile.getBoolean("channels." + channel + ".range", false),
                            inFile.getInt("channels." + channel + ".range-value", 0)
                    );
                    this.craftChatService.channelObjectsMap.put(channel.hashCode(), channelObject);
                }
            }

            if (this.craftChatService.channelObjectsMap.isEmpty()) {
                this.craftChatService.channelObjectsMap.putAll(tempChannelMap);
            }

            if (lista == null || lista.isEmpty()) {
                lista = Set.of("global", "local");
            }

            this.craftChatService.channels.addAll(lista);

            this.craftChatService.channelObjectsMap.forEach((k, v) -> {
                String channel = v.name();
                outFile.set("channels." + channel + ".format", v.format());
                outFile.set("channels." + channel + ".perm", v.perm());
                outFile.set("channels." + channel + ".color", v.channelColor());
                outFile.set("channels." + channel + ".range", v.hasRange());
                outFile.set("channels." + channel + ".range-value", v.range());
            });

            String filter = inFile.getString("filter", "(((http|ftp|https):\\/\\/)?[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?)");

            outFile.set("filter", filter);

            craftChatService.setFilter(Pattern.compile(filter));

            this.craftChatService.customPlaceholdersObjectsMap.put("prefix", new CustomPlaceholder("eternia.chat.default", "%vault_prefix%", "", "", 3, false));
            this.craftChatService.customPlaceholdersObjectsMap.put("player", new CustomPlaceholder("eternia.chat.default", "%player_displayname%", "<color:#AAAAAA>Nome real<color:#555555>: <color:#00AAAA>%player_name%<color:#555555>.", "/profile %player_name%", 4, false));
            this.craftChatService.customPlaceholdersObjectsMap.put("separator", new CustomPlaceholder("eternia.chat.default", " <color:#555555>➤", "", "", 6, true));
            this.craftChatService.customPlaceholdersObjectsMap.put("suffix", new CustomPlaceholder("eternia.chat.default", "%vault_suffix% ", "<color:#AAAAAA>Clique para enviar uma mensagem<color:#555555>.", "/msg %player_name% ", 2, false));
            this.craftChatService.customPlaceholdersObjectsMap.put("clan", new CustomPlaceholder("eternia.chat.default", "%simpleclans_tag_label%", "<color:#555555>Clan<color:#555555>: <color:#00AAAA>%simpleclans_clan_name%<color:#555555>.", "", 1, false));
            this.craftChatService.customPlaceholdersObjectsMap.put("global", new CustomPlaceholder("eternia.chat.global", "<color:#555555>[<color:#ffffff>G<color:#555555>]", "<color:#AAAAAA>Clique para entrar no <color:#ffffff>Global<color:#555555>.", "/global", 0, true));
            this.craftChatService.customPlaceholdersObjectsMap.put("local", new CustomPlaceholder("eternia.chat.local", "<color:#555555>[<color:#ffff55>L<color:#555555>]", "<color:#AAAAAA>Clique para entrar no <color:#ffff55>Local<color:#555555>.", "/local", 0, true));
            this.craftChatService.customPlaceholdersObjectsMap.put("marry", new CustomPlaceholder("eternia.chat.default", "%eterniamarriage_statusheart%", "<color:#AAAAAA>Casado(a) com<color:#555555>: <color:#00AAAA>%eterniamarriage_partner%<color:#555555>.", "", 5, false));

            Map<String, CustomPlaceholder> tempCustomPlaceholdersMap = new HashMap<>();
            configurationSection = inFile.getConfigurationSection("placeholders");
            if (configurationSection != null) {
                for (String key : configurationSection.getKeys(false)) {
                    tempCustomPlaceholdersMap.put(key, new Utils.CustomPlaceholder(
                            inFile.getString("placeholders." + key + ".perm", "eternia.default"),
                            inFile.getString("placeholders." + key + ".value", "default"),
                            inFile.getString("placeholders." + key + ".hover-text", "default"),
                            inFile.getString("placeholders." + key + ".suggest-command", "default"),
                            inFile.getInt("placeholders." + key + ".priority", 0),
                            inFile.getBoolean("placeholders." + key + ".static", false)
                    ));
                }
            }

            if (tempCustomPlaceholdersMap.isEmpty()) {
                tempCustomPlaceholdersMap = new HashMap<>(this.craftChatService.customPlaceholdersObjectsMap);
            }

            this.craftChatService.customPlaceholdersObjectsMap.clear();
            this.craftChatService.customPlaceholdersObjectsMap.putAll(tempCustomPlaceholdersMap);

            this.craftChatService.customPlaceholdersObjectsMap.forEach((k, v) -> {
                outFile.set("placeholders." + k + ".perm", v.permission());
                outFile.set("placeholders." + k + ".value", v.value());
                outFile.set("placeholders." + k + ".hover-text", v.hoverText());
                outFile.set("placeholders." + k + ".suggest-command", v.suggestCmd());
                outFile.set("placeholders." + k + ".priority", v.priority());
                outFile.set("placeholders." + k + ".static", v.isStatic());
            });
        }

        @Override
        public void executeCritical() { }

    }

    static class ChatConfiguration implements ReloadableConfiguration {

        private final EterniaServer plugin;
        private final CraftChat craftChatService;

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        public ChatConfiguration(EterniaServer plugin, CraftChat craftChatService) {
            this.plugin = plugin;
            this.craftChatService = craftChatService;
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();
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
            return Constants.CHAT_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.CHAT_CONFIG_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return new String[0];
        }

        @Override
        public CommandLocale[] commandsLocale() {
            return new CommandLocale[0];
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.WARNING_ADVICE;
        }

        @Override
        public void executeConfig() {
            boolean[] booleans = plugin.booleans();
            String[] strings = plugin.strings();

            booleans[Booleans.DISCORD_SRV.ordinal()] = inFile.getBoolean("general.discord-srv", true);

            strings[Strings.SHOW_ITEM_PLACEHOLDER.ordinal()] = inFile.getString("placeholder.show-item", "[item]");
            strings[Strings.DEFAULT_CHANNEL.ordinal()] = inFile.getString("general.default-channel", "global");
            strings[Strings.CHAT_DISCORD_SRV_CHANNEL.ordinal()] = inFile.getString("general.discord-srv-channel", "global");
            strings[Strings.PERM_CHAT_BYPASS_PROTECTION.ordinal()] = inFile.getString("general.bypass-protection", "eternia.chat.bypass.protection");
            strings[Strings.PERM_SPY.ordinal()] = inFile.getString("general.spy", "eternia.chat.spy");
            strings[Strings.PERM_CHAT_MENTION.ordinal()] = inFile.getString("general.mention", "eternia.chat.mention");
            strings[Strings.CONS_MENTION_TITLE.ordinal()] = inFile.getString("general.mention-title", "<color:#aaaaaa>Mencionado por <color:#00aaaa>{1}");
            strings[Strings.CONS_MENTION_SUBTITLE.ordinal()] = inFile.getString("general.mention-subtitle", "<color:#aaaaaa>Acorda<color:#555555>!!!");
            strings[Strings.PERM_CHAT_ITEM.ordinal()] = inFile.getString("general.item", "eternia.chat.item");
            strings[Strings.CONS_SHOW_ITEM.ordinal()] = inFile.getString("general.show-item", "<color:#00aaaa>x{0} {1}<color:#ffffff>");
            strings[Strings.CHAT_TABLE_NAME.ordinal()] = inFile.getString("general.table-name.chat", "e_chat_info");
            strings[Strings.CHAT_DEFAULT_TAG_COLOR.ordinal()] = inFile.getString("general.default-tag-color", "#1594AB");

            craftChatService.updateTextColor();

            outFile.set("general.discord-srv", booleans[Booleans.DISCORD_SRV.ordinal()]);

            outFile.set("placeholder.show-item", strings[Strings.SHOW_ITEM_PLACEHOLDER.ordinal()]);
            outFile.set("general.default-channel", strings[Strings.DEFAULT_CHANNEL.ordinal()]);
            outFile.set("general.discord-srv-channel", strings[Strings.CHAT_DISCORD_SRV_CHANNEL.ordinal()]);
            outFile.set("general.bypass-protection", strings[Strings.PERM_CHAT_BYPASS_PROTECTION.ordinal()]);
            outFile.set("general.spy", strings[Strings.PERM_SPY.ordinal()]);
            outFile.set("general.mention", strings[Strings.PERM_CHAT_MENTION.ordinal()]);
            outFile.set("general.mention-title", strings[Strings.CONS_MENTION_TITLE.ordinal()]);
            outFile.set("general.mention-subtitle", strings[Strings.CONS_MENTION_SUBTITLE.ordinal()]);
            outFile.set("general.item", strings[Strings.PERM_CHAT_ITEM.ordinal()]);
            outFile.set("general.show-item", strings[Strings.CONS_SHOW_ITEM.ordinal()]);
            outFile.set("general.table-name.chat", strings[Strings.CHAT_TABLE_NAME.ordinal()]);
            outFile.set("general.default-tag-color", strings[Strings.CHAT_DEFAULT_TAG_COLOR.ordinal()]);
        }

        @Override
        public void executeCritical() { }
    }

}
