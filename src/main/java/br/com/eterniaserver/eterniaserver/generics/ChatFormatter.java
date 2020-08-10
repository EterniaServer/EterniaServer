package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.eterniaserver.utils.ChatMessage;
import br.com.eterniaserver.eterniaserver.utils.FormatInfo;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormatter {

	public void filter(AsyncPlayerChatEvent e, ChatMessage message) {
		Player p = e.getPlayer();
		if (!Vars.uufi.containsKey(p.getName())) return;
		if (EterniaServer.chatConfig.getBoolean("chat.autoUpdateGroups", false)) InternMethods.addUUIF(p);
		FormatInfo fi = Vars.uufi.get(p.getName());
		if (EterniaServer.groupConfig.getBoolean(fi.getName() + ".useChatColor")) {
			ChatObject msg = new ChatObject(Constants.MESSAGE);
			msg.setColor(ChatColor.getByChar(EterniaServer.groupConfig.getString(fi.getName() + ".chatColor").toCharArray()[0]));
			String total = parse(p, EterniaServer.groupConfig.getString(fi.getName() + ".format"));
			int i = total.indexOf(Constants.MESSAGE);
			int i2 = i + 9;
			String prefix = total.substring(0, i);
			message.get(0).setMessage(prefix);
			message.getChatObjects().add(msg);
			if(i2+1<=total.length()) {
				message.getChatObjects().add(new ChatObject(total.substring(i2+1)));
			}
		} else {
			message.get(0).setMessage(parse(p, EterniaServer.groupConfig.getString(fi.getName() + ".format")));
		}
	}

	public String parse(Player p, String s) {
		if (s.contains("&")) {
			s = Strings.getColor(s);
		}
		s = InternMethods.setPlaceholders(p, s);
		return s;
	}
}
