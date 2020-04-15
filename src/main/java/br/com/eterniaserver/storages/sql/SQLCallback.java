package br.com.eterniaserver.storages.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface SQLCallback {

    void call(Connection t) throws SQLException;

}