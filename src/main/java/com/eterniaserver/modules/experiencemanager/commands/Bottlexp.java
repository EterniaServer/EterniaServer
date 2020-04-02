package com.eterniaserver.modules.experiencemanager.commands;

import com.eterniaserver.configs.MVar;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Bottlexp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.bottlexp")) {
                int xp_real = 0;
                int xp_atual = player.getLevel();
                if (xp_atual >= 1 && xp_atual < 16) {
                    xp_real = (xp_atual * xp_atual) + 6 * xp_atual;
                } else if (xp_atual >= 16 && xp_atual < 31) {
                    xp_real = (int) (2.5 * (xp_atual * xp_atual) - 40.5 * xp_atual + 360);
                } else if (xp_atual >= 31) {
                    xp_real = (int) (4.5 * (xp_atual * xp_atual) - 162.5 * xp_atual + 2220);
                }
                if (xp_real >= 10) {
                    xp_real = (xp_real / 10);
                    player.getWorld().dropItem(player.getLocation().add(0, 1, 0), new ItemStack(Material.EXPERIENCE_BOTTLE, xp_real));
                    MVar.playerMessage("xp.bottlexp", player);
                    player.setLevel(0);
                } else {
                    MVar.playerMessage("xp.noxp", player);
                }
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }
}