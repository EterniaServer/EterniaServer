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

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

@CommandAlias("%glow")
public class Glow extends BaseCommand {

    private Scoreboard scoreboard;

    public Glow(EterniaServer plugin) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
            this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

            List<ChatColor> colorss = List.of(ChatColor.BLACK, ChatColor.DARK_BLUE, ChatColor.DARK_GREEN,
            ChatColor.DARK_AQUA, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GRAY,
            ChatColor.DARK_GRAY, ChatColor.BLUE, ChatColor.GREEN, ChatColor.AQUA, ChatColor.RED, ChatColor.LIGHT_PURPLE,
            ChatColor.YELLOW, ChatColor.WHITE);
            List<Colors> colors = Arrays.asList(Colors.values());
            for (int i = 0; i < 16; i++) {
                scoreboard.registerNewTeam(colors.get(i).name()).setColor(colorss.get(i));
            }
        });
    }

    @Default
    @Description("%glow_description")
    @CommandPermission("%glow_perm")
    public void onGlow(Player player) {
        if (!player.isGlowing()) {
            EterniaServer.sendMessage(player, Messages.GLOW_ENABLED);
        } else {
            EterniaServer.sendMessage(player, Messages.GLOW_DISABLED);
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
        scoreboard.getTeam(colors.name()).addEntry(user.getName());
        user.putGlowing(colors.getColorStr());
        user.sendMessage(Messages.GLOW_COLOR_CHANGED, colors.getName());
    }

}
