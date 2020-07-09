package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.ParseException;

public class OnPlayerJoin implements Listener {

    private final EterniaServer plugin;

    public OnPlayerJoin(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String playerName = player.getName();
        if (plugin.serverConfig.getBoolean("modules.chat")) {
            plugin.getInternMethods().addUUIF(player);
            Vars.global.put(playerName, 0);
            if (player.hasPermission("eternia.spy")) Vars.spy.put(player, true);
            if (!Vars.playerMuted.containsKey(playerName)) checkMuted(playerName);
        }
        if (plugin.serverConfig.getBoolean("modules.economy") && !Vars.balances.containsKey(playerName)) {
            Vars.balances.put(playerName, 300.0);
            EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + plugin.serverConfig.getDouble("money.start") + "');");
        }
        event.setJoinMessage(null);
        plugin.getEFiles().broadcastMessage("server.join", "%player_name%", playerName);
    }

    private void checkMuted(String playerName) {
        final String time = EQueries.queryString("SELECT * FROM " + plugin.serverConfig.getString("sql.table-muted") + " WHERE player_name='" + playerName + "';", "time");
        if (!time.equals("")) {
            try {
                Vars.playerMuted.put(playerName, plugin.sdf.parse(time).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Vars.playerMuted.put(playerName, System.currentTimeMillis());
        }
    }

}