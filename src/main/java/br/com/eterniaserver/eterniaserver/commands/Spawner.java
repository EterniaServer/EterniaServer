package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.NBTItem;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.Vars;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@CommandAlias("%spawnergive")
public class Spawner extends BaseCommand {

    @Default
    @CommandCompletion("@entidades 1 @players")
    @Syntax("%spawnergive_syntax")
    @Description("%spawnergive_description")
    @CommandPermission("%spawnergive_perm")
    public void onSpawnerGive(CommandSender player, String spawner, Integer value, OnlinePlayer target) {
        final Player targetP = target.getPlayer();
        final Inventory inventory = targetP.getInventory();
        final String spawnerName = spawner.toUpperCase();

        if (Vars.entityList.contains(spawnerName)) {
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
        meta.setDisplayName(Vars.colors.get(8) + "[" + EterniaServer.configs.mobSpawnerColor + spawnerName + Vars.colors.get(7) + " Spawner" +  Vars.colors.get(8) + "]");
        item.setItemMeta(meta);
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("ms_mob", spawnerName.toUpperCase());
        return nbtItem.getItem();
    }

    private void sendTypes(final CommandSender player) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < Vars.entityList.size(); i++) {
            if (i + 1 == Vars.entityList.size()) {
                str.append(ChatColor.DARK_AQUA).append(Vars.entityList.get(i)).append(ChatColor.DARK_GRAY).append(".");
            } else {
                str.append(ChatColor.DARK_AQUA).append(Vars.entityList.get(i)).append(ChatColor.DARK_GRAY).append(", ");
            }
        }
        EterniaServer.msg.sendMessage(player, Messages.SPAWNER_SEND_TYPES, str.toString());
    }

}