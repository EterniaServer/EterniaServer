package br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.ChatMessage;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.ChatObject;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.FormatInfo;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.StringHelper;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormatter {

	private final EterniaServer plugin;
	private final Vars vars;

	public ChatFormatter(EterniaServer plugin) {
		this.plugin = plugin;
		this.vars = plugin.getVars();
	}

	public void filter(AsyncPlayerChatEvent e, ChatMessage message) {
		Player p = e.getPlayer();
		if (!vars.uufi.containsKey(p.getName())) return;
		if (plugin.chatConfig.getBoolean("chat.autoUpdateGroups", false)) plugin.getChecks().addUUIF(p);
		FormatInfo fi = vars.uufi.get(p.getName());
		if (plugin.groupConfig.getBoolean(fi.getName() + ".useChatColor")) {
			ChatObject msg = new ChatObject("%message%");
			msg.setColor(ChatColor.getByChar(plugin.groupConfig.getString(fi.getName() + ".chatColor").toCharArray()[0]));
			String total = parse(p, plugin.groupConfig.getString(fi.getName() + ".format"));
			int i = total.indexOf("%message%");
			int i2 = i+"%message%".length();
			String prefix = total.substring(0, i);
			message.get(0).setMessage(prefix);
			message.getChatObjects().add(msg);
			if(i2+1<=total.length()) {
				message.getChatObjects().add(new ChatObject(total.substring(i2+1)));
			}
		} else {
			message.get(0).setMessage(parse(p, plugin.groupConfig.getString(fi.getName() + ".format")));
		}
	}

	public String parse(Player p, String s) {
		if (s.contains("&")) {
			s = StringHelper.cc(s);
		}
		s = plugin.getChecks().setPlaceholders(p, s);
		return s;
	}
}
