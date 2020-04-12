package br.com.eterniaserver.modules.teleportsmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                    final java.lang.String query = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-warp") + ";";
                    StringBuilder accounts = new StringBuilder();
                    ResultSet rs = null;
                    try {
                        rs = EterniaServer.connection.Query(query);
                        while (rs.next()) {
                            final java.lang.String string2 = rs.getString("name");
                            accounts.append(string2).append("&8, &3");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        try {
                            rs.close();
                        } catch (SQLException ee) {
                            ee.printStackTrace();
                        }
                    } finally {
                        try {
                            assert rs != null;
                            rs.close();
                        } catch (SQLException e2) {
                            e2.printStackTrace();
                        }
                    }
                    Messages.PlayerMessage("warps.list", Strings.getColor(accounts.toString()), player);
                });
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
