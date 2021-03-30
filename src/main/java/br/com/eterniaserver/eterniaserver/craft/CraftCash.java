package br.com.eterniaserver.eterniaserver.craft;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.queries.Update;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.CashItem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CraftCash  {

    private final Map<UUID, CashItem> cashItem;
    private final EterniaServer plugin;

    public CraftCash(final EterniaServer plugin) {
        this.cashItem = new HashMap<>();
        this.plugin = plugin;
    }

    /**
     * Get the cash balance of the player
     * @param uuid of player
     * @return the cash balance
     */
    public int getCash(UUID uuid) {
        if (EterniaServer.getUserAPI().hasProfile(uuid)) {
            return EterniaServer.getUserAPI().getProfile(uuid).getCash();
        }
        EterniaServer.getUserAPI().createProfile(uuid, Bukkit.getOfflinePlayer(uuid).getName());
        return 0;
    }

    /**
     * Check if the player not has cash enough
     * @param uuid of player
     * @param amount the amount of cash needed
     * @return if has or not
     */
    public boolean notHasCash(UUID uuid, int amount) {
        return getCash(uuid) < amount;
    }

    /**
     * Defines the cash balance of the player
     * @param uuid of player
     * @param amount the amount of cash to set
     */
    public void setCash(UUID uuid, int amount) {
        if (EterniaServer.getUserAPI().hasProfile(uuid)) {
            EterniaServer.getUserAPI().getProfile(uuid).setCash(amount);

            final Update update = new Update(plugin.getString(Strings.TABLE_PLAYER));
            update.set.set("cash", amount);
            update.where.set("uuid", uuid.toString());
            SQL.executeAsync(update);
            return;
        }

        EterniaServer.getUserAPI().createProfile(uuid, Bukkit.getOfflinePlayer(uuid).getName());
        setCash(uuid, amount);
    }

    /**
     * Add cash to player account
     * @param uuid of player
     * @param amount the amount of cash to add
     */
    public void addCash(UUID uuid, int amount) {
        setCash(uuid, getCash(uuid) + amount);
    }

    /**
     * Remove cash to player account
     * @param uuid of player
     * @param amount the amount of cash to remove
     */
    public void removeCash(UUID uuid, int amount) {
        setCash(uuid, getCash(uuid) - amount);
    }

    /**
     * Check if player not is buying a product
     * @param uuid of player
     * @return if is or not
     */
    public boolean notBuying(UUID uuid) {
        return !cashItem.containsKey(uuid);
    }

    /**
     * Get the object of the product that the player
     * is about to buy
     * @param uuid of player
     * @return the CashItem object
     */
    public CashItem getCashBuy(UUID uuid) {
        return cashItem.get(uuid);
    }

    /**
     * Remove the product that the player
     * is about to buy
     * @param uuid of player
     */
    public void removeCashBuy(UUID uuid) {
        cashItem.remove(uuid);
    }

    /**
     * Opens a specific cash GUI for the player
     * @param player the player object
     * @param slotInt of GUI
     */
    public void menuGui(final Player player, int slotInt) {
        if (!plugin.getGuis().containsKey(slotInt)) {
            return;
        }

        final List<CashItem> items = plugin.getOthersGui().get(slotInt);
        final Inventory gui = Bukkit.getServer().createInventory(player, items.size(), plugin.getGuis().get(slotInt));
        for (int i = 0; i < items.size(); i++) {
            gui.setItem(i, items.get(i).getItemStack());
        }

        player.closeInventory();
        player.openInventory(gui);
    }

    /**
     * Defines which option the player chose
     * from the current GUI
     * @param player the player object
     * @param title of GUI
     * @param slot of GUI
     */
    public void permGui(final Player player, final String title, int slot) {
        final UUID uuid = player.getUniqueId();
        if (cashItem.containsKey(uuid)) {
            plugin.sendMessage(player, Messages.CASH_ALREADY_BUYING);
            plugin.sendMessage(player, Messages.CASH_CHOOSE);
            return;
        }

        final CashItem actualCashItem = plugin.getOthersGui().get(plugin.getGuisInvert().get(title)).get(slot);
        if (actualCashItem.isGlass()) {
            final Inventory gui = Bukkit.getServer().createInventory(player, plugin.getMenuGui().size(), "Cash");
            for (int i = 0; i < plugin.getMenuGui().size(); i++) {
                gui.setItem(i, plugin.getMenuGui().get(i));
            }

            player.closeInventory();
            player.openInventory(gui);
            return;
        }

        final int cost = actualCashItem.getCost();
        if (notHasCash(uuid, cost)) {
            plugin.sendMessage(player, Messages.CASH_NO_HAS, String.valueOf(cost));
            return;
        }

        plugin.sendMessage(player, Messages.CASH_COST, String.valueOf(cost));
        plugin.sendMessage(player, Messages.CASH_CHOOSE);
        cashItem.put(uuid, actualCashItem);
    }

}
