package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;

import br.com.eterniaserver.eterniaserver.Constants;
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

    private final EFiles message;
    private final Scoreboard sc;

    public Glow(EFiles message) {
        this.message = message;
        this.sc = Bukkit.getScoreboardManager().getMainScoreboard();
        String[] arrData = new String[] { "tblack", "tdarkblue", "tdarkgreen", "tdarkaqua", "tdarkred", "tdarkpurple", "tgold", "tlightgray", "tdarkgray", "tblue", "tgreen", "taqua", "tred", "tpurple", "tyellow", "twhite" };

        for (String arrDatum : arrData) {
            if (sc.getTeam(arrDatum) == null) {
                sc.registerNewTeam(arrDatum);
            }
        }
        colourSetup();

    }

    @Default
    public void onGlow(Player player) {
        if (!player.isGlowing()) {
            player.setGlowing(true);
            message.sendMessage("glow.brilho", player);
        } else {
            player.removePotionEffect(PotionEffectType.GLOWING);
            player.setGlowing(false);
            message.sendMessage("glow.desbrilho", player);
        }
    }

    @Subcommand("color")
    @CommandCompletion("@colors")
    public void onGlowColor(Player player, String color) {
        final String playerName = player.getName();
        switch (color) {
            case "black":
                Vars.glowingColor.put(playerName, ChatColor.BLACK + "");
                sc.getTeam("tblack").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "preto", player);
                break;
            case "darkblue":
                Vars.glowingColor.put(playerName, ChatColor.DARK_BLUE + "");
                sc.getTeam("tdarkblue").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "azul escuro", player);
                break;
            case "darkgreen":
                Vars.glowingColor.put(playerName, ChatColor.DARK_GREEN + "");
                sc.getTeam("tdarkgreen").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "verde escuro", player);
                break;
            case "darkaqua":
                Vars.glowingColor.put(playerName, ChatColor.DARK_AQUA + "");
                sc.getTeam("tdarkaqua").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "ciano", player);
                break;
            case "darkred":
                Vars.glowingColor.put(playerName, ChatColor.DARK_RED + "");
                sc.getTeam("tdarkred").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "vermelho", player);
                break;
            case "darkpurple":
                Vars.glowingColor.put(playerName, ChatColor.DARK_PURPLE + "");
                sc.getTeam("tdarkpurple").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "roxo", player);
                break;
            case "gold":
                Vars.glowingColor.put(playerName, ChatColor.GOLD + "");
                sc.getTeam("tgold").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "dourado", player);
                break;
            case "lightgray":
                Vars.glowingColor.put(playerName, ChatColor.GRAY + "");
                sc.getTeam("tlightgray").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "cinza claro", player);
                break;
            case "darkgray":
                Vars.glowingColor.put(playerName, ChatColor.DARK_GRAY + "");
                sc.getTeam("tdarkgray").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "cinza escuro", player);
                break;
            case "blue":
                Vars.glowingColor.put(playerName, ChatColor.BLUE + "");
                sc.getTeam("tblue").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "azul", player);
                break;
            case "green":
                Vars.glowingColor.put(playerName, ChatColor.GREEN + "");
                sc.getTeam("tgreen").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "verde", player);
                break;
            case "aqua":
                Vars.glowingColor.put(playerName, ChatColor.AQUA + "");
                sc.getTeam("taqua").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "azul claro", player);
                break;
            case "red":
                Vars.glowingColor.put(playerName, ChatColor.RED + "");
                sc.getTeam("tred").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "tomate", player);
                break;
            case "purple":
                Vars.glowingColor.put(playerName, ChatColor.LIGHT_PURPLE + "");
                sc.getTeam("tpurple").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "rosa", player);
                break;
            case "yellow":
                Vars.glowingColor.put(playerName, ChatColor.YELLOW + "");
                sc.getTeam("tyellow").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "amarelo", player);
                break;
            default:
                Vars.glowingColor.put(playerName, ChatColor.WHITE + "");
                sc.getTeam("twhite").addEntry(playerName);
                message.sendMessage("glow.color", Constants.AMOUNT, "branco", player);
                break;
        }
    }

    private void colourSetup() {
        sc.getTeam("tblack").setColor(ChatColor.BLACK);
        sc.getTeam("tdarkblue").setColor(ChatColor.DARK_BLUE);
        sc.getTeam("tdarkgreen").setColor(ChatColor.DARK_GREEN);
        sc.getTeam("tdarkaqua").setColor(ChatColor.DARK_AQUA);
        sc.getTeam("tdarkred").setColor(ChatColor.DARK_RED);
        sc.getTeam("tdarkpurple").setColor(ChatColor.DARK_PURPLE);
        sc.getTeam("tgold").setColor(ChatColor.GOLD);
        sc.getTeam("tlightgray").setColor(ChatColor.GRAY);
        sc.getTeam("tdarkgray").setColor(ChatColor.DARK_GRAY);
        sc.getTeam("tblue").setColor(ChatColor.BLUE);
        sc.getTeam("tgreen").setColor(ChatColor.GREEN);
        sc.getTeam("taqua").setColor(ChatColor.AQUA);
        sc.getTeam("tred").setColor(ChatColor.RED);
        sc.getTeam("tpurple").setColor(ChatColor.LIGHT_PURPLE);
        sc.getTeam("tyellow").setColor(ChatColor.YELLOW);
        sc.getTeam("twhite").setColor(ChatColor.WHITE);
    }

}
