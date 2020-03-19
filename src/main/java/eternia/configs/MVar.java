package eternia.configs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MVar {
    public static void broadcastReplaceMessage(String message, Object valor) {
        String mensagem;
        mensagem = Vars.getString(message);
        Bukkit.broadcastMessage(Vars.getColor(mensagem.replace("%s", String.valueOf(valor))));
    }

    public static void playerReplaceMessage(String message, Object valor, Player target) {
        String mensagem;
        mensagem = Vars.getString(message);
        target.sendMessage(Vars.getColor(mensagem.replace("%s", String.valueOf(valor))));
    }

    public static void consoleReplaceMessage(String message, Object valor) {
        String mensagem;
        mensagem = Vars.getString(message);
        Bukkit.getConsoleSender().sendMessage(Vars.getColor(mensagem.replace("%s", String.valueOf(valor))));
    }

    public static void playerMessage(String message, Player target) {
        target.sendMessage(getMessage(message));
    }

    public static void consoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(getMessage(message));
    }

    public static String getMessage(String message) {
        return Vars.getColor(Vars.getString(message));
    }

}