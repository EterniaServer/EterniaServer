package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.utils.CustomPlaceholder;

public class AdvancedChatTorch {

	private final EFiles messages;

	public AdvancedChatTorch(EterniaServer plugin) {
		this.messages = plugin.getEFiles();
		registerCustomPlaceholders();
		checkGroups();
	}

	public void registerCustomPlaceholders() {
		Vars.customPlaceholders.clear();
		for (String id: EterniaServer.placeholderConfig.getKeys(false)) {
			if (!id.equals("customplaceholders")) {
				CustomPlaceholder cp = new CustomPlaceholder(id);
				Vars.customPlaceholders.add(cp);
			}
		}
	}

	public void checkGroups() {
		for(Object s: EterniaServer.groupConfig.getKeys(false)) {
			if (!s.equals("groups")) {
				if (!EterniaServer.groupConfig.contains(s.toString())) {
					messages.sendConsole("server.chat-error", "%error%", "nenhum grupo encontrado");
				}
				if (!EterniaServer.groupConfig.contains(s.toString() + ".perm")) {
					messages.sendConsole("server.chat-error", "%error%", "permissão para o grupo não encontrada");
				}
			}
		}
	}

}
