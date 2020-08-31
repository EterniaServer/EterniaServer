package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.strings.MSG;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class EventEntityInventoryClick implements Listener {

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityInventoryClick(InventoryClickEvent e) {
        if (e.isCancelled()) return;

        final Player player = (Player) e.getWhoClicked();
        final ItemStack itemStack = e.getCurrentItem();
        if (itemStack != null && (EterniaServer.serverConfig.getBoolean("spawners.prevent-anvil")
                && EterniaServer.serverConfig.getBoolean("modules.spawners")
                && e.getInventory().getType() == InventoryType.ANVIL
                && itemStack.getType() == Material.SPAWNER)) {
            e.setCancelled(true);
            player.sendMessage(MSG.MSG_SPAWNER_NAME);
            player.sendMessage(InternMethods.putName(player, MSG.MSG_SPAWNER_LOG));
        }

        if (EterniaServer.serverConfig.getBoolean("modules.cash")) {
            switch (e.getView().getTitle()) {
                case "Cash":
                    menuGui(player, e.getSlot());
                    e.setCancelled(true);
                    break;
                case "Perm":
                    e.setCancelled(chooseGUi(player, "guis.perm.", e.getSlot()));
                    break;
                case "Pacotes":
                    e.setCancelled(chooseGUi(player, "guis.pacotes.", e.getSlot()));
                    break;
                case "Tags":
                    e.setCancelled(chooseGUi(player, "guis.tags.", e.getSlot()));
                    break;
                case "Spawners":
                    e.setCancelled(chooseGUi(player, "guis.spawners.", e.getSlot()));
                    break;
                default:
                    break;
            }
        }

    }

    private boolean chooseGUi(Player player, String guiName, int slot) {
        permGui(player, guiName + slot);
        return true;
    }

    private void menuGui(final Player player, int slotInt) {
        switch (slotInt) {
            case 10:
                player.closeInventory();
                Inventory gui = Bukkit.getServer().createInventory(player, BaseCmdCash.cashGui.getPermGui().size(), "Perm");
                for (int i = 0; i < BaseCmdCash.cashGui.getPermGui().size(); i++) {
                    gui.setItem(i, BaseCmdCash.cashGui.getPermGui().get(i));
                }
                player.openInventory(gui);
                break;
            case 12:
                player.closeInventory();
                Inventory paco = Bukkit.getServer().createInventory(player, BaseCmdCash.cashGui.getPacoteGui().size(), "Pacotes");
                for (int i = 0; i < BaseCmdCash.cashGui.getPacoteGui().size(); i++) {
                    paco.setItem(i, BaseCmdCash.cashGui.getPacoteGui().get(i));
                }
                player.openInventory(paco);
                break;
            case 14:
                player.closeInventory();
                Inventory tag = Bukkit.getServer().createInventory(player, BaseCmdCash.cashGui.getTagGui().size(), "Tags");
                for (int i = 0; i < BaseCmdCash.cashGui.getTagGui().size(); i++) {
                    tag.setItem(i, BaseCmdCash.cashGui.getTagGui().get(i));
                }
                player.openInventory(tag);
                break;
            case 16:
                player.closeInventory();
                Inventory spawner = Bukkit.getServer().createInventory(player, BaseCmdCash.cashGui.getSpawnerGui().size(), "Spawners");
                for (int i = 0; i < BaseCmdCash.cashGui.getSpawnerGui().size(); i++) {
                    spawner.setItem(i, BaseCmdCash.cashGui.getSpawnerGui().get(i));
                }
                player.openInventory(spawner);
                break;
            default:
                break;
        }
    }

    private void permGui(final Player player, final String permString) {
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (!Vars.cashItem.containsKey(uuid)) {
            if (EterniaServer.cashConfig.contains(permString)) {
                final int cost = EterniaServer.cashConfig.getInt(permString + ".cost");
                if (APICash.hasCash(uuid, cost)) {
                    player.sendMessage(MSG.M_CASH_COST.replace(Constants.AMOUNT, String.valueOf(cost)));
                    player.sendMessage(MSG.M_CASH);
                    Vars.cashItem.put(uuid, permString);
                } else {
                    player.sendMessage(MSG.M_CASH_NO);
                }
            } else {
                player.closeInventory();
                Inventory gui = Bukkit.getServer().createInventory(player, BaseCmdCash.cashGui.getMenuGui().size(), "Cash");
                for (int i = 0; i < BaseCmdCash.cashGui.getMenuGui().size(); i++) {
                    gui.setItem(i, BaseCmdCash.cashGui.getMenuGui().get(i));
                }
                player.openInventory(gui);
            }
        } else {
            player.sendMessage(MSG.M_CASH_ALREADY);
            player.sendMessage(MSG.M_CASH);
        }
    }

}
