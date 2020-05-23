package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class OnBlockBreak implements Listener {

    private final EterniaServer plugin;
    private final Messages messages;

    public OnBlockBreak(EterniaServer plugin, Messages messages) {
        this.plugin = plugin;
        this.messages = messages;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent breakEvent) {
        if (breakEvent.isCancelled()) return;

        final Player player = breakEvent.getPlayer();
        final Block block = breakEvent.getBlock();
        final Material material = block.getType();
        if (plugin.serverConfig.getBoolean("modules.spawners")) {
            if (material == Material.SPAWNER) {
                if (plugin.serverConfig.getStringList("spawners.blacklisted-worlds").contains(player.getWorld().getName()) && (!player.hasPermission("eternia.spawners.bypass"))) {
                    messages.sendMessage("spawners.block", player);
                    breakEvent.setCancelled(true);
                    return;
                }
                if (player.hasPermission("eternia.spawners.break")) {
                    ItemStack itemInHand = player.getInventory().getItemInMainHand();
                    if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH) || player.hasPermission("eternia.spawners.nosilk")) {
                        final CreatureSpawner spawner = (CreatureSpawner) block.getState();
                        ItemStack item = new ItemStack(material);
                        ItemMeta meta = item.getItemMeta();
                        String mob = spawner.getSpawnedType().toString().replace("_", " ");
                        final String mobFormatted = mob.substring(0, 1).toUpperCase() + mob.substring(1).toLowerCase();
                        if (meta != null) {
                            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ("&8[" + plugin.serverConfig.getString("spawners.mob-name-color") + "%mob% &7Spawner&8]".replace("%mob%", mobFormatted))));
                            List<String> newLore = new ArrayList<>();
                            plugin.serverConfig.getStringList("spawners.lore");
                            if (plugin.serverConfig.getBoolean("spawners.enable-lore")) {
                                for (String line : plugin.serverConfig.getStringList("spawners.lore")) {
                                    newLore.add(ChatColor.translateAlternateColorCodes('&', line.replace("%s", mobFormatted)));
                                }
                                meta.setLore(newLore);
                            }
                        }
                        item.setItemMeta(meta);
                        if (plugin.serverConfig.getDouble("spawners.drop-chance") != 1) {
                            double random = Math.random();
                            if (random >= plugin.serverConfig.getDouble("spawners.drop-chance")) {
                                messages.sendMessage("spawners.no-drop", player);
                                return;
                            }
                        }
                        if (plugin.serverConfig.getBoolean("spawners.drop-in-inventory")) {
                            if (player.getInventory().firstEmpty() == -1) {
                                breakEvent.setCancelled(true);
                                messages.sendMessage("spawners.invfull", player);
                                return;
                            }
                            player.getInventory().addItem(item);
                            block.getDrops().clear();
                            breakEvent.setExpToDrop(0);
                            return;
                        }
                        final Location loc = block.getLocation();
                        loc.getWorld().dropItemNaturally(loc, item);
                        breakEvent.setExpToDrop(0);
                    } else {
                        breakEvent.setCancelled(true);
                        messages.sendMessage("spawners.no-silktouch", player);
                    }
                } else {
                    breakEvent.setCancelled(true);
                    messages.sendMessage("server.no-perm", player);
                }
            }
        }
        if (plugin.serverConfig.getBoolean("modules.block-reward")) {
            if (plugin.serverConfig.contains("blocks." + material)) {
                final ConfigurationSection cs = plugin.serverConfig.getConfigurationSection("blocks." + material);
                if (cs == null) return;

                double choice = 1.1, current;
                for (String chance : cs.getKeys(true)) {
                    current = Double.parseDouble(chance);
                    if (current > new Random().nextDouble() && current < choice) {
                        choice = current;
                    }
                }
                if (choice <= 1) {
                    for (String command : plugin.blockConfig.getStringList("blocks." + material + "." + choice)) {
                        String modifiedCommand;

                        if (plugin.hasPlaceholderAPI) modifiedCommand = messages.putPAPI(player, command);
                        else modifiedCommand = command.replace("%player_name%", player.getName());

                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                    }
                }
            }
        }
    }

}
