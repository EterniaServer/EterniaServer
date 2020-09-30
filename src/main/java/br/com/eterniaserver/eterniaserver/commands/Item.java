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

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.APIServer;
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

    @Subcommand("nbt")
    @CommandPermission("eternia.item.nbt")
    public class Nbt extends BaseCommand {

        @Subcommand("addstring")
        @Description(" Adiciona uma chave e um valor NBT ao item")
        @Syntax("<chave> <valor>")
        public void onItemAddString(Player player, String key, String value) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR) {
                NBTItem nbtItem = new NBTItem(item);
                nbtItem.setString(key, value);
                player.getInventory().setItemInMainHand(nbtItem.getItem());
                EterniaServer.msg.sendMessage(player, Messages.ITEM_NBT_ADDKEY, key, value);
            } else {
                EterniaServer.msg.sendMessage(player, Messages.ITEM_NOT_FOUND);
            }
        }

        @Subcommand("addint")
        @Description(" Adiciona uma chave e um valor NBT ao item")
        @Syntax("<chave> <valor>")
        public void onItemAddInteger(Player player, String key, Integer value) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR) {
                NBTItem nbtItem = new NBTItem(item);
                nbtItem.setInteger(key, value);
                player.getInventory().setItemInMainHand(nbtItem.getItem());
                EterniaServer.msg.sendMessage(player, Messages.ITEM_NBT_ADDKEY, key, String.valueOf(value));
            } else {
                EterniaServer.msg.sendMessage(player, Messages.ITEM_NOT_FOUND);
            }
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
                EterniaServer.msg.sendMessage(player, Messages.ITEM_CLEAR_LORE);
            } else {
                EterniaServer.msg.sendMessage(player, Messages.ITEM_NOT_FOUND);
            }
        }

        @Subcommand("name")
        @Description(" Remove o nome do item")
        public void onItemClearName(Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR) {
                item.getItemMeta().setDisplayName(item.getI18NDisplayName());
                player.getInventory().setItemInMainHand(item);
                EterniaServer.msg.sendMessage(player, Messages.ITEM_CLEAR_NAME);
            } else {
                EterniaServer.msg.sendMessage(player, Messages.ITEM_NOT_FOUND);
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
            name = APIServer.getColor(name);
            List<String> lore = item.getLore();
            if (lore != null) {
                lore.add(name);
                item.setLore(lore);
            } else {
                item.setLore(List.of(name));
            }
            player.getInventory().setItemInMainHand(item);
            EterniaServer.msg.sendMessage(player, Messages.ITEM_ADD_LORE, name);
        } else {
            EterniaServer.msg.sendMessage(player, Messages.ITEM_NOT_FOUND);
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
                name = APIServer.getColor(name);
                item.setLore(List.of(name));
                player.getInventory().setItemInMainHand(item);
                EterniaServer.msg.sendMessage(player, Messages.ITEM_SET_LORE, name);
            } else {
                EterniaServer.msg.sendMessage(player, Messages.ITEM_NOT_FOUND);
            }
        }

        @Subcommand("name")
        @CommandCompletion("<nome>")
        @Syntax("<nome>")
        @Description(" Define o nome de um item")
        public void onItemSetName(Player player, String name) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR) {
                name = APIServer.getColor(name);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(name);
                item.setItemMeta(meta);
                player.getInventory().setItemInMainHand(item);
                EterniaServer.msg.sendMessage(player, Messages.ITEM_SET_NAME, name);
            } else {
                EterniaServer.msg.sendMessage(player, Messages.ITEM_NOT_FOUND);
            }
        }

    }

}
