package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Conditions;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.HelpCommand;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

@CommandAlias("%item")
public class Item extends BaseCommand {

    private final EterniaServer plugin;

    public Item(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Default
    @HelpCommand
    @Syntax("%item_syntax")
    @CommandPermission("%item_perm")
    @Description("%item_description")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("%item_send_custon")
    @CommandPermission("%item_send_custon_perm")
    @Description("%item_send_custon_description")
    @Syntax("%item_send_custon_syntax")
    public void sendItemCustom(final CommandSender sender, final OnlinePlayer target,
                               @Conditions("limits:min=0,max=2147483647") final int usages,
                               @Conditions("limits:min=0,max=1") final int console, final String item, final String line) {
        try {
            final ItemStack itemStack = new ItemStack(Material.valueOf(item));
            final ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_FUNCTION), PersistentDataType.INTEGER, 2);
            itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_RUN_IN_CONSOLE), PersistentDataType.INTEGER, console);
            itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_USAGES), PersistentDataType.INTEGER, usages);
            itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_RUN_COMMAND), PersistentDataType.STRING, line);
            itemStack.setItemMeta(itemMeta);

            final Player player = target.getPlayer();
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(itemStack);
            } else {
                player.getWorld().dropItem(player.getLocation(), itemStack);
            }

            plugin.sendMessage(sender, Messages.ITEM_NBT_GIVE, true);
            plugin.sendMessage(player, Messages.ITEM_NBT_RECEIVED, true);
        } catch (Exception ignored) {
            plugin.sendMessage(sender, Messages.ITEM_NBT_CANT_GIVE, true);
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
            help.showHelp();
        }

        @Subcommand("%item_clear_lore")
        @Description("%item_clear_lore_description")
        @CommandPermission("%item_clear_lore_perm")
        public void onItemClearLore(Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR && item.getLore() != null) {
                item.getLore().clear();
                player.getInventory().setItemInMainHand(item);
                plugin.sendMessage(player, Messages.ITEM_CLEAR_LORE);
            } else {
                plugin.sendMessage(player, Messages.ITEM_NOT_FOUND);
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
                plugin.sendMessage(player, Messages.ITEM_CLEAR_NAME);
            } else {
                plugin.sendMessage(player, Messages.ITEM_NOT_FOUND);
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
            name = plugin.getColor(name);
            List<String> lore = item.getLore();
            if (lore != null) {
                lore.add(name);
                item.setLore(lore);
            } else {
                item.setLore(List.of(name));
            }
            player.getInventory().setItemInMainHand(item);
            plugin.sendMessage(player, Messages.ITEM_ADD_LORE, name);
        } else {
            plugin.sendMessage(player, Messages.ITEM_NOT_FOUND);
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
            help.showHelp();
        }

        @Subcommand("%item_set_lore")
        @Syntax("%item_set_lore_syntax")
        @Description("%item_set_lore_description")
        @CommandPermission("%item_set_lore_perm")
        public void onItemSetLore(Player player, String name) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR) {
                name = plugin.getColor(name);
                item.setLore(List.of(name));
                player.getInventory().setItemInMainHand(item);
                plugin.sendMessage(player, Messages.ITEM_SET_LORE, name);
            } else {
                plugin.sendMessage(player, Messages.ITEM_NOT_FOUND);
            }
        }

        @Subcommand("%item_set_name")
        @Syntax("%item_set_name_syntax")
        @Description("%item_set_name_description")
        @CommandPermission("%item_set_name_perm")
        public void onItemSetName(Player player, String name) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR) {
                name = plugin.getColor(name);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(name);
                item.setItemMeta(meta);
                player.getInventory().setItemInMainHand(item);
                plugin.sendMessage(player, Messages.ITEM_SET_NAME, name);
            } else {
                plugin.sendMessage(player, Messages.ITEM_NOT_FOUND);
            }
        }

    }

}
