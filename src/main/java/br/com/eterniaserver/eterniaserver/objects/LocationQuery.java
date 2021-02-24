package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eternialib.core.baseobjects.Cells;
import br.com.eterniaserver.eternialib.core.interfaces.Query;

public class LocationQuery implements Query {

    private final String table;

    private String worldName;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public final Cells where = new Cells();

    public LocationQuery(final String table) {
        this.table = table;
    }

    public void setLocation(final String worldName, final double x, final double y, final double z, final float yaw, final float pitch) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public String queryString() {
        return "UPDATE " + table + " SET world='" + worldName + "', coord_x='" + x + "', coord_y='" + y + "', coord_z='"
                + z + "', coord_yaw='" + yaw + "', coord_pitch='" + pitch + "' WHERE " + where.get() + ";";
    }
}
