package br.com.eterniaserver.eterniaserver.modules.teleport;

import br.com.eterniaserver.eternialib.database.annotations.DataField;
import br.com.eterniaserver.eternialib.database.annotations.PrimaryKeyField;
import br.com.eterniaserver.eternialib.database.annotations.ReferenceField;
import br.com.eterniaserver.eternialib.database.annotations.Table;
import br.com.eterniaserver.eternialib.database.enums.FieldType;
import br.com.eterniaserver.eternialib.database.enums.ReferenceMode;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

import java.util.UUID;

public final class Entities {

    private Entities() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Table(tableName = "%eternia_warp%")
    public static class WarpLocation {

        @PrimaryKeyField(columnName = "name", type = FieldType.STRING, autoIncrement = false)
        private String name;

        @DataField(columnName = "worldName", type = FieldType.STRING, notNull = true)
        private String worldName;

        @DataField(columnName = "coordX", type = FieldType.DOUBLE, notNull = true)
        private Double coordX;

        @DataField(columnName = "coordY", type = FieldType.DOUBLE, notNull = true)
        private Double coordY;

        @DataField(columnName = "coordZ", type = FieldType.DOUBLE, notNull = true)
        private Double coordZ;

        @DataField(columnName = "coordYaw", type = FieldType.DOUBLE, notNull = true)
        private Double coordYaw;

        @DataField(columnName = "coordPitch", type = FieldType.DOUBLE, notNull = true)
        private Double coordPitch;

        public Location getLocation(EterniaServer plugin) {
            return new Location(
                    plugin.getServer().getWorld(worldName),
                    coordX,
                    coordY,
                    coordZ,
                    coordYaw.floatValue(),
                    coordPitch.floatValue()
            );
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Table(tableName = "%eternia_home%")
    public static class HomeLocation {

        @PrimaryKeyField(columnName = "id", type = FieldType.INTEGER, autoIncrement = true)
        private Integer id;

        @ReferenceField(columnName = "uuid",
                referenceTableName = "%eternia_server_profile%",
                referenceColumnName = "uuid",
                notNull = true,
                mode = ReferenceMode.CASCADE)
        @DataField(columnName = "uuid", type = FieldType.UUID, notNull = true)
        private UUID uuid;

        @DataField(columnName = "name", type = FieldType.STRING, notNull = true)
        private String name;

        @DataField(columnName = "worldName", type = FieldType.STRING, notNull = true)
        private String worldName;

        @DataField(columnName = "coordX", type = FieldType.DOUBLE, notNull = true)
        private Double coordX;

        @DataField(columnName = "coordY", type = FieldType.DOUBLE, notNull = true)
        private Double coordY;

        @DataField(columnName = "coordZ", type = FieldType.DOUBLE, notNull = true)
        private Double coordZ;

        @DataField(columnName = "coordYaw", type = FieldType.DOUBLE, notNull = true)
        private Double coordYaw;

        @DataField(columnName = "coordPitch", type = FieldType.DOUBLE, notNull = true)
        private Double coordPitch;

        public HomeLocation(UUID uuid,
                            String name,
                            String worldName,
                            Double coordX,
                            Double coordY,
                            Double coordZ,
                            Double coordYaw,
                            Double coordPitch) {
            this.uuid = uuid;
            this.name = name;
            this.worldName = worldName;
            this.coordX = coordX;
            this.coordY = coordY;
            this.coordZ = coordZ;
            this.coordYaw = coordYaw;
            this.coordPitch = coordPitch;
        }

        public Location getLocation(EterniaServer plugin) {
            return new Location(
                    plugin.getServer().getWorld(worldName),
                    coordX,
                    coordY,
                    coordZ,
                    coordYaw.floatValue(),
                    coordPitch.floatValue()
            );
        }

    }

}
