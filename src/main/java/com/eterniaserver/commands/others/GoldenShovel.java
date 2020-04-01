package com.eterniaserver.commands.others;

import com.eterniaserver.configs.MVar;
import com.eterniaserver.configs.Vars;
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
                        MVar.playerReplaceMessage("server.timing", secondsLeft, player);
                    }
                }
                Vars.shovel_cooldown.put(player, System.currentTimeMillis());
                PlayerInventory inventory = player.getInventory();
                inventory.addItem(new ItemStack(Material.GOLDEN_SHOVEL));
                MVar.playerMessage("other.shovel", player);
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }
}