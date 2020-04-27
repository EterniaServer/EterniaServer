package br.com.eterniaserver.modules.chatmanager.act;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.chatmanager.act.utils.CustomPlaceholder;

public class AdvancedChatTorch {

	public AdvancedChatTorch() {
		registerCustomPlaceholders();
		checkGroups();
	}

	public void registerCustomPlaceholders() {
		Vars.customPlaceholders.clear();
		for(String id: EterniaServer.cph.getKeys(false)) {
			if(id.equals("customplaceholders")) {
				continue;
			}
			CustomPlaceholder cp = new CustomPlaceholder(id);
			Vars.customPlaceholders.add(cp);
		}
	}

	public void checkGroups() {
		for(Object s: EterniaServer.groups.getKeys(false)) {
			if(s.equals("groups")) {
				continue;
			}
			if(!EterniaServer.groups.contains(s.toString())) {
				Messages.ConsoleMessage("server.chat-error", "%error%", "nenhum grupo encontrado");
			}
			if(!EterniaServer.groups.contains(s.toString() + ".perm")) {
				Messages.ConsoleMessage("server.chat-error", "%error%", "permissão para o grupo não encontrada");
			}

		}
	}

}
