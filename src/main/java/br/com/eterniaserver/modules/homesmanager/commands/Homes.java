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
                    final java.lang.String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-home") + " WHERE name='" + player.getName() + "';";
                    final ResultSet rsn = EterniaServer.connection.Query(querie);
                    try {
                        if (rsn.next()) {
                            rsn.getString("homes");
                        }
                        java.lang.String[] values = rsn.getString("homes").split(":");
                        for (java.lang.String line : values) {
                            accounts.append(line).append("&8, &3");
                        }
                        Messages.PlayerMessage("home.list", Strings.getColor(accounts.toString()), player);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
