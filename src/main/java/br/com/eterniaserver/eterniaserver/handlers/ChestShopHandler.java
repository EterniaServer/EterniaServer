package br.com.eterniaserver.eterniaserver.handlers;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.queries.Delete;
import br.com.eterniaserver.eternialib.core.queries.Insert;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;

import br.com.eterniaserver.eterniaserver.enums.Strings;
import com.Acrobot.ChestShop.Events.ShopCreatedEvent;
import com.Acrobot.ChestShop.Events.ShopDestroyedEvent;
import com.Acrobot.ChestShop.Events.TransactionEvent;
import com.Acrobot.ChestShop.Signs.ChestShopSign;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

import java.util.Locale;
import java.util.UUID;

public class ChestShopHandler implements Listener {

    private final EterniaServer plugin;

    public ChestShopHandler(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onShopCreated(ShopCreatedEvent event) {
        if (!ChestShopSign.isAdminShop(event.getSignLine(ChestShopSign.NAME_LINE))) {
            return;
        }

        final Sign sign = event.getSign();
        final TransactionEvent.TransactionType type = event.getSignLine(ChestShopSign.PRICE_LINE).contains("B") ?
                TransactionEvent.TransactionType.BUY : TransactionEvent.TransactionType.SELL;
        final String uuid = UUID.randomUUID().toString();

        sign.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CHEST_NAME), PersistentDataType.STRING, uuid);

        if (type == TransactionEvent.TransactionType.BUY) {
            sign.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CHEST_BUY_AMOUNT), PersistentDataType.INTEGER, 0);
        }
        else {
            sign.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CHEST_SELL_AMOUNT), PersistentDataType.INTEGER, 0);
        }

        sign.update();
        EterniaServer.getEconomyAPI().addSign(uuid, sign.getLocation());

        final Insert insert = new Insert(plugin.getString(Strings.TABLE_SHOP_ADDON));
        insert.columns.set("shop_uuid", "world", "coord_x", "coord_y", "coord_z");
        insert.values.set(uuid, sign.getWorld().getName(), (int) sign.getLocation().getX(), (int) sign.getLocation().getY(), (int) sign.getLocation().getZ());
        SQL.executeAsync(insert);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onShopDeleted(ShopDestroyedEvent event) {
        if (!ChestShopSign.isAdminShop(event.getSign().getLine(ChestShopSign.NAME_LINE))) {
            return;
        }

        final Sign sign = event.getSign();
        final String uuid = sign.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CHEST_NAME), PersistentDataType.STRING);

        if (uuid == null) {
            return;
        }

        final Delete delete = new Delete(plugin.getString(Strings.TABLE_SHOP_ADDON));
        delete.where.set("shop_uuid", uuid);
        SQL.executeAsync(delete);
        EterniaServer.getEconomyAPI().deleteSign(uuid);
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
        final Integer buySaved = sign.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CHEST_BUY_AMOUNT), PersistentDataType.INTEGER);

        if (buySaved == null) {
            return;
        }

        final double basePrice = plugin.getChestShopBuyRoof(material);
        final int amountBuying = Integer.parseInt(sign.getLine(ChestShopSign.QUANTITY_LINE));
        final int amountBuy = buySaved + amountBuying;
        final double finalPrice = (basePrice + (amountBuy * (basePrice / 100000)));

        if (finalPrice >= basePrice * 10) {
            return;
        }

        sign.setLine(ChestShopSign.PRICE_LINE, "B " + String.format(Locale.US, "%.2f", finalPrice));
        sign.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CHEST_BUY_AMOUNT), PersistentDataType.INTEGER, amountBuy);
        sign.update();
    }

    private void chestShopSell(final Sign sign, final Material material) {
        final Integer sellSaved = sign.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CHEST_SELL_AMOUNT), PersistentDataType.INTEGER);

        if (sellSaved == null) {
            return;
        }

        final double roofPrice = plugin.getChestShopSellRoof(material);
        final int amountSelling = Integer.parseInt(sign.getLine(ChestShopSign.QUANTITY_LINE));
        final int amountSell = sellSaved + amountSelling;

        final double finalPrice = (roofPrice - (amountSell * (roofPrice / 100000)));

        if (finalPrice < 0) {
            return;
        }

        sign.setLine(ChestShopSign.PRICE_LINE, "S " + String.format(Locale.US, "%.2f", finalPrice));
        sign.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CHEST_SELL_AMOUNT), PersistentDataType.INTEGER, amountSell);
        sign.update();
    }

}
