package eternia.configs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MVar {
    public static void broadcastReplaceMessage(String message, Object valor) {
        String mensagem;
        mensagem = Vars.getString(message);
        Bukkit.broadcastMessage(Vars.getColor(mensagem.replace("%s", String.valueOf(valor))));
    }

    public static void playerReplaceMessage(String message, Object valor, Object valor2, Object valor3, Player target) {
        String mensagem;
        mensagem = Vars.getString(message);
        mensagem = mensagem.replace("%s", String.valueOf(valor));
        mensagem = mensagem.replace("%b", String.valueOf(valor2));
        target.sendMessage(Vars.getColor(mensagem.replace("%v", String.valueOf(valor3))));
    }

    public static void playerReplaceMessage(String message, Object valor, Object valor2, Player target) {
        String mensagem;
        mensagem = Vars.getString(message);
        mensagem = mensagem.replace("%s", String.valueOf(valor));
        target.sendMessage(Vars.getColor(mensagem.replace("%b", String.valueOf(valor2))));
    }

    public static void playerReplaceMessage(String message, Object valor, Player target) {
        String mensagem;
        mensagem = Vars.getString(message);
        target.sendMessage(Vars.getColor(mensagem.replace("%s", String.valueOf(valor))));
    }

    public static void consoleReplaceMessage(String message, Object valor, Object valor2, Object valor3) {
        String mensagem;
        mensagem = Vars.getString(message);
        mensagem = mensagem.replace("%s", String.valueOf(valor));
        mensagem = mensagem.replace("%b", String.valueOf(valor2));
        Bukkit.getConsoleSender().sendMessage(Vars.getColor(mensagem.replace("%v", String.valueOf(valor3))));
    }

    public static void consoleReplaceMessage(String message, Object valor, Object valor2) {
        String mensagem;
        mensagem = Vars.getString(message);
        mensagem = mensagem.replace("%s", String.valueOf(valor));
        Bukkit.getConsoleSender().sendMessage(Vars.getColor(mensagem.replace("%b", String.valueOf(valor2))));
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