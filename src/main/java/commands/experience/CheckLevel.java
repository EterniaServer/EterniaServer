package commands.experience;

import center.Main;
import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class CheckLevel implements CommandExecutor {
    private final Main plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.checklevel")) {
                UUID uuid = player.getUniqueId();
                try {
                    Statement statement = plugin.getConnection().createStatement();
                    String get_xp = String.format("SELECT XP FROM eternia WHERE UUID='%s'", uuid.toString());
                    ResultSet results = statement.executeQuery(get_xp);
                    String Vu = "";
                    while (results.next()) {
                        Vu = results.getString("XP");
                    }
                    Vars.playerReplaceMessage("tem-xp", Vu, player);
                    results.close();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                Vars.playerMessage("sem-permissão", player);
            }
        } else {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}