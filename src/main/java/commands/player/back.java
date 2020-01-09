package commands.player;

import center.vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.milkbowl.vault.economy.Economy;

public class back implements CommandExecutor
{
    private final Economy economy = center.main.getEconomy();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.back"))
            {
                if (center.playerlistener.back.containsKey(player))
                {
                    double money = economy.getBalance(player);
                    double valor = vars.getInteiro("valor-do-back");
                    if (player.hasPermission("eternia.backfree"))
                    {
                        player.teleport(center.playerlistener.back.get(player));
                        center.playerlistener.back.remove(player);
                        player.sendMessage(vars.getString("back-gratis"));
                        return true;
                    }
                    else
                    {
                        if (money >= valor)
                        {
                            player.teleport(center.playerlistener.back.get(player));
                            center.playerlistener.back.remove(player);
                            player.sendMessage(vars.replaceObject("back-sucesso", valor));
                            economy.withdrawPlayer(player, valor);
                            return true;
                        }
                        else
                        {
                            player.sendMessage(vars.replaceObject("back-sem-dinheiro", valor));
                            return false;
                        }
                    }
                }
                else
                {
                    player.sendMessage(vars.getString("back-nao-pode"));
                    return true;
                }
            }
            else
            {
                player.sendMessage(vars.getString("sem-permissao"));
                return true;
            }
        }
        return false;
    }
}