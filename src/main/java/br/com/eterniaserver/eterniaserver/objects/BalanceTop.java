package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eternialib.interfaces.Query;

public class BalanceTop implements Query {

    private final String table;
    private final int size;

    public BalanceTop(String table, int size) {
        this.table = table;
        this.size = size;
    }


    @Override
    public String queryString() {
        return "SELECT " + "uuid" + " FROM " + table + " ORDER BY " + "balance" + " DESC LIMIT " + size + ";";
    }
}
