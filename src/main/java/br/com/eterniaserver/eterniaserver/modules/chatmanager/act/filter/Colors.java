package br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.ChatMessage;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.StringHelper;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Colors extends StringHelper {

	Pattern colorPattern = Pattern.compile("(?<!\\\\)(#([a-fA-F0-9]{6}))");
	
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

		if(perms.isEmpty()) return;
		
		String m = message.messageSent;
		for(String s: perms) {
			if(s.equals("&")) {
				m = cc(m);
				break;
			}
			if(m.contains(s)) {
				m = m.replace(s, cc(s));
			}
		}

		if (p.hasPermission("eternia.chat.color.hex")) {
			Matcher matcher = colorPattern.matcher(m);
			if (matcher.find()) {
				StringBuffer buffer = new StringBuffer();
				do {
					matcher.appendReplacement(buffer, "" + ChatColor.of(matcher.group(1)));
				} while (matcher.find());
				matcher.appendTail(buffer);
				m = buffer.toString();
			}
		}
		message.messageSent = m;
		perms.clear();
	}
	
}
