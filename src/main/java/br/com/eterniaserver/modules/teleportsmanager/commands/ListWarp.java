package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ListWarp implements CommandExecutor {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Strings strings;

    public ListWarp(EterniaServer plugin, Messages messages, Strings strings) {
        this.plugin = plugin;
        this.messages = messages;
        this.strings = strings;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, java.lang.String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.listwarp")) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-warp") + ";";
                    StringBuilder accounts = new StringBuilder();
                    plugin.connections.executeSQLQuery(connection -> {
                        PreparedStatement getHome = connection.prepareStatement(querie);
                        ResultSet resultSet = getHome.executeQuery();
                        while (resultSet.next()) {
                            final String warpname = resultSet.getString("name");
                            accounts.append(warpname).append("&8, &3");
                        }
                        resultSet.close();
                        getHome.close();
                    });
                    messages.PlayerMessage("warps.list", "%warps%", strings.getColor(accounts.toString()), player);
                });
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
