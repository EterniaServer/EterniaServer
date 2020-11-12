package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eternialib.interfaces.Query;
import br.com.eterniaserver.eternialib.sql.objects.Cells;

public class Profile implements Query {

    private final String table;
    private Object[] objects;
    public final Cells where = new Cells();


    public Profile(String table) {
        this.table = table;
    }

    public void setObjects(Object... objects) {
        this.objects = objects;
    }

    @Override
    public String queryString() {
        return "UPDATE " + table + " SET player_name='" + objects[0] + "', player_display='" + objects[1] + "', time='" + objects[2] + "', last='" + objects[3] + "', hours='" + objects[4] + "', muted='" + objects[5] + "' WHERE " + where.get();
    }
}
