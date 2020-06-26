package br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils;

import org.bukkit.ChatColor;

public class StringHelper {

	public String cc(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

}
