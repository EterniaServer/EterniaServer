package br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter;

import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.PlaceholderAPIIntegrator;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.*;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CustomPlaceholdersFilter {

	private final Vars vars;

	public CustomPlaceholdersFilter(Vars vars) {
		this.vars = vars;
	}

	public void filter(AsyncPlayerChatEvent e, ChatMessage message) {
		Player p = e.getPlayer();
		for(CustomPlaceholder cp: vars.customPlaceholders) {
			for(int i = 0; i < message.size(); i++) {
				ChatObject chatObj = message.get(i);
				String objMsg = chatObj.getMessage();
				if(!objMsg.contains("{" + cp.getId() + "}")) {
					continue;
				}
				SubPlaceholder bestPlaceholder = null;
				try {
					for(SubPlaceholder subPlaceholder: cp.getPlaceholders()) {
						if(subPlaceholder.hasPerm(p)) {
							if(bestPlaceholder == null) {
								bestPlaceholder = subPlaceholder;
							} else {
								if(bestPlaceholder.getPriority() < subPlaceholder.getPriority()) {
									bestPlaceholder = subPlaceholder;
								}
							}
						}
					}
				}catch(Exception e1) {
					e1.printStackTrace();
				}
				if(cp.isNotIndependent() && bestPlaceholder != null) {
					chatObj.setMessage(objMsg.replace("{" + cp.getId() + "}", StringHelper.cc(PlaceholderAPIIntegrator.setPlaceholders(p, bestPlaceholder.getValue()))));
				} else {
					int i2 = objMsg.indexOf("{" + cp.getId() + "}");
					int i3 = i2 + ("{" + cp.getId() + "}").length();
					chatObj.setMessage(objMsg.substring(0, i2));
					if(bestPlaceholder != null) {
						String hover = bestPlaceholder.getHover();
						String suggest = bestPlaceholder.getSuggest();
						String run = bestPlaceholder.getRun();
						message.getChatObjects().add(i+1, new ChatObject(StringHelper.cc(PlaceholderAPIIntegrator.setPlaceholders(p, bestPlaceholder.getValue())), hover, suggest, run));
						if(bestPlaceholder.isText()) {
							message.getChatObjects().get(i+1).setIsText(true);
						}
					} else {
						message.getChatObjects().add(i+1, new ChatObject(""));
					}
					message.getChatObjects().add(i+2, new ChatObject(objMsg.substring(i3)));
				}
			}
		}
	}

}