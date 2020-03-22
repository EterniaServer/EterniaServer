package eternia.commands.experience;

import eternia.EterniaServer;
import eternia.configs.MVar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@SuppressWarnings("NullableProblems")
public class CheckLevel implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.checklevel")) {
                UUID uuid = player.getUniqueId();
                int xp;
                try {
                    final ResultSet rs = EterniaServer.sqlcon.Query("SELECT XP FROM eternia WHERE UUID='" + uuid.toString() + "';");
                    if (rs.next()) {
                        rs.getInt("XP");
                    }
                    xp = rs.getInt("XP");
                    MVar.playerReplaceMessage("tem-xp", xp, player);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                MVar.playerMessage("sem-permissão", player);
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
}