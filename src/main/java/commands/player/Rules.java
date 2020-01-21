package commands.player;

import center.NetherTrapCheck;
import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Rules implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            if (sender.hasPermission("eternia.rules"))
            {
                if (args.length == 1)
                {
                    int valor = Integer.parseInt(args[0]);
                    if (valor > 0)
                    {
                        int inicio = 5 * (valor - 1);
                        int fim = 6 * (valor);
                        int cont = 0;
                        String regras = NetherTrapCheck.file.getString("regras");
                        assert regras != null;
                        String[] regralista = regras.split("/split/");
                        for (int i = inicio; i < fim; i++)
                        {
                            try
                            {
                                sender.sendMessage(Vars.ChatColor(regralista[i]));
                                cont += 1;
                            }
                            catch (Exception e)
                            {
                                break;
                            }
                        }
                        if (cont == fim - inicio)
                        {
                            sender.sendMessage(Vars.replaceObject("proxima-pagina", Integer.parseInt(args[0]) + 1 ));
                        }
                        return true;
                    }
                    else
                    {
                        sender.sendMessage(Vars.getString("pagina-negativa"));
                    }
                }
                else
                {
                    sender.sendMessage(Vars.getString("precisa-numero"));
                }
            }
            else
            {
                sender.sendMessage(Vars.getString("sem-permissao"));
            }
        }
        return false;
    }
}