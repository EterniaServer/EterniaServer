package br.com.eterniaserver.eterniaserver.core;

import java.util.ArrayList;

import br.com.eterniaserver.eterniaserver.objects.ChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class UtilColors {
	
	public void filter(AsyncPlayerChatEvent e, ChatMessage message) {
		Player p = e.getPlayer();
		ArrayList<String> perms = new ArrayList<>();

		if (p.hasPermission("eternia.chat.color")) {
			perms.add("&a");
			perms.add("&b");
			perms.add("&c");
			perms.add("&d");
			perms.add("&e");
			perms.add("&f");
			perms.add("&0");
			perms.add("&1");
			perms.add("&2");
			perms.add("&3");
			perms.add("&4");
			perms.add("&5");
			perms.add("&6");
			perms.add("&7");
			perms.add("&8");
			perms.add("&9");
		}
		if (p.hasPermission("eternia.chat.special")) {
			perms.add("&l");
			perms.add("&m");
			perms.add("&n");
			perms.add("&o");
			perms.add("&k");
		}

		if (perms.isEmpty()) return;
		
		String m = message.getMessageSent();
		for (String s : perms) {
			if (s.equals("&")) {
				m = APIServer.getColor(m);
				break;
			}
			if (m.contains(s)) {
				m = m.replace(s, APIServer.getColor(s));
			}
		}

		message.setMessageSent(m);
		perms.clear();
	}
	
}
