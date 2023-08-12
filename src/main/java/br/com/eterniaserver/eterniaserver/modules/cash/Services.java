package br.com.eterniaserver.eterniaserver.modules.cash;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.CashAPI;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.cash.Entities.CashBalance;
import br.com.eterniaserver.eterniaserver.objects.BuyingItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;


final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class Cash {

        private final EterniaServer plugin;

        private final Map<UUID, BuyingItem> cashItem = new HashMap<>();

        protected Cash(final EterniaServer plugin) {
            this.plugin = plugin;
        }

        /**
         * Get the object of the product that the player
         * @param uuid of player
         * @return the BuyingItem object
         */
        public BuyingItem getCashBuy(UUID uuid) {
            return cashItem.get(uuid);
        }

        /**
         * Remove the product that the player
         * about to buy
         * @param uuid of player
         */
        public void removeCashBuy(UUID uuid) {
            cashItem.remove(uuid);
        }

        /**
         * Defines, which option the player chose
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

            PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
            boolean hasGuiName = dataContainer.has(
                    plugin.getKey(ItemsKeys.CASH_GUI_NAME),
                    PersistentDataType.STRING
            );
            boolean hasCommands = dataContainer.has(
                    plugin.getKey(ItemsKeys.CASH_ITEM_COMMANDS),
                    PersistentDataType.STRING
            );
            boolean hasLore = dataContainer.has(
                    plugin.getKey(ItemsKeys.CASH_ITEM_LORE),
                    PersistentDataType.STRING
            );

            if (hasGuiName && hasLore && !hasCommands) {
                String guiName = dataContainer.get(
                        plugin.getKey(ItemsKeys.CASH_GUI_NAME),
                        PersistentDataType.STRING
                );
                Inventory inventory = EterniaServer.getGuiAPI().getGUI(guiName, player);
                player.closeInventory();
                player.openInventory(inventory);
                return 1;
            }

            else if (!hasGuiName && hasLore && !hasCommands) {
                Inventory inventory = EterniaServer.getGuiAPI().getGUI(
                        plugin.getString(Strings.CASH_MENU_TITLE),
                        player
                );
                player.closeInventory();
                player.openInventory(inventory);
                return 1;
            }

            else if (hasGuiName && hasLore) {
                String commands = dataContainer.get(
                        plugin.getKey(ItemsKeys.CASH_ITEM_COMMANDS),
                        PersistentDataType.STRING
                );
                Integer cost = dataContainer.get(
                        plugin.getKey(ItemsKeys.CASH_ITEM_COST),
                        PersistentDataType.INTEGER
                );
                String messages = dataContainer.get(
                        plugin.getKey(ItemsKeys.CASH_ITEM_MESSAGE),
                        PersistentDataType.STRING
                );
                UUID uuid = player.getUniqueId();

                if (messages == null || commands == null || cost == null || cashItem.containsKey(uuid)) {
                    plugin.sendMiniMessages(player, Messages.CASH_ALREADY_BUYING);
                    plugin.sendMiniMessages(player, Messages.CASH_CHOOSE);
                    player.closeInventory();
                    return 3;
                }

                BuyingItem buyingItem = new BuyingItem(messages, commands, cost);
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

        private final DatabaseInterface databaseInterface;

        private UUID uuidCache;
        private CashBalance cashBalance;

        protected CraftCash() {
            this.databaseInterface = EterniaLib.getDatabase();

            this.uuidCache = null;
            this.cashBalance = null;
        }

        private CashBalance getCash(UUID uuid) {
            if (this.uuidCache == null || this.uuidCache != uuid) {
                this.uuidCache = uuid;
                this.cashBalance = databaseInterface.get(CashBalance.class, uuid);
            }

            return this.cashBalance;
        }

        @Override
        public CompletableFuture<Boolean> hasAccount(UUID uuid) {
            return CompletableFuture.supplyAsync(() -> getCash(uuid).getUuid() != null);
        }

        @Override
        public CompletableFuture<CashBalance> createAccount(UUID uuid, Integer balance) {
            return CompletableFuture.supplyAsync(() -> {
                CashBalance cash = getCash(uuid);

                cash.setUuid(uuid);
                databaseInterface.insert(CashBalance.class, cash);

                return cash;
            });
        }

        @Override
        public int getBalance(UUID uuid) {
            return getCash(uuid).getBalance();
        }

        @Override
        public boolean has(UUID uuid, int amount) {
            return getCash(uuid).getBalance() >= amount;
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
            CashBalance cash = getCash(uuid);
            cash.setBalance(amount);

            hasAccount(uuid).whenCompleteAsync((has, throwableVerify) -> {
                if (throwableVerify != null) {
                    EterniaLib.registerLog("EE-201-Cash-Verify");
                    return;
                }

                if (Boolean.TRUE.equals(has)) {
                    databaseInterface.update(CashBalance.class, cash);
                    return;
                }

                createAccount(uuid, amount).whenCompleteAsync((createCash, throwableCreate) -> {
                    if (throwableCreate != null) {
                        EterniaLib.registerLog("EE-201-Cash-Create");
                        return;
                    }

                    Bukkit.getLogger().log(Level.INFO, "Created cash account for %s with %d balance.".formatted(
                            createCash.getUuid().toString(),
                            createCash.getBalance()
                    ));
                });
            });
        }
    }

}
