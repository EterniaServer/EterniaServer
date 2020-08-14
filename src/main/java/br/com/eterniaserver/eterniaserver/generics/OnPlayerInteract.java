package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import com.google.common.collect.ImmutableList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OnPlayerInteract implements Listener {

    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        final Action action = e.getAction();

        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            final ItemStack is = player.getInventory().getItemInMainHand();
            final List<String> lore = is.getLore();

            if (EterniaServer.serverConfig.getBoolean("modules.home") && is.getType().equals(Material.COMPASS)
                    && lore != null) {
                final String[] isso = lore.get(0).split(":");
                final Location location = new Location(Bukkit.getWorld(isso[0]), Double.parseDouble(isso[1]) + 1, Double.parseDouble(isso[2]), Double.parseDouble(isso[3]), Float.parseFloat(isso[4]), Float.parseFloat(isso[5]));

                if (Vars.teleports.containsKey(player)) {
                    player.sendMessage(Strings.MSG_IN_TELEPORT);
                } else {
                    Vars.teleports.put(player, new PlayerTeleport(player, location, Strings.M_HOME_DONE));
                }
                e.setCancelled(true);
            }

            if (EterniaServer.serverConfig.getBoolean("modules.experience") && is.getType().equals(Material.EXPERIENCE_BOTTLE)
                    && lore != null) {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                player.giveExp(Integer.parseInt(lore.get(0)));
            }

            if (e.getClickedBlock() != null && list.contains(e.getClickedBlock().getType())) {
                final Location location = e.getClickedBlock().getLocation();
                location.getNearbyEntities(1, 1, 1).forEach(k -> {
                    if (k instanceof Minecart) {
                        e.setCancelled(true);
                    }
                });
            }
        }

        if (EterniaServer.serverConfig.getBoolean("modules.spawners") && e.getClickedBlock() != null
                && action.equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null
                && e.getClickedBlock().getType() == Material.SPAWNER
                && !player.hasPermission("eternia.change-spawner")) {
            e.setCancelled(true);
        }
    }

    private final List<Material> list = ImmutableList.of(Material.RAIL, Material.POWERED_RAIL, Material.DETECTOR_RAIL, Material.ACTIVATOR_RAIL);

}
