package br.com.eterniaserver.modules.chatmanager.act.utils;

import org.bukkit.ChatColor;

public class StringHelper {

	public static String cc(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

}
