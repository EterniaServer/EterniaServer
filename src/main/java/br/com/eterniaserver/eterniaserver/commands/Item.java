package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.CommandAlias;
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

@CommandAlias("%item")
public class Item extends BaseCommand {

    @Default
    @HelpCommand
    @Syntax("%item_syntax")
    @CommandPermission("%item_perm")
    @Description("%item_description")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("%item_nbt")
    public class Nbt extends BaseCommand {

        @Default
        @HelpCommand
        @Syntax("%item_nbt_syntax")
        @Description("%item_nbt_description")
        @CommandPermission("%item_nbt_perm")
        public void onNbt(CommandHelp help) {
            onHelp(help);
        }

        @Subcommand("%item_nbt_addstring")
        @CommandPermission("%item_nbt_addstring_perm")
        @Description("%item_nbt_addstring_description")
        @Syntax("item_nbt_addstring_syntax")
        public void onItemAddString(Player player, String key, String value) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR) {
                NBTItem nbtItem = new NBTItem(item);
                nbtItem.setString(key, value);
                player.getInventory().setItemInMainHand(nbtItem.getItem());
                EterniaServer.sendMessage(player, Messages.ITEM_NBT_ADDKEY, key, value);
            } else {
                EterniaServer.sendMessage(player, Messages.ITEM_NOT_FOUND);
            }
        }

        @Subcommand("%item_nbt_addint")
        @CommandPermission("%item_nbt_addint_perm")
        @Description("%item_nbt_addint_description")
        @Syntax("item_nbt_addint_syntax")
        public void onItemAddInteger(Player player, String key, Integer value) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR) {
                NBTItem nbtItem = new NBTItem(item);
                nbtItem.setInteger(key, value);
                player.getInventory().setItemInMainHand(nbtItem.getItem());
                EterniaServer.sendMessage(player, Messages.ITEM_NBT_ADDKEY, key, String.valueOf(value));
            } else {
                EterniaServer.sendMessage(player, Messages.ITEM_NOT_FOUND);
            }
        }

    }

    @Subcommand("%item_clear")
    public class Clear extends BaseCommand {

        @Default
        @HelpCommand
        @Syntax("%item_clear_syntax")
        @Description("%item_clear_description")
        @CommandPermission("%item_clear_perm")
        public void onClear(CommandHelp help) {
            onHelp(help);
        }

        @Subcommand("%item_clear_lore")
        @Description("%item_clear_lore_description")
        @CommandPermission("%item_clear_lore_perm")
        public void onItemClearLore(Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR && item.getLore() != null) {
                item.getLore().clear();
                player.getInventory().setItemInMainHand(item);
                EterniaServer.sendMessage(player, Messages.ITEM_CLEAR_LORE);
            } else {
                EterniaServer.sendMessage(player, Messages.ITEM_NOT_FOUND);
            }
        }

        @Subcommand("%item_clear_name")
        @CommandPermission("%item_clear_name_perm")
        @Description("%item_clear_name_description")
        public void onItemClearName(Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR) {
                item.getItemMeta().setDisplayName(item.getI18NDisplayName());
                player.getInventory().setItemInMainHand(item);
                EterniaServer.sendMessage(player, Messages.ITEM_CLEAR_NAME);
            } else {
                EterniaServer.sendMessage(player, Messages.ITEM_NOT_FOUND);
            }
        }

    }


    @Subcommand("%item_add_lore")
    @Syntax("%item_add_lore_syntax")
    @CommandPermission("%item_add_lore_perm")
    @Description("%item_add_lore_description")
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
            EterniaServer.sendMessage(player, Messages.ITEM_ADD_LORE, name);
        } else {
            EterniaServer.sendMessage(player, Messages.ITEM_NOT_FOUND);
        }
    }

    @Subcommand("%item_set")
    public class Set extends BaseCommand {

        @Default
        @HelpCommand
        @Syntax("%item_set_syntax")
        @Description("%item_set_description")
        @CommandPermission("%item_set_perm")
        public void onSet(CommandHelp help) {
            onHelp(help);
        }

        @Subcommand("%item_set_lore")
        @Syntax("%item_set_lore_syntax")
        @Description("%item_set_lore_description")
        @CommandPermission("%item_set_lore_perm")
        public void onItemSetLore(Player player, String name) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR) {
                name = APIServer.getColor(name);
                item.setLore(List.of(name));
                player.getInventory().setItemInMainHand(item);
                EterniaServer.sendMessage(player, Messages.ITEM_SET_LORE, name);
            } else {
                EterniaServer.sendMessage(player, Messages.ITEM_NOT_FOUND);
            }
        }

        @Subcommand("%item_set_name")
        @Syntax("%item_set_name_syntax")
        @Description("%item_set_name_description")
        @CommandPermission("%item_set_name_perm")
        public void onItemSetName(Player player, String name) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR) {
                name = APIServer.getColor(name);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(name);
                item.setItemMeta(meta);
                player.getInventory().setItemInMainHand(item);
                EterniaServer.sendMessage(player, Messages.ITEM_SET_NAME, name);
            } else {
                EterniaServer.sendMessage(player, Messages.ITEM_NOT_FOUND);
            }
        }

    }

}
