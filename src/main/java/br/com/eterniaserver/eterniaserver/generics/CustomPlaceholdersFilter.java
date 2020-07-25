package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.utils.*;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CustomPlaceholdersFilter extends StringHelper {

	private final EterniaServer plugin;

	public CustomPlaceholdersFilter(EterniaServer plugin) {
		this.plugin = plugin;
	}

	public void filter(AsyncPlayerChatEvent e, ChatMessage message) {
		Player p = e.getPlayer();
		for (CustomPlaceholder cp: Vars.customPlaceholders) {
			for (int i = 0; i < message.size(); i++) {
				ChatObject chatObj = message.get(i);
				String objMsg = chatObj.getMessage();
				if (objMsg.contains("{" + cp.getId() + "}")) {
					SubPlaceholder bestPlaceholder = plugin.getInternMethods().getSubPlaceholder(p, cp);
					if (cp.isNotIndependent() && bestPlaceholder != null) {
						chatObj.setMessage(objMsg.replace("{" + cp.getId() + "}", cc(plugin.getInternMethods().setPlaceholders(p, bestPlaceholder.getValue()))));
					} else {
						putplaceholders(objMsg, cp, chatObj, bestPlaceholder, message, i, p);
					}
				}
			}
		}
	}

	private void putplaceholders(String objMsg, CustomPlaceholder cp, ChatObject chatObj, SubPlaceholder bestPlaceholder, ChatMessage message, int i, Player p) {
		int i2 = objMsg.indexOf("{" + cp.getId() + "}");
		int i3 = i2 + ("{" + cp.getId() + "}").length();
		chatObj.setMessage(objMsg.substring(0, i2));
		if (bestPlaceholder != null) {
			String hover = bestPlaceholder.getHover();
			String suggest = bestPlaceholder.getSuggest();
			String run = bestPlaceholder.getRun();
			message.getChatObjects().add(i + 1, new ChatObject(cc(plugin.getInternMethods().setPlaceholders(p, bestPlaceholder.getValue())), hover, suggest, run));
			if (bestPlaceholder.isText()) {
				message.getChatObjects().get(i + 1).setIsText(true);
			}
		} else {
			message.getChatObjects().add(i + 1, new ChatObject(""));
		}
		message.getChatObjects().add(i + 2, new ChatObject(objMsg.substring(i3)));
	}

}