package br.com.eterniaserver.eterniaserver.modules.cash;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.queries.Update;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.CashAPI;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.BuyingItem;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


final class Services {

    static class Cash {

        private final EterniaServer plugin;

        private final Map<UUID, BuyingItem> cashItem = new HashMap<>();

        protected Cash(final EterniaServer plugin) {
            this.plugin = plugin;
        }

        /**
         * Get the object of the product that the player
         * is about to buy
         * @param uuid of player
         * @return the BuyingItem object
         */
        public BuyingItem getCashBuy(UUID uuid) {
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
         * Defines which option the player chose
         * from the current GUI
         * @param player the player object
         * @param itemStack clicked by user
         * @return int indicating if it has a gui
         */
        public int guiCash(final Player player, ItemStack itemStack) {
            if (itemStack == null) {
                return 0;
            }

            final ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null) {
                return 0;
            }

            final PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
            final boolean hasGuiName = dataContainer.has(plugin.getKey(ItemsKeys.CASH_GUI_NAME), PersistentDataType.STRING);
            final boolean hasCommands = dataContainer.has(plugin.getKey(ItemsKeys.CASH_ITEM_COMMANDS), PersistentDataType.STRING);
            final boolean hasLore = dataContainer.has(plugin.getKey(ItemsKeys.CASH_ITEM_LORE), PersistentDataType.STRING);

            if (hasGuiName && hasLore && !hasCommands) {
                final String guiName = dataContainer.get(plugin.getKey(ItemsKeys.CASH_GUI_NAME), PersistentDataType.STRING);
                final Inventory inventory = EterniaServer.getGuiAPI().getGUI(guiName, player);
                player.closeInventory();
                player.openInventory(inventory);
                return 1;
            }

            else if (!hasGuiName && hasLore && !hasCommands) {
                final Inventory inventory = EterniaServer.getGuiAPI().getGUI(plugin.getString(Strings.CASH_MENU_TITLE), player);
                player.closeInventory();
                player.openInventory(inventory);
                return 1;
            }

            else if (hasGuiName && hasLore) {
                final String commands = dataContainer.get(plugin.getKey(ItemsKeys.CASH_ITEM_COMMANDS), PersistentDataType.STRING);
                final Integer cost = dataContainer.get(plugin.getKey(ItemsKeys.CASH_ITEM_COST), PersistentDataType.INTEGER);
                final String messages = dataContainer.get(plugin.getKey(ItemsKeys.CASH_ITEM_MESSAGE), PersistentDataType.STRING);
                final UUID uuid = player.getUniqueId();

                if (messages == null || commands == null || cost == null || cashItem.containsKey(uuid)) {
                    plugin.sendMiniMessages(player, Messages.CASH_ALREADY_BUYING);
                    plugin.sendMiniMessages(player, Messages.CASH_CHOOSE);
                    player.closeInventory();
                    return 3;
                }

                final BuyingItem buyingItem = new BuyingItem(messages, commands, cost);
                if (!EterniaServer.getCashAPI().has(uuid, buyingItem.getCost())) {
                    plugin.sendMiniMessages(player, Messages.CASH_NO_HAS, String.valueOf(buyingItem.getCost()));
                    return 2;
                }

                plugin.sendMiniMessages(player, Messages.CASH_COST, String.valueOf(buyingItem.getCost()));
                plugin.sendMiniMessages(player, Messages.CASH_CHOOSE);
                cashItem.put(uuid, buyingItem);
                player.closeInventory();
                return 1;
            }

            return 0;
        }
    }

    static class CraftCash implements CashAPI {

        private final EterniaServer plugin;

        private UUID uuidCache;
        private PlayerProfile playerProfileCache;

        protected CraftCash(final EterniaServer plugin) {
            this.plugin = plugin;
            this.uuidCache = null;
            this.playerProfileCache = null;
        }

        private PlayerProfile getPlayerProfile(UUID uuid) {
            if (this.uuidCache == null || this.uuidCache != uuid) {
                this.uuidCache = uuid;
                this.playerProfileCache = plugin.userManager().get(uuid);
            }

            return this.playerProfileCache;
        }

        @Override
        public boolean hasAccount(UUID uuid) {
            return getPlayerProfile(uuid) != null;
        }

        @Override
        public int getBalance(UUID uuid) {
            return getPlayerProfile(uuid).getCash();
        }

        @Override
        public boolean has(UUID uuid, int amount) {
            return getPlayerProfile(uuid).getCash() >= amount;
        }

        @Override
        public void depositBalance(UUID uuid, int amount) {
            setBalance(uuid, getBalance(uuid) + amount);
        }

        @Override
        public void withdrawBalance(UUID uuid, int amount) {
            setBalance(uuid, getBalance(uuid) - amount);
        }

        @Override
        public void setBalance(UUID uuid, int amount) {
            getPlayerProfile(uuid).setCash(amount);

            final Update update = new Update(plugin.getString(Strings.TABLE_PLAYER));
            update.set.set("cash", amount);
            update.where.set("uuid", uuid.toString());
            SQL.executeAsync(update);
        }
    }

}
