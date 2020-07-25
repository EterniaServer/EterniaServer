package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.Strings;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

@CommandAlias("glow")
@CommandPermission("eternia.glow")
public class Glow extends BaseCommand {

    private final String[] arrData = new String[] {
            "tblack", "tdarkblue", "tdarkgreen", "tdarkaqua", "tdarkred", "tdarkpurple", "tgold", "tlightgray",
            "tdarkgray", "tblue", "tgreen", "taqua", "tred", "tpurple", "tyellow", "twhite"
    };
    private final EFiles message;
    private final Scoreboard sc;

    public Glow(EFiles message) {
        this.message = message;
        this.sc = Bukkit.getScoreboardManager().getMainScoreboard();
        final ChatColor[] colors = new ChatColor[] {
                ChatColor.BLACK, ChatColor.DARK_BLUE, ChatColor.DARK_GREEN, ChatColor.DARK_AQUA, ChatColor.DARK_RED,
                ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GRAY, ChatColor.DARK_GRAY, ChatColor.BLUE, ChatColor.GREEN,
                ChatColor.AQUA, ChatColor.RED, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW, ChatColor.WHITE
        };
        for (int i = 0; i < arrData.length; i++) if (sc.getTeam(arrData[i]) == null) sc.registerNewTeam(arrData[i]).setColor(colors[i]);
    }

    @Default
    public void onGlow(Player player) {
        if (!player.isGlowing()) {
            player.setGlowing(true);
            message.sendMessage(Strings.M_GLOW_ENABLED, player);
        } else {
            player.removePotionEffect(PotionEffectType.GLOWING);
            player.setGlowing(false);
            message.sendMessage(Strings.M_GLOW_DISABLED, player);
        }
    }

    @Subcommand("color")
    @CommandCompletion("@colors")
    public void onGlowColor(Player player, String color) {
        switch (color) {
            case "black":
                changeColor(player, arrData[0], "&0", "preto");
                break;
            case "darkblue":
                changeColor(player, arrData[1], "&1", "azul escuro");
                break;
            case "darkgreen":
                changeColor(player, arrData[2], "&2", "verde escuro");
                break;
            case "darkaqua":
                changeColor(player, arrData[3], "&3", "ciano");
                break;
            case "darkred":
                changeColor(player, arrData[4], "&4", "vermelho");
                break;
            case "darkpurple":
                changeColor(player, arrData[5], "&5", "roxo");
                break;
            case "gold":
                changeColor(player, arrData[6], "&6", "dourado");
                break;
            case "lightgray":
                changeColor(player, arrData[7], "&7", "cinza claro");
                break;
            case "darkgray":
                changeColor(player, arrData[8], "&8", "cinza escuro");
                break;
            case "blue":
                changeColor(player, arrData[9], "&9", "azul");
                break;
            case "green":
                changeColor(player, arrData[10], "&a", "verde");
                break;
            case "aqua":
                changeColor(player, arrData[11], "&b", "azul claro");
                break;
            case "red":
                changeColor(player, arrData[12], "&c", "tomate");
                break;
            case "purple":
                changeColor(player, arrData[13], "&d", "rosa");
                break;
            case "yellow":
                changeColor(player, arrData[14], "&e", "amarelo");
                break;
            default:
                changeColor(player, arrData[15], "&f", "branco");
                break;
        }
    }

    public void changeColor(final Player player, final String team, final String nameColor, final String color) {
        final String playerName = player.getName();
        Vars.glowingColor.put(playerName, nameColor);
        sc.getTeam(team).addEntry(playerName);
        message.sendMessage(Strings.M_GLOW_COLOR, Constants.AMOUNT, color, player);
    }

}
