package br.com.eterniaserver.modules.spawnermanager.commands;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
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
    public boolean onCommand(CommandSender sender, Command command, java.lang.String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (sender.hasPermission("eternia.spawnergive")) {
                if (args.length == 3) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        Messages.PlayerMessage("server.player-offline", player);
                        return true;
                    }
                    int amount;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        Messages.PlayerMessage("server.no-number", player);
                        return true;
                    }
                    try {
                        java.lang.String type = args[1];
                        EntityType.valueOf(type.toUpperCase());
                        if (amount > 0) {
                            if (target.getInventory().firstEmpty() == -1) {
                                Messages.PlayerMessage("spawners.invfull", player);
                            } else {
                                ItemStack item = new ItemStack(Material.SPAWNER);
                                ItemMeta meta = item.getItemMeta();
                                item.setAmount(amount);
                                java.lang.String mobFormatted = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
                                assert meta != null;
                                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ("&8[" + EterniaServer.configs.getString("spawners.mob-name-color") + "%mob% &7Spawner&8]".replace("%mob%", mobFormatted))));
                                List<java.lang.String> newLore = new ArrayList<>();
                                EterniaServer.configs.getStringList("spawners.lore");
                                if (EterniaServer.configs.getBoolean("spawners.enable-lore")) {
                                    for (java.lang.String line : EterniaServer.configs.getStringList("spawners.lore")) {
                                        newLore.add(ChatColor.translateAlternateColorCodes('&', line.replace("%s", mobFormatted)));
                                    }
                                    meta.setLore(newLore);
                                }
                                item.setItemMeta(meta);
                                target.getInventory().addItem(item);
                                Messages.PlayerMessage("spawners.send", amount, mobFormatted, target.getName(), player);
                                Messages.PlayerMessage("spawners.receive", amount, mobFormatted, player.getName(), target);
                            }
                        } else {
                            Messages.PlayerMessage("server.no-negative", player);
                        }
                    } catch (IllegalArgumentException e) {
                        StringBuilder str = new StringBuilder();
                        for (EntityType entity : EntityType.values()) {
                            str.append(entity.name().toLowerCase());
                            str.append(", ");
                        }
                        str.append("&7algumas entidades não funcionam");
                        Messages.PlayerMessage("spawners.types", str.toString(), player);
                    }
                } else {
                    Messages.PlayerMessage("spawners.use", player);
                }
            } else {
                Messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            if (args.length == 3) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    Messages.ConsoleMessage("server.player-offline");
                    return true;
                }
                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    Messages.ConsoleMessage("server.no-number");
                    return true;
                }
                try {
                    java.lang.String type = args[1];
                    EntityType.valueOf(type.toUpperCase());
                    if (amount > 0) {
                        if (target.getInventory().firstEmpty() == -1) {
                            Messages.ConsoleMessage("spawners.invfull");
                        } else {
                            ItemStack item = new ItemStack(Material.SPAWNER);
                            ItemMeta meta = item.getItemMeta();
                            item.setAmount(amount);
                            java.lang.String mobFormatted = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
                            assert meta != null;
                            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ("&8[" + EterniaServer.configs.getString("spawners.mob-name-color") + "%mob% &7Spawner&8]".replace("%mob%", mobFormatted))));
                            List<java.lang.String> newLore = new ArrayList<>();
                            EterniaServer.configs.getStringList("spawners.lore");
                            if (EterniaServer.configs.getBoolean("spawners.enable-lore")) {
                                for (java.lang.String line : EterniaServer.configs.getStringList("spawners.lore")) {
                                    newLore.add(ChatColor.translateAlternateColorCodes('&', line.replace("%s", mobFormatted)));
                                }
                                meta.setLore(newLore);
                            }
                            item.setItemMeta(meta);
                            target.getInventory().addItem(item);
                            Messages.ConsoleMessage("spawners.send", amount, mobFormatted, target.getName());
                            Messages.PlayerMessage("spawners.receive", amount, mobFormatted, "console", target);
                        }
                    } else {
                        Messages.ConsoleMessage("server.no-negative");
                    }
                } catch (IllegalArgumentException e) {
                    StringBuilder str = new StringBuilder();
                    for (EntityType entity : EntityType.values()) {
                        str.append(entity.name().toLowerCase());
                        str.append("&8, &3");
                    }
                    str.append("&7algumas entidades não funcionam");
                    Messages.ConsoleMessage("spawners.types", Strings.getColor(str.toString()));
                }
            } else {
                Messages.ConsoleMessage("spawners.use");
            }
        }
        return true;
    }
}