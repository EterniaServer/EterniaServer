package comandos.player;
import principal.main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class retirarxp implements CommandExecutor {
    private final main plugin = main.getPlugin(main.class);
    private int xp1 = 0;
    @EventHandler
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            try {
                xp1 = player.getLevel();
                RetirarXP(uuid, player);
                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                        "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Pronto seu level agora é " + ChatColor.DARK_AQUA
                        + xp1 + ChatColor.DARK_GRAY + ".");
                return true;
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }
    private void RetirarXP(UUID uuid, Player player){
        try {
            Statement statement = plugin.getConnection().createStatement();
            ResultSet results = statement.executeQuery("SELECT XP FROM " + plugin.table + " WHERE UUID = '"+uuid.toString()+"'");
            String Vu = "";
            int XP;
            while (results.next())
            {
                Vu = results.getString("XP");
            }
            XP = Integer.parseInt(Vu);
            xp1 = Math.max(xp1 + XP, 0);
            int v = 0;
            statement.executeUpdate("UPDATE " + plugin.table + " SET XP='"+v+"' WHERE UUID='"+uuid.toString()+"'");
            player.setLevel(xp1);
            statement.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}