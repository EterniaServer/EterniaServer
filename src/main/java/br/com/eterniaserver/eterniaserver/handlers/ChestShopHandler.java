package br.com.eterniaserver.eterniaserver.handlers;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;

import com.Acrobot.ChestShop.Events.ShopCreatedEvent;
import com.Acrobot.ChestShop.Events.TransactionEvent;
import com.Acrobot.ChestShop.Signs.ChestShopSign;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

public class ChestShopHandler implements Listener {

    private final EterniaServer plugin;

    public ChestShopHandler(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onSignCreated(ShopCreatedEvent event) {
        final Sign sign = event.getSign();

        if (!ChestShopSign.isAdminShop(sign)) {
            return;
        }

        final TransactionEvent.TransactionType type = sign.getLine(ChestShopSign.PRICE_LINE).contains("B") ?
                TransactionEvent.TransactionType.BUY : TransactionEvent.TransactionType.SELL;

        if (type == TransactionEvent.TransactionType.BUY) {
            sign.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CHEST_BUY_AMOUNT), PersistentDataType.INTEGER, 0);
        }
        else {
            sign.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CHEST_SELL_AMOUNT), PersistentDataType.INTEGER, 0);
        }

        sign.update();
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onTransaction(TransactionEvent event) {
        final Sign sign = event.getSign();
        final Material material;

        if (!ChestShopSign.isAdminShop(sign)) {
            return;
        }

        try {
            material = Material.valueOf(sign.getLine(ChestShopSign.ITEM_LINE));
        } catch (IllegalArgumentException e) {
            return;
        }

        final TransactionEvent.TransactionType Type = event.getTransactionType();

        if (Type == TransactionEvent.TransactionType.BUY) {
            chestShopBuy(sign, material);
        }
        else {
            chestShopSell(sign, material);
        }
        sign.update();
    }

    private void chestShopBuy(final Sign sign, final Material material) {
        final double price = getPrice(sign.getLine(ChestShopSign.PRICE_LINE), true);
        final double roofPrice = plugin.getChestShopBuyRoof(material);

        if (price >= roofPrice) {
            return;
        }

        final int amountBuying = Integer.parseInt(sign.getLine(ChestShopSign.QUANTITY_LINE));
        final int amountBuy = sign.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CHEST_BUY_AMOUNT), PersistentDataType.INTEGER) + amountBuying;
        final double finalPrice = (roofPrice / 100000 * amountBuy) + price;

        sign.setLine(ChestShopSign.PRICE_LINE, "B " + finalPrice);
        sign.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CHEST_BUY_AMOUNT), PersistentDataType.INTEGER, amountBuy);
    }

    private void chestShopSell(final Sign sign, final Material material) {
        final double price = getPrice(sign.getLine(ChestShopSign.PRICE_LINE), false);
        final double roofPrice = plugin.getChestShopSellRoof(material);

        if (price < roofPrice) {
            return;
        }

        final int amountSelling = Integer.parseInt(sign.getLine(ChestShopSign.QUANTITY_LINE));
        final int amountSell = sign.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CHEST_SELL_AMOUNT), PersistentDataType.INTEGER) + amountSelling;
        final double finalPrice = (roofPrice / 100000 * amountSell) + price;

        sign.setLine(ChestShopSign.PRICE_LINE, "B " + finalPrice);
        sign.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CHEST_SELL_AMOUNT), PersistentDataType.INTEGER, amountSell);
    }

    private double getPrice(final String line, final boolean buying) {
        if (buying) {
            return Double.parseDouble(line.replace("B ", ""));
        }
        return Double.parseDouble(line.replace("S ", ""));
    }

}
