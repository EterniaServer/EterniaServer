package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.eterniaserver.objects.UUIDFetcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OnPlayerLeave implements Listener {

    private final EterniaServer plugin;

    public OnPlayerLeave(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {

        final Player player = event.getPlayer();
        final String playerName = player.getName();
        Vars.afkTime.remove(playerName);
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        int hours = Vars.playerHours.get(uuid) + (int) TimeUnit.MICROSECONDS.toHours(System.currentTimeMillis() - Vars.playerLast.get(uuid));
        Vars.playerHours.put(uuid, hours);
        EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_PLAYER, Strings.HOURS, hours, Strings.UUID, uuid.toString()));


        if (EterniaServer.serverConfig.getBoolean("modules.chat")) {
            plugin.getInternMethods().removeUUIF(player);
            if (player.hasPermission("eternia.spy")) Vars.spy.remove(playerName);
        }

        event.setQuitMessage(null);
        plugin.getEFiles().broadcastMessage(Strings.MSG_LEAVE, Constants.PLAYER, player.getDisplayName());

    }

}