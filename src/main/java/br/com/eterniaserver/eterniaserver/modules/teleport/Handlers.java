package br.com.eterniaserver.eterniaserver.modules.teleport;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

final class Handlers implements Listener {

    private final EterniaServer plugin;
    private final Services.WarpService warpService;

    public Handlers(EterniaServer plugin, Services.WarpService warpService) {
        this.plugin = plugin;
        this.warpService = warpService;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Optional<Location> spawnOptional = warpService.getSpawnLocation();
        spawnOptional.ifPresent(event::setRespawnLocation);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        ItemStack itemStack = event.getItem();
        Player player = event.getPlayer();

        if (itemStack == null
                || !(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))
                || !player.hasPermission(plugin.getString(Strings.PERM_HOME_COMPASS))) {
            return;
        }

        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        Integer function = container.get(plugin.getKey(ItemsKeys.TAG_FUNCTION), PersistentDataType.INTEGER);
        if (function == null) {
            return;
        }

        Location playerLoc = player.getLocation();

        String name = container.getOrDefault(
                plugin.getKey(ItemsKeys.TAG_WORLD), PersistentDataType.STRING, playerLoc.getWorld().getName()
        );
        double worldX = container.getOrDefault(
                plugin.getKey(ItemsKeys.TAG_COORD_X), PersistentDataType.DOUBLE, playerLoc.getX()
        );
        double worldY = container.getOrDefault(
                plugin.getKey(ItemsKeys.TAG_COORD_Y), PersistentDataType.DOUBLE, playerLoc.getY()
        );
        double worldZ = container.getOrDefault(
                plugin.getKey(ItemsKeys.TAG_COORD_Z), PersistentDataType.DOUBLE, playerLoc.getZ()
        );
        float worldYaw = container.getOrDefault(
                plugin.getKey(ItemsKeys.TAG_COORD_YAW), PersistentDataType.FLOAT, playerLoc.getYaw()
        );
        float worldPitch = container.getOrDefault(
                plugin.getKey(ItemsKeys.TAG_COORD_PITCH), PersistentDataType.FLOAT, playerLoc.getPitch()
        );
        String locName = container.getOrDefault(
                plugin.getKey(ItemsKeys.TAG_LOC_NAME), PersistentDataType.STRING, "DEFAULT"
        );

        Utils.TeleportCommand.addTeleport(
                plugin,
                player,
                new Location(
                        plugin.getServer().getWorld(name),
                        worldX,
                        worldY,
                        worldZ,
                        worldYaw,
                        worldPitch
                ),
                locName
        );

        event.setCancelled(true);
    }
}
