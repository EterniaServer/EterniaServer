package commands.player;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.milkbowl.vault.economy.Economy;

public class back implements CommandExecutor
{
    private final Economy economy = center.main.getEconomy();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (center.playerlistener.back.containsKey(player))
            {
                if(player.hasPermission("eternia.backfree"))
                {
                    player.teleport(center.playerlistener.back.get(player));
                    center.playerlistener.back.remove(player);
                    player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                            "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você voltou aonde morreu de graça :)" +
                            ChatColor.DARK_GRAY + ".");
                    return true;
                } else {
                    double money = economy.getBalance(player);
                    if (money >= Double.parseDouble("10000"))
                    {
                        player.teleport(center.playerlistener.back.get(player));
                        center.playerlistener.back.remove(player);
                        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você voltou aonde morreu e isso custou" +
                                ChatColor.DARK_AQUA + " 10000" + ChatColor.GRAY + " Eternias" + ChatColor.DARK_GRAY + "!");
                        economy.withdrawPlayer(player, Double.parseDouble("10000"));
                        return true;
                    } else {
                        player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                                "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você não tem" + ChatColor.DARK_AQUA
                                + " 10000" + ChatColor.GRAY + " Eternias" + ChatColor.DARK_GRAY + "!");
                        return false;
                    }
                }
            } else {
                player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                        "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Você ainda não morreu" +
                        ChatColor.DARK_GRAY + ".");
                return true;
            }
        }
        return false;
    }
}