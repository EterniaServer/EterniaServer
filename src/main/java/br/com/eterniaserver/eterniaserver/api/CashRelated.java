package br.com.eterniaserver.eterniaserver.api;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.sql.queries.Update;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.CashItem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CashRelated {

    private static final Map<UUID, CashItem> cashItem = new HashMap<>();

    private CashRelated() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Get the cash balance of the player
     * @param uuid of player
     * @return the cash balance
     */
    public static int getCash(UUID uuid) {
        if (PlayerRelated.hasProfile(uuid)) {
            return PlayerRelated.getProfile(uuid).getCash();
        } else {
            PlayerRelated.createProfile(uuid, Bukkit.getOfflinePlayer(uuid).getName());
            return 0;
        }
    }

    /**
     * Check if the player has cash enough
     * @param uuid of player
     * @param amount the amount of cash needed
     * @return if has or not
     */
    public static boolean hasCash(UUID uuid, int amount) {
        return getCash(uuid) >= amount;
    }

    /**
     * Defines the cash balance of the player
     * @param uuid of player
     * @param amount the amount of cash to set
     */
    public static void setCash(UUID uuid, int amount) {
        if (PlayerRelated.hasProfile(uuid)) {
            PlayerRelated.getProfile(uuid).setCash(amount);

            Update update = new Update(EterniaServer.getString(Strings.TABLE_PLAYER));
            update.set.set("cash", amount);
            update.where.set("uuid", uuid.toString());
            SQL.executeAsync(update);
            return;
        }
        
        PlayerRelated.createProfile(uuid, Bukkit.getOfflinePlayer(uuid).getName());
        setCash(uuid, amount);
    }

    /**
     * Add cash to player account
     * @param uuid of player
     * @param amount the amount of cash to add
     */
    public static void addCash(UUID uuid, int amount) {
        setCash(uuid, getCash(uuid) + amount);
    }

    /**
     * Remove cash to player account
     * @param uuid of player
     * @param amount the amount of cash to remove
     */
    public static void removeCash(UUID uuid, int amount) {
        setCash(uuid, getCash(uuid) - amount);
    }

    /**
     * Check if player is buying a product
     * @param uuid of player
     * @return if is or not
     */
    public static boolean isBuying(UUID uuid) {
        return cashItem.containsKey(uuid);
    }

    /**
     * Get the object of the product that the player
     * is about to buy
     * @param uuid of player
     * @return the CashItem object
     */
    public static CashItem getCashBuy(UUID uuid) {
        return cashItem.get(uuid);
    }

    /**
     * Remove the product that the player
     * is about to buy
     * @param uuid of player
     */
    public static void removeCashBuy(UUID uuid) {
        cashItem.remove(uuid);
    }

    /**
     * Opens a specific cash GUI for the player
     * @param player the player object
     * @param slotInt of GUI
     */
    public static void menuGui(final Player player, int slotInt) {
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
    public static void permGui(final Player player, final String title, int slot) {
        final String playerName = player.getName();
        final UUID uuid = player.getUniqueId();
        if (!cashItem.containsKey(uuid)) {
            CashItem actualCashItem = EterniaServer.getOthersGui().get(EterniaServer.getGuisInvert().get(title)).get(slot);
            if (!actualCashItem.isGlass()) {
                final int cost = actualCashItem.getCost();
                if (CashRelated.hasCash(uuid, cost)) {
                    EterniaServer.sendMessage(player, Messages.CASH_COST, String.valueOf(cost));
                    EterniaServer.sendMessage(player, Messages.CASH_CHOOSE);
                    cashItem.put(uuid, actualCashItem);
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
