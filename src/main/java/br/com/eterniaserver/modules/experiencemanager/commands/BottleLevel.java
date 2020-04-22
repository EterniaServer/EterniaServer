package br.com.eterniaserver.modules.experiencemanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.methods.Checks;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class BottleLevel implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.bottlexp")) {
                if (args.length == 1) {
                    try {
                        int xp_want = Checks.getXPForLevel(Integer.parseInt(args[0]));
                        int xp_real = Checks.getXPForLevel(player.getLevel());
                        if (Integer.parseInt(args[0]) > 0 && xp_real > xp_want) {
                            ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);
                            ItemMeta meta = item.getItemMeta();
                            if (meta != null) {
                                meta.setLore(Collections.singletonList(String.valueOf(xp_want)));
                                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&eGarrafa de EXP&8]"));
                                item.setItemMeta(meta);
                            }
                            PlayerInventory inventory = player.getInventory();
                            inventory.addItem(item);
                            Messages.PlayerMessage("xp.bottlexp", player);
                            player.setLevel(0);
                            player.setExp(0);
                            player.giveExp(xp_real - xp_want);
                        } else {
                            Messages.PlayerMessage("xp.noxp", player);
                        }
                    } catch (NumberFormatException e) {
                        Messages.PlayerMessage("server.no-number", player);
                    }
                } else {
                    Messages.PlayerMessage("xp.use3", player);
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}