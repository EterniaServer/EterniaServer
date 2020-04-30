package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;

import br.com.eterniaserver.configs.methods.Checks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Checks checks;
    private final Vars vars;

    public OnPlayerLeave(EterniaServer plugin, Messages messages, Checks checks, Vars vars) {
        this.plugin = plugin;
        this.messages = messages;
        this.checks = checks;
        this.vars = vars;
    }

    @EventHandler
    public void OnLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.serverConfig.getBoolean("modules.playerchecks")) {
            vars.afktime.remove(player.getName());
        }
        if (plugin.serverConfig.getBoolean("modules.chat")) {
            checks.removeUUIF(event.getPlayer());
            if (player.hasPermission("eternia.spy")) {
                vars.spy.remove(player);
            }
        }
        event.setQuitMessage(null);
        messages.BroadcastMessage("server.leave", "%player_name%", player.getName());
    }

}