package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.entity.Player;

public class Simplifications extends BaseCommand {

    private final EFiles messages;

    public Simplifications(EFiles messages) {
        this.messages = messages;
    }

    @CommandAlias("rain|chuva")
    @CommandPermission("eternia.rain")
    public void onRain(Player player) {
        player.getWorld().setStorm(true);
        messages.sendMessage("generic.simp.weather-changed", player);
    }

    @CommandAlias("sun|sol")
    @CommandPermission("eternia.sun")
    public void onSun(Player player) {
        player.getWorld().setStorm(false);
        messages.sendMessage("generic.simp.weather-changed", player);
    }

    @CommandAlias("thor|lightning")
    @Syntax("<jogador>")
    @CommandCompletion("@players")
    @CommandPermission("eternia.thor")
    public void onThor(Player player, @Optional OnlinePlayer target) {
        if (target != null) {
            final Player targetP = target.getPlayer();

            targetP.getWorld().strikeLightning(targetP.getLocation());
            messages.sendMessage("generic.simp.sent-lightning", Constants.TARGET, targetP.getDisplayName(), player);
            messages.sendMessage("generic.simp.received-lightning", Constants.TARGET, player.getDisplayName(), targetP);
        } else {
            player.getWorld().strikeLightning(player.getTargetBlock(null, 100).getLocation());
        }
    }

    @CommandAlias("suicide|suicidio")
    @Syntax("<mensagem>")
    @CommandPermission("eternia.suicide")
    public void onSuicide(Player player, String[] args) {
        if (args.length >= 1) {
            StringBuilder sb = new StringBuilder();
            for (java.lang.String arg : args) sb.append(arg).append(" ");
            player.setHealth(0);
            messages.broadcastMessage("generic.simp.suicide", Constants.MESSAGE, sb.toString(), Constants.PLAYER, player.getDisplayName());
        } else {
            player.setHealth(0);
        }
    }

}
