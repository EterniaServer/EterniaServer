package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.Configs;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface APICash {

    /**
     * Get the cash balance of the player
     * @param uuid of player
     * @return the cash balance
     */
    static int getCash(UUID uuid) {
        if (PluginVars.playerProfile.containsKey(uuid)) {
            return PluginVars.playerProfile.get(uuid).cash;
        } else {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);
            EQueries.executeQuery(PluginConstants.getQueryInsert(Configs.instance.tablePlayer, "(uuid, player_name, time, last, hours, balance, cash)",
                    "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + Configs.instance.startMoney + "', '" + 0 +"')"));
            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.balance = Configs.instance.startMoney;
            PluginVars.playerProfile.put(uuid, playerProfile);
            return 0;
        }
    }

    /**
     * Check if the player has cash enough
     * @param uuid of player
     * @param amount the amount of cash needed
     * @return if has or not
     */
    static boolean hasCash(UUID uuid, int amount) {
        return getCash(uuid) >= amount;
    }

    /**
     * Defines the cash balance of the player
     * @param uuid of player
     * @param amount the amount of cash to set
     */
    static void setCash(UUID uuid, int amount) {
        if (PluginVars.playerProfile.containsKey(uuid)) {
            PluginVars.playerProfile.get(uuid).cash = amount;
            EQueries.executeQuery(PluginConstants.getQueryUpdate(Configs.instance.tablePlayer, PluginConstants.CASH_STR, amount, PluginConstants.UUID_STR, uuid.toString()));
        } else {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);
            EQueries.executeQuery(PluginConstants.getQueryInsert(Configs.instance.tablePlayer, "(uuid, player_name, time, last, hours, balance, cash)",
                    "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + Configs.instance.startMoney + "', '" + 0 +"')"));
            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.balance = Configs.instance.startMoney;
            PluginVars.playerProfile.put(uuid, playerProfile);
            setCash(uuid, amount);
        }
    }

    /**
     * Add cash to player account
     * @param uuid of player
     * @param amount the amount of cash to add
     */
    static void addCash(UUID uuid, int amount) {
        setCash(uuid, getCash(uuid) + amount);
    }

    /**
     * Remove cash to player account
     * @param uuid of player
     * @param amount the amount of cash to remove
     */
    static void removeCash(UUID uuid, int amount) {
        setCash(uuid, getCash(uuid) - amount);
    }

    static boolean isBuying(UUID uuid) {
        return PluginVars.cashItem.containsKey(uuid);
    }

    static String getCashBuy(UUID uuid) {
        return PluginVars.cashItem.get(uuid);
    }

    static void removeCashBuy(UUID uuid) {
        PluginVars.cashItem.remove(uuid);
    }

    static int getCashGuiSize() {
        if (PluginVars.cashGui == null) {
            PluginVars.cashGui = new UtilCashGui().get();
        }

        return PluginVars.cashGui.getMenuGui().size();
    }

    static ItemStack getItemCashGui(int id) {
        return PluginVars.cashGui.getMenuGui().get(id);
    }

    static void menuGui(final Player player, int slotInt) {
        switch (slotInt) {
            case 10:
                player.closeInventory();
                Inventory gui = Bukkit.getServer().createInventory(player, PluginVars.cashGui.getPermGui().size(), "Perm");
                for (int i = 0; i < PluginVars.cashGui.getPermGui().size(); i++) {
                    gui.setItem(i, PluginVars.cashGui.getPermGui().get(i));
                }
                player.openInventory(gui);
                break;
            case 12:
                player.closeInventory();
                Inventory paco = Bukkit.getServer().createInventory(player, PluginVars.cashGui.getPacoteGui().size(), "Pacotes");
                for (int i = 0; i < PluginVars.cashGui.getPacoteGui().size(); i++) {
                    paco.setItem(i, PluginVars.cashGui.getPacoteGui().get(i));
                }
                player.openInventory(paco);
                break;
            case 14:
                player.closeInventory();
                Inventory tag = Bukkit.getServer().createInventory(player, PluginVars.cashGui.getTagGui().size(), "Tags");
                for (int i = 0; i < PluginVars.cashGui.getTagGui().size(); i++) {
                    tag.setItem(i, PluginVars.cashGui.getTagGui().get(i));
                }
                player.openInventory(tag);
                break;
            case 16:
                player.closeInventory();
                Inventory spawner = Bukkit.getServer().createInventory(player, PluginVars.cashGui.getSpawnerGui().size(), "Spawners");
                for (int i = 0; i < PluginVars.cashGui.getSpawnerGui().size(); i++) {
                    spawner.setItem(i, PluginVars.cashGui.getSpawnerGui().get(i));
                }
                player.openInventory(spawner);
                break;
            default:
                break;
        }
    }

    static void permGui(final Player player, final String permString) {
        if (PluginVars.cashGui == null) {
            PluginVars.cashGui = new UtilCashGui().get();
        }

        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (!PluginVars.cashItem.containsKey(uuid)) {
            if (EterniaServer.cashConfig.contains(permString)) {
                final int cost = EterniaServer.cashConfig.getInt(permString + ".cost");
                if (APICash.hasCash(uuid, cost)) {
                    Configs.instance.sendMessage(player, Messages.CashCost, String.valueOf(cost));
                    Configs.instance.sendMessage(player, Messages.CashChoose);
                    PluginVars.cashItem.put(uuid, permString);
                } else {
                    Configs.instance.sendMessage(player, Messages.CashNoHas);
                }
            } else {
                player.closeInventory();
                Inventory gui = Bukkit.getServer().createInventory(player, PluginVars.cashGui.getMenuGui().size(), "Cash");
                for (int i = 0; i < PluginVars.cashGui.getMenuGui().size(); i++) {
                    gui.setItem(i, PluginVars.cashGui.getMenuGui().get(i));
                }
                player.openInventory(gui);
            }
        } else {
            Configs.instance.sendMessage(player, Messages.CashAlreadyBuying);
            Configs.instance.sendMessage(player, Messages.CashChoose);
        }
    }

}
