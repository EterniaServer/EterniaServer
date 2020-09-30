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

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Colors;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.APIServer;
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
            EterniaServer.msg.sendMessage(player, Messages.GLOW_ENABLED);
        } else {
            EterniaServer.msg.sendMessage(player, Messages.GLOW_DISABLED);
            player.removePotionEffect(PotionEffectType.GLOWING);
        }
        player.setGlowing(!player.isGlowing());
    }

    @Subcommand("color")
    @CommandCompletion("@colors")
    @Syntax("<cor>")
    @Description(" Escolhe qual cor utilizar no glow")
    public void onGlowColor(Player player, String color) {
        Colors colors = APIServer.colorFromString(color);
        final String playerName = player.getName();
        APIServer.getScoreboard().getTeam(colors.name()).addEntry(playerName);
        APIServer.putGlowing(playerName, colors.getColorStr());
        EterniaServer.msg.sendMessage(player, Messages.GLOW_COLOR_CHANGED, colors.getName());
    }

}
