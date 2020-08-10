package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

@CommandAlias("glow")
@CommandPermission("eternia.glow")
public class Glow extends BaseCommand {

    private final Scoreboard sc;

    public Glow() {
        this.sc = Bukkit.getScoreboardManager().getMainScoreboard();
        for (int i = 0; i < 16; i++) {
            if (sc.getTeam(EterniaServer.arrData.get(i)) == null) {
                sc.registerNewTeam(EterniaServer.arrData.get(i)).setColor(EterniaServer.colors.get(i));
            }
        }
    }

    @Default
    public void onGlow(Player player) {
        if (!player.isGlowing()) {
            player.sendMessage(Strings.M_GLOW_ENABLED);
        } else {
            player.removePotionEffect(PotionEffectType.GLOWING);
            player.sendMessage(Strings.M_GLOW_DISABLED);
        }
        player.setGlowing(!player.isGlowing());
    }

    @Subcommand("color")
    @CommandCompletion("@colors")
    public void onGlowColor(Player player, String color) {
        final String dark = "escuro";
        final String light = "claro";
        switch (color.hashCode()) {
            case 1741606617:
                changeColor(player, EterniaServer.arrData.get(8), "&8", "cinza " + dark);
                break;
            case 1741452496:
                changeColor(player, EterniaServer.arrData.get(1), "&1", "azul " + dark);
                break;
            case 1741427506:
                changeColor(player, EterniaServer.arrData.get(3), "&3", "ciano");
                break;
            case 1441664347:
                changeColor(player, EterniaServer.arrData.get(4), "&4", "vermelho");
                break;
            case 686244985:
                changeColor(player, EterniaServer.arrData.get(7), "&7", "cinza " + light);
                break;
            case 93818879:
                changeColor(player, EterniaServer.arrData.get(0), "&0", "preto");
                break;
            case 98619139:
                changeColor(player, EterniaServer.arrData.get(10), "&a", "verde");
                break;
            case 3178592:
                changeColor(player, EterniaServer.arrData.get(6), "&6", "dourado");
                break;
            case 3027034:
                changeColor(player, EterniaServer.arrData.get(9), "&9", "azul");
                break;
            case 3002044:
                changeColor(player, EterniaServer.arrData.get(11), "&b", "azul " + light);
                break;
            case 112785:
                changeColor(player, EterniaServer.arrData.get(12), "&c", "tomate");
                break;
            case -734239628:
                changeColor(player, EterniaServer.arrData.get(14), "&e", "amarelo");
                break;
            case -976943172:
                changeColor(player, EterniaServer.arrData.get(13), "&d", "rosa");
                break;
            case -1092352334:
                changeColor(player, EterniaServer.arrData.get(5), "&5", "roxo");
                break;
            case -1844766387:
                changeColor(player, EterniaServer.arrData.get(2), "&2", "verde " + dark);
                break;
            default:
                changeColor(player, EterniaServer.arrData.get(15), "&f", "branco");
                break;
        }
    }

    public void changeColor(final Player player, final String team, final String nameColor, final String color) {
        final String playerName = player.getName();
        Vars.glowingColor.put(playerName, nameColor);
        sc.getTeam(team).addEntry(playerName);
        player.sendMessage(Strings.M_GLOW_COLOR.replace(Constants.AMOUNT, color));
    }

}
