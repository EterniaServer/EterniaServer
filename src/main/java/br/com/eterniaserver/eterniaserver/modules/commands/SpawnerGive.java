package br.com.eterniaserver.eterniaserver.modules.commands;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
    public void onSpawnerGive(CommandSender player, String spawner, Integer value, OnlinePlayer target) {
        Player targetP = target.getPlayer();
        try {
            EntityType.valueOf(spawner.toUpperCase());
            if (value > 0) {
                if (targetP.getInventory().firstEmpty() == -1) {
                    messages.sendMessage("spawners.invfull", player);
                } else {
                    String mobFormatted = spawner.substring(0, 1).toUpperCase() + spawner.substring(1).toLowerCase();
                    giveSpawner(value, targetP, mobFormatted);
                    messages.sendMessage("spawner.give.sent", "%amount%", value, "%mob_type%", mobFormatted, "%target_name%", targetP.getName(), player);
                    messages.sendMessage("spawner.give.received", "%amount%", value, "%mob_type%", mobFormatted, "%target_name%", player.getName(), targetP);
                }
            } else {
                messages.sendMessage("server.no-negative", player);
            }
        } catch (IllegalArgumentException e) {
            StringBuilder str = new StringBuilder();
            for (EntityType entity : EntityType.values()) {
                str.append(entity.name().toLowerCase());
                str.append(", ");
            }
            str.append("&7algumas entidades não funcionam");
            messages.sendMessage("spawner.give.types", "%types%", str.toString(), player);
        }
    }

    private void giveSpawner(int value, Player target, String mobFormatted) {
        ItemStack item = new ItemStack(Material.SPAWNER);
        ItemMeta meta = item.getItemMeta();
        item.setAmount(value);
        target.getInventory().addItem(plugin.getChecks().getSpawner(meta, item, mobFormatted));
    }

}