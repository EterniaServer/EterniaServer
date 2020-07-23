package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.Strings;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class Others extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;
    private final PlaceHolders placeHolders;

    private final ItemStack coali = new ItemStack(Material.COAL);
    private final ItemStack lapizi = new ItemStack(Material.LAPIS_LAZULI);
    private final ItemStack redstonei = new ItemStack(Material.REDSTONE);
    private final ItemStack ironi = new ItemStack(Material.IRON_INGOT);
    private final ItemStack goldi = new ItemStack(Material.GOLD_INGOT);
    private final ItemStack diamondi = new ItemStack(Material.DIAMOND);
    private final ItemStack esmeraldai = new ItemStack(Material.EMERALD);

    public Others(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
        this.placeHolders = plugin.getPlaceHolders();
    }

    @CommandAlias("reloadeternia|eterniareload")
    @CommandPermission("eternia.reload")
    public void onReload(CommandSender sender) {
        messages.sendMessage(Strings.M_RELOAD_S, sender);
        plugin.getFiles().loadConfigs();
        plugin.getFiles().loadMessages();
        plugin.getFiles().loadBlocksRewards();
        plugin.getFiles().loadCommands();
        plugin.getFiles().loadChat();
        plugin.getFiles().loadKits();
        plugin.getFiles().loadRewards();
        plugin.getFiles().loadDatabase();
        PlaceholderAPI.unregisterPlaceholderHook("eterniaserver");
        PlaceholderAPI.registerPlaceholderHook("eterniaserver", placeHolders);
        messages.sendMessage(Strings.M_RELOAD_F, sender);
    }

    @CommandAlias("itemrename|renameitem")
    @Syntax("<name/lore> <nome>")
    @CommandPermission("eternia.renameitem")
    public void onItemRename(Player player, String func, String[] nome) {
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            if (func.equals("name")) {
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(transformToString(nome));
                item.setItemMeta(meta);
                player.getInventory().setItemInMainHand(item);
            } else if (func.equals("lore")) {
                ItemStack item = player.getInventory().getItemInMainHand();
                item.setLore(Collections.singletonList(transformToString(nome)));
                player.getInventory().setItemInMainHand(item);
            } else {
                messages.sendMessage(Strings.M_ITEM_RENAME, player);
            }
        } else {
            messages.sendMessage(Strings.M_ITEM_NO, player);
        }
    }

    @CommandAlias("god")
    @Syntax("<jogador>")
    @CommandPermission("eternia.god")
    public void onGod(Player player, @Optional OnlinePlayer target) {
        if (target != null) {
            changeGod(target.getPlayer());
        } else {
            changeGod(player);
        }
    }

    @CommandAlias("fly|voar")
    @CommandPermission("eternia.fly")
    public void onFly(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            if (player.getWorld() == Bukkit.getWorld("evento") && !player.hasPermission("eternia.fly.evento")) {
                messages.sendMessage(Strings.M_NO_PERM, player);
            } else {
                changeFlyState(player);
            }
        } else {
            changeFlyState(target.getPlayer());
        }
    }

    @CommandAlias("feed|saciar")
    @Syntax("<jogador>")
    @CommandPermission("eternia.feed")
    public void onFeed(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            player.setFoodLevel(20);
            messages.sendMessage("generic.others.feeded", player);
        } else {
            final Player targetP = target.getPlayer();
            if (player.hasPermission("eternia.feed.other")) {
                targetP.setFoodLevel(20);
                messages.sendMessage("generic.others.feeded-target", Constants.TARGET, targetP.getDisplayName(), player);
                messages.sendMessage("generic.others.feeded", Constants.TARGET, player.getDisplayName(), targetP);
            } else {
                messages.sendMessage("server.no-perm", player);
            }
        }
    }

    @CommandAlias("blocks|condenser")
    @CommandPermission("eternia.blocks")
    public void onBlocks(Player player) {
        int coal = 0;
        int lapiz = 0;
        int redstone = 0;
        int iron = 0;
        int gold = 0;
        int diamond = 0;
        int esmeralda = 0;
        for (ItemStack i : player.getInventory().getContents()) {
            if (i != null) {
                coal += checkItems(i, coali);
                lapiz += checkItems(i, lapizi);
                redstone += checkItems(i, redstonei);
                iron += checkItems(i, ironi);
                gold += checkItems(i, goldi);
                diamond += checkItems(i, diamondi);
                esmeralda += checkItems(i, esmeraldai);
            }
        }
        convertItems(coal, Material.COAL, Material.COAL_BLOCK, player);
        convertItems(lapiz, Material.LAPIS_LAZULI, Material.LAPIS_BLOCK, player);
        convertItems(redstone, Material.REDSTONE, Material.REDSTONE_BLOCK, player);
        convertItems(iron, Material.IRON_INGOT, Material.IRON_BLOCK, player);
        convertItems(gold, Material.GOLD_INGOT, Material.GOLD_BLOCK, player);
        convertItems(diamond, Material.DIAMOND, Material.DIAMOND_BLOCK, player);
        convertItems(esmeralda, Material.EMERALD, Material.EMERALD_BLOCK, player);
        messages.sendMessage(Strings.M_CONDENSER, player);
    }

    @CommandAlias("afk")
    @CommandPermission("eternia.afk")
    public void onAFK(Player player) {
        final String playerName = player.getName();
        if (Vars.afk.contains(playerName)) {
            messages.broadcastMessage(Strings.M_AFK_DISABLE, Constants.PLAYER, playerName);
            Vars.afk.remove(playerName);
        } else {
            Vars.afk.add(playerName);
            messages.broadcastMessage(Strings.M_AFK_ENABLE, Constants.PLAYER, playerName);
        }
    }

    private String transformToString(final String[] nome) {
        StringBuilder sb = new StringBuilder();
        for (String arg : nome) sb.append(arg).append(" ");
        return messages.getColor(sb.toString());
    }

    private void changeGod(final Player player) {
        final String playerName = player.getName();
        if (Vars.god.contains(playerName)) {
            messages.sendMessage(Strings.M_GOD_DISABLE, player);
            Vars.god.remove(playerName);
        } else {
            messages.sendMessage(Strings.M_GOD_ENABLE, player);
            Vars.god.add(playerName);
        }
    }

    private int checkItems(ItemStack item1, ItemStack item2) {
        if (item1.isSimilar(item2) && item1.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName())) return item1.getAmount();
        else return 0;
    }

    private void convertItems(int items, Material material, Material block, Player player) {
        int amount = items / 9;
        if (amount != 0) {
            player.getInventory().removeItem(new ItemStack(material, amount * 9));
            player.getInventory().addItem(new ItemStack(block, amount));
        }
    }

    public void changeFlyState(Player player) {
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            messages.sendMessage(Strings.M_FLY_DISABLE, player);
        } else {
            player.setAllowFlight(true);
            messages.sendMessage(Strings.M_FLY_ENABLE, player);
        }
    }

}
