package eternia.commands.economy;

import java.sql.ResultSet;
import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;

import eternia.EterniaServer;
import eternia.api.MoneyAPI;
import eternia.configs.MVar;
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
                final String query = "SELECT * FROM eternia ORDER BY BALANCE DESC LIMIT " + 10 + ";";
                final List<String> accounts = new ArrayList<>();
                Bukkit.getScheduler().runTaskAsynchronously(EterniaServer.getMain(), () -> {
                    ResultSet rs = null;
                    try {
                        rs = EterniaServer.sqlcon.Query(query);
                        while (rs.next()) {
                            final String string2 = rs.getString("NAME");
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
                    MVar.playerMessage("eco.baltop", player);
                    accounts.forEach(name -> MVar.playerReplaceMessage("eco.ballist", (accounts.indexOf(name) + 1), name, MoneyAPI.getMoney(name), player));
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
