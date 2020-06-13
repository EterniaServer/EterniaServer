package br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils;

import org.bukkit.ChatColor;

public class StringHelper {

	private StringHelper() {
		throw new IllegalStateException("Utility class");
	}

	public static String cc(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

}
