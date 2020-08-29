package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.eterniaserver.configs.Strings;

import com.google.common.collect.ImmutableList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@CommandAlias("item")
@CommandPermission("eternia.item")
public class Item extends BaseCommand {

    @Default
    public void itemHelp(Player player) {

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
            lore.add(name);
            item.setLore(lore);
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
            item.setLore(ImmutableList.of(name));
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
