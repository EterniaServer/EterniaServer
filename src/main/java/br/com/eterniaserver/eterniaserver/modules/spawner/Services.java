package br.com.eterniaserver.eterniaserver.modules.spawner;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import net.kyori.adventure.text.Component;

import org.bukkit.entity.EntityType;

final class Services {

    static class Spawner {

        private final Component[] entityComponents = new Component[EntityType.values().length];

        protected Spawner(final EterniaServer plugin) {
            for (EntityType type : EntityType.values()) {
                this.entityComponents[type.ordinal()] = plugin.parseColor(
                        plugin.getString(Strings.MINI_MESSAGES_SPAWNERS_FORMAT).replace("%spawner_name%", type.name())
                );
            }
        }

        public Component getSpawnerName(final EntityType entityType) {
            return this.entityComponents[entityType.ordinal()];
        }
    }
}
