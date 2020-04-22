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

    public ListWarp(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, java.lang.String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.listwarp")) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-warp") + ";";
                    StringBuilder accounts = new StringBuilder();
                    EterniaServer.connection.executeSQLQuery(connection -> {
                        PreparedStatement getHome = connection.prepareStatement(querie);
                        ResultSet resultSet = getHome.executeQuery();
                        while (resultSet.next()) {
                            final String warpname = resultSet.getString("name");
                            accounts.append(warpname).append("&8, &3");
                        }
                        resultSet.close();
                        getHome.close();
                    });
                    Messages.PlayerMessage("warps.list", "%warps%", Strings.getColor(accounts.toString()), player);
                });
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
