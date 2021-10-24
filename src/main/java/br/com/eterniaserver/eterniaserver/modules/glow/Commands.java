package br.com.eterniaserver.eterniaserver.modules.glow;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.events.GlowStatusEvent;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

final class Commands {

    @CommandAlias("%GLOW")
    static class Glow extends BaseCommand {

        final EterniaServer plugin;

        final Services.Glow servicesGlow;

        protected Glow(final EterniaServer plugin, final Services.Glow servicesGlow) {
            this.plugin = plugin;
            this.servicesGlow = servicesGlow;
        }

        @Default
        @CommandCompletion("@valid_entities 1 @players")
        @Syntax("%GLOW_GIVE_SYNTAX")
        @Description("%GLOW_GIVE_DESCRIPTION")
        @CommandPermission("%GLOW_GIVE_PERM")
        public void onGlow(Player player) {
            final GlowStatusEvent glowStatusEvent = new GlowStatusEvent(player, "UNKNOWN", !player.isGlowing());

            if (!player.isGlowing()) {
                if (glowStatusEvent.isCancelled() || !glowStatusEvent.getGlowingStatus()) return;

                plugin.sendMiniMessages(player, Messages.GLOW_ENABLED);
                player.addPotionEffect(PotionEffectType.GLOWING.createEffect(Integer.MAX_VALUE, 1));
                player.setGlowing(true);
                return;
            }

            if (glowStatusEvent.isCancelled() || glowStatusEvent.getGlowingStatus()) return;

            plugin.sendMiniMessages(player, Messages.GLOW_DISABLED);
            player.removePotionEffect(PotionEffectType.GLOWING);
            player.setGlowing(false);
        }

        @Subcommand("%GLOW_COLOR")
        @CommandCompletion("@es_colors")
        @Syntax("%GLOW_COLOR_SYNTAX")
        @Description("%GLOW_COLOR_DESCRIPTION")
        @CommandPermission("%GLOW_COLOR_PERM")
        public void onGlowColor(Player player, String colorName) {
            try {
                final Enums.Color color = Enums.Color.valueOf(colorName);
                GlowStatusEvent glowStatusEvent = new GlowStatusEvent(player, servicesGlow.getColor(color), player.isGlowing());

                if (glowStatusEvent.isCancelled()) return;

                plugin.userManager().get(player.getUniqueId()).setColor(color.getColor());
                servicesGlow.getTeam(color).addEntry(player.getName());
                plugin.sendMiniMessages(player, Messages.GLOW_COLOR_CHANGED, servicesGlow.getColor(color));
            }
            catch (IllegalArgumentException ignored) {
                plugin.sendMiniMessages(player, Messages.GLOW_INVALID_COLOR);
            }
        }
    }

}
