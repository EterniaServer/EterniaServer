package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.PluginVars;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@CommandAlias("spawnergive|givespawner")
@CommandPermission("eternia.spawnergive")
public class Spawner extends BaseCommand {

    @Default
    @Syntax("<mob> <quantia> <jogador>")
    @CommandCompletion("@entidades 1 @players")
    @Description(" Dá uma quantia de spawners para um jogador")
    public void onSpawnerGive(CommandSender player, String spawner, Integer value, OnlinePlayer target) {
        final Player targetP = target.getPlayer();
        final Inventory inventory = targetP.getInventory();
        final String spawnerName = spawner.toUpperCase();

        if (PluginVars.entityList.contains(spawnerName)) {
            EntityType.valueOf(spawnerName);
            if (value <= 0) value = 1;
            if (inventory.firstEmpty() == -1) {
                EterniaServer.msg.sendMessage(player, Messages.SPAWNER_INV_FULL);
            } else {
                inventory.addItem(getSpawner(spawnerName, value));
                final String playerDisplay = player instanceof Player ? ((Player) player).getPlayer().getDisplayName() : player.getName();
                EterniaServer.msg.sendMessage(targetP, Messages.SPAWNER_RECEIVED, spawnerName, player.getName(), playerDisplay, String.valueOf(value));
                EterniaServer.msg.sendMessage(player, Messages.SPAWNER_SENT, spawnerName, targetP.getName(), targetP.getDisplayName(), String.valueOf(value));
            }
        } else {
            sendTypes(player);
        }
    }

    private ItemStack getSpawner(final String spawnerName, final int value) {
        ItemStack item = new ItemStack(Material.SPAWNER);
        ItemMeta meta = item.getItemMeta();
        item.setAmount(value);
        meta.setDisplayName(PluginVars.colors.get(8) + "[" + EterniaServer.configs.mobSpawnerColor + spawnerName + PluginVars.colors.get(7) + " Spawner" +  PluginVars.colors.get(8) + "]");
        item.setItemMeta(meta);
        return item;
    }

    private void sendTypes(final CommandSender player) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < PluginVars.entityList.size(); i++) {
            if (i + 1 == PluginVars.entityList.size()) {
                str.append(ChatColor.DARK_AQUA).append(PluginVars.entityList.get(i));
            } else {
                str.append(ChatColor.DARK_AQUA).append(PluginVars.entityList.get(i)).append(ChatColor.DARK_GRAY).append(", ");
            }
        }
        str.append(ChatColor.GRAY).append("algumas entidades não funcionam");
        EterniaServer.msg.sendMessage(player, Messages.SPAWNER_SEND_TYPES, str.toString());
    }

}