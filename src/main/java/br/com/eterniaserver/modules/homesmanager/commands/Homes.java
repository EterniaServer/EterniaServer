package br.com.eterniaserver.modules.homesmanager.commands;

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

public class Homes implements CommandExecutor {

    private final EterniaServer plugin;

    public Homes(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, java.lang.String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.homes")) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    StringBuilder accounts = new StringBuilder();
                    String[] values = new String[0];
                    try {
                        String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-home") + " WHERE player_name='" + player.getName() + "';";
                        ResultSet rs = EterniaServer.connection.Query(querie);
                        if (rs.next()) {
                            rs.getString("homes");
                        }
                        values = rs.getString("homes").split(":");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    for (String line : values) {
                        accounts.append(line).append("&8, &3");
                    }
                    Messages.PlayerMessage("home.list", Strings.getColor(accounts.toString()), player);
                });
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
