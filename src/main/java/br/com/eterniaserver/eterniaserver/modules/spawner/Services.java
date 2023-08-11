package br.com.eterniaserver.eterniaserver.modules.spawner;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class Spawner {

        private final EterniaServer plugin;
        private final Component[] entityComponents = new Component[EntityType.values().length];

        protected Spawner(final EterniaServer plugin) {
            this.plugin = plugin;

            for (EntityType type : EntityType.values()) {
                this.entityComponents[type.ordinal()] = plugin.parseColor(
                        plugin.getString(Strings.MINI_MESSAGES_SPAWNERS_FORMAT).replace("%spawner_name%", type.name())
                );
            }
        }

        private Component getSpawnerName(final EntityType entityType) {
            return this.entityComponents[entityType.ordinal()];
        }

        public ItemStack createSpawner(EntityType entityType, int amount) {
            ItemStack item = new ItemStack(Material.SPAWNER, amount);

            setBasicData(item, entityType);

            return item;
        }

        public ItemStack getSpawner(Block block) {
            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            EntityType spawnerType = spawner.getSpawnedType();
            ItemStack item = new ItemStack(block.getType());

            setBasicData(item, spawnerType);

            return item;
        }

        private void setBasicData(ItemStack item, EntityType entityType) {
            ItemMeta meta = item.getItemMeta();

            meta.displayName(getSpawnerName(entityType));
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(plugin.getKey(ItemsKeys.TAG_SPAWNER), PersistentDataType.STRING, entityType.name());

            item.setItemMeta(meta);
        }
    }
}
