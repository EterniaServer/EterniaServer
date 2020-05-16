package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.others;

import java.util.Collections;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemRename implements CommandExecutor {

    private final Messages messages;
    private final Strings strings;

    public ItemRename(Messages messages, Strings strings) {
        this.messages = messages;
        this.strings = strings;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, java.lang.String label, java.lang.String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("eternia.renameitem")) {
                messages.PlayerMessage("server.no-perm", player);
                return false;
            }
            if (args.length == 2) {
                player.getInventory().getItemInMainHand();
                if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                    if (args[0].equals("name")) {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            meta.setDisplayName(strings.getColor(args[1]));
                            item.setItemMeta(meta);
                            player.getInventory().setItemInMainHand(item);
                        } else {
                            messages.PlayerMessage("other.noitem", player);
                        }
                    } else if (args[0].equals("lore")) {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            meta.setLore(Collections.singletonList(strings.getColor(args[1])));
                            item.setItemMeta(meta);
                            player.getInventory().setItemInMainHand(item);
                        } else {
                            messages.PlayerMessage("other.noitem", player);
                        }
                    } else {
                        messages.PlayerMessage("other.rename", player);
                    }
                } else {
                    messages.PlayerMessage("other.noitem", player);
                }
            } else {
                messages.PlayerMessage("other.rename", player);
            }
        } else {
            messages.sendConsole("server.only-player");
        }
        return true;

    }

}