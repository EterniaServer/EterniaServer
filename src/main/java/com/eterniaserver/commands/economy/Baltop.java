package com.eterniaserver.commands.economy;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.api.MoneyAPI;
import com.eterniaserver.configs.MVar;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class Baltop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (sender.hasPermission("eternia.baltop")) {
                Bukkit.getScheduler().runTaskAsynchronously(EterniaServer.getMain(), () -> {
                    final String query = "SELECT * FROM economy ORDER BY balance DESC LIMIT " + 10 + ";";
                    List<String> accounts = new ArrayList<>();
                    ResultSet rs = null;
                    try {
                        rs = EterniaServer.sqlcon.Query(query);
                        while (rs.next()) {
                            final String string2 = rs.getString("player_name");
                            accounts.add(string2);
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
                    DecimalFormat df2 = new DecimalFormat(".##");
                    MVar.playerMessage("eco.baltop", player);
                    accounts.forEach(name -> MVar.playerReplaceMessage("eco.ballist", (accounts.indexOf(name) + 1), name, df2.format(MoneyAPI.getMoney(name)), player));
                });
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }

}
