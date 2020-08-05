package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.eterniaserver.objects.UUIDFetcher;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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

    private final EterniaServer plugin;
    private final EFiles messages;
    private final int size = EterniaServer.cashConfig.getInt("size");

    public Cash(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();

        final HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_CASH), Strings.UUID, Strings.BALANCE);
        temp.forEach((k, v) -> Vars.cash.put(UUID.fromString(k), Integer.parseInt(v)));
        messages.sendConsole(Strings.MSG_LOAD_DATA, Constants.MODULE, "Cash", Constants.AMOUNT, temp.size());

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
                messages.sendMessage(Strings.M_CASH_BALANCE_OTHER, Constants.AMOUNT, Vars.cash.get(uuid), player);
            } else {
                messages.sendMessage(Strings.M_CASH_NO_PLAYER, player);
            }
        } else {
            messages.sendMessage(Strings.M_CASH_BALANCE, Constants.AMOUNT, Vars.cash.get(UUIDFetcher.getUUIDOf(player.getName())), player);
        }
    }

    @Subcommand("accept")
    public void onCashAccept(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        if (Vars.cashBuy.containsKey(uuid)) {
            final int slot = Vars.cashBuy.get(uuid);
            for (String line : EterniaServer.cashConfig.getStringList("gui." + slot + ".commands")) {
                final String modifiedCommand = PlaceholderAPI.setPlaceholders((OfflinePlayer) player, line);
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
            }
            for (String line : EterniaServer.cashConfig.getStringList("gui." + slot + ".messages")) {
                final String modifiedText = PlaceholderAPI.setPlaceholders((OfflinePlayer) player, line);
                player.sendMessage(messages.getColor(modifiedText));
            }
            APICash.removeCash(uuid, EterniaServer.cashConfig.getInt("gui." + slot + ".cost"));
            messages.sendMessage(Strings.M_CASH_SUCESS, player);
            Vars.cashBuy.remove(uuid);
        } else {
            messages.sendMessage(Strings.M_CASH_NO_BUY, player);
        }
    }

    @Subcommand("deny")
    public void onCashDeny(Player player) {
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        if (Vars.cashBuy.containsKey(uuid)) {
            messages.sendMessage(Strings.M_CASH_CANCEL, player);
            Vars.cashBuy.remove(uuid);
        } else {
            messages.sendMessage(Strings.M_CASH_NO_BUY, player);
        }
    }

    @Subcommand("give")
    @CommandCompletion("@players 10")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.cash.admin")
    public void onCashGive(CommandSender player, OnlinePlayer targetP, Integer value) {
        if (value <= 0) {
            messages.sendMessage(Strings.MSG_NO_NEGATIVE, player);
            return;
        }

        final Player target = targetP.getPlayer();
        APICash.addCash(UUIDFetcher.getUUIDOf(target.getName()), value);
        messages.sendMessage(Strings.M_CASH_RECEIVED, Constants.AMOUNT, value, target);
        messages.sendMessage(Strings.M_CASH_SEND, Constants.AMOUNT, value, Constants.TARGET, target.getDisplayName(), player);
    }

    @Subcommand("remove")
    @CommandCompletion("@players 10")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.cash.admin")
    public void onCashRemove(CommandSender player, OnlinePlayer targetP, Integer value) {
        if (value <= 0) {
            messages.sendMessage(Strings.MSG_NO_NEGATIVE, player);
            return;
        }

        final Player target = targetP.getPlayer();
        APICash.removeCash(UUIDFetcher.getUUIDOf(target.getName()), value);
        messages.sendMessage(Strings.M_CASH_REMOVED, Constants.AMOUNT, value, target);
        messages.sendMessage(Strings.M_CASH_REMOVE, Constants.AMOUNT, value, Constants.TARGET, target.getDisplayName(), player);
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
