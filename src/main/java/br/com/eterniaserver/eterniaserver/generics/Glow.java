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

@CommandAlias("glow")
@CommandPermission("eternia.glow")
public class Glow extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles message;
    private final Scoreboard sc;

    public Glow(EterniaServer plugin) {
        this.plugin = plugin;
        this.message = plugin.getEFiles();
        this.sc = Bukkit.getScoreboardManager().getMainScoreboard();
        for (int i = 0; i < 16; i++) {
            if (sc.getTeam(plugin.arrData.get(i)) == null) {
                sc.registerNewTeam(plugin.arrData.get(i)).setColor(plugin.colors.get(i));
            }
        }
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
        byte var;
        String colorCode;
        String colorName;
        switch (color.hashCode()) {
            case 1741606617:
                var = 8;
                colorCode = "&8";
                colorName = "cinza escuro";
                break;
            case 1741452496:
                var = 1;
                colorCode = "&1";
                colorName = "azul escuro";
                break;
            case 1741427506:
                var = 3;
                colorCode = "&3";
                colorName = "ciano";
                break;
            case 1441664347:
                var = 4;
                colorCode = "&4";
                colorName = "vermelho";
                break;
            case 686244985:
                var = 7;
                colorCode = "&7";
                colorName = "cinza claro";
                break;
            case 93818879:
                var = 0;
                colorCode = "&0";
                colorName = "preto";
                break;
            case 9861939:
                var = 10;
                colorCode = "&a";
                colorName = "verde";
                break;
            case 3178592:
                var = 6;
                colorCode = "&6";
                colorName = "dourado";
                break;
            case 3027034:
                var = 9;
                colorCode = "&9";
                colorName = "azul";
                break;
            case 3002044:
                var = 11;
                colorCode = "&b";
                colorName = "azul claro";
                break;
            case 112785:
                var = 12;
                colorCode = "&c";
                colorName = "tomate";
                break;
            case -734239628:
                var = 14;
                colorCode = "&e";
                colorName = "amarelo";
                break;
            case -976943172:
                var = 13;
                colorCode = "&d";
                colorName = "rosa";
                break;
            case -1092352334:
                var = 5;
                colorCode = "&5";
                colorName = "roxo";
                break;
            case -1844766387:
                var = 2;
                colorCode = "&2";
                colorName = "verde escuro";
                break;
            default:
                var = 15;
                colorCode = "&f";
                colorName = "branco";
                break;
        }
        changeColor(player, plugin.arrData.get(var), colorCode, colorName);
    }

    public void changeColor(final Player player, final String team, final String nameColor, final String color) {
        final String playerName = player.getName();
        Vars.glowingColor.put(playerName, nameColor);
        sc.getTeam(team).addEntry(playerName);
        message.sendMessage(Strings.M_GLOW_COLOR, Constants.AMOUNT, color, player);
    }

}
