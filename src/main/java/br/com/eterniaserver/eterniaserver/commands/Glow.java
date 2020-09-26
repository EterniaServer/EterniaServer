package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.HelpCommand;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;

import br.com.eterniaserver.eterniaserver.Configs;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.generics.APIServer;
import br.com.eterniaserver.eterniaserver.generics.PluginConstants;
import br.com.eterniaserver.eterniaserver.generics.PluginMSGs;
import br.com.eterniaserver.eterniaserver.generics.PluginVars;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;


@CommandAlias("glow")
@CommandPermission("eternia.glow")
public class Glow extends BaseCommand {

    @Subcommand("help")
    @HelpCommand
    @Syntax("<página>")
    @Description(" Ajuda para o comando Glow")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    @Default
    @Description(" Ativa ou desativa o glow")
    public void onGlow(Player player) {
        if (!player.isGlowing()) {
            Configs.instance.sendMessage(player, Messages.GlowEnabled);
        } else {
            Configs.instance.sendMessage(player, Messages.GlowDisabled);
            player.removePotionEffect(PotionEffectType.GLOWING);
        }
        player.setGlowing(!player.isGlowing());
    }

    @Subcommand("color")
    @CommandCompletion("@colors")
    @Syntax("<cor>")
    @Description(" Escolhe qual cor utilizar no glow")
    public void onGlowColor(Player player, String color) {
        final String dark = "escuro";
        final String light = "claro";
        switch (color.hashCode()) {
            case 1741606617:
                changeColor(player, PluginVars.arrData.get(8), "&8", "cinza " + dark);
                break;
            case 1741452496:
                changeColor(player, PluginVars.arrData.get(1), "&1", "azul " + dark);
                break;
            case 1741427506:
                changeColor(player, PluginVars.arrData.get(3), "&3", "ciano");
                break;
            case 1441664347:
                changeColor(player, PluginVars.arrData.get(4), "&4", "vermelho");
                break;
            case 686244985:
                changeColor(player, PluginVars.arrData.get(7), "&7", "cinza " + light);
                break;
            case 93818879:
                changeColor(player, PluginVars.arrData.get(0), "&0", "preto");
                break;
            case 98619139:
                changeColor(player, PluginVars.arrData.get(10), "&a", "verde");
                break;
            case 3178592:
                changeColor(player, PluginVars.arrData.get(6), "&6", "dourado");
                break;
            case 3027034:
                changeColor(player, PluginVars.arrData.get(9), "&9", "azul");
                break;
            case 3002044:
                changeColor(player, PluginVars.arrData.get(11), "&b", "azul " + light);
                break;
            case 112785:
                changeColor(player, PluginVars.arrData.get(12), "&c", "tomate");
                break;
            case -734239628:
                changeColor(player, PluginVars.arrData.get(14), "&e", "amarelo");
                break;
            case -976943172:
                changeColor(player, PluginVars.arrData.get(13), "&d", "rosa");
                break;
            case -1092352334:
                changeColor(player, PluginVars.arrData.get(5), "&5", "roxo");
                break;
            case -1844766387:
                changeColor(player, PluginVars.arrData.get(2), "&2", "verde " + dark);
                break;
            default:
                changeColor(player, PluginVars.arrData.get(15), "&f", "branco");
                break;
        }
    }

    private void changeColor(final Player player, final String team, final String nameColor, final String color) {
        final String playerName = player.getName();
        APIServer.getScoreboard().getTeam(team).addEntry(playerName);
        APIServer.putGlowing(playerName, nameColor);
        Configs.instance.sendMessage(player, Messages.GlowColorChanged, color);
    }


}
