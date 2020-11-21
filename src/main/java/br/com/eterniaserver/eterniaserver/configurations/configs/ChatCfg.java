package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configurations.GenericCfg;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.ChannelObject;
import br.com.eterniaserver.eterniaserver.objects.CustomPlaceholder;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class ChatCfg extends GenericCfg {

    public ChatCfg(Map<String, CustomPlaceholder> customPlaceholdersObjectsMap, Map<Integer, ChannelObject> channelObjectMap, List<String> channels, Integer[] integers, String[] strings) {
        super(strings, null, integers, null, null);

        channelObjectMap.clear();
        customPlaceholdersObjectsMap.clear();
        channels.clear();

        FileConfiguration chatConfig = YamlConfiguration.loadConfiguration(new File(Constants.CHAT_FILE_PATH));
        FileConfiguration outChat = new YamlConfiguration();

        Map<Integer, ChannelObject> tempChannelMap = new HashMap<>();
        tempChannelMap.put("global".hashCode(), new ChannelObject("{global}{clan}{sufix}{prefix}{player}{marry}{separator}", "global", "eternia.chat.global", "$f", false, 0));

        Set<String> lista = null;
        if (chatConfig.getConfigurationSection("channels") != null) {
            lista = chatConfig.getConfigurationSection("channels").getKeys(false);

            for (String channel : lista) {
                ChannelObject channelObject = new ChannelObject(
                        chatConfig.getString("channels." + channel + ".format", "{player}"),
                        channel,
                        chatConfig.getString("channels." + channel + ".perm", "eternia.chat.default"),
                        chatConfig.getString("channels." + channel + ".color", "$f"),
                        chatConfig.getBoolean("channels." + channel + ".range", false),
                        chatConfig.getInt("channels." + channel + ".range-value", 0)
                );
                channelObjectMap.put(channel.hashCode(), channelObject);
            }
        }

        setString(Strings.DEFAULT_CHANNEL, chatConfig, outChat, "default-channel", "global");
        setString(Strings.DISCORD_SRV, chatConfig, outChat, "discord-srv-channel", "global");

        if (channelObjectMap.isEmpty()) {
            channelObjectMap.putAll(tempChannelMap);
        }

        if (lista == null || lista.isEmpty()) {
            lista = Set.of("global");
        }

        channels.addAll(lista);

        channelObjectMap.forEach((k, v) -> {
            String channel = v.getName();
            outChat.set("channels." + channel + ".format", v.getFormat());
            outChat.set("channels." + channel + ".perm", v.getPerm());
            outChat.set("channels." + channel + ".color", v.getChannelColor());
            outChat.set("channels." + channel + ".range", v.isHasRange());
            outChat.set("channels." + channel + ".range-value", v.getRange());
        });

        String filter = chatConfig.getString("filter", "(((http|ftp|https):\\/\\/)?[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?)");

        outChat.set("filter", filter);

        EterniaServer.setFilter(Pattern.compile(filter));

        customPlaceholdersObjectsMap.put("prefix", new CustomPlaceholder("eternia.chat.default", "%vault_prefix%", "", "", 3));
        customPlaceholdersObjectsMap.put("player", new CustomPlaceholder("eternia.chat.default", "%player_displayname% ", "&7Nome real&8: &3%player_name%&8.", "/profile %player_name%", 4));
        customPlaceholdersObjectsMap.put("separator", new CustomPlaceholder("eternia.chat.default", " &8➤ ", "", "", 6));
        customPlaceholdersObjectsMap.put("sufix", new CustomPlaceholder("eternia.chat.default", "%vault_suffix% ", "&7Clique para enviar uma mensagem&8.", "/msg %player_name% ", 2));
        customPlaceholdersObjectsMap.put("clan", new CustomPlaceholder("eternia.chat.default", "%simpleclans_tag_label%", "&7Clan&8: &3%simpleclans_clan_name%&8.", "", 1));
        customPlaceholdersObjectsMap.put("global", new CustomPlaceholder("eternia.chat.global", "&8[&fG&8] ", "&7Clique para entrar no &fGlobal&8.", "/global ", 0));
        customPlaceholdersObjectsMap.put("marry", new CustomPlaceholder("eternia.chat.default", "%eterniamarriage_statusheart%", "&7Casado(a) com&8: &3%eterniamarriage_partner%&8.", "", 5));

        Map<String, CustomPlaceholder> tempCustomPlaceholdersMap = new HashMap<>();
        ConfigurationSection configurationSection = chatConfig.getConfigurationSection("placeholders");
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                tempCustomPlaceholdersMap.put(key, new CustomPlaceholder(chatConfig.getString("placeholders." + key + ".perm"), chatConfig.getString("placeholders." + key + ".value"), chatConfig.getString("placeholders." + key + ".hover-text"), chatConfig.getString("placeholders." + key + ".suggest-command"), chatConfig.getInt("placeholders." + key + ".priority")));
            }
        }

        if (tempCustomPlaceholdersMap.isEmpty()) {
            tempCustomPlaceholdersMap = new HashMap<>(customPlaceholdersObjectsMap);
        }

        customPlaceholdersObjectsMap.clear();
        tempCustomPlaceholdersMap.forEach(customPlaceholdersObjectsMap::put);

        tempCustomPlaceholdersMap.forEach((k, v) -> {
            outChat.set("placeholders." + k + ".perm", v.getPermission());
            outChat.set("placeholders." + k + ".value", v.getValue());
            outChat.set("placeholders." + k + ".hover-text", v.getHoverText());
            outChat.set("placeholders." + k + ".suggest-command", v.getSuggestCmd());
            outChat.set("placeholders." + k + ".priority", v.getPriority());
        });

        saveFile(outChat, Constants.CHAT_FILE_PATH, Constants.DATA_LAYER_FOLDER_PATH);

    }

}
