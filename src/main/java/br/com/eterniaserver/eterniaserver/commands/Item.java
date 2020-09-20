package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.HelpCommand;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.NBTItem;

import br.com.eterniaserver.eterniaserver.generics.PluginConstants;
import br.com.eterniaserver.eterniaserver.generics.PluginMSGs;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@CommandAlias("item")
@CommandPermission("eternia.item")
public class Item extends BaseCommand {

    @Default
    @HelpCommand
    @Syntax("<página>")
    @Description(" Ajuda para os comandos de Item")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("addkey")
    @Syntax("<chave> <valor>")
    @CommandPermission("eternia.item.nbt")
    @Description(" Adiciona uma chave e um valor NBT ao item")
    public void onItemAddKey(Player player, String key, String value) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.AIR) {
            NBTItem nbtItem = new NBTItem(item);
            nbtItem.setString(key, value);
            player.getInventory().setItemInMainHand(nbtItem.getItem());
            player.sendMessage(PluginMSGs.ITEM_ADDKEY.replace(PluginConstants.KEY, key).replace(PluginConstants.VALUE, value));
        } else {
            player.sendMessage(PluginMSGs.ITEM_NO);
        }
    }

    @Subcommand("clear")
    public class Clear extends BaseCommand {

        @Subcommand("lore")
        @Description(" Remove a lore do item")
        public void onItemClearLore(Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR && item.getLore() != null) {
                item.getLore().clear();
                player.getInventory().setItemInMainHand(item);
                player.sendMessage(PluginMSGs.ITEM_LORE_CLEAR);
            } else {
                player.sendMessage(PluginMSGs.ITEM_NO);
            }
        }

        @Subcommand("name")
        @Description(" Remove o nome do item")
        public void onItemClearName(Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR) {
                item.getItemMeta().setDisplayName(item.getI18NDisplayName());
                player.getInventory().setItemInMainHand(item);
                player.sendMessage(PluginMSGs.ITEM_NAME_CLEAR);
            } else {
                player.sendMessage(PluginMSGs.ITEM_NO);
            }
        }

    }


    @Subcommand("add lore")
    @CommandCompletion("<lore>")
    @Syntax("<lore>")
    @Description(" Adiciona uma linha de lore ao item")
    public void onItemAddLore(Player player, String name) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.AIR) {
            name = PluginMSGs.getColor(name);
            List<String> lore = item.getLore();
            if (lore != null) {
                lore.add(name);
                item.setLore(lore);
            } else {
                item.setLore(List.of(name));
            }
            player.getInventory().setItemInMainHand(item);
            player.sendMessage(PluginMSGs.ITEM_LORE_ADD.replace("%name%", name));
        } else {
            player.sendMessage(PluginMSGs.ITEM_NO);
        }
    }

    @Subcommand("set")
    public class Set extends BaseCommand {

        @Subcommand("lore")
        @CommandCompletion("<lore>")
        @Syntax("<lore>")
        @Description(" Define a lore de um item")
        public void onItemSetLore(Player player, String name) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR) {
                name = PluginMSGs.getColor(name);
                item.setLore(List.of(name));
                player.getInventory().setItemInMainHand(item);
                player.sendMessage(PluginMSGs.ITEM_LORE_SET.replace("%name%", name));
            } else {
                player.sendMessage(PluginMSGs.ITEM_NO);
            }
        }

        @Subcommand("name")
        @CommandCompletion("<nome>")
        @Syntax("<nome>")
        @Description(" Define o nome de um item")
        public void onItemSetName(Player player, String name) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR) {
                name = PluginMSGs.getColor(name);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(name);
                item.setItemMeta(meta);
                player.getInventory().setItemInMainHand(item);
                player.sendMessage(PluginMSGs.ITEM_NAME_SET.replace("%name%", name));
            } else {
                player.sendMessage(PluginMSGs.ITEM_NO);
            }
        }

    }

}