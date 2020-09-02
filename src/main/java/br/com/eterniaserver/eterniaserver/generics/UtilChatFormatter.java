package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.objects.ChatObject;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.ChatMessage;
import br.com.eterniaserver.eterniaserver.objects.FormatInfo;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class UtilChatFormatter {

	public void filter(AsyncPlayerChatEvent e, ChatMessage message) {
		Player p = e.getPlayer();
		if (!PluginVars.uufi.containsKey(p.getName())) return;
		if (EterniaServer.chatConfig.getBoolean("chat.autoUpdateGroups", false)) UtilInternMethods.addUUIF(p);
		FormatInfo fi = PluginVars.uufi.get(p.getName());
		if (EterniaServer.groupConfig.getBoolean(fi.getName() + ".useChatColor")) {
			ChatObject msg = new ChatObject(PluginConstants.MESSAGE);
			msg.setColor(ChatColor.getByChar(EterniaServer.groupConfig.getString(fi.getName() + ".chatColor").toCharArray()[0]));
			String total = parse(p, EterniaServer.groupConfig.getString(fi.getName() + ".format"));
			int i = total.indexOf(PluginConstants.MESSAGE);
			int i2 = i + 9;
			String prefix = total.substring(0, i);
			message.get(0).setMessage(prefix);
			message.getChatObjects().add(msg);
			if(i2 + 1 <=total.length()) {
				message.getChatObjects().add(new ChatObject(total.substring(i2+1)));
			}
		} else {
			message.get(0).setMessage(parse(p, EterniaServer.groupConfig.getString(fi.getName() + ".format")));
		}
	}

	public String parse(Player p, String s) {
		if (s.contains("&")) {
			s = PluginMSGs.getColor(s);
		}
		s = UtilInternMethods.setPlaceholders(p, s);
		return s;
	}
}
