package commands.player;
import center.sql.xpmanager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import java.util.UUID;

public class depositlvl implements CommandExecutor
{
    final private xpmanager xpm = new xpmanager();
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
                        xpm.deposit_xp(uuid, player);
                        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Pronto, você tem " + ChatColor.DARK_AQUA
                                + xp_atual + ChatColor.GRAY + " leveis guardados" + ChatColor.DARK_GRAY + ".");
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