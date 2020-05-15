package br.com.eterniaserver.eterniaserver.storages.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface SQLCallback {

    void call(Connection t) throws SQLException;

}