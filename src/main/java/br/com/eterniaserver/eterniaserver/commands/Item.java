package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.NBTItem;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.ServerRelated;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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

    @Subcommand("%item_send_custon")
    @CommandPermission("%item_send_custon_perm")
    @Description("%item_send_custon_description")
    @Syntax("%item_send_custon_syntax")
    public void sendItemCustom(final CommandSender sender, final OnlinePlayer target,
                               @Conditions("limits:min=0,max=2147483647") final int usages,
                               @Conditions("limits:min=0,max=1") final int console, final String item, final String lines) {
        try {
            final NBTItem nbtItem = new NBTItem(new ItemStack(Material.valueOf(item)));
            nbtItem.setInteger(Constants.NBT_FUNCTION, 2);
            if (console == 0) {
                nbtItem.setBoolean(Constants.NBT_RUN_IN_CONSOLE, false);
            } else {
                nbtItem.setBoolean(Constants.NBT_RUN_IN_CONSOLE, true);
            }
            nbtItem.setInteger(Constants.NBT_USAGES, usages);
            final String[] stringList = lines.split("\\|");
            for (final String line : stringList) {
                nbtItem.getStringList(Constants.NBT_RUN_COMMAND).add(line);
            }
            final Player player = target.getPlayer();
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(nbtItem.getItem());
            } else {
                player.getWorld().dropItem(player.getLocation(), nbtItem.getItem());
            }
            EterniaServer.sendMessage(sender, Messages.ITEM_NBT_GIVE, true);
            EterniaServer.sendMessage(player, Messages.ITEM_NBT_RECEIVED, true);
        } catch (Exception ignored) {
            EterniaServer.sendMessage(sender, Messages.ITEM_NBT_CANT_GIVE, true);
        }
    }

    @Subcommand("%item_nbt")
    public class Nbt extends BaseCommand {

        @Default
        @HelpCommand
        @Syntax("%item_nbt_syntax")
        @Description("%item_nbt_description")
        @CommandPermission("%item_nbt_perm")
        public void onNbt(CommandHelp help) {
            help.showHelp();
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

        @Subcommand("%item_nbt_setcommandlist")
        @CommandPermission("%item_nbt_setcommandlist_perm")
        @Description("%item_nbt_setcommandlist_description")
        @Syntax("%item_nbt_setcommandlist_syntax")
        public void onItemSetCommandList(final Player player, final String line) {
            final PlayerInventory playerInventory = player.getInventory();
            final NBTItem nbtItem = new NBTItem(playerInventory.getItemInMainHand());
            nbtItem.getStringList(Constants.NBT_RUN_COMMAND).clear();
            nbtItem.getStringList(Constants.NBT_RUN_COMMAND).add(line);
            nbtItem.setInteger(Constants.NBT_FUNCTION, 2);
            playerInventory.setItemInMainHand(nbtItem.getItem());
            EterniaServer.sendMessage(player, Messages.ITEM_NBT_DEFINE_COMMAND, true);
        }

        @Subcommand("%item_nbt_defineconsole")
        @CommandPermission("%item_nbt_defineconsole_perm")
        @Description("%item_nbt_defineconsole_description")
        @Syntax("%item_nbt_defineconsole_syntax")
        @CommandCompletion("0")
        public void onItemDefineConsole(final Player player, @Conditions("limits:min=0,max=1") Integer value) {
            final PlayerInventory playerInventory = player.getInventory();
            final NBTItem nbtItem = new NBTItem(playerInventory.getItemInMainHand());
            nbtItem.setInteger(Constants.NBT_FUNCTION, 2);
            if (value == 0) {
                nbtItem.setBoolean(Constants.NBT_RUN_IN_CONSOLE, false);
                playerInventory.setItemInMainHand(nbtItem.getItem());
                EterniaServer.sendMessage(player, Messages.ITEM_NBT_RUN_IN_CONSOLE, true, "false");
                return;
            }
            nbtItem.setBoolean(Constants.NBT_RUN_IN_CONSOLE, true);
            playerInventory.setItemInMainHand(nbtItem.getItem());
            EterniaServer.sendMessage(player, Messages.ITEM_NBT_RUN_IN_CONSOLE, true, "true");
        }

        @Subcommand("%item_nbt_addcommand")
        @CommandPermission("%item_nbt_addcommand_perm")
        @Description("%item_nbt_addcommand_description")
        @Syntax("%item_nbt_addcommand_syntax")
        public void onItemAddCommand(final Player player, final String line) {
            final PlayerInventory playerInventory = player.getInventory();
            final NBTItem nbtItem = new NBTItem(playerInventory.getItemInMainHand());
            nbtItem.getStringList(Constants.NBT_RUN_COMMAND).add(line);
            nbtItem.setInteger(Constants.NBT_FUNCTION, 2);
            playerInventory.setItemInMainHand(nbtItem.getItem());
            EterniaServer.sendMessage(player, Messages.ITEM_NBT_ADD_LINE, true);
        }

        @Subcommand("%item_nbt_setusages")
        @CommandPermission("%item_nbt_setusages_perm")
        @Description("%item_nbt_setusages_description")
        @Syntax("%item_nbt_setusages_syntax")
        @CommandCompletion("0")
        public void onItemSetUsages(final Player player, @Conditions("limits:min=0,max=2147483647")  final Integer amount) {
            final PlayerInventory playerInventory = player.getInventory();
            final NBTItem nbtItem = new NBTItem(playerInventory.getItemInMainHand());
            nbtItem.setInteger(Constants.NBT_USAGES, amount);
            nbtItem.setInteger(Constants.NBT_FUNCTION, 2);
            playerInventory.setItemInMainHand(nbtItem.getItem());
            if (amount == 0) {
                EterniaServer.sendMessage(player, Messages.ITEM_NBT_SET_USAGES, true, "unlimited");
            } else {
                EterniaServer.sendMessage(player, Messages.ITEM_NBT_SET_USAGES, true, String.valueOf(amount));
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
            name = ServerRelated.getColor(name);
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
            help.showHelp();
        }

        @Subcommand("%item_set_lore")
        @Syntax("%item_set_lore_syntax")
        @Description("%item_set_lore_description")
        @CommandPermission("%item_set_lore_perm")
        public void onItemSetLore(Player player, String name) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR) {
                name = ServerRelated.getColor(name);
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
                name = ServerRelated.getColor(name);
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
