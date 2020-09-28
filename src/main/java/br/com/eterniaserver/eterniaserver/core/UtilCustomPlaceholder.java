package br.com.eterniaserver.eterniaserver.core;

import java.util.ArrayList;
import java.util.List;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.SubPlaceholder;
import org.bukkit.configuration.ConfigurationSection;

public class UtilCustomPlaceholder {

	final String id;
	final boolean independentTextComponent;
	final List<SubPlaceholder> placeholders = new ArrayList<>();

	public UtilCustomPlaceholder(String id) {
		this.id = id;
		if(EterniaServer.placeholderConfig.contains(id + ".independentTextComponent")) {
			independentTextComponent = EterniaServer.placeholderConfig.getBoolean(id + ".independentTextComponent");
		} else {
			independentTextComponent = true;
		}
		ConfigurationSection cpSection = EterniaServer.placeholderConfig.getConfigurationSection(id);
		for(String placeholder: cpSection.getKeys(false)) {
			if(!placeholder.equals("independentTextComponent") && !placeholder.equals("list")) {
				ConfigurationSection section = cpSection.getConfigurationSection(placeholder);
				SubPlaceholder subPlaceholder = new SubPlaceholder(placeholder, section);
				placeholders.add(subPlaceholder);
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