package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;

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
					APIServer.logError("Nenhum grupo definido", 3);
				}
				if (!EterniaServer.groupConfig.contains(s.toString() + ".perm")) {
					APIServer.logError("Permissão para o grupo não definida", 2);
				}
			}
		}
	}

}
