package br.com.eterniaserver.eterniaserver.configurations;

import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.objects.CustomPlaceholders;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChatCfg {

    public final int localRange;
    public final String localFormat;
    public final String globalFormat;
    public final String staffFormat;

    public final Map<String, CustomPlaceholders> customPlaceholdersObjectsMap = new HashMap<>();


    public ChatCfg() {

        FileConfiguration chatConfig = YamlConfiguration.loadConfiguration(new File(Constants.CHAT_FILE_PATH));
        FileConfiguration outChat = new YamlConfiguration();

        this.localFormat = chatConfig.getString("format.local", "$8[$eL$8] %vault_suffix% $e%player_displayname%$8 ➤ $e%message%").replace('$', (char) 0x00A7);
        this.globalFormat = chatConfig.getString("format.global", "{canal}{clan}{sufix}{prefix}{player}{marry}{separator}").replace('$', (char) 0x00A7);
        this.staffFormat = chatConfig.getString("format.staff", "$8[$bS$8] %vault_prefix%%player_displayname%$8 ➤ $b%message%").replace('$', (char) 0x00A7);
        this.localRange = chatConfig.getInt("format.local-range", 64);

        this.customPlaceholdersObjectsMap.put("prefix", new CustomPlaceholders(Constants.CHAT_GLOBAL_PERM, "%vault_prefix%", "", "", 3));
        this.customPlaceholdersObjectsMap.put("player", new CustomPlaceholders(Constants.CHAT_GLOBAL_PERM, "%player_displayname% ", "&7Nome real&8: &3%player_name%&8.", "/profile %player_name%", 4));
        this.customPlaceholdersObjectsMap.put("separator", new CustomPlaceholders(Constants.CHAT_GLOBAL_PERM, " &8➤ ", "", "", 6));
        this.customPlaceholdersObjectsMap.put("sufix", new CustomPlaceholders(Constants.CHAT_GLOBAL_PERM, "%vault_suffix% ", "&7Clique para enviar uma mensagem&8.", "/msg %player_name% ", 2));
        this.customPlaceholdersObjectsMap.put("clan", new CustomPlaceholders(Constants.CHAT_GLOBAL_PERM, "%simpleclans_tag_label%", "&7Clan&8: &3%simpleclans_clan_name%&8.", "", 1));
        this.customPlaceholdersObjectsMap.put("canal", new CustomPlaceholders(Constants.CHAT_GLOBAL_PERM, "&8[&fG&8] ", "&7Clique para entrar no &fGlobal&8.", "/global ", 0));
        this.customPlaceholdersObjectsMap.put("marry", new CustomPlaceholders(Constants.CHAT_GLOBAL_PERM, "%eterniamarriage_statusheart%", "&7Casado(a) com&8: &3%eterniamarriage_partner%&8.", "", 5));

        outChat.set("format.local", this.localFormat);
        outChat.set("format.global", this.globalFormat);
        outChat.set("format.staff", this.staffFormat);
        outChat.set("format.local-range", this.localRange);

        Map<String, CustomPlaceholders> tempCustomPlaceholdersMap = new HashMap<>();
        ConfigurationSection configurationSection = chatConfig.getConfigurationSection("placeholders");
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                tempCustomPlaceholdersMap.put(key, new CustomPlaceholders(chatConfig.getString(Constants.PLACEHOLDER + key + ".perm"), chatConfig.getString(Constants.PLACEHOLDER + key + ".value"), chatConfig.getString(Constants.PLACEHOLDER + key + ".hover-text"), chatConfig.getString(Constants.PLACEHOLDER + key + ".suggest-command"), chatConfig.getInt(Constants.PLACEHOLDER + key + ".priority")));
            }
        }

        if (tempCustomPlaceholdersMap.isEmpty()) {
            tempCustomPlaceholdersMap = new HashMap<>(this.customPlaceholdersObjectsMap);
        }

        this.customPlaceholdersObjectsMap.clear();
        tempCustomPlaceholdersMap.forEach(this.customPlaceholdersObjectsMap::put);

        tempCustomPlaceholdersMap.forEach((k, v) -> {
            outChat.set(Constants.PLACEHOLDER + k + ".perm", v.getPermission());
            outChat.set(Constants.PLACEHOLDER + k + ".value", v.getValue());
            outChat.set(Constants.PLACEHOLDER + k + ".hover-text", v.getHoverText());
            outChat.set(Constants.PLACEHOLDER + k + ".suggest-command", v.getSuggestCmd());
            outChat.set(Constants.PLACEHOLDER + k + ".priority", v.getPriority());
        });

        outChat.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");

        try {
            outChat.save(Constants.CHAT_FILE_PATH);
        } catch (IOException exception) {
            APIServer.logError("Impossível de criar arquivos em " + Constants.DATA_LAYER_FOLDER_PATH, 3);
        }

    }

}
