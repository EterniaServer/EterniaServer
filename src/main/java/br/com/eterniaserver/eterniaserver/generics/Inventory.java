package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Inventory extends BaseCommand {

    @CommandAlias("workbench|craftingtable")
    @CommandPermission("eternia.workbench")
    public void onWorkbench(Player player) {
        player.openWorkbench(null, true);
    }

    @CommandAlias("openinv|invsee")
    @Syntax("<jogador>")
    @CommandPermission("eternia.openinv")
    public void onOpenInventory(Player player, OnlinePlayer target) {
        player.openInventory(target.getPlayer().getInventory());
    }

    @CommandAlias("enderchest|ec")
    @CommandPermission("eternia.enderchest")
    public void onEnderChest(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            player.openInventory(player.getEnderChest());
        } else {
            if (player.hasPermission("eternia.enderchest.other")) {
                player.openInventory(target.getPlayer().getEnderChest());
            } else {
                player.sendMessage(Strings.MSG_NO_PERM);
            }
        }
    }

    @CommandAlias("hat|capacete")
    @CommandPermission("eternia.hat")
    public void onHat(Player player) {
        dropHelmet(player);
        setHelmet(player);
        player.sendMessage(Strings.MSG_ITEM_HELMET);
    }

    private void dropHelmet(Player player) {
        ItemStack capacete = player.getInventory().getHelmet();
        if (capacete != null) {
            player.getWorld().dropItem(player.getLocation().add(0, 1, 0), capacete);
        }
    }

    private void setHelmet(Player player) {
        player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
    }

}
