package commands.player;

import center.Main;
import center.PlayerListener;
import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.milkbowl.vault.economy.Economy;

public class back implements CommandExecutor
{
    private final Economy economy = Main.getEconomy();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.back"))
            {
                if (PlayerListener.back.containsKey(player))
                {
                    double money = economy.getBalance(player);
                    double valor = Vars.getInteiro("valor-do-back");
                    if (player.hasPermission("eternia.backfree"))
                    {
                        player.teleport(PlayerListener.back.get(player));
                        PlayerListener.back.remove(player);
                        player.sendMessage(Vars.getString("back-gratis"));
                        return true;
                    }
                    else
                    {
                        if (money >= valor)
                        {
                            player.teleport(PlayerListener.back.get(player));
                            PlayerListener.back.remove(player);
                            player.sendMessage(Vars.replaceObject("back-sucesso", valor));
                            economy.withdrawPlayer(player, valor);
                            return true;
                        }
                        else
                        {
                            player.sendMessage(Vars.replaceObject("back-sem-dinheiro", valor));
                            return false;
                        }
                    }
                }
                else
                {
                    player.sendMessage(Vars.getString("back-nao-pode"));
                    return true;
                }
            }
            else
            {
                player.sendMessage(Vars.getString("sem-permissao"));
                return true;
            }
        }
        return false;
    }
}