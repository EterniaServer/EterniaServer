package br.com.eterniaserver.modules.chatmanager.act.utils;

import java.util.ArrayList;
import java.util.List;

import br.com.eterniaserver.EterniaServer;
import org.bukkit.configuration.ConfigurationSection;


public class CustomPlaceholder {
	
	final String id;
	final boolean independentTextComponent;
	final List<SubPlaceholder> placeholders = new ArrayList<>();
	
	public CustomPlaceholder(String id) {
		this.id = id;
		if(EterniaServer.cph.contains(id + ".independentTextComponent")) {
			independentTextComponent = EterniaServer.cph.getBoolean(id + ".independentTextComponent");
		} else {
			independentTextComponent = true;
		}
		ConfigurationSection cpSection = EterniaServer.cph.getConfigurationSection(id);
		if (cpSection != null) {
			for (String placeholder : cpSection.getKeys(false)) {
				if (placeholder.equals("independentTextComponent")) {
					continue;
				}
				if (placeholder.equals("list")) {
					continue;
				}
				ConfigurationSection section = cpSection.getConfigurationSection(placeholder);
				if (section != null) {
					SubPlaceholder subPlaceholder = new SubPlaceholder(placeholder, section);
					placeholders.add(subPlaceholder);
				}
			}
		}
	}
	
	public boolean isNotIndependent() {
		return !this.independentTextComponent;
	}

	public String getId() {
		return id;
	}
	
	public List<SubPlaceholder> getPlaceholders() {
		return placeholders;
	}
	
}
