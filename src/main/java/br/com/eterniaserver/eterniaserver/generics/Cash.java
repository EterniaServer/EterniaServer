package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.clip.placeholderapi.PlaceholderAPI;
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

@CommandAlias("cash")
@CommandPermission("eternia.cash")
public class Cash extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;

    public Cash(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();

        final String query = "SELECT * FROM " + EterniaServer.serverConfig.getString("sql.table-cash") + ";";
        final HashMap<String, String> temp = EQueries.getMapString(query, "player_name", "balance");

        temp.forEach((k, v) -> Vars.cash.put(k, Integer.parseInt(v)));
        messages.sendConsole("server.load-data", Constants.MODULE.get(), "Cash", Constants.AMOUNT.get(), temp.size());

        loadGui();

    }

    @Default
    public void onCash(Player player) {
        Inventory gui = Bukkit.getServer().createInventory(player, 27, "Cash");
        for (int i = 0; i < 27; i++) {
            gui.setItem(i, Vars.cashGui.get(i));
        }
        player.openInventory(gui);
    }

    @Subcommand("balance")
    @CommandCompletion("balance @players")
    @Syntax("<jogador>")
    public void onCashBalance(Player player, @Optional String playerName) {
        if (playerName != null) {
            if (Vars.cash.containsKey(playerName)) {
                messages.sendMessage("cash.balance-other", Constants.AMOUNT.get(), Vars.cash.get(playerName), player);
            } else {
                messages.sendMessage("cash.no-player", player);
            }
        } else {
            messages.sendMessage("cash.balance", Constants.AMOUNT.get(), Vars.cash.get(player.getName()), player);
        }
    }

    @Subcommand("accept")
    public void onCashAccept(Player player) {
        final String playerName = player.getName();
        if (Vars.cashBuy.containsKey(playerName)) {
            final int slot = Vars.cashBuy.get(playerName);
            for (String line : EterniaServer.cashConfig.getStringList("gui." + slot + ".commands")) {
                final String modifiedCommand = PlaceholderAPI.setPlaceholders(player, line);
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
            }
            for (String line : EterniaServer.cashConfig.getStringList("gui." + slot + ".messages")) {
                final String modifiedText = PlaceholderAPI.setPlaceholders(player, line);
                player.sendMessage(messages.getColor(modifiedText));
            }
            APICash.removeCash(playerName, EterniaServer.cashConfig.getInt("gui." + slot + ".cost"));
            messages.sendMessage("cash.sucess", player);
            Vars.cashBuy.remove(playerName);
        } else {
            messages.sendMessage("cash.no-buy", player);
        }
    }

    @Subcommand("deny")
    public void onCashDeny(Player player) {
        final String playerName = player.getName();
        if (Vars.cashBuy.containsKey(playerName)) {
            messages.sendMessage("cash.canc", player);
            Vars.cashBuy.remove(playerName);
        } else {
            messages.sendMessage("cash.no-buy", player);
        }
    }

    @Subcommand("give")
    @CommandCompletion("@players 10")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.cash.admin")
    public void onCashGive(CommandSender player, OnlinePlayer targetP, Integer value) {
        final Player target = targetP.getPlayer();
        if (value > 0) {
            final String targetName = target.getName();
            APICash.addCash(targetName, value);
            messages.sendMessage("cash.receive", Constants.AMOUNT.get(), value, target);
            messages.sendMessage("cash.send", Constants.AMOUNT.get(), value, Constants.TARGET.get(), target.getDisplayName(), player);
        } else {
            messages.sendMessage("server.neg", player);
        }
    }

    @Subcommand("remove")
    @CommandCompletion("@players 10")
    @Syntax("<jogador> <quantia>")
    @CommandPermission("eternia.cash.admin")
    public void onCashRemove(CommandSender player, OnlinePlayer targetP, Integer value) {
        final Player target = targetP.getPlayer();
        if (value > 0) {
            final String targetName = target.getName();
            APICash.removeCash(targetName, value);
            messages.sendMessage("cash.removed", Constants.AMOUNT.get(), value, target);
            messages.sendMessage("cash.remove", Constants.AMOUNT.get(), value, Constants.TARGET.get(), target.getDisplayName(), player);
        } else {
            messages.sendMessage("server.neg", player);
        }
    }

    private void loadGui() {

        Vars.cashGui.clear();

        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Loja de &aCash&8."));
        itemStack.setItemMeta(itemMeta);

        for (int i = 0; i < 27; i++) {

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
