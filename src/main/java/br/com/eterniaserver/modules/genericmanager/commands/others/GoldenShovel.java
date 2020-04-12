package br.com.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class GoldenShovel implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.goldenshovel")) {
                int cooldownTime = 300;
                if (Vars.shovel_cooldown.containsKey(player.getName())) {
                    long secondsLeft = ((Vars.shovel_cooldown.get(player.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
                    if (secondsLeft > 0) {
                        Messages.PlayerMessage("server.timing", secondsLeft, player);
                        return true;
                    }
                }
                Vars.shovel_cooldown.put(player.getName(), System.currentTimeMillis());
                PlayerInventory inventory = player.getInventory();
                inventory.addItem(new ItemStack(Material.GOLDEN_SHOVEL));
                Messages.PlayerMessage("other.shovel", player);
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}