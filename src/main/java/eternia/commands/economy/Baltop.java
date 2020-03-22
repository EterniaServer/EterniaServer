package eternia.commands.economy;

import java.sql.ResultSet;
import java.util.List;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import java.util.ArrayList;

import eternia.EterniaServer;
import eternia.api.MoneyAPI;
import eternia.configs.MVar;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

@SuppressWarnings("NullableProblems")
public class Baltop implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (sender.hasPermission("eternia.baltop")) {
                try {
                    int amount = 10;
                    if (args.length == 1) {
                        if (!this.isnumber(args[0])) {
                            MVar.playerMessage("precisa-numero", player);
                            return true;
                        }
                        amount = Integer.parseInt(args[0]);
                    }
                    final String query = "SELECT * FROM eternia ORDER BY BALANCE DESC LIMIT " + amount + ";";
                    final List<UUID> accounts = new ArrayList<>();
                    ResultSet rs = null;
                    try {
                        rs = EterniaServer.sqlcon.Query(query);
                        while (rs.next()) {
                            final UUID uuid2 = UUID.fromString(rs.getString("player_name"));
                            accounts.add(uuid2);
                        }
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                        try {
                            rs.close();
                        }
                        catch (SQLException ee) {
                            ee.printStackTrace();
                        }
                    }
                    finally {
                        try {
                            assert rs != null;
                            rs.close();
                        }
                        catch (SQLException e2) {
                            e2.printStackTrace();
                        }
                    }
                    MVar.playerMessage("baltop", player);
                    accounts.forEach(uuid -> player.sendMessage("§3" + (accounts.indexOf(uuid) + 1) + " §7Posição§8: §3" + Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName() + " §7Dinheiro§8: §3" + MoneyAPI.getMoney(player.getUniqueId()) + "§8."));
                }
                catch (Exception e3) {
                    e3.printStackTrace();
                }
            } else {
                MVar.playerMessage("sem-permissao", player);
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
    
    public boolean isnumber(String eingabe) {
        boolean zahl = true;
        eingabe = eingabe.trim();
        final char[] c = eingabe.toCharArray();
        for (int i = 0; i < eingabe.length(); ++i) {
            if (!Character.isDigit(c[i])) {
                zahl = false;
            }
        }
        return zahl;
    }
}
