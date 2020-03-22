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
public class DepositLevel implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.depositlvl")) {
                UUID uuid = player.getUniqueId();
                if (args.length == 1) {
                    int xp_atual = player.getLevel();
                    if (xp_atual >= Integer.parseInt(args[0])) {
                        xp_atual = Integer.parseInt(args[0]);
                        MVar.playerReplaceMessage("xp-guardado", xp_atual, player);
                        int xp = xp_atual;
                        if (xp >= 1 && xp <= 15) {
                            xp = (xp * xp) + (6 * xp);
                        } else if (xp >= 16 && xp <= 30) {
                            xp = (int) ((2.5 * (xp * xp)) - (40.5 * xp) + 360);
                        } else {
                            xp = (int) ((4.5 * (xp * xp)) - (162.5 * xp) + 2220);
                        }
                        int XP = 0;
                        try {
                            final ResultSet rs = EterniaServer.sqlcon.Query("SELECT XP FROM eternia WHERE UUID='" + uuid.toString() + "';");
                            if (rs.next()) {
                                rs.getInt("XP");
                            }
                            XP = rs.getInt("XP");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        xp = Math.max(xp + XP, 0);
                        EterniaServer.sqlcon.Update("UPDATE eternia SET XP = '" + xp + "' WHERE UUID='" + uuid.toString() + "';");
                        player.setLevel(Math.max(player.getLevel() - xp_atual, 0));
                    } else {
                        MVar.playerMessage("xp-insuficiente", player);
                    }
                } else {
                    MVar.playerMessage("guarda-level", player);
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