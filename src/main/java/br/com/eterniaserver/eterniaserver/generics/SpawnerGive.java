package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SpawnerGive extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;

    public SpawnerGive(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
    }

    @CommandAlias("spawnergive|givespawner")
    @Syntax("<mob> <quantia> <jogador>")
    @CommandCompletion("@mobs 1 @players")
    @CommandPermission("eternia.spawnergive")
    public void onSpawnerGive(Player player, String spawner, Integer value, OnlinePlayer target) {
        try {
            final Player targetP = target.getPlayer();
            final Inventory inventory = targetP.getInventory();
            EntityType.valueOf(spawner.toUpperCase());
            if (value > 0) {
                if (inventory.firstEmpty() == -1) {
                    messages.sendMessage("spawners.invfull", player);
                } else {
                    ItemStack item = new ItemStack(Material.SPAWNER);
                    ItemMeta meta = item.getItemMeta();
                    item.setAmount(value);
                    java.lang.String mobFormatted = spawner.substring(0, 1).toUpperCase() + spawner.substring(1).toLowerCase();
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
                        item.setItemMeta(meta);
                        inventory.addItem(item);
                        messages.sendMessage("spawner.give.sent", Constants.VALUE.get(), value, Constants.TYPE.get(), mobFormatted, Constants.TARGET.get(), targetP.getDisplayName(), player);
                        messages.sendMessage("spawner.give.received", Constants.VALUE.get(), value, Constants.TYPE.get(), mobFormatted, Constants.TARGET.get(), player.getDisplayName(), targetP);
                    }
                }
            } else {
                messages.sendMessage("server.no-negative", player);
            }
        } catch (IllegalArgumentException e) {
            StringBuilder str = new StringBuilder();
            for (EntityType entity : EntityType.values()) {
                str.append(entity.toString());
                str.append(", ");
            }
            str.append(ChatColor.GRAY).append("algumas entidades não funcionam");
            messages.sendMessage("spawner.give.types", Constants.TYPE.get(), str.toString(), player);
        }
    }

}