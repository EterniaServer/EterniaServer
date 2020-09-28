package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
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

    static String getCashBuy(UUID uuid) {
        return PluginVars.cashItem.get(uuid);
    }

    static void removeCashBuy(UUID uuid) {
        PluginVars.cashItem.remove(uuid);
    }

    static int getCashGuiSize() {
        if (PluginVars.getCashGui() == null) {
            PluginVars.setCashGui(new UtilCashGui().get());
        }

        return PluginVars.getCashGui().getMenuGui().size();
    }

    static ItemStack getItemCashGui(int id) {
        return PluginVars.getCashGui().getMenuGui().get(id);
    }

    static void menuGui(final Player player, int slotInt) {
        switch (slotInt) {
            case 10:
                player.closeInventory();
                Inventory gui = Bukkit.getServer().createInventory(player, PluginVars.getCashGui().getPermGui().size(), "Perm");
                for (int i = 0; i < PluginVars.getCashGui().getPermGui().size(); i++) {
                    gui.setItem(i, PluginVars.getCashGui().getPermGui().get(i));
                }
                player.openInventory(gui);
                break;
            case 12:
                player.closeInventory();
                Inventory paco = Bukkit.getServer().createInventory(player, PluginVars.getCashGui().getPacoteGui().size(), "Pacotes");
                for (int i = 0; i < PluginVars.getCashGui().getPacoteGui().size(); i++) {
                    paco.setItem(i, PluginVars.getCashGui().getPacoteGui().get(i));
                }
                player.openInventory(paco);
                break;
            case 14:
                player.closeInventory();
                Inventory tag = Bukkit.getServer().createInventory(player, PluginVars.getCashGui().getTagGui().size(), "Tags");
                for (int i = 0; i < PluginVars.getCashGui().getTagGui().size(); i++) {
                    tag.setItem(i, PluginVars.getCashGui().getTagGui().get(i));
                }
                player.openInventory(tag);
                break;
            case 16:
                player.closeInventory();
                Inventory spawner = Bukkit.getServer().createInventory(player, PluginVars.getCashGui().getSpawnerGui().size(), "Spawners");
                for (int i = 0; i < PluginVars.getCashGui().getSpawnerGui().size(); i++) {
                    spawner.setItem(i, PluginVars.getCashGui().getSpawnerGui().get(i));
                }
                player.openInventory(spawner);
                break;
            default:
                break;
        }
    }

    static void permGui(final Player player, final String permString) {
        if (PluginVars.getCashGui() == null) {
            PluginVars.setCashGui(new UtilCashGui().get());
        }

        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (!PluginVars.cashItem.containsKey(uuid)) {
            if (EterniaServer.cashConfig.contains(permString)) {
                final int cost = EterniaServer.cashConfig.getInt(permString + ".cost");
                if (APICash.hasCash(uuid, cost)) {
                    EterniaServer.configs.sendMessage(player, Messages.CASH_COST, String.valueOf(cost));
                    EterniaServer.configs.sendMessage(player, Messages.CASH_CHOOSE);
                    PluginVars.cashItem.put(uuid, permString);
                } else {
                    EterniaServer.configs.sendMessage(player, Messages.CASH_NO_HAS);
                }
            } else {
                player.closeInventory();
                Inventory gui = Bukkit.getServer().createInventory(player, PluginVars.getCashGui().getMenuGui().size(), "Cash");
                for (int i = 0; i < PluginVars.getCashGui().getMenuGui().size(); i++) {
                    gui.setItem(i, PluginVars.getCashGui().getMenuGui().get(i));
                }
                player.openInventory(gui);
            }
        } else {
            EterniaServer.configs.sendMessage(player, Messages.CASH_ALREADY_BUYING);
            EterniaServer.configs.sendMessage(player, Messages.CASH_CHOOSE);
        }
    }

}
