package br.com.eterniaserver.eterniaserver.modules.item;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.chat.MessageOptions;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

final class Commands {

    private Commands() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @CommandAlias("%ITEM")
    static class Item extends BaseCommand {

        private final EterniaServer plugin;

        public Item(EterniaServer plugin) {
            this.plugin = plugin;
        }

        @Default
        @HelpCommand
        @Syntax("%ITEM_SYNTAX")
        @Description("%ITEM_DESCRIPTION")
        @CommandPermission("%ITEM_PERM")
        public void onHelp(CommandHelp help) {
            help.showHelp();
        }

        @Subcommand("%ITEM_SEND_CUSTOM")
        @CommandPermission("%ITEM_SEND_CUSTOM_PERM")
        @Description("%ITEM_SEND_CUSTOM_DESCRIPTION")
        @Syntax("%ITEM_SEND_CUSTOM_SYNTAX")
        @CommandCompletion("@players 3 1 STONE sendmessage %player_name% &8[&aE&9S&8] &7Item preula;give %player_name% minecraft:stone")
        public void sendItemCustom(CommandSender sender,
                                   OnlinePlayer target,
                                   @Conditions("limits:min=0,max=2147483647") Integer usages,
                                   @Conditions("limits:min=0,max=1") Integer console,
                                   String item,
                                   String line) {
            try {
                ItemStack itemStack = new ItemStack(Material.valueOf(item));
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_FUNCTION), PersistentDataType.INTEGER, 2);
                itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_RUN_IN_CONSOLE), PersistentDataType.INTEGER, console);
                itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_USAGES), PersistentDataType.INTEGER, usages);
                itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_RUN_COMMAND), PersistentDataType.STRING, line);
                itemStack.setItemMeta(itemMeta);

                Player player = target.getPlayer();
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(itemStack);
                } else {
                    player.getWorld().dropItem(player.getLocation(), itemStack);
                }

                EterniaLib.getChatCommons().sendMessage(sender, Messages.ITEM_CUSTOM_GIVE);
                EterniaLib.getChatCommons().sendMessage(player, Messages.ITEM_CUSTOM_RECEIVED);
            } catch (Exception ignored) {
                EterniaLib.getChatCommons().sendMessage(sender, Messages.ITEM_CUSTOM_CANT_GIVE);
            }
        }

        @Subcommand("%ITEM_CUSTOM_MODEL")
        @CommandPermission("%ITEM_CUSTOM_MODEL_PERM")
        @Description("%ITEM_CUSTOM_MODEL_DESCRIPTION")
        @Syntax("%ITEM_CUSTOM_MODEL_SYNTAX")
        @CommandCompletion("0")
        public void setCustomModelData(Player player, @Conditions("limits:min=0,max=2147483647") Integer integer) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(integer);
            itemStack.setItemMeta(itemMeta);
            player.getInventory().setItemInMainHand(itemStack);
            MessageOptions options = new MessageOptions(String.valueOf(integer));
            EterniaLib.getChatCommons().sendMessage(player, Messages.ITEM_SET_CUSTOM, options);
        }

        @Subcommand("%ITEM_CLEAR")
        public class Clear extends BaseCommand {

            @Default
            @HelpCommand
            @Syntax("%ITEM_CLEAR_SYNTAX")
            @Description("%ITEM_CLEAR_DESCRIPTION")
            @CommandPermission("%ITEM_CLEAR_PERM")
            public void onClear(CommandHelp help) {
                help.showHelp();
            }

            @Subcommand("%ITEM_CLEAR_LORE")
            @CommandPermission("%ITEM_CLEAR_LORE_PERM")
            @Description("%ITEM_CLEAR_LORE_DESCRIPTION")
            public void onItemClearLore(Player player) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType() == Material.AIR || item.lore() == null) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ITEM_NOT_FOUND);
                    return;
                }

                item.lore(new ArrayList<>());
                player.getInventory().setItemInMainHand(item);
                EterniaLib.getChatCommons().sendMessage(player, Messages.ITEM_CLEAR_LORE);
            }

            @Subcommand("%ITEM_CLEAR_NAME")
            @CommandPermission("%ITEM_CLEAR_NAME_PERM")
            @Description("%ITEM_CLEAR_NAME_DESCRIPTION")
            public void onItemClearName(Player player) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType() == Material.AIR) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ITEM_NOT_FOUND);
                    return;
                }

                item.getItemMeta().displayName(item.getItemMeta().displayName());
                player.getInventory().setItemInMainHand(item);
                EterniaLib.getChatCommons().sendMessage(player, Messages.ITEM_CLEAR_NAME);
            }

        }

        @Subcommand("%ITEM_ADD_LORE")
        @Syntax("%ITEM_ADD_LORE_SYNTAX")
        @Description("%ITEM_ADD_LORE_DESCRIPTION")
        @CommandPermission("%ITEM_ADD_LORE_PERM")
        public void onItemAddLore(Player player, String loreStr) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.ITEM_NOT_FOUND);
                return;
            }

            Component loreComponent = EterniaLib.getChatCommons().parseColor(loreStr);
            List<Component> lore = item.lore();
            if (lore != null) {
                lore.add(loreComponent);
                item.lore(lore);
            } else {
                item.lore(List.of(loreComponent));
            }


            player.getInventory().setItemInMainHand(item);
            MessageOptions options = new MessageOptions(loreStr);
            EterniaLib.getChatCommons().sendMessage(player, Messages.ITEM_ADD_LORE, options);
        }

        @Subcommand("%ITEM_SET")
        public class Set extends BaseCommand {

            @Default
            @HelpCommand
            @Syntax("%ITEM_SET_SYNTAX")
            @Description("%ITEM_SET_DESCRIPTION")
            @CommandPermission("%ITEM_SET_PERM")
            public void onSet(CommandHelp help) {
                help.showHelp();
            }

            @Subcommand("%ITEM_SET_LORE")
            @Syntax("%ITEM_SET_LORE_SYNTAX")
            @Description("%ITEM_SET_LORE_DESCRIPTION")
            @CommandPermission("%ITEM_SET_LORE_PERM")
            public void onItemSetLore(Player player, String loreStr) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType() == Material.AIR) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ITEM_NOT_FOUND);
                    return;
                }

                Component loreComponent = EterniaLib.getChatCommons().parseColor(loreStr);
                item.lore(List.of(loreComponent));
                player.getInventory().setItemInMainHand(item);
                MessageOptions options = new MessageOptions(loreStr);
                EterniaLib.getChatCommons().sendMessage(player, Messages.ITEM_SET_LORE, options);
            }

            @Subcommand("%ITEM_SET_NAME")
            @Syntax("%ITEM_SET_NAME_SYNTAX")
            @Description("%ITEM_SET_NAME_DESCRIPTION")
            @CommandPermission("%ITEM_SET_NAME_PERM")
            public void onItemSetName(Player player, String nameStr) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType() == Material.AIR) {
                    EterniaLib.getChatCommons().sendMessage(player, Messages.ITEM_NOT_FOUND);
                    return;
                }

                Component nameComponent = EterniaLib.getChatCommons().parseColor(nameStr);
                ItemMeta meta = item.getItemMeta();
                meta.displayName(nameComponent);
                item.setItemMeta(meta);
                player.getInventory().setItemInMainHand(item);
                MessageOptions options = new MessageOptions(nameStr);
                EterniaLib.getChatCommons().sendMessage(player, Messages.ITEM_SET_NAME, options);
            }

        }

    }

}
