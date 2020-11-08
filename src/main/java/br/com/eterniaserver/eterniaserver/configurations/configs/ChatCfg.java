package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configurations.GenericCfg;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.enums.ConfigIntegers;
import br.com.eterniaserver.eterniaserver.enums.ConfigStrings;
import br.com.eterniaserver.eterniaserver.objects.CustomPlaceholder;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ChatCfg extends GenericCfg {

    public ChatCfg(Map<String, CustomPlaceholder> customPlaceholdersObjectsMap, String[] strings, Integer[] integers) {
        super(strings, null, integers, null, null);

        customPlaceholdersObjectsMap.clear();

        FileConfiguration chatConfig = YamlConfiguration.loadConfiguration(new File(Constants.CHAT_FILE_PATH));
        FileConfiguration outChat = new YamlConfiguration();

        setString(ConfigStrings.LOCAL_FORMAT, chatConfig, outChat, "format.local", "$8[$eL$8] %vault_suffix% $e%player_displayname%$8 ➤ $e%message%");
        setString(ConfigStrings.GLOBAL_FORMAT, chatConfig, outChat, "format.global", "{canal}{clan}{sufix}{prefix}{player}{marry}{separator}");
        setString(ConfigStrings.STAFF_FORMAT, chatConfig, outChat, "format.staff", "$8[$bS$8] %vault_prefix%%player_displayname%$8 ➤ $b%message%");
        setString(ConfigStrings.CHAT_FILTER, chatConfig, outChat, "format.filter", "(((http|ftp|https):\\/\\/)?[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?)");

        setInteger(ConfigIntegers.LOCAL_RANGE, chatConfig, outChat, "format.local-range", 64);

        EterniaServer.setFilter(Pattern.compile(EterniaServer.getString(ConfigStrings.CHAT_FILTER)));

        customPlaceholdersObjectsMap.put("prefix", new CustomPlaceholder("eternia.chat.global", "%vault_prefix%", "", "", 3));
        customPlaceholdersObjectsMap.put("player", new CustomPlaceholder("eternia.chat.global", "%player_displayname% ", "&7Nome real&8: &3%player_name%&8.", "/profile %player_name%", 4));
        customPlaceholdersObjectsMap.put("separator", new CustomPlaceholder("eternia.chat.global", " &8➤ ", "", "", 6));
        customPlaceholdersObjectsMap.put("sufix", new CustomPlaceholder("eternia.chat.global", "%vault_suffix% ", "&7Clique para enviar uma mensagem&8.", "/msg %player_name% ", 2));
        customPlaceholdersObjectsMap.put("clan", new CustomPlaceholder("eternia.chat.global", "%simpleclans_tag_label%", "&7Clan&8: &3%simpleclans_clan_name%&8.", "", 1));
        customPlaceholdersObjectsMap.put("canal", new CustomPlaceholder("eternia.chat.global", "&8[&fG&8] ", "&7Clique para entrar no &fGlobal&8.", "/global ", 0));
        customPlaceholdersObjectsMap.put("marry", new CustomPlaceholder("eternia.chat.global", "%eterniamarriage_statusheart%", "&7Casado(a) com&8: &3%eterniamarriage_partner%&8.", "", 5));

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
