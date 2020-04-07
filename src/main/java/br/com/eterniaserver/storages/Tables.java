package br.com.eterniaserver.storages;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.CVar;

public class Tables {

    public Tables() {
        String createTable = "CREATE TABLE IF NOT EXISTS %table% (player_name varchar(32), xp int(11));";
        EterniaServer.sqlcon.Update(createTable.replace("%table%", CVar.getString("sql.table-xp")));
        createTable = "CREATE TABLE IF NOT EXISTS %table% (player_name varchar(32), balance double(22));";
        EterniaServer.sqlcon.Update(createTable.replace("%table%", CVar.getString("sql.table-money")));
        createTable = "CREATE TABLE IF NOT EXISTS %table% (name varchar(16), location varchar(128));";
        EterniaServer.sqlcon.Update(createTable.replace("%table%", CVar.getString("sql.table-warp")));
        createTable = "CREATE TABLE IF NOT EXISTS %table% (name varchar(16), location varchar(128));";
        EterniaServer.sqlcon.Update(createTable.replace("%table%", CVar.getString("sql.table-shop")));
    }

}
