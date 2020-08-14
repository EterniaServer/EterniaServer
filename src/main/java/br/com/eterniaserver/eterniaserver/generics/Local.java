package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Strings;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Local {

    public void sendChatMessage(String message, Player player, int radius) {
        int pes = 0;
        final String format = getFormat(message, player);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (Vars.ignoredPlayer.get(player.getName()) != null && Vars.ignoredPlayer.get(player.getName()).contains(p)) return;
            final Boolean b = Vars.spy.get(p.getName());
            if ((player.getWorld() == p.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= Math.pow(radius, 2)) || radius <= 0) {
                pes += 1;
                p.sendMessage(format);
            } else if (p.hasPermission("eternia.spy") && Boolean.TRUE.equals(b)) {
                p.sendMessage(Strings.getColor("&8[&7SPY&8-&eL&8] &8" + player.getDisplayName() + ": " + message));
            }
        }
        if (pes <= 1) {
            player.sendMessage(Strings.M_CHAT_NOONE);
        }
    }

    private String getFormat(String message, final Player player) {
        String format = InternMethods.setPlaceholders(player, EterniaServer.chatConfig.getString("local.format"));
        if (player.hasPermission("eternia.chat.color")) {
            return Strings.getColor(format.replace(Constants.MESSAGE, message));
        } else {
            return(format.replace(Constants.MESSAGE, message));
        }
    }

}
