package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.NBTItem;
import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.strings.Strings;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@CommandAlias("item")
@CommandPermission("eternia.item")
public class BaseCmdItem extends BaseCommand {

    @Default
    public void itemHelp(Player player) {

    }

    @Subcommand("addkey")
    @Syntax("<chave> <valor>")
    public void onItemAddKey(Player player, String key, String value) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.getType() != Material.AIR) {
            NBTItem nbtItem = new NBTItem(item);
            nbtItem.setString(key, value);
            player.sendMessage(Strings.ITEM_ADDKEY.replace(Constants.KEY, key).replace(Constants.VALUE, value));
        } else {
            player.sendMessage(Strings.ITEM_NO);
        }
    }

    @Subcommand("clear lore")
    public void onItemClearLore(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.getType() != Material.AIR) {
            item.getLore().clear();
            player.getInventory().setItemInMainHand(item);
            player.sendMessage(Strings.ITEM_LORE_CLEAR);
        } else {
            player.sendMessage(Strings.ITEM_NO);
        }
    }

    @Subcommand("clear name")
    public void onItemClearName(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.getType() != Material.AIR) {
            item.getItemMeta().setDisplayName(item.getI18NDisplayName());
            player.getInventory().setItemInMainHand(item);
            player.sendMessage(Strings.ITEM_NAME_CLEAR);
        } else {
            player.sendMessage(Strings.ITEM_NO);
        }
    }

    @Subcommand("add lore")
    @Syntax("<lore>")
    @CommandCompletion("<lore>")
    public void onItemAddLore(Player player, String name) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.getType() != Material.AIR) {
            name = Strings.getColor(name);
            List<String> lore = item.getLore();
            if (lore != null) {
                lore.add(name);
                item.setLore(lore);
            } else {
                item.setLore(List.of(name));
            }
            player.getInventory().setItemInMainHand(item);
            player.sendMessage(Strings.ITEM_LORE_ADD.replace("%name%", name));
        } else {
            player.sendMessage(Strings.ITEM_NO);
        }
    }

    @Subcommand("set lore")
    @Syntax("<lore>")
    @CommandCompletion("<lore>")
    public void onItemSetLore(Player player, String name) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.getType() != Material.AIR) {
            name = Strings.getColor(name);
            item.setLore(List.of(name));
            player.getInventory().setItemInMainHand(item);
            player.sendMessage(Strings.ITEM_LORE_SET.replace("%name%", name));
        } else {
            player.sendMessage(Strings.ITEM_NO);
        }
    }

    @Subcommand("set name")
    @Syntax("<nome>")
    @CommandCompletion("<nome>")
    public void onItemSetName(Player player, String name) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.getType() != Material.AIR) {
            name = Strings.getColor(name);
            item.getItemMeta().setDisplayName(name);
            player.getInventory().setItemInMainHand(item);
            player.sendMessage(Strings.ITEM_NAME_SET.replace("%name%", name));
        } else {
            player.sendMessage(Strings.ITEM_NO);
        }
    }

}
