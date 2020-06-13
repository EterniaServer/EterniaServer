package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import me.clip.placeholderapi.PlaceholderAPI;
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
import java.util.Random;

public class OnPlayerBlockBreak implements Listener {

    private final EterniaServer plugin;
    private final EFiles messages;

    public OnPlayerBlockBreak(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final Material material = block.getType();
        if (plugin.serverConfig.getBoolean("modules.spawners") && (material == Material.SPAWNER)) {
            if (plugin.serverConfig.getStringList("spawners.blacklisted-worlds").contains(player.getWorld().getName()) && (!player.hasPermission("eternia.spawners.bypass"))) {
                messages.sendMessage("spawner.others.blocked", player);
                event.setCancelled(true);
                return;
            }
            if (player.hasPermission("eternia.spawners.break")) {
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH) || player.hasPermission("eternia.spawners.nosilk")) {
                    final CreatureSpawner spawner = (CreatureSpawner) block.getState();
                    ItemStack item = new ItemStack(material);
                    ItemMeta meta = item.getItemMeta();
                    String mob = spawner.getSpawnedType().toString().replace("_", " ");
                    String mobFormatted = mob.substring(0, 1).toUpperCase() + mob.substring(1).toLowerCase();
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
                            messages.sendMessage("spawner.others.failed", player);
                            return;
                        }
                    }
                    if (plugin.serverConfig.getBoolean("spawners.drop-in-inventory")) {
                        if (player.getInventory().firstEmpty() == -1) {
                            event.setCancelled(true);
                            messages.sendMessage("spawner.others.inv-full", player);
                            return;
                        }
                        player.getInventory().addItem(item);
                        block.getDrops().clear();
                        event.setExpToDrop(0);
                        return;
                    }
                    final Location loc = block.getLocation();
                    loc.getWorld().dropItemNaturally(loc, item);
                    event.setExpToDrop(0);
                } else {
                    event.setCancelled(true);
                    messages.sendMessage("spawner.others.need-silktouch", player);
                }
            } else {
                event.setCancelled(true);
                messages.sendMessage("server.no-perm", player);
            }
        }
        if (plugin.serverConfig.getBoolean("modules.block-reward") && (plugin.blockConfig.contains("blocks." + material.name().toUpperCase()))) {
            ConfigurationSection cs = plugin.blockConfig.getConfigurationSection("blocks." + material.name().toUpperCase());
            double randomNumber = new Random().nextDouble();
            if (cs != null) {
                List<String> mainList = new ArrayList<>(cs.getKeys(true));
                double lowestNumberAboveRandom = 1.1;
                for (int i = 1; i < cs.getKeys(true).size(); i++) {
                    double current = Double.parseDouble(mainList.get(i));
                    if (current < lowestNumberAboveRandom && current > randomNumber) {
                        lowestNumberAboveRandom = current;
                    }
                }
                if (lowestNumberAboveRandom <= 1) {
                    for (String command : plugin.blockConfig.getStringList("blocks." + material.name().toUpperCase() + "." + lowestNumberAboveRandom)) {
                        String modifiedCommand;
                        if (plugin.hasPlaceholderAPI) {
                            modifiedCommand = putPAPI(event.getPlayer(), command);
                        } else {
                            modifiedCommand = command.replace("%player_name%", event.getPlayer().getPlayerListName());
                        }
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                    }
                }
            }
        }
    }

    private String putPAPI(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

}
