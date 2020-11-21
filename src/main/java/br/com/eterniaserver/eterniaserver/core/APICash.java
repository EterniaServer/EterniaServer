package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eternialib.sql.queries.Insert;
import br.com.eterniaserver.eternialib.sql.queries.Update;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Strings;
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
        if (Vars.playerProfile.containsKey(uuid)) {
            return Vars.playerProfile.get(uuid).getCash();
        } else {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);

            Insert insert = new Insert(EterniaServer.getString(Strings.TABLE_PLAYER));
            insert.columns.set("uuid", "player_name", "time", "last", "hours", "balance");
            insert.values.set(uuid.toString(), playerName, time, time, 0, EterniaServer.getDouble(Doubles.START_MONEY));
            SQL.executeAsync(insert);

            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.setBalance(EterniaServer.getDouble(Doubles.START_MONEY));
            Vars.playerProfile.put(uuid, playerProfile);
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
        if (Vars.playerProfile.containsKey(uuid)) {
            Vars.playerProfile.get(uuid).setCash(amount);

            Update update = new Update(EterniaServer.getString(Strings.TABLE_PLAYER));
            update.set.set("cash", amount);
            update.where.set("uuid", uuid.toString());
            SQL.executeAsync(update);
        } else {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);

            Insert insert = new Insert(EterniaServer.getString(Strings.TABLE_PLAYER));
            insert.columns.set("uuid", "player_name", "time", "last", "hours", "balance");
            insert.values.set(uuid.toString(), playerName, time, time, 0, EterniaServer.getDouble(Doubles.START_MONEY));
            SQL.executeAsync(insert);

            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.setBalance(EterniaServer.getDouble(Doubles.START_MONEY));
            Vars.playerProfile.put(uuid, playerProfile);
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

    /**
     * Check if player is buying a product
     * @param uuid of player
     * @return if is or not
     */
    static boolean isBuying(UUID uuid) {
        return Vars.cashItem.containsKey(uuid);
    }

    /**
     * Get the object of the product that the player
     * is about to buy
     * @param uuid of player
     * @return the CashItem object
     */
    static CashItem getCashBuy(UUID uuid) {
        return Vars.cashItem.get(uuid);
    }

    /**
     * Remove the product that the player
     * is about to buy
     * @param uuid of player
     */
    static void removeCashBuy(UUID uuid) {
        Vars.cashItem.remove(uuid);
    }

    /**
     * Opens a specific cash GUI for the player
     * @param player the player object
     * @param slotInt of GUI
     */
    static void menuGui(final Player player, int slotInt) {
        if (EterniaServer.getGuis().containsKey(slotInt)) {
            player.closeInventory();
            List<CashItem> itens = EterniaServer.getOthersGui().get(slotInt);
            Inventory gui = Bukkit.getServer().createInventory(player, itens.size(), EterniaServer.getGuis().get(slotInt));
            for (int i = 0; i < itens.size(); i++) {
                gui.setItem(i, itens.get(i).getItemStack());
            }
            player.openInventory(gui);
        }
    }

    /**
     * Defines which option the player chose
     * from the current GUI
     * @param player the player object
     * @param title of GUI
     * @param slot of GUI
     */
    static void permGui(final Player player, final String title, int slot) {
        final String playerName = player.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (!Vars.cashItem.containsKey(uuid)) {
            CashItem cashItem = EterniaServer.getOthersGui().get(EterniaServer.getGuisInvert().get(title)).get(slot);
            if (!cashItem.isGlass()) {
                final int cost = cashItem.getCost();
                if (APICash.hasCash(uuid, cost)) {
                    EterniaServer.sendMessage(player, Messages.CASH_COST, String.valueOf(cost));
                    EterniaServer.sendMessage(player, Messages.CASH_CHOOSE);
                    Vars.cashItem.put(uuid, cashItem);
                } else {
                    EterniaServer.sendMessage(player, Messages.CASH_NO_HAS, String.valueOf(cost));
                }
            } else {
                player.closeInventory();
                Inventory gui = Bukkit.getServer().createInventory(player, EterniaServer.getMenuGui().size(), "Cash");

                for (int i = 0; i < EterniaServer.getMenuGui().size(); i++) {
                    gui.setItem(i, EterniaServer.getMenuGui().get(i));
                }

                player.openInventory(gui);
            }
        } else {
            EterniaServer.sendMessage(player, Messages.CASH_ALREADY_BUYING);
            EterniaServer.sendMessage(player, Messages.CASH_CHOOSE);
        }
    }

}
