package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.CashItem;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.UUID;

public interface APICash {

    /**
     * Get the cash balance of the player
     * @param uuid of player
     * @return the cash balance
     */
    static int getCash(UUID uuid) {
        if (PluginVars.playerProfile.containsKey(uuid)) {
            return PluginVars.playerProfile.get(uuid).getCash();
        } else {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);
            EQueries.executeQuery(PluginConstants.getQueryInsert(EterniaServer.configs.tablePlayer, "(uuid, player_name, time, last, hours, balance, cash)",
                    "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + EterniaServer.configs.startMoney + "', '" + 0 +"')"));
            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.setBalance(EterniaServer.configs.startMoney);
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
            PluginVars.playerProfile.get(uuid).setCash(amount);
            EQueries.executeQuery(PluginConstants.getQueryUpdate(EterniaServer.configs.tablePlayer, PluginConstants.CASH_STR, amount, PluginConstants.UUID_STR, uuid.toString()));
        } else {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);
            EQueries.executeQuery(PluginConstants.getQueryInsert(EterniaServer.configs.tablePlayer, "(uuid, player_name, time, last, hours, balance, cash)",
                    "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + EterniaServer.configs.startMoney + "', '" + 0 +"')"));
            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.setBalance(EterniaServer.configs.startMoney);
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

    static CashItem getCashBuy(UUID uuid) {
        return PluginVars.cashItem.get(uuid);
    }

    static void removeCashBuy(UUID uuid) {
        PluginVars.cashItem.remove(uuid);
    }

    static void menuGui(final Player player, int slotInt) {
        if (EterniaServer.configs.guis.containsKey(slotInt)) {
            player.closeInventory();
            List<CashItem> itens = EterniaServer.configs.othersGui.get(slotInt);
            Inventory gui = Bukkit.getServer().createInventory(player, itens.size(), EterniaServer.configs.guis.get(slotInt));
            for (int i = 0; i < itens.size(); i++) {
                gui.setItem(i, itens.get(i).getItemStack());
            }
            player.openInventory(gui);
        }
    }

    static void permGui(final Player player, final String title, int slot) {
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (!PluginVars.cashItem.containsKey(uuid)) {
            CashItem cashItem = EterniaServer.configs.othersGui.get(EterniaServer.configs.guisInvert.get(title)).get(slot);
            if (!cashItem.isGlass()) {
                final int cost = cashItem.getCost();
                if (APICash.hasCash(uuid, cost)) {
                    EterniaServer.configs.sendMessage(player, Messages.CASH_COST, String.valueOf(cost));
                    EterniaServer.configs.sendMessage(player, Messages.CASH_CHOOSE);
                    PluginVars.cashItem.put(uuid, cashItem);
                } else {
                    EterniaServer.configs.sendMessage(player, Messages.CASH_NO_HAS, String.valueOf(getCash(uuid)));
                }
            } else {
                player.closeInventory();
                Inventory gui = Bukkit.getServer().createInventory(player, EterniaServer.configs.menuGui.size(), "Cash");

                for (int i = 0; i < EterniaServer.configs.menuGui.size(); i++) {
                    gui.setItem(i, EterniaServer.configs.menuGui.get(i));
                }

                player.openInventory(gui);
            }
        } else {
            EterniaServer.configs.sendMessage(player, Messages.CASH_ALREADY_BUYING);
            EterniaServer.configs.sendMessage(player, Messages.CASH_CHOOSE);
        }
    }

}
