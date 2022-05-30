package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.FileCfg;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;

import br.com.eterniaserver.eterniaserver.objects.ChannelObject;
import br.com.eterniaserver.eterniaserver.objects.CustomPlaceholder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


final class Configurations {

    static class Configs implements FileCfg {

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;
        private final Services.ChatService chatService;

        protected Configs(final EterniaServer plugin, Services.ChatService chatService) {
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();
            this.chatService = chatService;

            this.chatService.channelObjectsMap.clear();
            this.chatService.customPlaceholdersObjectsMap.clear();
            this.chatService.channels.clear();

            final String[] strings = plugin.strings();

            Map<Integer, ChannelObject> tempChannelMap = new HashMap<>();
            tempChannelMap.put("global".hashCode(), new ChannelObject(
                    "{global}{clan}{suffix}{prefix}{player}{marry}{separator}",
                    "global",
                    "eternia.chat.global",
                    "&f",
                    false,
                    0
            ));

            Set<String> lista = null;
            if (inFile.getConfigurationSection("channels") != null) {
                lista = inFile.getConfigurationSection("channels").getKeys(false);
                for (String channel : lista) {
                    ChannelObject channelObject = new ChannelObject(
                            inFile.getString("channels." + channel + ".format", "{player}"),
                            channel,
                            inFile.getString("channels." + channel + ".perm", "eternia.chat.default"),
                            inFile.getString("channels." + channel + ".color", "§f"),
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
                String channel = v.getName();
                outFile.set("channels." + channel + ".format", v.getFormat());
                outFile.set("channels." + channel + ".perm", v.getPerm());
                outFile.set("channels." + channel + ".color", v.getChannelColor());
                outFile.set("channels." + channel + ".range", v.isHasRange());
                outFile.set("channels." + channel + ".range-value", v.getRange());
            });

            String filter = inFile.getString("filter", "(((http|ftp|https):\\/\\/)?[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?)");

            outFile.set("filter", filter);

            EterniaServer.getChatAPI().setFilter(Pattern.compile(filter));

            this.chatService.customPlaceholdersObjectsMap.put("prefix", new CustomPlaceholder("eternia.chat.default", "%vault_prefix%", "", "", 3, false));
            this.chatService.customPlaceholdersObjectsMap.put("player", new CustomPlaceholder("eternia.chat.default", "%player_displayname% ", "&7Nome real&8: &3%player_name%&8.", "/profile %player_name%", 4, false));
            this.chatService.customPlaceholdersObjectsMap.put("separator", new CustomPlaceholder("eternia.chat.default", " &8➤ ", "", "", 6, true));
            this.chatService.customPlaceholdersObjectsMap.put("suffix", new CustomPlaceholder("eternia.chat.default", "%vault_suffix% ", "&7Clique para enviar uma mensagem&8.", "/msg %player_name% ", 2, false));
            this.chatService.customPlaceholdersObjectsMap.put("clan", new CustomPlaceholder("eternia.chat.default", "%simpleclans_tag_label%", "&7Clan&8: &3%simpleclans_clan_name%&8.", "", 1, false));
            this.chatService.customPlaceholdersObjectsMap.put("global", new CustomPlaceholder("eternia.chat.global", "&8[&fG&8] ", "&7Clique para entrar no &fGlobal&8.", "/global ", 0, true));
            this.chatService.customPlaceholdersObjectsMap.put("marry", new CustomPlaceholder("eternia.chat.default", "%eterniamarriage_statusheart%", "&7Casado(a) com&8: &3%eterniamarriage_partner%&8.", "", 5, false));

            Map<String, CustomPlaceholder> tempCustomPlaceholdersMap = new HashMap<>();
            ConfigurationSection configurationSection = inFile.getConfigurationSection("placeholders");
            if (configurationSection != null) {
                for (String key : configurationSection.getKeys(false)) {
                    tempCustomPlaceholdersMap.put(key, new CustomPlaceholder(
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

            // Strings
            strings[Strings.DEFAULT_CHANNEL.ordinal()] = inFile.getString("general.default-channel", "global");
            strings[Strings.CHAT_DISCORD_SRV_CHANNEL.ordinal()] = inFile.getString("general.discord-srv-channel", "global");
            strings[Strings.PERM_CHAT_BYPASS_PROTECTION.ordinal()] = inFile.getString("perm.by-pass-protection", "eternia.chat.bypass");
            strings[Strings.PERM_MUTE_BYPASS.ordinal()] = inFile.getString("perm.by-pass-mute", "eternia.mute.bypass");

            // Strings
            outFile.set("general.default-channel", strings[Strings.DEFAULT_CHANNEL.ordinal()]);
            outFile.set("general.discord-srv-channel", strings[Strings.CHAT_DISCORD_SRV_CHANNEL.ordinal()]);
            outFile.set("perm.by-pass-protection", strings[Strings.PERM_CHAT_BYPASS_PROTECTION.ordinal()]);
            outFile.set("perm-by-pass-mute", strings[Strings.PERM_MUTE_BYPASS.ordinal()]);

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
            return Constants.CHAT_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.CHAT_CONFIG_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return null;
        }

    }

}
