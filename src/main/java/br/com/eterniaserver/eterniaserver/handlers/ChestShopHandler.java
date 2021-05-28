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

import java.text.DecimalFormat;

public class ChestShopHandler implements Listener {

    private final EterniaServer plugin;
    private final DecimalFormat economyFormat = new DecimalFormat(".##");

    public ChestShopHandler(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onSignCreated(ShopCreatedEvent event) {
        if (!ChestShopSign.isAdminShop(event.getSignLine(ChestShopSign.NAME_LINE))) {
            return;
        }

        final Sign sign = event.getSign();
        final TransactionEvent.TransactionType type = event.getSignLine(ChestShopSign.PRICE_LINE).contains("B") ?
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

        if (!ChestShopSign.isAdminShop(sign)) {
            return;
        }

        final Material material = Material.getMaterial(sign.getLine(ChestShopSign.ITEM_LINE).toUpperCase());
        final TransactionEvent.TransactionType Type = event.getTransactionType();

        if (Type == TransactionEvent.TransactionType.BUY) {
            chestShopBuy(sign, material);
        }
        else {
            chestShopSell(sign, material);
        }
    }

    private void chestShopBuy(final Sign sign, final Material material) {
        final double roofPrice = plugin.getChestShopBuyRoof(material);
        final int amountBuying = Integer.parseInt(sign.getLine(ChestShopSign.QUANTITY_LINE));
        final int amountBuy = sign.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CHEST_BUY_AMOUNT), PersistentDataType.INTEGER) + amountBuying;
        final double finalPrice = (roofPrice + (amountBuy * (roofPrice * 100000)));

        if (finalPrice >= roofPrice) {
            return;
        }

        sign.setLine(ChestShopSign.PRICE_LINE, "B " + economyFormat.format(finalPrice));
        sign.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CHEST_BUY_AMOUNT), PersistentDataType.INTEGER, amountBuy);
        sign.update();
    }

    private void chestShopSell(final Sign sign, final Material material) {
        final double roofPrice = plugin.getChestShopSellRoof(material);
        final int amountSelling = Integer.parseInt(sign.getLine(ChestShopSign.QUANTITY_LINE));
        final int amountSell = sign.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CHEST_SELL_AMOUNT), PersistentDataType.INTEGER) + amountSelling;

        final double finalPrice = (roofPrice - (amountSell * (roofPrice * 100000)));

        if (finalPrice < 0) {
            return;
        }

        sign.setLine(ChestShopSign.PRICE_LINE, "S " + finalPrice);
        sign.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CHEST_SELL_AMOUNT), PersistentDataType.INTEGER, amountSell);
        sign.update();
    }

}
