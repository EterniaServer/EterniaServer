package br.com.eterniaserver.eterniaserver.modules.glow;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;


final class Services {

    static class Glow {

        private final String[] colors = new String[Enums.Color.values().length];
        private final Team[] teams = new Team[Enums.Color.values().length];

        protected Glow(final EterniaServer plugin) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
                final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

                registerTeam(scoreboard, Enums.Color.AQUA, NamedTextColor.AQUA);
                registerTeam(scoreboard, Enums.Color.BLACK, NamedTextColor.BLACK);
                registerTeam(scoreboard, Enums.Color.BLUE, NamedTextColor.BLUE);
                registerTeam(scoreboard, Enums.Color.DARK_AQUA, NamedTextColor.DARK_AQUA);
                registerTeam(scoreboard, Enums.Color.DARK_BLUE, NamedTextColor.DARK_BLUE);
                registerTeam(scoreboard, Enums.Color.DARK_GRAY, NamedTextColor.DARK_GRAY);
                registerTeam(scoreboard, Enums.Color.DARK_GREEN, NamedTextColor.DARK_GREEN);
                registerTeam(scoreboard, Enums.Color.DARK_PURPLE, NamedTextColor.DARK_PURPLE);
                registerTeam(scoreboard, Enums.Color.DARK_RED, NamedTextColor.DARK_RED);
                registerTeam(scoreboard, Enums.Color.GOLD, NamedTextColor.GOLD);
                registerTeam(scoreboard, Enums.Color.GRAY, NamedTextColor.GRAY);
                registerTeam(scoreboard, Enums.Color.GREEN, NamedTextColor.GREEN);
                registerTeam(scoreboard, Enums.Color.LIGHT_PURPLE, NamedTextColor.LIGHT_PURPLE);
                registerTeam(scoreboard, Enums.Color.RED, NamedTextColor.RED);
                registerTeam(scoreboard, Enums.Color.WHITE, NamedTextColor.WHITE);
                registerTeam(scoreboard, Enums.Color.YELLOW, NamedTextColor.YELLOW);
            });
        }

        protected void setColor(Enums.Color color, String value) {
            this.colors[color.ordinal()] = value;
        }

        protected String getColor(Enums.Color color) {
            return this.colors[color.ordinal()];
        }

        protected Team getTeam(Enums.Color color) {
            return this.teams[color.ordinal()];
        }

        private void registerTeam(final Scoreboard scoreboard, Enums.Color entry, NamedTextColor color) {
            Team team = scoreboard.getTeam(entry.name());
            if (team == null) team = scoreboard.registerNewTeam(entry.name());

            team.color(color);
            team.setAllowFriendlyFire(true);

            teams[entry.ordinal()] = team;
        }

    }

}
