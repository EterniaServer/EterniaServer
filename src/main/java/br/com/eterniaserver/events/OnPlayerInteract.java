package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.player.PlayerTeleport;
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

    private final EterniaServer plugin;
    private final Messages messages;
    private final Vars vars;

    public OnPlayerInteract(EterniaServer plugin, Messages messages, Vars vars) {
        this.plugin = plugin;
        this.messages = messages;
        this.vars = vars;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (plugin.serverConfig.getBoolean("modules.home")) {
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
                if (is.getType().equals(Material.COMPASS) && is.getItemMeta() != null && is.getItemMeta().getLore() != null) {
                    Player player = e.getPlayer();
                    String values = is.getItemMeta().getLore().get(0);
                    String[] isso = values.split(":");
                    final Location location = new Location(Bukkit.getWorld(isso[0]), Double.parseDouble(isso[1]) + 1, Double.parseDouble(isso[2]), Double.parseDouble(isso[3]), Float.parseFloat(isso[4]), Float.parseFloat(isso[5]));
                    if (vars.teleports.containsKey(player)) {
                        messages.PlayerMessage("server.telep", player);
                    } else {
                        vars.teleports.put(player, new PlayerTeleport(player, location, "home.suc", plugin));
                    }
                }
            }
        }
        if (plugin.serverConfig.getBoolean("modules.experience")) {
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
                if (is.getType().equals(Material.EXPERIENCE_BOTTLE) && is.getItemMeta() != null && is.getItemMeta().getLore() != null) {
                    e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    e.getPlayer().giveExp(Integer.parseInt(is.getItemMeta().getLore().get(0)));
                }
            }
        }
        if (plugin.serverConfig.getBoolean("modules.spawners")) {
            if (e.getClickedBlock() != null && e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null && e.getClickedBlock().getType() == Material.SPAWNER) {
                if (!e.getPlayer().hasPermission("eternia.change-spawner")) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
