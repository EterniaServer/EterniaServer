package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.dependencies.papi.PlaceHolders;
import br.com.eterniaserver.eterniaserver.player.PlayerFlyState;
import br.com.eterniaserver.eterniaserver.storages.Files;
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

    private final Messages messages;
    private final Files files;
    private final PlaceHolders placeHolders;
    private final Strings strings;
    private final Vars vars;
    private final PlayerFlyState playerFlyState;

    private final ItemStack coali = new ItemStack(Material.COAL);
    private final ItemStack lapizi = new ItemStack(Material.LAPIS_LAZULI);
    private final ItemStack redstonei = new ItemStack(Material.REDSTONE);
    private final ItemStack ironi = new ItemStack(Material.IRON_INGOT);
    private final ItemStack goldi = new ItemStack(Material.GOLD_INGOT);
    private final ItemStack diamondi = new ItemStack(Material.DIAMOND);
    private final ItemStack esmeraldai = new ItemStack(Material.EMERALD);

    public Others(Messages messages, Files files, PlaceHolders placeHolders, Strings strings, Vars vars, PlayerFlyState playerFlyState) {
        this.messages = messages;
        this.files = files;
        this.placeHolders = placeHolders;
        this.strings = strings;
        this.vars = vars;
        this.playerFlyState = playerFlyState;
    }

    @CommandAlias("reloadeternia|eterniareload")
    @CommandPermission("eternia.reload")
    public void onReload(CommandSender sender) {
        messages.sendMessage("server.reload-s", sender);
        files.loadConfigs();
        files.loadMessages();
        files.loadChat();
        files.loadDatabase();
        PlaceholderAPI.unregisterPlaceholderHook("eterniaserver");
        PlaceholderAPI.registerPlaceholderHook("eterniaserver", placeHolders);
        messages.sendMessage("eternia.reload-f", sender);
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
                        for (java.lang.String arg : nome) {
                            sb.append(arg).append(" ");
                        }
                        meta.setDisplayName(strings.getColor(sb.toString()));
                        item.setItemMeta(meta);
                        player.getInventory().setItemInMainHand(item);
                    } else {
                        messages.sendMessage("other.noitem", player);
                    }
                } else if (func.equals("lore")) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        StringBuilder sb = new StringBuilder();
                        for (java.lang.String arg : nome) {
                            sb.append(arg).append(" ");
                        }
                        meta.setLore(Collections.singletonList(strings.getColor(sb.toString())));
                        item.setItemMeta(meta);
                        player.getInventory().setItemInMainHand(item);
                    } else {
                        messages.sendMessage("other.noitem", player);
                    }
                } else {
                    messages.sendMessage("other.rename", player);
                }
            } else {
            messages.sendMessage("other.noitem", player);
        }
    }

    @CommandAlias("god")
    @Syntax("<jogador>")
    @CommandPermission("eternia.god")
    public void onGod(Player player, @Optional OnlinePlayer target) {
        if (target != null) {
            if (vars.god.contains(target.getPlayer().getName())) {
                messages.sendMessage("simp.godd", target.getPlayer());
                vars.god.remove(target.getPlayer().getName());
            } else {
                messages.sendMessage("simp.gode", target.getPlayer());
                vars.god.add(target.getPlayer().getName());
            }
        } else {
            if (vars.god.contains(player.getName())) {
                messages.sendMessage("simp.godd", player);
                vars.god.remove(player.getName());
            } else {
                messages.sendMessage("simp.gode", player);
                vars.god.add(player.getName());
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
                playerFlyState.changeFlyState(player);
            }
        } else {
            playerFlyState.changeFlyState(target.getPlayer());
        }
    }

    @CommandAlias("feed|saciar")
    @Syntax("<jogador>")
    @CommandPermission("eternia.feed")
    public void onFeed(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            player.setFoodLevel(20);
            messages.sendMessage("other.feed", player);
        } else {
            if (player.hasPermission("eternia.feed.other")) {
                target.getPlayer().setFoodLevel(20);
                messages.sendMessage("other.feed-other", "%target_name%", target.getPlayer().getName(), player);
                messages.sendMessage("other.other-feed", "%target_name%", player.getName(), target.getPlayer());
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
        messages.sendMessage("other.done", player);
    }

    @CommandAlias("afk")
    @CommandPermission("eternia.afk")
    public void onAFK(Player player) {
        if (vars.afk.contains(player.getName())) {
            messages.BroadcastMessage("text.afkd", "%player_name%", player.getName());
            vars.afk.remove(player.getName());
        } else {
            vars.afk.add(player.getName());
            messages.BroadcastMessage("text.afke", "%player_name%", player.getName());
        }
    }



    private int checkItems(ItemStack item1, ItemStack item2) {
        if (item1.isSimilar(item2) && item1.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName())) {
            return item1.getAmount();
        }
        return 0;
    }

    private void convertItems(int items, Material material, Material block, Player player) {
        int amount = items / 9;
        if (amount != 0) {
            player.getInventory().removeItem(new ItemStack(material, amount * 9));
            player.getInventory().addItem(new ItemStack(block, amount));
        }
    }

}
