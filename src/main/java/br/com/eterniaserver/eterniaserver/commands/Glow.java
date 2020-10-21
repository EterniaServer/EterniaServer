package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.core.User;
import br.com.eterniaserver.eterniaserver.enums.Colors;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.APIServer;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

@CommandAlias("%glow")
public class Glow extends BaseCommand {

    @Default
    @Description("%glow_description")
    @CommandPermission("%glow_perm")
    public void onGlow(Player player) {
        if (!player.isGlowing()) {
            EterniaServer.msg.sendMessage(player, Messages.GLOW_ENABLED);
        } else {
            EterniaServer.msg.sendMessage(player, Messages.GLOW_DISABLED);
            player.removePotionEffect(PotionEffectType.GLOWING);
        }
        player.setGlowing(!player.isGlowing());
    }

    @Subcommand("%glow_color")
    @CommandCompletion("@colors")
    @Syntax("%glow_color_syntax")
    @Description("%glow_color_description")
    @CommandPermission("%glow_color_perm")
    public void onGlowColor(Player player, String color) {
        User user = new User(player);
        Colors colors = APIServer.colorFromString(color);
        APIServer.getScoreboard().getTeam(colors.name()).addEntry(user.getName());
        user.putGlowing(colors.getColorStr());
        user.sendMessage(Messages.GLOW_COLOR_CHANGED, colors.getName());
    }

}
