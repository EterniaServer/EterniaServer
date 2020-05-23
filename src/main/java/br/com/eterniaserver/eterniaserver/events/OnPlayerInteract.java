package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.player.PlayerTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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
        final Player player = e.getPlayer();
        final Action action = e.getAction();

        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            final ItemStack is = player.getInventory().getItemInMainHand();
            final List<String> lore = is.getLore();

            if (plugin.serverConfig.getBoolean("modules.home")) {
                if (is.getType().equals(Material.COMPASS) && lore != null) {
                    final String[] isso = lore.get(0).split(":");
                    final Location location = new Location(Bukkit.getWorld(isso[0]), Double.parseDouble(isso[1]) + 1, Double.parseDouble(isso[2]), Double.parseDouble(isso[3]), Float.parseFloat(isso[4]), Float.parseFloat(isso[5]));

                    if (vars.teleports.containsKey(player)) messages.sendMessage("server.telep", player);
                    else vars.teleports.put(player, new PlayerTeleport(player, location, "home.suc", plugin));
                }
            }
            if (plugin.serverConfig.getBoolean("modules.experience")) {
                if (is.getType().equals(Material.EXPERIENCE_BOTTLE) && lore != null) {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    player.giveExp(Integer.parseInt(lore.get(0)));
                }
            }
        }
        if (plugin.serverConfig.getBoolean("modules.spawners")) {
            if (e.getClickedBlock() != null && action.equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null && e.getClickedBlock().getType() == Material.SPAWNER) {
                if (!player.hasPermission("eternia.change-spawner")) e.setCancelled(true);
            }
        }
    }
}
