package br.com.eterniaserver.eterniaserver.modules.spawnermanager.commands;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;

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

    private final EterniaServer plugin;
    private final Messages messages;
    private final Strings strings;

    public SpawnerGive(EterniaServer plugin, Messages messages, Strings strings) {
        this.plugin = plugin;
        this.messages = messages;
        this.strings = strings;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, java.lang.String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (sender.hasPermission("eternia.spawnergive")) {
                if (args.length == 3) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        messages.PlayerMessage("server.player-offline", player);
                        return true;
                    }
                    int amount;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        messages.PlayerMessage("server.no-number", player);
                        return true;
                    }
                    try {
                        java.lang.String type = args[1];
                        EntityType.valueOf(type.toUpperCase());
                        if (amount > 0) {
                            if (target.getInventory().firstEmpty() == -1) {
                                messages.PlayerMessage("spawners.invfull", player);
                            } else {
                                ItemStack item = new ItemStack(Material.SPAWNER);
                                ItemMeta meta = item.getItemMeta();
                                item.setAmount(amount);
                                java.lang.String mobFormatted = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
                                assert meta != null;
                                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ("&8[" + plugin.serverConfig.getString("spawners.mob-name-color") + "%mob% &7Spawner&8]".replace("%mob%", mobFormatted))));
                                List<java.lang.String> newLore = new ArrayList<>();
                                plugin.serverConfig.getStringList("spawners.lore");
                                if (plugin.serverConfig.getBoolean("spawners.enable-lore")) {
                                    for (java.lang.String line : plugin.serverConfig.getStringList("spawners.lore")) {
                                        newLore.add(ChatColor.translateAlternateColorCodes('&', line.replace("%s", mobFormatted)));
                                    }
                                    meta.setLore(newLore);
                                }
                                item.setItemMeta(meta);
                                target.getInventory().addItem(item);
                                messages.PlayerMessage("spawners.send", "%amount%", amount, "%mob_type%", mobFormatted, "%target_name%", target.getName(), player);
                                messages.PlayerMessage("spawners.receive", "amount%", amount, "%mob_type%", mobFormatted, "%target_name%", player.getName(), target);
                            }
                        } else {
                            messages.PlayerMessage("server.no-negative", player);
                        }
                    } catch (IllegalArgumentException e) {
                        StringBuilder str = new StringBuilder();
                        for (EntityType entity : EntityType.values()) {
                            str.append(entity.name().toLowerCase());
                            str.append(", ");
                        }
                        str.append("&7algumas entidades não funcionam");
                        messages.PlayerMessage("spawners.types", "%types%", str.toString(), player);
                    }
                } else {
                    messages.PlayerMessage("spawners.use", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            if (args.length == 3) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    messages.sendConsole("server.player-offline");
                    return true;
                }
                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    messages.sendConsole("server.no-number");
                    return true;
                }
                try {
                    java.lang.String type = args[1];
                    EntityType.valueOf(type.toUpperCase());
                    if (amount > 0) {
                        if (target.getInventory().firstEmpty() == -1) {
                            messages.sendConsole("spawners.invfull");
                        } else {
                            ItemStack item = new ItemStack(Material.SPAWNER);
                            ItemMeta meta = item.getItemMeta();
                            item.setAmount(amount);
                            java.lang.String mobFormatted = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
                            assert meta != null;
                            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ("&8[" + plugin.serverConfig.getString("spawners.mob-name-color") + "%mob% &7Spawner&8]".replace("%mob%", mobFormatted))));
                            List<java.lang.String> newLore = new ArrayList<>();
                            plugin.serverConfig.getStringList("spawners.lore");
                            if (plugin.serverConfig.getBoolean("spawners.enable-lore")) {
                                for (java.lang.String line : plugin.serverConfig.getStringList("spawners.lore")) {
                                    newLore.add(ChatColor.translateAlternateColorCodes('&', line.replace("%s", mobFormatted)));
                                }
                                meta.setLore(newLore);
                            }
                            item.setItemMeta(meta);
                            target.getInventory().addItem(item);
                            messages.sendConsole("spawners.send", "%amount%", amount, "%mob_type%", mobFormatted, "%target_name%",target.getName());
                            messages.PlayerMessage("spawners.receive", "%amount%", amount, "%mob_type%", mobFormatted, "%target_name%", "console", target);
                        }
                    } else {
                        messages.sendConsole("server.no-negative");
                    }
                } catch (IllegalArgumentException e) {
                    StringBuilder str = new StringBuilder();
                    for (EntityType entity : EntityType.values()) {
                        str.append(entity.name().toLowerCase());
                        str.append("&8, &3");
                    }
                    str.append("&7algumas entidades não funcionam");
                    messages.sendConsole("spawners.types", "%types%", strings.getColor(str.toString()));
                }
            } else {
                messages.sendConsole("spawners.use");
            }
        }
        return true;
    }
}