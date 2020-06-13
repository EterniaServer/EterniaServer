package br.com.eterniaserver.eterniaserver.modules.homesmanager.commands;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.homesmanager.HomesManager;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import io.papermc.lib.PaperLib;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class HomeSystem extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;
    private final HomesManager homesManager;

    public HomeSystem(EterniaServer plugin, HomesManager homesManager) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
        this.homesManager = homesManager;
    }

    @CommandAlias("delhome|delhouse|delcasa")
    @Syntax("<home>")
    @CommandPermission("eternia.delhome")
    public void onDelHome(Player player, String nome) {
        final String playerName = player.getName();

        if (homesManager.existHome(nome.toLowerCase(), playerName)) {
            homesManager.delHome(nome.toLowerCase(), playerName);
            messages.sendMessage("home.deleted", player);
        } else {
            messages.sendMessage("home.no-exists", player);
        }
    }

    @CommandAlias("home|house|casa|h")
    @Syntax("<home> <jogador>")
    @CommandPermission("eternia.home")
    public void onHome(Player player, String nome, @Optional OnlinePlayer target) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (target == null) {
                Location location = homesManager.getHome(nome.toLowerCase(), player.getName());
                if (location != EterniaServer.error) {
                    if (EterniaServer.teleports.containsKey(player)) {
                        messages.sendMessage("server.telep", player);
                    } else {
                        EterniaServer.teleports.put(player, new PlayerTeleport(player, location, "home.done", plugin));
                    }
                } else {
                    messages.sendMessage("home.no-exists", player);
                }
            } else {
                if (player.hasPermission("eternia.home.other")) {
                    Location location = homesManager.getHome(nome.toLowerCase(), target.getPlayer().getName());
                    if (location != EterniaServer.error) {
                        PaperLib.teleportAsync(player, location);
                        messages.sendMessage("home.done", player);
                    } else {
                        messages.sendMessage("home.no-exists", player);
                    }
                } else {
                    messages.sendMessage("server.no-perm", player);
                }
            }
        });
    }

    @CommandAlias("homes|houses|casas")
    @Syntax("<jogador>")
    @CommandPermission("eternia.homes")
    public void onHomes(Player player, @Optional OnlinePlayer target) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            StringBuilder accounts = new StringBuilder();
            String[] values;
            if (target != null) {
                if (player.hasPermission("eternia.homes.other")) {
                    values = homesManager.getHomes(target.getPlayer().getName());
                    for (String line : values) {
                        accounts.append(line).append("&8, &3");
                    }
                    messages.sendMessage("home.list", "%homes%", messages.getColor(accounts.toString()), player);
                } else {
                    messages.sendMessage("server.no-perm", player);
                }
            } else {
                values = homesManager.getHomes(player.getName());
                for (String line : values) {
                    accounts.append(line).append("&8, &3");
                }
                messages.sendMessage("home.list", "%homes%", messages.getColor(accounts.toString()), player);
            }
        });
    }

    @CommandAlias("sethome|sethouse|setcasa")
    @Syntax("<nome>")
    @CommandPermission("eternia.sethome")
    public void onSetHome(Player player, String nome) {
        int i = 3;
        for (int x = 4; x < 50; x++) if (player.hasPermission("eternia.sethome." + x)) i = x;

        if (nome.length() <= 8) {
            if (homesManager.canHome(player.getName()) < i) {
                homesManager.setHome(player.getLocation(), nome.toLowerCase(), player.getName());
                messages.sendMessage("home.created", player);
            } else {
                if (homesManager.existHome(nome.toLowerCase(), player.getName())) {
                    homesManager.setHome(player.getLocation(), nome.toLowerCase(), player.getName());
                    messages.sendMessage("home.created", player);
                } else {
                    ItemStack item = new ItemStack(Material.COMPASS);
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        final Location loc = player.getLocation();
                        final String saveloc = loc.getWorld().getName() +
                                ":" + ((int) loc.getX()) +
                                ":" + ((int) loc.getY()) +
                                ":" + ((int) loc.getZ()) +
                                ":" + ((int) loc.getYaw()) +
                                ":" + ((int) loc.getPitch());
                        meta.setLore(Collections.singletonList(saveloc));
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&e" + nome.toLowerCase() + "&8]"));
                        item.setItemMeta(meta);
                    }
                    PlayerInventory inventory = player.getInventory();
                    inventory.addItem(item);
                    messages.sendMessage("home.limit", player);
                }
            }
        } else {
            messages.sendMessage("home.exceeded", player);
        }
    }

}
