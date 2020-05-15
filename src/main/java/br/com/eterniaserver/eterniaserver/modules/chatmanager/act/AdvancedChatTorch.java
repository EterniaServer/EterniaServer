package br.com.eterniaserver.eterniaserver.modules.chatmanager.act;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.CustomPlaceholder;

public class AdvancedChatTorch {

	private final EterniaServer plugin;
	private final Messages messages;
	private final Vars vars;

	public AdvancedChatTorch(EterniaServer plugin, Messages messages, Vars vars) {
		this.plugin = plugin;
		this.messages = messages;
		this.vars = vars;
		registerCustomPlaceholders();
		checkGroups();
	}

	public void registerCustomPlaceholders() {
		vars.customPlaceholders.clear();
		for(String id: plugin.placeholderConfig.getKeys(false)) {
			if(id.equals("customplaceholders")) continue;
			CustomPlaceholder cp = new CustomPlaceholder(id, plugin);
			vars.customPlaceholders.add(cp);
		}
	}

	public void checkGroups() {
		for(Object s: plugin.groupConfig.getKeys(false)) {
			if(s.equals("groups")) continue;
			if(!plugin.groupConfig.contains(s.toString())) {
				messages.sendConsole("server.chat-error", "%error%", "nenhum grupo encontrado");
			}
			if(!plugin.groupConfig.contains(s.toString() + ".perm")) {
				messages.sendConsole("server.chat-error", "%error%", "permissão para o grupo não encontrada");
			}

		}
	}

}
