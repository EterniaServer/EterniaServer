package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.configs.Messages;

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
        if (e.isCancelled()) return;

        final Player player = (Player) e.getWhoClicked();
        final String playerName = player.getName();
        if (plugin.serverConfig.getBoolean("spawners.prevent-anvil") && plugin.serverConfig.getBoolean("modules.spawners")) {
            if (e.getInventory().getType() == InventoryType.ANVIL && Objects.requireNonNull(e.getCurrentItem()).getType() == Material.SPAWNER) {
                e.setCancelled(true);
                messages.sendMessage("spawners.anvil", player);
                messages.sendConsole("spawners.anvil-try", "%player_name%", playerName);
            }
        }
        if (plugin.serverConfig.getBoolean("modules.playerchecks")) {
            vars.afktime.put(playerName, System.currentTimeMillis());
        }
    }

}
