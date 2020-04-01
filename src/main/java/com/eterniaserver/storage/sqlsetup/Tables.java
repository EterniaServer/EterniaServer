package com.eterniaserver.storage.sqlsetup;

import com.eterniaserver.EterniaServer;

public class Tables {
    public Tables() {
        String createTable = "CREATE TABLE IF NOT EXISTS xp (player_name varchar(32), xp int(11));";
        EterniaServer.sqlcon.Update(createTable);
        createTable = "CREATE TABLE IF NOT EXISTS economy (player_name varchar(32), balance double(22));";
        EterniaServer.sqlcon.Update(createTable);
        createTable = "CREATE TABLE IF NOT EXISTS warp (name varchar(16), location varchar(128));";
        EterniaServer.sqlcon.Update(createTable);
        createTable = "CREATE TABLE IF NOT EXISTS shop (name varchar(16), location varchar(128));";
        EterniaServer.sqlcon.Update(createTable);
    }
}
