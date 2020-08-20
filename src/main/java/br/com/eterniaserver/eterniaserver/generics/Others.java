package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.UUID;

public class Others extends BaseCommand {

    private final EterniaServer plugin;

    private final ItemStack coali = new ItemStack(Material.COAL);
    private final ItemStack lapizi = new ItemStack(Material.LAPIS_LAZULI);
    private final ItemStack redstonei = new ItemStack(Material.REDSTONE);
    private final ItemStack ironi = new ItemStack(Material.IRON_INGOT);
    private final ItemStack goldi = new ItemStack(Material.GOLD_INGOT);
    private final ItemStack diamondi = new ItemStack(Material.DIAMOND);
    private final ItemStack esmeraldai = new ItemStack(Material.EMERALD);

    public Others(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("reloadeternia|eterniareload")
    @CommandPermission("eternia.reload")
    public void onReload(CommandSender sender) {
        sender.sendMessage(Strings.MSG_RELOAD_START);
        plugin.getFiles().loadConfigs();
        plugin.getFiles().loadMessages();
        plugin.getFiles().loadBlocksRewards();
        plugin.getFiles().loadCommands();
        plugin.getFiles().loadChat();
        plugin.getFiles().loadKits();
        plugin.getFiles().loadRewards();
        plugin.getFiles().loadDatabase();
        sender.sendMessage(Strings.MSG_RELOAD_FINISH);
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
                player.sendMessage(Strings.MSG_ITEM_RENAME);
            }
        } else {
            player.sendMessage(Strings.MSG_ITEM_NO);
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

    @CommandAlias("fly")
    @CommandPermission("eternia.fly")
    public void onFly(Player player, @Optional OnlinePlayer targetS) {
        if (targetS != null) {
            final String worldName = player.getWorld().getName();
            if ((worldName.equals("evento") || worldName.equals("world_evento")) && !player.hasPermission("eternia.fly.evento")) {
                player.sendMessage(Strings.MSG_NO_PERM);
                return;
            }

            final Player target = targetS.getPlayer();
            final UUID uuid = UUIDFetcher.getUUIDOf(target.getName());

            if (APIFly.isOnPvP(uuid)) {
                player.sendMessage(InternMethods.putName(target, Strings.FLY_TARGET_IN_PVP.replace(Constants.AMOUNT, String.valueOf(EterniaServer.serverConfig.getInt("server.pvp-time") - APIFly.getPvPCooldown(uuid)))));
                return;
            }

            APIFly.changeFlyState(target);
            if (target.isFlying()) {
                target.sendMessage(InternMethods.putName(player, Strings.FLY_ENABLED_BY));
                player.sendMessage(InternMethods.putName(target, Strings.FLY_ENABLED_FOR));
                return;
            }
            target.sendMessage(InternMethods.putName(player, Strings.FLY_DISABLED_BY));
            player.sendMessage(InternMethods.putName(target, Strings.FLY_DISABLED_FOR));
            return;
        }
        final UUID uuid = UUIDFetcher.getUUIDOf(player.getName());

        if (APIFly.isOnPvP(uuid)) {
            player.sendMessage(Strings.FLY_IN_PVP.replace(Constants.AMOUNT, String.valueOf(EterniaServer.serverConfig.getInt("server.pvp-time") - APIFly.getPvPCooldown(uuid))));
            return;
        }

        APIFly.changeFlyState(player);
        if (player.isFlying()) {
            player.sendMessage(Strings.FLY_ENABLED);
            return;
        }
        player.sendMessage(Strings.FLY_DISABLED);
    }

    @CommandAlias("feed|saciar")
    @Syntax("<jogador>")
    @CommandPermission("eternia.feed")
    public void onFeed(Player player, @Optional OnlinePlayer target) {
        if (target == null) {
            player.setFoodLevel(20);
            player.sendMessage(Strings.MSG_FEEDED);
        } else {
            final Player targetP = target.getPlayer();
            if (player.hasPermission("eternia.feed.other")) {
                targetP.setFoodLevel(20);
                player.sendMessage(InternMethods.putName(targetP, Strings.MSG_FEEDED_TARGET));
                player.sendMessage(Strings.MSG_FEEDED_TARGET.replace(Constants.TARGET, targetP.getDisplayName()));
                targetP.sendMessage(Strings.MSG_FEEDED);
            } else {
                player.sendMessage(Strings.MSG_NO_PERM);
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
            if (i != null && i.getType() != Material.AIR) {
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
        player.sendMessage(Strings.MSG_CONDENSER);
    }

    @CommandAlias("afk")
    @CommandPermission("eternia.afk")
    public void onAFK(Player player) {
        final String playerName = player.getName();
        if (Vars.afk.contains(playerName)) {
            Bukkit.broadcastMessage(InternMethods.putName(player, Strings.MSG_AFK_DISABLE));
            Vars.afk.remove(playerName);
        } else {
            Vars.afk.add(playerName);
            Bukkit.broadcastMessage(InternMethods.putName(player, Strings.MSG_AFK_ENABLE));
        }
    }

    private String transformToString(final String[] nome) {
        StringBuilder sb = new StringBuilder();
        for (String arg : nome) sb.append(arg).append(" ");
        return Strings.getColor(sb.toString());
    }

    private void changeGod(final Player player) {
        final String playerName = player.getName();
        if (Vars.god.contains(playerName)) {
            player.sendMessage(Strings.MSG_GOD_DISABLE);
            Vars.god.remove(playerName);
        } else {
            player.sendMessage(Strings.MSG_GOD_ENABLE);
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

}
