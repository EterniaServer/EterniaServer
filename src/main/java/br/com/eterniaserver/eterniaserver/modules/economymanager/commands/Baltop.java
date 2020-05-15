package br.com.eterniaserver.eterniaserver.modules.economymanager.commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.API.Money;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class Baltop implements CommandExecutor {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Money moneyx;

    public Baltop(EterniaServer plugin, Messages messages, Money moneyx) {
        this.plugin = plugin;
        this.messages = messages;
        this.moneyx = moneyx;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (sender.hasPermission("eternia.baltop")) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-money") + " ORDER BY balance DESC LIMIT " + 10 + ";";
                    List<String> accounts = new ArrayList<>();
                    plugin.connections.executeSQLQuery(connection -> {
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
                    messages.PlayerMessage("eco.baltop", player);
                    accounts.forEach(name -> messages.PlayerMessage("eco.ballist", "%position%", (accounts.indexOf(name) + 1), "%player_name%", name, "%money%", df2.format(moneyx.getMoney(name)), player));
                });
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.sendConsole("server.only-player");
        }
        return true;
    }

}
