package br.com.eterniaserver.modules.kitsmanager.sql;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.player.PlayerManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class KitsAPI {

    public static void setKitCooldown(final String jogador, final String kit) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        final String data = format.format(date);
        if (PlayerManager.playerCooldownExist(jogador, kit)) {
            Vars.kits_cooldown.put(kit + "." + jogador, data);
            final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-kits") + " SET cooldown='" + data + "' WHERE name='" + kit + "." + jogador + "';";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement putkit = connection.prepareStatement(querie);
                putkit.execute();
                putkit.close();
            }, true);
        } else {
            Vars.kits_cooldown.put(kit + "." + jogador, data);
            final String querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-kits") + " (name, cooldown) VALUES ('" + kit + "." + jogador + "', '" + data + "');";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement putkit = connection.prepareStatement(querie);
                putkit.execute();
                putkit.close();
            }, true);
        }
    }

    public static String getKitCooldown(final String jogador, final String kit) {
        if (Vars.kits_cooldown.containsKey(kit + "." + jogador)) {
            return Vars.kits_cooldown.get(kit + "." + jogador);
        }

        AtomicReference<String> cooldown = new AtomicReference<>("");
        if (PlayerManager.playerCooldownExist(jogador, kit)) {
            final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-kits") + " WHERE name='" + kit + "." + jogador + "';";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement getkit = connection.prepareStatement(querie);
                ResultSet resultSet = getkit.executeQuery();
                if (resultSet.next() && resultSet.getString("cooldown") != null) {
                    cooldown.set(resultSet.getString("cooldown"));
                }
            });
        } else {
            cooldown.set("2020/01/01 00:00");
        }

        Vars.kits_cooldown.put(kit + "." + jogador, cooldown.get());
        return cooldown.get();
    }

}
