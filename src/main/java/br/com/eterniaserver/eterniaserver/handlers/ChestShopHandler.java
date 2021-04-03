package br.com.eterniaserver.eterniaserver.handlers;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.objects.User;

import com.Acrobot.ChestShop.Events.PreTransactionEvent;
import com.Acrobot.ChestShop.Events.ShopCreatedEvent;
import com.Acrobot.ChestShop.Events.TransactionEvent;
import com.Acrobot.ChestShop.Signs.ChestShopSign;

import net.kyori.adventure.text.Component;

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
        final User user = new User(event.getPlayer());
        final Sign sign = event.getSign();
        final TransactionEvent.TransactionType type = sign.getLine(ChestShopSign.PRICE_LINE).contains("B") ?
                TransactionEvent.TransactionType.BUY : TransactionEvent.TransactionType.SELL;

        if (ChestShopSign.isAdminShop(sign)) {

            sign.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.CHEST_BUY), PersistentDataType.INTEGER, 20);
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onTransaction(PreTransactionEvent event) {
        final TransactionEvent.TransactionType Type = event.getTransactionType();
        if (Type == TransactionEvent.TransactionType.BUY) {
            Sign sign = event.getSign();
            sign.line(2, Component.text("B 80 : 40 S"));
            sign.update();
        }
    }
}
