package br.com.eterniaserver.eterniaserver.modules.chat;

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
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

final class Configurations {

    private Configurations() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class ChatConfiguration implements ReloadableConfiguration {
        private final EterniaServer plugin;
        private final Services.Chat chatService;

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        private final CommandLocale[] commandsLocalesArray;

        public ChatConfiguration(EterniaServer plugin, Services.Chat chatService) {
            this.plugin = plugin;
            this.chatService = chatService;
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
            return Constants.CHAT_CONFIG_FILE_PATH;
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
            return ConfigurationCategory.WARNING_ADVICE;
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
                    "Você está silenciado por <color:#00aaaa>{0}<color:#555555> segundos<color:#555555>.",
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

            String[] strings = plugin.strings();

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
            strings[Strings.CHAT_TABLE_NAME.ordinal()] = inFile.getString("general.table-name.chat", "eternia_server_chat");

            chatService.channelObjectsMap.clear();
            chatService.customPlaceholdersObjectsMap.clear();
            chatService.channels.clear();

            Map<Integer, Utils.ChannelObject> tempChannelMap = new HashMap<>();
            tempChannelMap.put("global".hashCode(), new Utils.ChannelObject(
                    "{global}{clan}{suffix}{prefix}{player}{marry}{separator}",
                    "global",
                    "eternia.chat.global",
                    "<color:#ffffff>",
                    false,
                    0
            ));

            Set<String> lista = null;
            ConfigurationSection configurationSection = inFile.getConfigurationSection("channels");
            if (configurationSection != null) {
                lista = configurationSection.getKeys(false);
                for (String channel : lista) {
                    Utils.ChannelObject channelObject = new Utils.ChannelObject(
                            inFile.getString("channels." + channel + ".format", "{player}"),
                            channel,
                            inFile.getString("channels." + channel + ".perm", "eternia.chat.default"),
                            inFile.getString("channels." + channel + ".color", "<color:#ffffff>"),
                            inFile.getBoolean("channels." + channel + ".range", false),
                            inFile.getInt("channels." + channel + ".range-value", 0)
                    );
                    this.chatService.channelObjectsMap.put(channel.hashCode(), channelObject);
                }
            }

            if (this.chatService.channelObjectsMap.isEmpty()) {
                this.chatService.channelObjectsMap.putAll(tempChannelMap);
            }

            if (lista == null || lista.isEmpty()) {
                lista = Set.of("global");
            }

            this.chatService.channels.addAll(lista);

            this.chatService.channelObjectsMap.forEach((k, v) -> {
                String channel = v.name();
                outFile.set("channels." + channel + ".format", v.format());
                outFile.set("channels." + channel + ".perm", v.perm());
                outFile.set("channels." + channel + ".color", v.channelColor());
                outFile.set("channels." + channel + ".range", v.hasRange());
                outFile.set("channels." + channel + ".range-value", v.range());
            });

            String filter = inFile.getString("filter", "(((http|ftp|https):\\/\\/)?[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?)");

            outFile.set("filter", filter);

            chatService.setFilter(Pattern.compile(filter));

            this.chatService.customPlaceholdersObjectsMap.put("prefix", new Utils.CustomPlaceholder("eternia.chat.default", "%vault_prefix%", "", "", 3, false));
            this.chatService.customPlaceholdersObjectsMap.put("player", new Utils.CustomPlaceholder("eternia.chat.default", "%player_displayname% ", "<color:#AAAAAA>Nome real<color:#555555>: <color:#00AAAA>%player_name%<color:#555555>.", "/profile %player_name%", 4, false));
            this.chatService.customPlaceholdersObjectsMap.put("separator", new Utils.CustomPlaceholder("eternia.chat.default", " <color:#555555>➤ ", "", "", 6, true));
            this.chatService.customPlaceholdersObjectsMap.put("suffix", new Utils.CustomPlaceholder("eternia.chat.default", "%vault_suffix% ", "<color:#AAAAAA>Clique para enviar uma mensagem<color:#555555>.", "/msg %player_name% ", 2, false));
            this.chatService.customPlaceholdersObjectsMap.put("clan", new Utils.CustomPlaceholder("eternia.chat.default", "%simpleclans_tag_label%", "<color:#555555>Clan<color:#555555>: <color:#00AAAA>%simpleclans_clan_name%<color:#555555>.", "", 1, false));
            this.chatService.customPlaceholdersObjectsMap.put("global", new Utils.CustomPlaceholder("eternia.chat.global", "<color:#555555>[<color:#ffffff>G<color:#555555>] ", "<color:#AAAAAA>Clique para entrar no <color:#555555>Global<color:#555555>.", "/global ", 0, true));
            this.chatService.customPlaceholdersObjectsMap.put("marry", new Utils.CustomPlaceholder("eternia.chat.default", "%eterniamarriage_statusheart%", "<color:#AAAAAA>Casado(a) com<color:#555555>: <color:#00AAAA>%eterniamarriage_partner%<color:#555555>.", "", 5, false));

            Map<String, Utils.CustomPlaceholder> tempCustomPlaceholdersMap = new HashMap<>();
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
                tempCustomPlaceholdersMap = new HashMap<>(this.chatService.customPlaceholdersObjectsMap);
            }

            this.chatService.customPlaceholdersObjectsMap.clear();
            this.chatService.customPlaceholdersObjectsMap.putAll(tempCustomPlaceholdersMap);

            this.chatService.customPlaceholdersObjectsMap.forEach((k, v) -> {
                outFile.set("placeholders." + k + ".perm", v.permission());
                outFile.set("placeholders." + k + ".value", v.value());
                outFile.set("placeholders." + k + ".hover-text", v.hoverText());
                outFile.set("placeholders." + k + ".suggest-command", v.suggestCmd());
                outFile.set("placeholders." + k + ".priority", v.priority());
                outFile.set("placeholders." + k + ".static", v.isStatic());
            });

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
        }

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
                    "reply/r",
                    "<mensagem>",
                    " Responde a última mensagem recebida",
                    "eternia.chat.reply",
                    "reply"
            ));
            addCommandLocale(Enums.Commands.CHAT_TELL, new CommandLocale(
                    "tell|msg",
                    "<jogador> <mensagem>",
                    " Envia uma mensagem privada para um jogador",
                    "eternia.chat.tell",
                    "tell|msg"
            ));
        }
    }

}
