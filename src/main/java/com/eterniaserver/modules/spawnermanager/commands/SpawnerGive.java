package com.eterniaserver.modules.spawnermanager.commands;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.MVar;
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
                        MVar.playerMessage("server.player-offline", player);
                        return true;
                    }
                    int amount;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        MVar.playerMessage("server.no-number", player);
                        return true;
                    }
                    try {
                        String type = args[1];
                        EntityType.valueOf(type.toUpperCase());
                        if (amount > 0) {
                            if (target.getInventory().firstEmpty() == -1) {
                                MVar.playerMessage("spawners.invfull", player);
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
                                MVar.playerReplaceMessage("spawners.send", amount, mobFormatted, target.getName(), player);
                                MVar.playerReplaceMessage("spawners.receive", amount, mobFormatted, player.getName(), target);
                            }
                        } else {
                            MVar.playerMessage("server.no-negative", player);
                        }
                    } catch (IllegalArgumentException e) {
                        StringBuilder str = new StringBuilder();
                        for (EntityType entity : EntityType.values()) {
                            str.append(entity.name().toLowerCase());
                            str.append(", ");
                        }
                        str.append("&7algumas entidades não funcionam");
                        MVar.playerReplaceMessage("spawners.types", str.toString(), player);
                    }
                } else {
                    MVar.playerMessage("spawners.use", player);
                }
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        } else {
            if (args.length == 3) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    MVar.consoleMessage("server.player-offline");
                    return true;
                }
                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    MVar.consoleMessage("server.no-number");
                    return true;
                }
                try {
                    String type = args[1];
                    EntityType.valueOf(type.toUpperCase());
                    if (amount > 0) {
                        if (target.getInventory().firstEmpty() == -1) {
                            MVar.consoleMessage("spawners.invfull");
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
                            MVar.consoleReplaceMessage("spawners.send", amount, mobFormatted, target.getName());
                            MVar.playerReplaceMessage("spawners.receive", amount, mobFormatted, "console", target);
                        }
                    } else {
                        MVar.consoleMessage("server.no-negative");
                    }
                } catch (IllegalArgumentException e) {
                    StringBuilder str = new StringBuilder();
                    for (EntityType entity : EntityType.values()) {
                        str.append(entity.name().toLowerCase());
                        str.append("&8, &3");
                    }
                    str.append("&7algumas entidades não funcionam");
                    MVar.consoleReplaceMessage("spawners.types", str.toString());
                }
            } else {
                MVar.consoleMessage("spawners.use");
            }
        }
        return true;
    }
}