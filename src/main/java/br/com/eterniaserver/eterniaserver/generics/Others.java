package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

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
        messages.sendMessage("generic.others.reload-start", sender);
        plugin.files.loadConfigs();
        plugin.files.loadMessages();
        plugin.files.loadChat();
        plugin.files.loadDatabase();
        PlaceholderAPI.unregisterPlaceholderHook("eterniaserver");
        PlaceholderAPI.registerPlaceholderHook("eterniaserver", placeHolders);
        messages.sendMessage("generic.others.reload-finish", sender);
    }

    @CommandAlias("itemrename|renameitem")
    @Syntax("<name/lore> <nome>")
    @CommandPermission("eternia.renameitem")
    public void onItemRename(Player player, String func, String[] nome) {
        player.getInventory().getItemInMainHand();
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                if (func.equals("name")) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        StringBuilder sb = new StringBuilder();
                        for (java.lang.String arg : nome) sb.append(arg).append(" ");
                        meta.setDisplayName(messages.getColor(sb.toString()));
                        item.setItemMeta(meta);
                        player.getInventory().setItemInMainHand(item);
                    } else {
                        messages.sendMessage("generic.items.no-item", player);
                    }
                } else if (func.equals("lore")) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (!item.isSimilar(new ItemStack(Material.AIR))) {
                        StringBuilder sb = new StringBuilder();
                        for (java.lang.String arg : nome) sb.append(arg).append(" ");
                        item.setLore(Collections.singletonList(messages.getColor(sb.toString())));
                        player.getInventory().setItemInMainHand(item);
                    } else {
                        messages.sendMessage("generic.items.no-item", player);
                    }
                } else {
                    messages.sendMessage("generic.items.rename", player);
                }
            } else {
            messages.sendMessage("generic.items.no-item", player);
        }
    }

    @CommandAlias("god")
    @Syntax("<jogador>")
    @CommandPermission("eternia.god")
    public void onGod(Player player, @Optional OnlinePlayer target) {
        final String playerName = player.getName();

        if (target != null) {
            final Player targetP = target.getPlayer();
            final String targetName = targetP.getName();

            if (Vars.god.contains(targetName)) {
                messages.sendMessage("generic.others.god-disabled", targetP);
                Vars.god.remove(targetName);
            } else {
                messages.sendMessage("generic.others.god-enabled", targetP);
                Vars.god.add(targetName);
            }
        } else {
            if (Vars.god.contains(playerName)) {
                messages.sendMessage("generic.others.god-disabled", player);
                Vars.god.remove(playerName);
            } else {
                messages.sendMessage("generic.others.god-enabled", player);
                Vars.god.add(playerName);
            }
        }
    }

    @CommandAlias("fly|voar")
    @CommandPermission("eternia.fly")
    public void onFly(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            if (player.getWorld() == Bukkit.getWorld("evento") && !player.hasPermission("eternia.fly.evento")) {
                messages.sendMessage("server.no-perm", player);
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
                messages.sendMessage("generic.others.feeded-target", "%target_name%", targetP.getName(), player);
                messages.sendMessage("generic.others.feeded", "%target_name%", player.getName(), targetP);
            } else {
                messages.sendMessage("server.no-perm", player);
            }
        }
    }

    @CommandAlias("blocks|condenser")
    @CommandPermission("eternia.blocks")
    public void onBlocks(Player player) {
        int coal = 0, lapiz = 0, redstone = 0, iron = 0, gold = 0, diamond = 0, esmeralda = 0;
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
        messages.sendMessage("generic.others.condenser", player);
    }

    @CommandAlias("afk")
    @CommandPermission("eternia.afk")
    public void onAFK(Player player) {
        final String playerName = player.getName();

        if (Vars.afk.contains(playerName)) {
            messages.broadcastMessage("generic.afk.disabled", "%player_name%", playerName);
            Vars.afk.remove(playerName);
        } else {
            Vars.afk.add(playerName);
            messages.broadcastMessage("generic.afk.enabled", "%player_name%", playerName);
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
            messages.sendMessage("generic.others.fly-disabled", player);
        } else {
            player.setAllowFlight(true);
            messages.sendMessage("generic.others.fly-enabled", player);
        }
    }

}
