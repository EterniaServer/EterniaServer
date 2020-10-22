package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.NBTItem;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Inventory extends BaseCommand {

    @CommandAlias("%workbench")
    @Description("%workbench_description")
    @CommandPermission("%workbench_perm")
    public void onWorkbench(Player player) {
        player.openWorkbench(null, true);
    }

    @CommandAlias("%openinv")
    @CommandCompletion("@players")
    @Syntax("%openinv_syntax")
    @Description("%openinv_description")
    @CommandPermission("%openinv_perm")
    public void onOpenInventory(Player player, OnlinePlayer target) {
        player.openInventory(target.getPlayer().getInventory());
    }

    @CommandAlias("%enderchest")
    @CommandCompletion("@players")
    @Syntax("%enderchest_syntax")
    @Description("%enderchest_description")
    @CommandPermission("%enderchest_perm")
    public void onEnderChest(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            player.openInventory(player.getEnderChest());
            return;
        }

        if (player.hasPermission(EterniaServer.constants.permEcOther)) {
            player.openInventory(target.getPlayer().getEnderChest());
            return;
        }

        EterniaServer.msg.sendMessage(player, Messages.SERVER_NO_PERM);
    }

    @CommandAlias("%hat")
    @Description("%hat_description")
    @CommandPermission("%hat_perm")
    public void onHat(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType() == Material.AIR) {
            EterniaServer.msg.sendMessage(player, Messages.ITEM_NOT_FOUND);
            return;
        }

        if (new NBTItem(itemStack).hasKey("EterniaLock").equals(Boolean.TRUE)) {
            return;
        }
        dropHelmet(player);
        setHelmet(player);
        EterniaServer.msg.sendMessage(player, Messages.ITEM_HELMET);
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
