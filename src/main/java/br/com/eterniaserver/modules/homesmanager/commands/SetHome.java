package br.com.eterniaserver.modules.homesmanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.homesmanager.sql.HomesAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.Objects;

public class SetHome implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.sethome")) {
                if (args.length == 1) {
                    int i = 4;
                    if (player.hasPermission("eternia.sethome.5")) i = 6;
                    if (player.hasPermission("eternia.sethome.10")) i = 11;
                    if (player.hasPermission("eternia.sethome.15")) i = 16;
                    if (player.hasPermission("eternia.sethome.20")) i = 21;
                    if (player.hasPermission("eternia.sethome.25")) i = 26;
                    if (player.hasPermission("eternia.sethome.30")) i = 31;
                    if (args[0].length() <= 8) {
                        if (HomesAPI.canHome(player.getName()) < i) {
                            HomesAPI.setHome(player.getLocation(), args[0].toLowerCase(), player.getName());
                            Messages.PlayerMessage("home.def", player);
                        } else {
                            if (HomesAPI.existHome(args[0].toLowerCase(), player.getName())) {
                                HomesAPI.setHome(player.getLocation(), args[0].toLowerCase(), player.getName());
                                Messages.PlayerMessage("home.def", player);
                            } else {
                                ItemStack item = new ItemStack(Material.COMPASS);
                                ItemMeta meta = item.getItemMeta();
                                if (meta != null) {
                                    final Location loc = player.getLocation();
                                    final String saveloc = Objects.requireNonNull(loc.getWorld()).getName() + ":" + ((int) loc.getX()) + ":" +
                                            ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
                                    meta.setLore(Collections.singletonList(saveloc));
                                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&e" + args[0].toLowerCase() + "&8]"));
                                    item.setItemMeta(meta);
                                }
                                PlayerInventory inventory = player.getInventory();
                                inventory.addItem(item);
                                Messages.PlayerMessage("home.max", player);
                            }
                        }
                    } else {
                        Messages.PlayerMessage("home.grand", player);
                    }
                } else {
                    Messages.PlayerMessage("home.use", player);
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