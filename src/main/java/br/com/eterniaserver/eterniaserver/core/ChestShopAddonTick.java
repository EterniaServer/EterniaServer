package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import com.Acrobot.ChestShop.Events.TransactionEvent;
import com.Acrobot.ChestShop.Signs.ChestShopSign;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

public class ChestShopAddonTick extends BukkitRunnable {

    private final EterniaServer plugin;
    private final DecimalFormat economyFormat = new DecimalFormat(".##");

    public ChestShopAddonTick(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        final int tick = plugin.getInteger(Integers.CS_CLEAR_TICK);
        final int removed = plugin.getInteger(Integers.CS_AMOUNT_REMOVED);
        int shop = 0;
        for (final Location location : EterniaServer.getEconomyAPI().getSigns()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                final BlockState state = location.getBlock().getState();

                if (state instanceof Sign) {
                    updateSign((Sign) state, removed);
                }
            }, (long) tick * ++shop);
        }
    }

    private synchronized void updateSign(Sign sign, int removed) {
        final TransactionEvent.TransactionType type = sign.getLine(ChestShopSign.PRICE_LINE).contains("B") ?
                TransactionEvent.TransactionType.BUY : TransactionEvent.TransactionType.SELL;
        final Material material = Material.getMaterial(sign.getLine(ChestShopSign.ITEM_LINE).toUpperCase());

        if (material == null) {
            return;
        }

        final Integer amount;
        if (type == TransactionEvent.TransactionType.BUY) {
            amount = sign.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CHEST_BUY_AMOUNT), PersistentDataType.INTEGER);

            if (amount == null) {
                return;
            }

            final double roofPrice = plugin.getChestShopBuyRoof(material);
            final int amountBuy = amount - removed;
            final double finalPrice = roofPrice + (amountBuy * (roofPrice * 100000));

            if (finalPrice >= roofPrice) {
                return;
            }

            sign.setLine(ChestShopSign.PRICE_LINE, "B " + economyFormat.format(finalPrice));
            sign.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CHEST_BUY_AMOUNT), PersistentDataType.INTEGER, amountBuy);
        }
        else {
            amount = sign.getPersistentDataContainer().get(plugin.getKey(ItemsKeys.CHEST_SELL_AMOUNT), PersistentDataType.INTEGER);

            if (amount == null) {
                return;
            }

            final double roofPrice = plugin.getChestShopSellRoof(material);
            final int amountSell = amount - removed;

            final double finalPrice = roofPrice - (amountSell * (roofPrice * 100000));

            if (finalPrice < 0) {
                return;
            }

            sign.setLine(ChestShopSign.PRICE_LINE, "S " + finalPrice);
            sign.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CHEST_SELL_AMOUNT), PersistentDataType.INTEGER, amountSell);
        }

        sign.update();
    }

}
