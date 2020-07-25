package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.Strings;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Syntax;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;

public class Experience extends BaseCommand {

    private final InternMethods internMethods;
    private final EFiles messages;

    public Experience(EterniaServer plugin) {
        this.internMethods = plugin.getInternMethods();
        this.messages = plugin.getEFiles();

        final HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_XP), Strings.PNAME, Strings.XP);
        temp.forEach((k, v) -> Vars.xp.put(k, Integer.parseInt(v)));
        messages.sendConsole(Strings.M_LOAD_DATA, Constants.MODULE, "Experience", Constants.AMOUNT, temp.size());
    }

    @CommandAlias("checklevel|verlevel")
    @CommandPermission("eternia.checklvl")
    public void onCheckLevel(Player player) {
        int lvl = player.getLevel();
        float xp = player.getExp();
        player.setLevel(0);
        player.setExp(0);
        player.giveExp(APIExperience.getExp(player.getName()));
        messages.sendMessage(Strings.M_XP_CHECK, Constants.AMOUNT, player.getLevel(), player);
        player.setLevel(lvl);
        player.setExp(xp);
    }

    @CommandAlias("bottlelvl|bottleexp|gaffinhas")
    @Syntax("<level>")
    @CommandPermission("eternia.bottlexp")
    public void onBottleLevel(Player player, Integer xpWant) {
        int xpReal = internMethods.getXPForLevel(player.getLevel());
        if (xpWant > 0 && xpReal > xpWant) {
            ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&eGarrafa de EXP&8]"));
            item.setItemMeta(meta);
            item.setLore(Collections.singletonList(String.valueOf(xpWant)));
            PlayerInventory inventory = player.getInventory();
            inventory.addItem(item);
            messages.sendMessage(Strings.M_XP_BOTTLE, player);
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(xpReal - xpWant);
        } else {
            messages.sendMessage(Strings.M_XP_INSUFFICIENT, player);
        }
    }

    @CommandAlias("withdrawlvl|pegarlvl|takelvl")
    @Syntax("<level>")
    @CommandPermission("eternia.withdrawlvl")
    public void onWithdrawLevel(Player player, Integer level) {
        final String playerName = player.getName();

        int xpla = internMethods.getXPForLevel(level);
        if (APIExperience.getExp(playerName) >= xpla) {
            APIExperience.removeExp(playerName, xpla);
            player.giveExp(xpla);
            messages.sendMessage(Strings.M_XP_WITHDRAW, Constants.AMOUNT, player.getLevel(), player);
        } else {
            messages.sendMessage(Strings.M_XP_INSUFFICIENT, player);
        }
    }

    @CommandAlias("depositlvl|depositarlvl")
    @Syntax("<level>")
    @CommandPermission("eternia.depositlvl")
    public void onDepositLevel(Player player, Integer xpla) {
        int xpAtual = player.getLevel();
        if (xpAtual >= xpla) {
            int xp = internMethods.getXPForLevel(xpla);
            int xpto = internMethods.getXPForLevel(xpAtual);
            APIExperience.addExp(player.getName(), xp);
            messages.sendMessage(Strings.M_XP_DEPOSIT, Constants.AMOUNT, xpla, player);
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(xpto - xp);
        } else {
            messages.sendMessage(Strings.M_XP_INSUFFICIENT, player);
        }
    }

}
