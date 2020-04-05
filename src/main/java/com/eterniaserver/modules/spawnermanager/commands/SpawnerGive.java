package com.eterniaserver.modules.spawnermanager.commands;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.MVar;
import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.configs.methods.PlayerMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SpawnerGive implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (sender.hasPermission("eternia.spawnergive")) {
                if (args.length == 3) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        new PlayerMessage("server.player-offline", player);
                        return true;
                    }
                    int amount;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        new PlayerMessage("server.no-number", player);
                        return true;
                    }
                    try {
                        String type = args[1];
                        EntityType.valueOf(type.toUpperCase());
                        if (amount > 0) {
                            if (target.getInventory().firstEmpty() == -1) {
                                new PlayerMessage("spawners.invfull", player);
                            } else {
                                ItemStack item = new ItemStack(Material.SPAWNER);
                                ItemMeta meta = item.getItemMeta();
                                item.setAmount(amount);
                                String mobFormatted = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
                                assert meta != null;
                                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ("&8[" + CVar.getString("spawners.mob-name-color") + "%mob% &7Spawner&8]".replace("%mob%", mobFormatted))));
                                List<String> newLore = new ArrayList<>();
                                EterniaServer.getMain().getConfig().getStringList("spawners.lore");
                                if (CVar.getBool("spawners.enable-lore")) {
                                    for (String line : EterniaServer.getMain().getConfig().getStringList("spawners.lore")) {
                                        newLore.add(ChatColor.translateAlternateColorCodes('&', line.replace("%s", mobFormatted)));
                                    }
                                    meta.setLore(newLore);
                                }
                                item.setItemMeta(meta);
                                target.getInventory().addItem(item);
                                new PlayerMessage("spawners.send", amount, mobFormatted, target.getName(), player);
                                new PlayerMessage("spawners.receive", amount, mobFormatted, player.getName(), target);
                            }
                        } else {
                            new PlayerMessage("server.no-negative", player);
                        }
                    } catch (IllegalArgumentException e) {
                        StringBuilder str = new StringBuilder();
                        for (EntityType entity : EntityType.values()) {
                            str.append(entity.name().toLowerCase());
                            str.append(", ");
                        }
                        str.append("&7algumas entidades não funcionam");
                        new PlayerMessage("spawners.types", str.toString(), player);
                    }
                } else {
                    new PlayerMessage("spawners.use", player);
                }
            } else {
                new PlayerMessage("server.no-perm", player);
            }
        } else {
            if (args.length == 3) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    new ConsoleMessage("server.player-offline");
                    return true;
                }
                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    new ConsoleMessage("server.no-number");
                    return true;
                }
                try {
                    String type = args[1];
                    EntityType.valueOf(type.toUpperCase());
                    if (amount > 0) {
                        if (target.getInventory().firstEmpty() == -1) {
                            new ConsoleMessage("spawners.invfull");
                        } else {
                            ItemStack item = new ItemStack(Material.SPAWNER);
                            ItemMeta meta = item.getItemMeta();
                            item.setAmount(amount);
                            String mobFormatted = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
                            assert meta != null;
                            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ("&8[" + CVar.getString("spawners.mob-name-color") + "%mob% &7Spawner&8]".replace("%mob%", mobFormatted))));
                            List<String> newLore = new ArrayList<>();
                            EterniaServer.getMain().getConfig().getStringList("spawners.lore");
                            if (CVar.getBool("spawners.enable-lore")) {
                                for (String line : EterniaServer.getMain().getConfig().getStringList("spawners.lore")) {
                                    newLore.add(ChatColor.translateAlternateColorCodes('&', line.replace("%s", mobFormatted)));
                                }
                                meta.setLore(newLore);
                            }
                            item.setItemMeta(meta);
                            target.getInventory().addItem(item);
                            new ConsoleMessage("spawners.send", amount, mobFormatted, target.getName());
                            new PlayerMessage("spawners.receive", amount, mobFormatted, "console", target);
                        }
                    } else {
                        new ConsoleMessage("server.no-negative");
                    }
                } catch (IllegalArgumentException e) {
                    StringBuilder str = new StringBuilder();
                    for (EntityType entity : EntityType.values()) {
                        str.append(entity.name().toLowerCase());
                        str.append("&8, &3");
                    }
                    str.append("&7algumas entidades não funcionam");
                    new ConsoleMessage("spawners.types", MVar.getColor(str.toString()));
                }
            } else {
                new ConsoleMessage("spawners.use");
            }
        }
        return true;
    }
}