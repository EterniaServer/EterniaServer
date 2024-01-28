package br.com.eterniaserver.eterniaserver.modules.entity;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityBreedEvent;

final class Handlers implements Listener {

    private final EterniaServer plugin;

    public Handlers(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityBreed(EntityBreedEvent event) {
        if (!plugin.getBoolean(Booleans.BREEDING_LIMITER)) {
            return;
        }

        EntityType entityType = event.getEntityType();
        Utils.EntityControl entityControl = plugin.getControl(entityType);

        if (entityControl.getBreedingLimit() == -1) {
            return;
        }

        int amount = 0;
        for (Entity entity : event.getEntity().getLocation().getChunk().getEntities()) {
            if (entity.getType().ordinal() == entityType.ordinal()) {
                if (amount > entityControl.getBreedingLimit()) {
                    event.setCancelled(true);
                    break;
                }
                amount++;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreCreatureSpawn(PreCreatureSpawnEvent event) {
        if (!plugin.getBoolean(Booleans.ENTITY_LIMITER)) {
            return;
        }

        int amount = 0;
        EntityType entityType = event.getType();
        Utils.EntityControl entityControl = plugin.getControl(entityType);

        if (entityControl.getSpawnLimit() == -1) {
            return;
        }

        for (Entity e : event.getSpawnLocation().getChunk().getEntities()) {
            if (e.getType().ordinal() == entityType.ordinal()) {
                if (amount > entityControl.getSpawnLimit()) {
                    event.setCancelled(true);
                    break;
                }
                amount++;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!plugin.getBoolean(Booleans.ENTITY_EDITOR)) {
            return;
        }

        LivingEntity entity = event.getEntity();
        Utils.EntityControl entityControl = plugin.getControl(entity.getType());

        if (!entityControl.isEditorState()) {
            return;
        }

        AttributeInstance maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.setBaseValue(entityControl.getHealth());
            entity.setHealth(entityControl.getHealth());
        }

        AttributeInstance movementSpeed = entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (movementSpeed != null) {
            movementSpeed.setBaseValue(entityControl.getSpeed());
        }

        AttributeInstance attackDamage = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (attackDamage != null) {
            attackDamage.setBaseValue(entityControl.getAttackDamage());
        }
    }
}
