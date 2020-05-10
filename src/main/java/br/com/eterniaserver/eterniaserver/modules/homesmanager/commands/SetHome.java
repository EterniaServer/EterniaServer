package br.com.eterniaserver.eterniaserver.modules.homesmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.modules.homesmanager.HomesManager;

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

public class SetHome implements CommandExecutor {

    private final Messages messages;
    private final HomesManager homesManager;

    public SetHome(Messages messages, HomesManager homesManager) {
        this.messages = messages;
        this.homesManager = homesManager;
    }

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
                        if (homesManager.canHome(player.getName()) < i) {
                            homesManager.setHome(player.getLocation(), args[0].toLowerCase(), player.getName());
                            messages.PlayerMessage("home.def", player);
                        } else {
                            if (homesManager.existHome(args[0].toLowerCase(), player.getName())) {
                                homesManager.setHome(player.getLocation(), args[0].toLowerCase(), player.getName());
                                messages.PlayerMessage("home.def", player);
                            } else {
                                ItemStack item = new ItemStack(Material.COMPASS);
                                ItemMeta meta = item.getItemMeta();
                                if (meta != null) {
                                    final Location loc = player.getLocation();
                                    final String saveloc = loc.getWorld().getName() + ":" + ((int) loc.getX()) + ":" +
                                            ((int) loc.getY()) + ":" + ((int) loc.getZ()) + ":" + ((int) loc.getYaw()) + ":" + ((int) loc.getPitch());
                                    meta.setLore(Collections.singletonList(saveloc));
                                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&e" + args[0].toLowerCase() + "&8]"));
                                    item.setItemMeta(meta);
                                }
                                PlayerInventory inventory = player.getInventory();
                                inventory.addItem(item);
                                messages.PlayerMessage("home.max", player);
                            }
                        }
                    } else {
                        messages.PlayerMessage("home.grand", player);
                    }
                } else {
                    messages.PlayerMessage("home.use", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }

}