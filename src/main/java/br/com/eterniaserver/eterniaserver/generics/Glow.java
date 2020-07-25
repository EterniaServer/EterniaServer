package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@CommandAlias("glow")
@CommandPermission("eternia.glow")
public class Glow extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles message;
    private final Scoreboard sc;

    private Team tBlack;
    private Team tDarkBlue;
    private Team tDarkGreen;
    private Team tDarkAqua;
    private Team tDarkRed;
    private Team tDarkPurple;
    private Team tGold;
    private Team tGray;
    private Team tDarkGray;
    private Team tBlue;
    private Team tGreen;
    private Team tAqua;
    private Team tRed;
    private Team tLightPurple;
    private Team tYellow;
    private Team tWhite;

    public Glow(EterniaServer plugin) {
        this.plugin = plugin;
        this.message = plugin.getEFiles();
        this.sc = Bukkit.getScoreboardManager().getMainScoreboard();
        setTeams();
        setColors();
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
        final String playerName = player.getName();
        switch (color.hashCode()) {
            case 1741606617:
                Vars.glowingColor.put(playerName, "&8");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(8), Constants.AMOUNT, "cinza escuro", player);
                break;
            case 1741452496:
                Vars.glowingColor.put(playerName, "&1");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(1), Constants.AMOUNT, "azul escuro", player);
                break;
            case 1741427506:
                Vars.glowingColor.put(playerName, "&3");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(3), Constants.AMOUNT, "ciano", player);
                break;
            case 1441664347:
                Vars.glowingColor.put(playerName, "&4");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(4), Constants.AMOUNT, "vermelho", player);
                break;
            case 686244985:
                Vars.glowingColor.put(playerName, "&7");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(7), Constants.AMOUNT, "cinza claro", player);
                break;
            case 93818879:
                Vars.glowingColor.put(playerName, "&0");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(0), Constants.AMOUNT, "preto", player);
                break;
            case 9861939:
                Vars.glowingColor.put(playerName, "&a");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(10), Constants.AMOUNT, "verde", player);
                break;
            case 3178592:
                Vars.glowingColor.put(playerName, "&6");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(6), Constants.AMOUNT, "dourado", player);
                break;
            case 3027034:
                Vars.glowingColor.put(playerName, "&9");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(9), Constants.AMOUNT, "azul", player);
                break;
            case 3002044:
                Vars.glowingColor.put(playerName, "&b");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(11), Constants.AMOUNT, "azul claro", player);
                break;
            case 112785:
                Vars.glowingColor.put(playerName, "&c");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(12), Constants.AMOUNT, "tomate", player);
                break;
            case -734239628:
                Vars.glowingColor.put(playerName, "&e");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(14), Constants.AMOUNT, "amarelo", player);
                break;
            case -976943172:
                Vars.glowingColor.put(playerName, "&d");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(13), Constants.AMOUNT, "rosa", player);
                break;
            case -1092352334:
                Vars.glowingColor.put(playerName, "&5");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(5), Constants.AMOUNT, "roxo", player);
                break;
            case -1844766387:
                Vars.glowingColor.put(playerName, "&2");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(2), Constants.AMOUNT, "verde escuro", player);
                break;
            default:
                Vars.glowingColor.put(playerName, "&f");
                tWhite.addEntry(playerName);
                message.sendMessage(plugin.arrData.get(15), Constants.AMOUNT, "branco", player);
                break;
        }
    }

    private void setTeams() {
        tBlack = sc.registerNewTeam(plugin.arrData.get(0));
        tDarkBlue = sc.registerNewTeam(plugin.arrData.get(1));
        tDarkGreen = sc.registerNewTeam(plugin.arrData.get(2));
        tDarkAqua = sc.registerNewTeam(plugin.arrData.get(3));
        tDarkRed = sc.registerNewTeam(plugin.arrData.get(4));
        tDarkPurple = sc.registerNewTeam(plugin.arrData.get(5));
        tGold = sc.registerNewTeam(plugin.arrData.get(6));
        tGray = sc.registerNewTeam(plugin.arrData.get(7));
        tDarkGray = sc.registerNewTeam(plugin.arrData.get(8));
        tBlue = sc.registerNewTeam(plugin.arrData.get(9));
        tGreen = sc.registerNewTeam(plugin.arrData.get(10));
        tAqua = sc.registerNewTeam(plugin.arrData.get(11));
        tRed = sc.registerNewTeam(plugin.arrData.get(12));
        tLightPurple = sc.registerNewTeam(plugin.arrData.get(13));
        tYellow = sc.registerNewTeam(plugin.arrData.get(14));
        tWhite = sc.registerNewTeam(plugin.arrData.get(15));
    }

    private void setColors() {
        tBlack.setColor(plugin.colors.get(0));
        tDarkBlue.setColor(plugin.colors.get(1));
        tDarkGreen.setColor(plugin.colors.get(2));
        tDarkAqua.setColor(plugin.colors.get(3));
        tDarkRed.setColor(plugin.colors.get(4));
        tDarkPurple.setColor(plugin.colors.get(5));
        tGold.setColor(plugin.colors.get(6));
        tGray.setColor(plugin.colors.get(7));
        tDarkGray.setColor(plugin.colors.get(8));
        tBlue.setColor(plugin.colors.get(9));
        tGreen.setColor(plugin.colors.get(10));
        tAqua.setColor(plugin.colors.get(11));
        tRed.setColor(plugin.colors.get(12));
        tLightPurple.setColor(plugin.colors.get(13));
        tYellow.setColor(plugin.colors.get(14));
        tWhite.setColor(plugin.colors.get(15));
    }

}
