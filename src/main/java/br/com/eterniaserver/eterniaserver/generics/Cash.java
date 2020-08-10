package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@CommandAlias("cash")
@CommandPermission("eternia.cash")
public class Cash extends BaseCommand {

    private final int size = EterniaServer.cashConfig.getInt("size");

    public Cash() {

        final HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_CASH), Strings.UUID, Strings.BALANCE);
        temp.forEach((k, v) -> Vars.cash.put(UUID.fromString(k), Integer.parseInt(v)));
        Bukkit.getConsoleSender().sendMessage(Strings.MSG_LOAD_DATA.replace(Constants.MODULE, "Cash").replace(Constants.AMOUNT, String.valueOf(temp.size())));

        loadGui();

    }

    @Default
    public void onCash(Player player) {
        Inventory gui = Bukkit.getServer().createInventory(player, size, "Cash");
        for (int i = 0; i < size; i++) {
            gui.setItem(i, Vars.cashGui.get(i));
        }
        player.openInventory(gui);
    }

    @Subcommand("balance")
    @CommandCompletion("balance @players")
    @Syntax("<jogador>")
    public void onCashBalance(Player player, @Optional String playerName) {
        if (playerName != null) {
            UUID uuid = UUIDFetcher.getUUIDOf(playerName);
            if (Vars.cash.containsKey(uuid)) {
                player.sendMessage(Strings.M_CASH_BALANCE_OTHER.replace(Constants.AMOUNT, String.valueOf(Vars.cash.get(uuid))));
            } else {
                player.sendMessage(Strings.M_CASH_NO_PLAYER);
            }
        } else {
            player.sendMessage(Strings.M_CASH_BALANCE.replace(Constants.AMOUNT, String.valueOf(Vars.cash.get(UUIDFetcher.getUUIDOf(player.getName())))));
        }
    }

    @Subcommand("accept")
    public void onCashAccept(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        if (Vars.cashBuy.containsKey(uuid)) {
            final int slot = Vars.cashBuy.get(uuid);
            for (String line : EterniaServer.cashConfig.getStringList("gui." + slot + ".commands")) {
                final String modifiedCommand = InternMethods.setPlaceholders(player, line);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), modifiedCommand);
            }
            for (String line : EterniaServer.cashConfig.getStringList("gui." + slot + ".messages")) {
                final String modifiedText = InternMethods.setPlaceholders(player, line);
                player.sendMessage(Strings.getColor(modifiedText));
            }
            APICash.removeCash(uuid, EterniaServer.cashConfig.getInt("gui." + slot + ".cost"));
            player.sendMessage(Strings.M_CASH_SUCESS);
            Vars.cashBuy.remove(uuid);
        } else {
            player.sendMessage(Strings.M_CASH_NO_BUY);
        }
    }

    @Subcommand("deny")
    public void onCashDeny(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        if (Vars.cashBuy.containsKey(uuid)) {
            player.sendMessage(Strings.M_CASH_CANCEL);
            Vars.cashBuy.remove(uuid);
        } else {
            player.sendMessage(Strings.M_CASH_NO_BUY);
        }
    }

    @Subcommand("give")
    @CommandCompletion("@players 10")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.cash.admin")
    public void onCashGive(CommandSender player, OnlinePlayer targetP, Integer value) {
        if (value <= 0) {
            player.sendMessage(Strings.MSG_NO_NEGATIVE);
            return;
        }

        final Player target = targetP.getPlayer();
        APICash.addCash(UUIDFetcher.getUUIDOf(target.getName()), value);
        target.sendMessage(Strings.M_CASH_RECEIVED.replace(Constants.AMOUNT, String.valueOf(value)));
        player.sendMessage(Strings.M_CASH_SEND.replace(Constants.AMOUNT, String.valueOf(value)).replace(Constants.TARGET, target.getDisplayName()));
    }

    @Subcommand("remove")
    @CommandCompletion("@players 10")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.cash.admin")
    public void onCashRemove(CommandSender player, OnlinePlayer targetP, Integer value) {
        if (value <= 0) {
            player.sendMessage(Strings.MSG_NO_NEGATIVE);
            return;
        }

        final Player target = targetP.getPlayer();
        APICash.removeCash(UUIDFetcher.getUUIDOf(target.getName()), value);
        target.sendMessage(Strings.M_CASH_REMOVED.replace(Constants.AMOUNT, String.valueOf(value)));
        player.sendMessage(Strings.M_CASH_REMOVE.replace(Constants.AMOUNT, String.valueOf(value)));
    }

    private void loadGui() {
        Vars.cashGui.clear();
        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Loja de &aCash&8."));
        itemStack.setItemMeta(itemMeta);
        for (int i = 0; i < size; i++) {
            if (EterniaServer.cashConfig.contains("gui." + i)) {
                ItemStack item = new ItemStack(Material.getMaterial(EterniaServer.cashConfig.getString("gui." + i + ".material")));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', EterniaServer.cashConfig.getString("gui." + i + ".name")));
                List<String> lista = EterniaServer.cashConfig.getStringList("gui." + i + ".lore");
                for (int j = 0; j < lista.size(); j++) {
                    lista.set(j, ChatColor.translateAlternateColorCodes('&', lista.get(j)));
                }
                meta.setLore(lista);
                item.setItemMeta(meta);
                Vars.cashGui.add(item);
            } else {
                Vars.cashGui.add(itemStack);
            }

        }
    }

}
