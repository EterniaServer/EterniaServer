package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.Messages;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Objects;

public class OnInventoryClick implements Listener {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Vars vars;

    public OnInventoryClick(EterniaServer plugin, Messages messages, Vars vars) {
        this.plugin = plugin;
        this.messages = messages;
        this.vars = vars;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (plugin.serverConfig.getBoolean("spawners.prevent-anvil") && plugin.serverConfig.getBoolean("modules.spawners")) {
            if (e.getInventory().getType() == InventoryType.ANVIL && Objects.requireNonNull(e.getCurrentItem()).getType() == Material.SPAWNER) {
                Player player = (Player) e.getWhoClicked();
                e.setCancelled(true);
                messages.PlayerMessage("spawners.anvil", player);
                messages.ConsoleMessage("spawners.anvil-try", "%player_name%", player.getName());
            }
        }
        if (plugin.serverConfig.getBoolean("modules.playerchecks")) {
            vars.afktime.put(e.getWhoClicked().getName(), System.currentTimeMillis());
        }
    }

}
