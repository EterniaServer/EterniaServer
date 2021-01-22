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
import br.com.eterniaserver.eterniaserver.objects.User;
import br.com.eterniaserver.eterniaserver.enums.Colors;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.api.ServerRelated;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@CommandAlias("%glow")
public class Glow extends BaseCommand {

    private final Map<Colors, Team> teams = new EnumMap<>(Colors.class);

    public Glow(EterniaServer plugin) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
            for (Colors colors : Colors.values()) {
                String teamName = "es_" + colors.name().toLowerCase();
                Team team = scoreboard.getTeam(teamName);
                
                if (team == null) {
                    team = scoreboard.registerNewTeam(teamName);
                }

                team.setColor(colors.getChatColor());
                team.setAllowFriendlyFire(true);
                teams.put(colors, team);
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
        Colors colors = ServerRelated.colorFromString(color);
        teams.get(colors).addEntry(user.getName());
        user.putGlowing(colors.getColorStr());
        user.sendMessage(Messages.GLOW_COLOR_CHANGED, colors.getName());
    }

}
