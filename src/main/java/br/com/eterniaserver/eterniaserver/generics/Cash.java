package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.configs.Configs;
import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Strings;

import br.com.eterniaserver.eterniaserver.objects.CashGui;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@CommandAlias("cash")
@CommandPermission("eternia.cash")
public class Cash extends BaseCommand {

    protected static CashGui cashGui;

    public Cash() {

        final Map<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Configs.tableCash), Constants.UUID_STR, Constants.BALANCE_STR);
        temp.forEach((k, v) -> Vars.cash.put(UUID.fromString(k), Integer.parseInt(v)));
        Bukkit.getConsoleSender().sendMessage(Strings.MSG_LOAD_DATA.replace(Constants.MODULE, "Cash").replace(Constants.AMOUNT, String.valueOf(temp.size())));

        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Loja de &aCash&8."));
        itemStack.setItemMeta(itemMeta);

        Map<Integer, ItemStack> menuGui = new HashMap<>();
        Map<Integer, ItemStack> permGui = new HashMap<>();
        Map<Integer, ItemStack> pacoGui = new HashMap<>();
        Map<Integer, ItemStack> tagsGui = new HashMap<>();
        Map<Integer, ItemStack> spawGui = new HashMap<>();

        int size = EterniaServer.cashConfig.getInt("menu.size");
        for (int i = 0; i < size; i++) {
            if (!EterniaServer.cashConfig.contains("menu." + i)) {
                menuGui.put(i, itemStack);
                continue;
            }

            ItemStack item = new ItemStack(Material.getMaterial(EterniaServer.cashConfig.getString("menu." + i + ".material")));
            ItemMeta meta = item.getItemMeta();
            List<String> lista = EterniaServer.cashConfig.getStringList("menu." + i + ".lore");
            for (int j = 0; j < lista.size(); j++) {
                lista.set(j, ChatColor.translateAlternateColorCodes('&', lista.get(j)));
            }

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', EterniaServer.cashConfig.getString("menu." + i + ".name")));
            meta.setLore(lista);
            item.setItemMeta(meta);
            menuGui.put(i, item);
        }

        for (int i = 0; i < EterniaServer.cashConfig.getInt("guis.perm.size"); i++) {
            if (!EterniaServer.cashConfig.contains("guis.perm." + i)) {
                permGui.put(i, itemStack);
                continue;
            }

            ItemStack item = new ItemStack(Material.getMaterial(EterniaServer.cashConfig.getString("guis.perm." + i + ".material")));
            ItemMeta meta = item.getItemMeta();
            List<String> lista = EterniaServer.cashConfig.getStringList("guis.perm." + i + ".lore");
            for (int j = 0; j < lista.size(); j++) {
                lista.set(j, ChatColor.translateAlternateColorCodes('&', lista.get(j)));
            }

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', EterniaServer.cashConfig.getString("guis.perm." + i + ".name")));
            meta.setLore(lista);
            item.setItemMeta(meta);
            permGui.put(i, item);
        }

        for (int i = 0; i < EterniaServer.cashConfig.getInt("guis.pacotes.size"); i++) {
            if (!EterniaServer.cashConfig.contains("guis.pacotes." + i)) {
                pacoGui.put(i, itemStack);
                continue;
            }

            ItemStack item = new ItemStack(Material.getMaterial(EterniaServer.cashConfig.getString("guis.pacotes." + i + ".material")));
            ItemMeta meta = item.getItemMeta();
            List<String> lista = EterniaServer.cashConfig.getStringList("guis.pacotes." + i + ".lore");
            for (int j = 0; j < lista.size(); j++) {
                lista.set(j, ChatColor.translateAlternateColorCodes('&', lista.get(j)));
            }

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', EterniaServer.cashConfig.getString("guis.pacotes." + i + ".name")));
            meta.setLore(lista);
            item.setItemMeta(meta);
            pacoGui.put(i, item);
        }

        for (int i = 0; i < EterniaServer.cashConfig.getInt("guis.tags.size"); i++) {
            if (!EterniaServer.cashConfig.contains("guis.tags." + i)) {
                tagsGui.put(i, itemStack);
                continue;
            }

            ItemStack item = new ItemStack(Material.getMaterial(EterniaServer.cashConfig.getString("guis.tags." + i + ".material")));
            ItemMeta meta = item.getItemMeta();
            List<String> lista = EterniaServer.cashConfig.getStringList("guis.tags." + i + ".lore");
            for (int j = 0; j < lista.size(); j++) {
                lista.set(j, ChatColor.translateAlternateColorCodes('&', lista.get(j)));
            }

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', EterniaServer.cashConfig.getString("guis.tags." + i + ".name")));
            meta.setLore(lista);
            item.setItemMeta(meta);
            tagsGui.put(i, item);
        }

        for (int i = 0; i < EterniaServer.cashConfig.getInt("guis.spawners.size"); i++) {
            if (!EterniaServer.cashConfig.contains("guis.spawners." + i)) {
                spawGui.put(i, itemStack);
                continue;
            }

            ItemStack item = new ItemStack(Material.getMaterial(EterniaServer.cashConfig.getString("guis.spawners." + i + ".material")));
            ItemMeta meta = item.getItemMeta();
            List<String> lista = EterniaServer.cashConfig.getStringList("guis.spawners." + i + ".lore");
            for (int j = 0; j < lista.size(); j++) {
                lista.set(j, ChatColor.translateAlternateColorCodes('&', lista.get(j)));
            }

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', EterniaServer.cashConfig.getString("guis.spawners." + i + ".name")));
            meta.setLore(lista);
            item.setItemMeta(meta);
            spawGui.put(i, item);
        }

        cashGui = new CashGui(menuGui, permGui, pacoGui, tagsGui, spawGui);
    }

    @Default
    public void onCash(Player player) {
        Inventory gui = Bukkit.getServer().createInventory(player, cashGui.getMenuGui().size(), "Cash");

        for (int i = 0; i < cashGui.getMenuGui().size(); i++) {
            gui.setItem(i, cashGui.getMenuGui().get(i));
        }

        player.openInventory(gui);
    }

    @Subcommand("balance")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    public void onCashBalance(Player player, @Optional String playerName) {
        if (playerName == null) {
            player.sendMessage(Strings.M_CASH_BALANCE.replace(Constants.AMOUNT, String.valueOf(Vars.cash.get(UUIDFetcher.getUUIDOf(player.getName())))));
            return;
        }

        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);

        if (Vars.cash.containsKey(uuid)) player.sendMessage(Strings.M_CASH_BALANCE_OTHER.replace(Constants.AMOUNT, String.valueOf(Vars.cash.get(uuid))));
        else player.sendMessage(Strings.M_CASH_NO_PLAYER);
    }

    @Subcommand("accept")
    public void onCashAccept(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());

        if (!Vars.cashItem.containsKey(uuid)) {
            player.sendMessage(Strings.M_CASH_NO_BUY);
            return;
        }

        final String cashString = Vars.cashItem.get(uuid);

        for (String line : EterniaServer.cashConfig.getStringList(cashString + ".commands")) {
            final String modifiedCommand = InternMethods.setPlaceholders(player, line);
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), modifiedCommand);
        }

        for (String line : EterniaServer.cashConfig.getStringList(cashString + ".messages")) {
            final String modifiedText = InternMethods.setPlaceholders(player, line);
            player.sendMessage(Strings.getColor(modifiedText));
        }

        APICash.removeCash(uuid, EterniaServer.cashConfig.getInt(cashString + ".cost"));
        player.sendMessage(Strings.M_CASH_SUCESS);
        Vars.cashItem.remove(uuid);
    }

    @Subcommand("deny")
    public void onCashDeny(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());

        if (!Vars.cashItem.containsKey(uuid)) {
            player.sendMessage(Strings.M_CASH_NO_BUY);
            return;
        }

        player.sendMessage(Strings.M_CASH_CANCEL);
        Vars.cashItem.remove(uuid);
    }

    @Subcommand("pay")
    @CommandCompletion("@players 10")
    @Syntax("<jogador> <quantia>")
    public void onCashPay(Player player, OnlinePlayer targetP, Integer value) {
        if (value <= 0) {
            player.sendMessage(Strings.MSG_NO_NEGATIVE);
            return;
        }

        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        final Player target = targetP.getPlayer();

        if (!APICash.hasCash(uuid, value)) {
            player.sendMessage(Strings.MSG_NO_MONEY);
            return;
        }

        APICash.removeCash(uuid, value);
        APICash.addCash(UUIDFetcher.getUUIDOf(target.getName()), value);
        target.sendMessage(Strings.M_CASH_RECEIVED.replace(Constants.AMOUNT, String.valueOf(value)));
        player.sendMessage(InternMethods.putName(target, Strings.M_CASH_SEND).replace(Constants.AMOUNT, String.valueOf(value)));
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
        player.sendMessage(InternMethods.putName(target, Strings.M_CASH_SEND).replace(Constants.AMOUNT, String.valueOf(value)));
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
        player.sendMessage(InternMethods.putName(target, Strings.M_CASH_REMOVE).replace(Constants.AMOUNT, String.valueOf(value)));
    }

}
