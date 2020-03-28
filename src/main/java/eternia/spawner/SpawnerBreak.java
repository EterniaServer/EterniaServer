package eternia.spawner;

import eternia.EterniaServer;
import eternia.configs.CVar;
import eternia.configs.MVar;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpawnerBreak {
    public SpawnerBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material material = block.getType();
        Player player = event.getPlayer();
        if (EterniaServer.getMain().getConfig().getStringList("spawners.blacklisted-worlds").contains(player.getWorld().getName()) && (!player.hasPermission("eternia.spawners.bypass"))) {
            MVar.playerMessage("spawners.block", player);
            event.setCancelled(true);
            return;
        }
        if (player.hasPermission("eternia.spawners.break")) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH) || player.hasPermission("eternia.spawners.nosilk")) {
                CreatureSpawner spawner = (CreatureSpawner) block.getState();

                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                String mob = spawner.getSpawnedType().toString().replace("_", " ");
                String mobFormatted = mob.substring(0, 1).toUpperCase() + mob.substring(1).toLowerCase();
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
                if (CVar.getDouble("spawners.drop-chance") != 1) {
                    double random = Math.random();
                    if (random >= CVar.getDouble("spawners.drop-chance")) {
                        MVar.playerMessage("spawners.no-drop", player);
                        return;
                    }
                }
                if (CVar.getBool("spawners.drop-in-inventory")) {
                    if (player.getInventory().firstEmpty() == -1) {
                        event.setCancelled(true);
                        MVar.playerMessage("spawners.invfull", player);
                        return;
                    }
                    player.getInventory().addItem(item);
                    block.getDrops().clear();
                    return;
                }
                Location loc = block.getLocation();
                Objects.requireNonNull(loc.getWorld()).dropItemNaturally(loc, item);
            } else {
                event.setCancelled(true);
                MVar.playerMessage("spawners.no-silktouch", player);
            }
        } else {
            event.setCancelled(true);
            MVar.playerMessage("server.no-perm", player);
        }
    }
}
