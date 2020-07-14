package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

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
    private final ExperienceManager exp;

    public Experience(EterniaServer plugin) {
        this.internMethods = plugin.getInternMethods();
        this.messages = plugin.getEFiles();
        this.exp = plugin.getExp();

        final String query = "SELECT * FROM " + EterniaServer.serverConfig.getString("sql.table-xp") + ";";
        final HashMap<String, String> temp = EQueries.getMapString(query, "player_name", "xp");

        temp.forEach((k, v) -> Vars.xp.put(k, Integer.parseInt(v)));
        messages.sendConsole("server.load-data", Constants.MODULE.get(), "Experience", Constants.AMOUNT.get(), temp.size());
    }

    @CommandAlias("checklevel|verlevel")
    @CommandPermission("eternia.checklvl")
    public void onCheckLevel(Player player) {
        int lvl = player.getLevel();
        float xp = player.getExp();
        player.setLevel(0);
        player.setExp(0);
        player.giveExp(exp.getExp(player.getName()));
        messages.sendMessage("experience.check", Constants.AMOUNT.get(), player.getLevel(), player);
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
            if (meta != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&eGarrafa de EXP&8]"));
                item.setItemMeta(meta);
                item.setLore(Collections.singletonList(String.valueOf(xpWant)));
            }
            PlayerInventory inventory = player.getInventory();
            inventory.addItem(item);
            messages.sendMessage("experience.bottleexp", player);
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(xpReal - xpWant);
        } else {
            messages.sendMessage("experience.insufficient", player);
        }
    }

    @CommandAlias("withdrawlvl|pegarlvl|takelvl")
    @Syntax("<level>")
    @CommandPermission("eternia.withdrawlvl")
    public void onWithdrawLevel(Player player, Integer level) {
        final String playerName = player.getName();

        int xpla = internMethods.getXPForLevel(level);
        if (exp.getExp(playerName) >= xpla) {
            exp.removeExp(playerName, xpla);
            player.giveExp(xpla);
            messages.sendMessage("experience.withdraw", Constants.AMOUNT.get(), player.getLevel(), player);
        } else {
            messages.sendMessage("experience.insufficient", player);
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
            exp.addExp(player.getName(), xp);
            messages.sendMessage("experience.deposit", Constants.AMOUNT.get(), xpla, player);
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(xpto - xp);
        } else {
            messages.sendMessage("experience.insufficient", player);
        }
    }

}
