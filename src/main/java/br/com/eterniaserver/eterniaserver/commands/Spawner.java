package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.eterniaserver.Configs;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import br.com.eterniaserver.eterniaserver.generics.PluginConstants;
import br.com.eterniaserver.eterniaserver.generics.PluginMSGs;
import br.com.eterniaserver.eterniaserver.generics.PluginVars;
import br.com.eterniaserver.eterniaserver.generics.UtilInternMethods;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Spawner extends BaseCommand {

    @CommandAlias("spawnergive|givespawner")
    @Syntax("<mob> <quantia> <jogador>")
    @CommandCompletion("@entidades 1 @players")
    @CommandPermission("eternia.spawnergive")
    public void onSpawnerGive(CommandSender player, String spawner, Integer value, OnlinePlayer target) {
        final Player targetP = target.getPlayer();
        final Inventory inventory = targetP.getInventory();
        final String spawnerName = spawner.toUpperCase();

        if (PluginVars.entityList.contains(spawnerName)) {
            EntityType.valueOf(spawnerName);
            if (value <= 0) value = 1;
            if (inventory.firstEmpty() == -1) {
                player.sendMessage(PluginMSGs.MSG_SPAWNER_INVFULL);
            } else {
                inventory.addItem(getSpawner(spawnerName, value));
                player.sendMessage(UtilInternMethods.putName(targetP, PluginMSGs.MSG_SPAWNER_SENT.replace(PluginConstants.VALUE, String.valueOf(value)).replace(PluginConstants.TYPE, spawnerName)));
                targetP.sendMessage(UtilInternMethods.putName(player, PluginMSGs.MSG_SPAWNER_RECEIVED.replace(PluginConstants.TYPE, spawnerName).replace(PluginConstants.VALUE, String.valueOf(value))));
            }
        } else {
            sendTypes(player);
        }
    }

    private ItemStack getSpawner(final String spawnerName, final int value) {
        ItemStack item = new ItemStack(Material.SPAWNER);
        ItemMeta meta = item.getItemMeta();
        item.setAmount(value);
        meta.setDisplayName(PluginVars.colors.get(8) + "[" + Configs.instance.mobSpawnerColor + spawnerName + PluginVars.colors.get(7) + " Spawner" +  PluginVars.colors.get(8) + "]");
        item.setItemMeta(meta);
        return item;
    }

    private void sendTypes(final CommandSender player) {
        StringBuilder str = new StringBuilder();
        for (String entity : PluginVars.entityList) str.append(ChatColor.DARK_AQUA).append(entity).append(ChatColor.DARK_GRAY).append(", ");
        str.append(ChatColor.GRAY).append("algumas entidades não funcionam");
        player.sendMessage(PluginMSGs.MSG_SPAWNER_GIVE.replace(PluginConstants.TYPE, str.toString()));
    }

}