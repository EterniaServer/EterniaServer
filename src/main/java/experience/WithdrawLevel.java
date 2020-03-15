package experience;

import center.Main;
import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class WithdrawLevel implements CommandExecutor {
    private final Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.withdrawlvl")) {
                UUID uuid = player.getUniqueId();
                try {
                    Statement statement = plugin.getConnection().createStatement();
                    String get_xp = String.format("SELECT XP FROM eternia WHERE UUID=%s", uuid.toString());
                    ResultSet results = statement.executeQuery(get_xp);
                    String Vu = "";
                    while (results.next()) {
                        Vu = results.getString("XP");
                    }
                    results.close();
                    player.giveExp(Integer.parseInt(Vu));
                    String att_xp = String.format("UPDATE eternia SET XP=0 WHERE UUID=%s", uuid.toString());
                    statement.executeUpdate(att_xp);
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Vars.playerMessage("tirar-xp-errou", player);
                }
                Vars.playerReplaceMessage("xp-tirar", player.getLevel(), player);
            } else {
                Vars.playerMessage("sem-permissão", player);
            }
        } else {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}