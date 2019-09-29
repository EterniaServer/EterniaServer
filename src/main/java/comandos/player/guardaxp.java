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

public class guardaxp implements CommandExecutor {
    private final main plugin = main.getPlugin(main.class);
    private int xp1 = 0;
    @EventHandler
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            if(args.length == 1) {
                try {
                    xp1 = player.getLevel();
                    if (xp1 >= Integer.parseInt(args[0])) {
                        xp1 = Integer.parseInt(args[0]);
                        GuardaEXP(uuid, player);
                        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Pronto, você tem " + ChatColor.DARK_AQUA
                                + xp1 + ChatColor.GRAY + " leveis guardados" + ChatColor.DARK_GRAY + ".");
                        return true;
                    } else {
                        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você não tem todos esses leveis"
                                + ChatColor.DARK_GRAY + ".");
                        return true;
                    }
                } catch (Exception e) {
                    player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                            "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Use" + ChatColor.DARK_GRAY + ": " +
                            ChatColor.GOLD + "/guardalvl " + ChatColor.DARK_AQUA + "<quantia>" + ChatColor.GRAY +
                            " não é difícil, é só digitar um número" + ChatColor.DARK_GRAY + ".");
                    return true;
                }
            } else {
                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                        "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Use" +
                        ChatColor.DARK_GRAY + ": " + ChatColor.GOLD + "/guardalvl " + ChatColor.DARK_AQUA + "<quantia>" +
                        ChatColor.DARK_GRAY + ".");
                return true;
            }
        }
        return false;
    }
    private void GuardaEXP(UUID uuid, Player player){
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
            statement.executeUpdate("UPDATE " + plugin.table + " SET XP='"+xp1+"' WHERE UUID='"+uuid.toString()+"'");
            player.setLevel(Math.max(player.getLevel() - (xp1 - XP), 0));
            statement.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}