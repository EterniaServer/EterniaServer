package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.strings.MSG;
import org.bukkit.Bukkit;

public class UtilAdvancedChatTorch {

	public UtilAdvancedChatTorch() {
		registerCustomPlaceholders();
		checkGroups();
	}

	public void registerCustomPlaceholders() {
		Vars.customPlaceholders.clear();
		for (String id: EterniaServer.placeholderConfig.getKeys(false)) {
			if (!id.equals("customplaceholders")) {
				UtilCustomPlaceholder cp = new UtilCustomPlaceholder(id);
				Vars.customPlaceholders.add(cp);
			}
		}
	}

	public void checkGroups() {
		for(Object s: EterniaServer.groupConfig.getKeys(false)) {
			if (!s.equals("groups")) {
				if (!EterniaServer.groupConfig.contains(s.toString())) {
					Bukkit.getConsoleSender().sendMessage(MSG.MSG_ERROR.replace(Constants.ERROR, "nenhum grupo encontrado"));
				}
				if (!EterniaServer.groupConfig.contains(s.toString() + ".perm")) {
					Bukkit.getConsoleSender().sendMessage(MSG.MSG_ERROR.replace(Constants.ERROR, "permissão para o grupo não encontrada"));
				}
			}
		}
	}

}
