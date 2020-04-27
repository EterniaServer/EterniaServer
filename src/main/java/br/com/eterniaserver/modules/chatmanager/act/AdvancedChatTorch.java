package br.com.eterniaserver.modules.chatmanager.act;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.modules.chatmanager.act.utils.CustomPlaceholder;
import br.com.eterniaserver.modules.chatmanager.act.utils.FormatInfo;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class AdvancedChatTorch {

	private static AdvancedChatTorch instance;
	public final HashMap<UUID, FormatInfo> uufi = new HashMap<>();
	public final List<CustomPlaceholder> customPlaceholders = new ArrayList<>();

	public AdvancedChatTorch(EterniaServer plugin) {
		instance = this;
		File fileGroups = new File(plugin.getDataFolder(), "groups.yml");
		if (!fileGroups.exists()) {
			plugin.saveResource("groups.yml", false);
		}
		EterniaServer.groups = new YamlConfiguration();
		try {
			EterniaServer.blocks.load(fileGroups);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		File placeholdersFile = new File(plugin.getDataFolder(), "customplaceholders.yml");
		if (!placeholdersFile.exists()) {
			plugin.saveResource("customplaceholders.yml", false);
		}
		EterniaServer.cph = new YamlConfiguration();
		try {
			EterniaServer.blocks.load(placeholdersFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		registerCustomPlaceholders();
	}

	public void registerCustomPlaceholders() {
		customPlaceholders.clear();
		for(String id: EterniaServer.cph.getKeys(false)) {
			if(id.equals("customplaceholders")) {
				continue;
			}
			CustomPlaceholder cp = new CustomPlaceholder(id);
			customPlaceholders.add(cp);
		}
	}


	public static AdvancedChatTorch getInstance() {
		return instance;
	}

	public List<CustomPlaceholder> getCustomPlaceholders() {
		return customPlaceholders;
	}
}
