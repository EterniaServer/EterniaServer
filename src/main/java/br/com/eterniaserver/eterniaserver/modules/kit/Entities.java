package br.com.eterniaserver.eterniaserver.modules.kit;

import br.com.eterniaserver.eternialib.database.annotations.DataField;
import br.com.eterniaserver.eternialib.database.annotations.PrimaryKeyField;
import br.com.eterniaserver.eternialib.database.annotations.Table;
import br.com.eterniaserver.eternialib.database.enums.FieldType;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

public final class Entities {

    private Entities() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Table(tableName = "%eternia_kit_time%")
    public static class KitTime {

        public KitTime(UUID uuid, String kitName, Timestamp lastUseTime) {
            this.uuid = uuid;
            this.kitName = kitName;
            this.lastUseTime = lastUseTime;
        }

        @PrimaryKeyField(columnName = "id", type = FieldType.INTEGER, autoIncrement = true)
        private Integer id;

        @DataField(columnName = "uuid", type = FieldType.UUID, notNull = true)
        private UUID uuid;

        @DataField(columnName = "kitName", type = FieldType.STRING, notNull = true)
        private String kitName;

        @DataField(columnName = "lastUseTime", type = FieldType.TIMESTAMP, notNull = true)
        private Timestamp lastUseTime;

    }

}
