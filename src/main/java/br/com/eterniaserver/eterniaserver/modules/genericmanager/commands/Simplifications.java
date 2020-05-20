package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;

import org.bukkit.entity.Player;

public class Simplifications extends BaseCommand {

    private final Messages messages;

    public Simplifications(Messages messages) {
        this.messages = messages;
    }

    @CommandAlias("rain|chuva")
    @CommandPermission("eternia.rain")
    public void onRain(Player player) {
        player.getWorld().setStorm(true);
        messages.sendMessage("simp.weather", player);
    }

    @CommandAlias("sun|sol")
    @CommandPermission("eternia.sun")
    public void onSun(Player player) {
        player.getWorld().setStorm(false);
        messages.sendMessage("simp.weather", player);
    }

    @CommandAlias("thor|lightning")
    @Syntax("<jogador>")
    @CommandCompletion("@players")
    @CommandPermission("eternia.thor")
    public void onThor(Player player, @Optional OnlinePlayer target) {
        if (target != null) {
            target.getPlayer().getWorld().strikeLightning(target.getPlayer().getLocation());
            messages.sendMessage("simp.thor-other", "%target_name%", target.getPlayer().getName(), player);
            messages.sendMessage("simp.other-thor", "%target_name%", player.getName(), target.getPlayer());
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
            for (java.lang.String arg : args) {
                sb.append(arg).append(" ");
            }
            player.setHealth(0);
            messages.BroadcastMessage("text.suicide", "%message%", sb.toString(), "%player_name%", player.getName());
        } else {
            player.setHealth(0);
        }
    }

}
