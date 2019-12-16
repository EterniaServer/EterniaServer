package commands.player;
import center.main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class depositlvl implements CommandExecutor
{
    private final main plugin = main.getPlugin(main.class);
    @EventHandler
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            if(args.length == 1)
            {
                try
                {
                    int xp_atual = player.getLevel();
                    if (xp_atual >= Integer.parseInt(args[0]))
                    {
                        xp_atual = Integer.parseInt(args[0]);
                        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Pronto, você tem " + ChatColor.DARK_AQUA
                                + xp_atual + ChatColor.GRAY + " leveis guardados" + ChatColor.DARK_GRAY + ".");
                        try
                        {
                            int xp = xp_atual;
                            if (xp >= 1 && xp <= 16)
                            {
                                xp = xp * (6 + xp);
                            }
                            else if (xp >= 17 && xp <= 25)
                            {
                                xp = xp * (8 + xp);
                            }
                            else if (xp >= 26 && xp <= 30)
                            {
                                xp = xp * (16 + xp);
                            }
                            else
                            {
                                xp = xp * (33 + xp);
                            }
                            Statement statement = plugin.getConnection().createStatement();
                            ResultSet results = statement.executeQuery("SELECT XP FROM " + plugin.table + " WHERE UUID = '"+uuid.toString()+"'");
                            String Vu = "";
                            while (results.next())
                            {
                                Vu = results.getString("XP");
                            }
                            int XP = Integer.parseInt(Vu);
                            xp = Math.max(xp + XP, 0);
                            results.close();
                            statement.executeUpdate("UPDATE " + plugin.table + " SET XP='"+xp+"' WHERE UUID='"+uuid.toString()+"'");
                            player.setLevel(Math.max(player.getLevel() - xp_atual, 0));
                            statement.close();
                        }
                        catch(SQLException e)
                        {
                            e.printStackTrace();
                        }
                        return true;
                    } else {
                        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você não tem todos esses leveis"
                                + ChatColor.DARK_GRAY + ".");
                        return true;
                    }
                }
                catch (Exception e)
                {
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
}