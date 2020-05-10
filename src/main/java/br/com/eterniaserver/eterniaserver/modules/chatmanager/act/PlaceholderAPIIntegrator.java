package br.com.eterniaserver.eterniaserver.modules.chatmanager.act;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.entity.Player;

public class PlaceholderAPIIntegrator {

	public static String setPlaceholders(Player p, String s) {
		s = s.contains("%player_name%") ? s.replace("%player_name%", p.getName()) : s;
		s = s.contains("%display_name%") ? s.replace("%display_name%", p.getDisplayName()) : s;
		return PlaceholderAPI.setPlaceholders(p, s);
	}

	public static String setRelationalPlaceholders(Player p, Player p2, String s) {
		return PlaceholderAPI.setRelationalPlaceholders(p, p2, s);
	}

	public static String setBothPlaceholders(Player p, Player to, String cc) {
		return setRelationalPlaceholders(p, to, setPlaceholders(p, cc));
	}
	
}
