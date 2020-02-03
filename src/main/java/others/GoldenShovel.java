package others;

import center.Vars;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class GoldenShovel implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.goldenshovel"))
            {
                int cooldownTime = 300;
                if (Vars.shovel_cooldown.containsKey(player))
                {
                    long secondsLeft = ((Vars.shovel_cooldown.get(player) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
                    if (secondsLeft > 0)
                    {
                        Vars.playerReplaceMessage("pa-falta-tempo", secondsLeft, player);
                    }
                }
                Vars.shovel_cooldown.put(player, System.currentTimeMillis());
                PlayerInventory inventory = player.getInventory();
                inventory.addItem(new ItemStack(Material.GOLDEN_SHOVEL));
                Vars.playerMessage("pa-sucesso", player);
            }
            else
            {
                Vars.playerMessage("sem-permissao", player);
            }
        }
        else
        {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}