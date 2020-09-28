package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.objects.ChatMessage;
import br.com.eterniaserver.eterniaserver.objects.ChatObject;
import br.com.eterniaserver.eterniaserver.objects.SubPlaceholder;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class UtilCustomPlaceholdersFilter {

	public void filter(AsyncPlayerChatEvent e, ChatMessage message) {
		Player p = e.getPlayer();
		for (UtilCustomPlaceholder cp: PluginVars.customPlaceholders) {
			for (int i = 0; i < message.size(); i++) {
				ChatObject chatObj = message.get(i);
				String objMsg = chatObj.getMessage();
				if (objMsg.contains("{" + cp.getId() + "}")) {
					SubPlaceholder bestPlaceholder = APIUnstable.getSubPlaceholder(p, cp);
					if (cp.isNotIndependent() && bestPlaceholder != null) {
						chatObj.setMessage(objMsg.replace("{" + cp.getId() + "}", APIServer.getColor(APIUnstable.setPlaceholders(p, bestPlaceholder.getValue()))));
					} else {
						putplaceholders(objMsg, cp, chatObj, bestPlaceholder, message, i, p);
					}
				}
			}
		}
	}

	private void putplaceholders(String objMsg, UtilCustomPlaceholder cp, ChatObject chatObj, SubPlaceholder bestPlaceholder, ChatMessage message, int i, Player p) {
		int i2 = objMsg.indexOf("{" + cp.getId() + "}");
		int i3 = i2 + ("{" + cp.getId() + "}").length();
		chatObj.setMessage(objMsg.substring(0, i2));
		if (bestPlaceholder != null) {
			String hover = bestPlaceholder.getHover();
			String suggest = bestPlaceholder.getSuggest();
			String run = bestPlaceholder.getRun();
			message.getChatObjects().add(i + 1, new ChatObject(APIServer.getColor(APIUnstable.setPlaceholders(p, bestPlaceholder.getValue())), hover, suggest, run));
			if (bestPlaceholder.isText()) {
				message.getChatObjects().get(i + 1).setIsText(true);
			}
		} else {
			message.getChatObjects().add(i + 1, new ChatObject(""));
		}
		message.getChatObjects().add(i + 2, new ChatObject(objMsg.substring(i3)));
	}

}