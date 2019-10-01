package comandos.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class feed implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if(player.hasPermission("eternia.comandos.staff.feed"))
            {
                if (args.length == 0)
                {
                    if (command.getName().equalsIgnoreCase("comandos.staff.feed"))
                    {
                        player.setFoodLevel(20);
                        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você se saciou" +
                                ChatColor.DARK_GRAY + ".");
                        return true;
                    }
                }
                else if (args.length == 1)
                {
                    if (player.hasPermission("eternia.comandos.staff.feed.other"))
                    {
                        String targetS = args[0];
                        Player target = Bukkit.getPlayer(targetS);
                        assert target != null;
                        if (target.isOnline())
                        {
                            target.setFoodLevel(20);
                            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                    "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você saciou o jogador " + ChatColor.DARK_AQUA +
                                    ChatColor.DARK_AQUA + target.getName() + ChatColor.DARK_GRAY + ".");
                            target.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                    "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você foi saciado por " + ChatColor.DARK_AQUA +
                                    ChatColor.DARK_AQUA + player.getName() + ChatColor.DARK_GRAY + ".");
                            return true;
                        } else {
                            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                    "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "O jogador não está online" +
                                    ChatColor.DARK_GRAY + ".");
                            return true;
                        }
                    } else {
                        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você não tem permissão para isso" +
                                ChatColor.DARK_GRAY + ".");
                        return true;
                    }
                }
            } else {
                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                        "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você não tem permissão para isso" +
                        ChatColor.DARK_GRAY + ".");
                return true;
            }
        }
        return false;
    }
}