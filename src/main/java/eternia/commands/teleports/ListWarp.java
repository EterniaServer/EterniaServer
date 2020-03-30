package eternia.commands.teleports;

import eternia.EterniaServer;
import eternia.api.MoneyAPI;
import eternia.configs.MVar;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ListWarp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.listwarp")) {
                final String query = "SELECT * FROM warp;";
                Bukkit.getScheduler().runTaskAsynchronously(EterniaServer.getMain(), () -> {
                    String accounts = "";
                    ResultSet rs = null;
                    try {
                        rs = EterniaServer.sqlcon.Query(query);
                        while (rs.next()) {
                            final String string2 = rs.getString("name");
                            accounts = string2 + "&8, &3";
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
                    MVar.playerReplaceMessage("warps.list", accounts, player);
                });
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
