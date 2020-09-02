package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import org.bukkit.Bukkit;

public class UtilAdvancedChatTorch {

	public UtilAdvancedChatTorch() {
		registerCustomPlaceholders();
		checkGroups();
	}

	public void registerCustomPlaceholders() {
		PluginVars.customPlaceholders.clear();
		for (String id: EterniaServer.placeholderConfig.getKeys(false)) {
			if (!id.equals("customplaceholders")) {
				UtilCustomPlaceholder cp = new UtilCustomPlaceholder(id);
				PluginVars.customPlaceholders.add(cp);
			}
		}
	}

	public void checkGroups() {
		for(Object s: EterniaServer.groupConfig.getKeys(false)) {
			if (!s.equals("groups")) {
				if (!EterniaServer.groupConfig.contains(s.toString())) {
					Bukkit.getConsoleSender().sendMessage(PluginMSGs.MSG_ERROR.replace(PluginConstants.ERROR, "nenhum grupo encontrado"));
				}
				if (!EterniaServer.groupConfig.contains(s.toString() + ".perm")) {
					Bukkit.getConsoleSender().sendMessage(PluginMSGs.MSG_ERROR.replace(PluginConstants.ERROR, "permissão para o grupo não encontrada"));
				}
			}
		}
	}

}
