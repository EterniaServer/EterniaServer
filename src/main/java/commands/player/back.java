package commands.player;

import center.vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.milkbowl.vault.economy.Economy;

import java.util.Objects;

public class back implements CommandExecutor
{
    private final Economy economy = center.main.getEconomy();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (center.playerlistener.back.containsKey(player))
            {
                double money = economy.getBalance(player);
                double valor = (center.looper.c.getInt("valor-do-back"));
                if(player.hasPermission("eternia.backfree"))
                {
                    player.teleport(center.playerlistener.back.get(player));
                    center.playerlistener.back.remove(player);
                    player.sendMessage(vars.c(center.looper.c.getString("back-gratis")));
                    return true;
                }
                else
                {
                    if (money >= valor)
                    {
                        player.teleport(center.playerlistener.back.get(player));
                        center.playerlistener.back.remove(player);
                        player.sendMessage(vars.c(replaced(Objects.requireNonNull(center.looper.c.getString("back-sucesso")), valor)));
                        economy.withdrawPlayer(player, valor);
                        return true;
                    }
                    else
                    {
                        player.sendMessage(vars.c(replaced(Objects.requireNonNull(center.looper.c.getString("back-sem-dinheiro")), valor)));
                        return false;
                    }
                }
            }
            else
            {
                player.sendMessage(vars.c(center.looper.c.getString("back-nao-pode")));
                return true;
            }
        }
        return false;
    }
    private String replaced(String args, double valor)
    {
        return args.replace("%s", String.valueOf(valor));
    }
}