package br.com.eterniaserver.modules.economymanager.commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.API.MoneyAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class Baltop implements CommandExecutor {

    private final EterniaServer plugin;

    public Baltop(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (sender.hasPermission("eternia.baltop")) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-money") + " ORDER BY balance DESC LIMIT " + 10 + ";";
                    List<String> accounts = new ArrayList<>();
                    EterniaServer.connection.executeSQLQuery(connection -> {
                        PreparedStatement getbaltop = connection.prepareStatement(querie);
                        ResultSet resultSet = getbaltop.executeQuery();
                        while (resultSet.next()) {
                            final String warpname = resultSet.getString("player_name");
                            accounts.add(warpname);
                        }
                        resultSet.close();
                        getbaltop.close();
                    });
                    DecimalFormat df2 = new DecimalFormat(".##");
                    Messages.PlayerMessage("eco.baltop", player);
                    accounts.forEach(name -> Messages.PlayerMessage("eco.ballist", "%position%", (accounts.indexOf(name) + 1), "%player_name%", name, "%money%", df2.format(MoneyAPI.getMoney(name)), player));
                });
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }

}
