package com.eterniaserver.modules.genericmanager.commands.others;

import java.util.Collections;

import com.eterniaserver.configs.MVar;
import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.configs.methods.PlayerMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemRename implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.renameitem")) {
                if (args.length == 2) {
                    player.getInventory().getItemInMainHand();
                    if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                        if (args[0].equals("name")) {
                            ItemStack item = player.getInventory().getItemInMainHand();
                            ItemMeta meta = item.getItemMeta();
                            if (meta != null) {
                                meta.setDisplayName(MVar.getColor(args[1]));
                                item.setItemMeta(meta);
                                player.getInventory().setItemInMainHand(item);
                            } else {
                                new PlayerMessage("other.noitem", player);
                            }
                        } else if (args[0].equals("lore")) {
                            ItemStack item = player.getInventory().getItemInMainHand();
                            ItemMeta meta = item.getItemMeta();
                            if (meta != null) {
                                meta.setLore(Collections.singletonList(MVar.getColor(args[1])));
                                item.setItemMeta(meta);
                                player.getInventory().setItemInMainHand(item);
                            } else {
                                new PlayerMessage("other.noitem", player);
                            }
                        } else {
                            new PlayerMessage("other.rename", player);
                        }
                    } else {
                        new PlayerMessage("other.noitem", player);
                    }
                } else {
                    new PlayerMessage("other.rename", player);
                }
            } else {
                new PlayerMessage("server.no-perm", player);
            }
        } else {
            new ConsoleMessage("server.only-player");
        }
        return true;
    }

}