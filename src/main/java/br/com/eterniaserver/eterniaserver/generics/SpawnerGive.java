package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import br.com.eterniaserver.eterniaserver.configs.Strings;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SpawnerGive extends BaseCommand {

    @CommandAlias("spawnergive|givespawner")
    @Syntax("<mob> <quantia> <jogador>")
    @CommandCompletion("@entidades 1 @players")
    @CommandPermission("eternia.spawnergive")
    public void onSpawnerGive(CommandSender player, String spawner, Integer value, OnlinePlayer target) {
        final Player targetP = target.getPlayer();
        final Inventory inventory = targetP.getInventory();
        final String spawnerName = spawner.toUpperCase();

        if (EterniaServer.entityList.contains(spawnerName)) {
            EntityType.valueOf(spawnerName);
            if (value <= 0) value = 1;
            if (inventory.firstEmpty() == -1) {
                player.sendMessage(Strings.MSG_SPAWNER_INVFULL);
            } else {
                inventory.addItem(getSpawner(spawnerName, value));
                player.sendMessage(InternMethods.putName(targetP, Strings.MSG_SPAWNER_SENT.replace(Constants.VALUE, String.valueOf(value)).replace(Constants.TYPE, spawnerName)));
                targetP.sendMessage(InternMethods.putName(player, Strings.MSG_SPAWNER_RECEIVED.replace(Constants.TYPE, spawnerName).replace(Constants.VALUE, String.valueOf(value))));
            }
        } else {
            sendTypes(player);
        }
    }

    private ItemStack getSpawner(final String spawnerName, final int value) {
        ItemStack item = new ItemStack(Material.SPAWNER);
        ItemMeta meta = item.getItemMeta();
        item.setAmount(value);
        String mobFormatted = spawnerName.substring(0, 1) + spawnerName.substring(1);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ("&8[" + EterniaServer.serverConfig.getString("spawners.mob-name-color") + "%mob% &7Spawner&8]".replace("%mob%", mobFormatted))));
        List<String> newLore = new ArrayList<>();
        if (EterniaServer.serverConfig.getBoolean("spawners.enable-lore")) {
            for (String line : EterniaServer.serverConfig.getStringList("spawners.lore")) {
                newLore.add(ChatColor.translateAlternateColorCodes('&', line.replace("%s", mobFormatted)));
            }
            meta.setLore(newLore);
        }
        item.setItemMeta(meta);
        return item;
    }

    private void sendTypes(final CommandSender player) {
        StringBuilder str = new StringBuilder();
        for (String entity : EterniaServer.entityList) str.append(ChatColor.DARK_AQUA).append(entity).append(ChatColor.DARK_GRAY).append(", ");
        str.append(ChatColor.GRAY).append("algumas entidades não funcionam");
        player.sendMessage(Strings.MSG_SPAWNER_GIVE.replace(Constants.TYPE, str.toString()));
    }

}