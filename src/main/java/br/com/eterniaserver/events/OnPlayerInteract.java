package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class OnPlayerInteract implements Listener {

    private EterniaServer plugin;

    public OnPlayerInteract (EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (EterniaServer.configs.getBoolean("modules.home")) {
            e.getAction();
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
                if (is.getType().equals(Material.COMPASS) && is.getItemMeta() != null && is.getItemMeta().getLore() != null) {
                    Player player = e.getPlayer();
                    String values = is.getItemMeta().getLore().get(0);
                    String[] isso = values.split(":");
                    final Location location = new Location(Bukkit.getWorld(isso[0]), Double.parseDouble(isso[1]), Double.parseDouble(isso[2]), Double.parseDouble(isso[3]), Float.parseFloat(isso[4]), Float.parseFloat(isso[5]));
                    if (player.hasPermission("eternia.timing.bypass")) {
                        player.teleport(location);
                        Messages.PlayerMessage("home.suc", player);
                    } else {
                        Messages.PlayerMessage("teleport.timing", EterniaServer.configs.getInt("server.cooldown"), player);
                        Vars.playerposition.put(player.getName(), player.getLocation());
                        Vars.moved.put(player.getName(), false);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            if (!Vars.moved.get(player.getName())) {
                                player.teleport(location);
                                Messages.PlayerMessage("home.suc", player);
                            } else {
                                Messages.PlayerMessage("warps.move", player);
                            }
                        }, 20 * EterniaServer.configs.getInt("server.cooldown"));
                    }
                }
            }
        }
    }
}
