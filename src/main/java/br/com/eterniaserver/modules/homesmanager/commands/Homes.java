package br.com.eterniaserver.modules.homesmanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.MVar;
import br.com.eterniaserver.configs.methods.PlayerMessage;
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.homes")) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    StringBuilder accounts = new StringBuilder();
                    final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-home") + " WHERE name='" + player.getName() + "';";
                    final ResultSet rsn = EterniaServer.sqlcon.Query(querie);
                    try {
                        if (rsn.next()) {
                            rsn.getString("homes");
                        }
                        String[] values = rsn.getString("homes").split(":");
                        for (String line : values) {
                            accounts.append(line).append("&8, &3");
                        }
                        new PlayerMessage("home.list", MVar.getColor(accounts.toString()), player);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                new PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
