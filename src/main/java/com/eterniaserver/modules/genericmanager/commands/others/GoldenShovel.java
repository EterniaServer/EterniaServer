package com.eterniaserver.modules.genericmanager.commands.others;

import com.eterniaserver.configs.Vars;
import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.configs.methods.PlayerMessage;
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
                if (Vars.shovel_cooldown.containsKey(player)) {
                    long secondsLeft = ((Vars.shovel_cooldown.get(player) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
                    if (secondsLeft > 0) {
                        new PlayerMessage("server.timing", secondsLeft, player);
                        return true;
                    }
                }
                Vars.shovel_cooldown.put(player, System.currentTimeMillis());
                PlayerInventory inventory = player.getInventory();
                inventory.addItem(new ItemStack(Material.GOLDEN_SHOVEL));
                new PlayerMessage("other.shovel", player);
            } else {
                new PlayerMessage("server.no-perm", player);
            }
        } else {
            new ConsoleMessage("server.only-player");
        }
        return true;
    }
}