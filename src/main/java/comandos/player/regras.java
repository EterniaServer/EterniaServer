package comandos.player;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class regras implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.BLUE +
                    "S" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Regras" + ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 1.0-" + ChatColor.GRAY + " Proibido ofensas" + ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 1.1-" + ChatColor.GRAY + " Qualquer forma de preconceito é proibida" +
                    ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 1.2-" + ChatColor.GRAY + " Proibido apologia a coisas ofensiva e/ou que trouxeram uma" +
                    " grande perda para a sociedade" + ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 2.0-" + ChatColor.GRAY + " Proibido o uso de apelido impropriado" +
                    ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 2.1-" + ChatColor.GRAY + " Também se aplica a pets e etc." +
                    ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 3.0-" + ChatColor.GRAY + " Proibido o uso de qualquer hack, exploit e etc." +
                    ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 3.1-" + ChatColor.GRAY + " Aproveitar-se ou esconder um bug também é proibido." +
                    ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 4.0-" + ChatColor.GRAY + " Proibido enganações" + ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 4.1-" + ChatColor.GRAY + " Certas traps são enganações sendo também proibido" +
                    ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 4.2-" + ChatColor.GRAY + " Certas promessas também são consideradas enganação" +
                    ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 5.0-" + ChatColor.GRAY + " Proibido se intrometer em certos assuntos, exemplos: " +
                    "'Você acha que uma certa pessoa está sendo ofendida por outra, porém ela não reclama e nem denunciou'," +
                    " 'Assuntos de staff'" + ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 6.0-" + ChatColor.GRAY + " Proibido pedir itens, permissões e etc." +
                    ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 6.1-" + ChatColor.GRAY + " O mesmo se aplica para cargos" + ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 7.0-" + ChatColor.GRAY + " Evitar falar vulgamente e/ou explicitamente sobre sexo no chat global" +
                    ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 8.0-" + ChatColor.GRAY + " Proibido apologia a coisas ilícitas, inclusive programas." +
                    ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 9.0-" + ChatColor.GRAY + " Proibido flood/spam" + ChatColor.DARK_GRAY + ".");
            player.sendMessage(ChatColor.BLUE + " 9.1-" + ChatColor.GRAY + " Proibido discutir sobre a punição dada ou não dada por um staff" +
                    ChatColor.DARK_GRAY + ".");
            return true;
        }
        return false;
    }
}